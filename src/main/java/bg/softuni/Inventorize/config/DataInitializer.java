package bg.softuni.Inventorize.config;

import bg.softuni.Inventorize.business.model.Business;
import bg.softuni.Inventorize.business.property.BusinessProperties;
import bg.softuni.Inventorize.business.repository.BusinessRepository;
import bg.softuni.Inventorize.user.model.User;
import bg.softuni.Inventorize.user.model.UserRole;
import bg.softuni.Inventorize.user.property.UserProperties;
import bg.softuni.Inventorize.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final BusinessRepository businessRepository;
    private final UserProperties userProperties;
    private final BusinessProperties businessProperties;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DataInitializer (UserRepository userRepository, BusinessRepository businessRepository, UserProperties userProperties, BusinessProperties businessProperties, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.businessRepository = businessRepository;
        this.userProperties = userProperties;
        this.businessProperties = businessProperties;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        initializeUser();
        initializeBusiness();
    }

    private void initializeUser() {
        Optional<User> existingUser = userRepository.findByUsername(userProperties.getUsername());
        if (existingUser.isPresent()) {
            log.info("DataInitializer: Admin skipped (already exists)");
            return;
        }

        User user = User.builder()
                .username(userProperties.getUsername())
                .password(passwordEncoder.encode(userProperties.getPassword()))
                .firstName(userProperties.getFirstName())
                .lastName(userProperties.getLastName())
                .role(userProperties.getRole() != null ? userProperties.getRole() : UserRole.ADMIN)
                .createdOn(LocalDateTime.now())
                .updatedOn(LocalDateTime.now())
                .build();

        userRepository.save(user);
        log.info("DataInitializer: Admin initialized");
    }

    private void initializeBusiness() {
        boolean businessExists = businessRepository.findAll().stream().anyMatch(b -> b.getName().equals(businessProperties.getName()));

        if (businessExists) {
            log.info("DataInitializer: Business skipped (already exists)");
            return;
        }

        Business business = Business.builder()
                .name(businessProperties.getName())
                .businessType(businessProperties.getType())
                .notificationsEnabled(true)
                .restockEnabled(true)
                .createdOn(LocalDateTime.now())
                .updatedOn(LocalDateTime.now())
                .build();

        businessRepository.save(business);
        log.info("DataInitializer: Business initialized");

        if (userProperties.getUsername() != null && !userProperties.getUsername().isEmpty()) {
            Optional<User> userOpt = userRepository.findByUsername(userProperties.getUsername());
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                user.setBusiness(business);
                userRepository.save(user);
            }
        }
    }
}

