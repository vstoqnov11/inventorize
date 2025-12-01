package bg.softuni.My_Inventory.user.service;

import bg.softuni.My_Inventory.business.model.Business;
import bg.softuni.My_Inventory.security.UserData;
import bg.softuni.My_Inventory.user.model.User;
import bg.softuni.My_Inventory.user.model.UserRole;
import bg.softuni.My_Inventory.user.repository.UserRepository;
import bg.softuni.My_Inventory.web.dto.EditProfileRequest;
import bg.softuni.My_Inventory.web.dto.NewEmployeeRequest;
import bg.softuni.My_Inventory.web.dto.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;
    @Autowired
    public UserService (UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void register (RegisterRequest registerRequest) {

        Optional<User> userOpt = userRepository.findByUsername(registerRequest.getUsername());

        if (userOpt.isPresent()) {
            throw new RuntimeException("Username already taken");
        }

        User user = User.builder()
                .username(registerRequest.getUsername())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .firstName(registerRequest.getFirstName())
                .lastName(registerRequest.getLastName())
                .email(registerRequest.getEmail())
                .role(UserRole.MANAGER)
                .createdOn(LocalDateTime.now())
                .updatedOn(LocalDateTime.now())
                .build();

        userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername (String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));

        return new UserData(user.getId(), user.getUsername(), user.getPassword(), user.getEmail(), user.getRole(), user.getBusiness());
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User with username: %s not found".formatted(username)));
    }

    public boolean userHasBusiness (String username) {

        User user = findByUsername(username);

        return user.getBusiness() != null;
    }

    public void addBusiness (Business business, String username) {

        User user = findByUsername(username);

        user.setBusiness(business);

        userRepository.save(user);
    }

    public Page<User> getAllUsers(int page) {

        Pageable pageable = PageRequest.of(page, 10);
        return userRepository.findAll(pageable);
    }

    public User getById (UUID id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User with id: %s not found".formatted(id)));
    }

    public void updateProfile (EditProfileRequest editProfileRequest, UUID id) {

        User user = getById(id);

        user.setFirstName(editProfileRequest.getFirstName());
        user.setLastName(editProfileRequest.getLastName());
        user.setEmail(editProfileRequest.getEmail());
        user.setPhoneNumber(editProfileRequest.getPhoneNumber());
        user.setUpdatedOn(LocalDateTime.now());

        userRepository.save(user);
    }

    public Page<User> getAllEmployees (int page, UUID id) {

        Pageable pageable = PageRequest.of(page, 10);
        return userRepository.findAllByBusinessId(id, pageable);
    }

    public void createEmployee (NewEmployeeRequest newEmployeeRequest, Business business) {

        User user = User.builder()
                .username(newEmployeeRequest.getUsername())
                .password(passwordEncoder.encode(newEmployeeRequest.getPassword()))
                .firstName(newEmployeeRequest.getFirstName())
                .lastName(newEmployeeRequest.getLastName())
                .email(newEmployeeRequest.getEmail())
                .phoneNumber(newEmployeeRequest.getPhoneNumber())
                .role(UserRole.EMPLOYEE)
                .business(business)
                .createdOn(LocalDateTime.now())
                .updatedOn(LocalDateTime.now())
                .build();

        userRepository.save(user);
    }
}
