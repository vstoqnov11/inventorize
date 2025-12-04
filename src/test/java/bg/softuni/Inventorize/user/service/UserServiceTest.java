package bg.softuni.Inventorize.user.service;

import bg.softuni.Inventorize.business.model.Business;
import bg.softuni.Inventorize.business.model.BusinessType;
import bg.softuni.Inventorize.exception.UserNotFoundException;
import bg.softuni.Inventorize.exception.UsernameAlreadyTakenException;
import bg.softuni.Inventorize.security.UserData;
import bg.softuni.Inventorize.user.model.User;
import bg.softuni.Inventorize.user.model.UserRole;
import bg.softuni.Inventorize.user.repository.UserRepository;
import bg.softuni.Inventorize.web.dto.EditProfileRequest;
import bg.softuni.Inventorize.web.dto.NewEmployeeRequest;
import bg.softuni.Inventorize.web.dto.RegisterRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private UUID userId;
    private User user;
    private Business business;
    private RegisterRequest registerRequest;
    private NewEmployeeRequest newEmployeeRequest;
    private EditProfileRequest editProfileRequest;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        UUID businessId = UUID.randomUUID();

        business = Business.builder()
                .id(businessId)
                .name("Test Business")
                .businessType(BusinessType.RETAIL)
                .build();

        user = User.builder()
                .id(userId)
                .username("testuser")
                .password("encodedPassword")
                .firstName("Test")
                .lastName("User")
                .email("test@example.com")
                .phoneNumber("1234567890")
                .role(UserRole.MANAGER)
                .business(business)
                .createdOn(LocalDateTime.now())
                .updatedOn(LocalDateTime.now())
                .build();

        registerRequest = RegisterRequest.builder()
                .username("newuser")
                .password("password123")
                .firstName("New")
                .lastName("User")
                .email("newuser@example.com")
                .build();

        newEmployeeRequest = NewEmployeeRequest.builder()
                .username("employee")
                .password("password123")
                .firstName("Employee")
                .lastName("Name")
                .email("employee@example.com")
                .phoneNumber("1111111111")
                .build();

        editProfileRequest = EditProfileRequest.builder()
                .firstName("Updated")
                .lastName("Name")
                .email("updated@example.com")
                .phoneNumber("0987654321")
                .build();
    }

    @Test
    void testRegister_WithNewUsername_ShouldCreateUser() {
        when(userRepository.findByUsername("newuser")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User savedUser = invocation.getArgument(0);
            savedUser.setId(userId);
            return savedUser;
        });

        userService.register(registerRequest);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).findByUsername("newuser");
        verify(passwordEncoder).encode("password123");
        verify(userRepository).save(userCaptor.capture());

        User capturedUser = userCaptor.getValue();
        assertEquals("newuser", capturedUser.getUsername());
        assertEquals("encodedPassword", capturedUser.getPassword());
        assertEquals("New", capturedUser.getFirstName());
        assertEquals("User", capturedUser.getLastName());
        assertEquals("newuser@example.com", capturedUser.getEmail());
        assertEquals(UserRole.MANAGER, capturedUser.getRole());
        assertNotNull(capturedUser.getCreatedOn());
        assertNotNull(capturedUser.getUpdatedOn());
    }

    @Test
    void testRegister_WithExistingUsername_ShouldThrowException() {
        when(userRepository.findByUsername("newuser")).thenReturn(Optional.of(user));

        assertThrows(UsernameAlreadyTakenException.class, () -> userService.register(registerRequest));
        verify(userRepository).findByUsername("newuser");
        verify(userRepository, never()).save(any());
    }

    @Test
    void testLoadUserByUsername_WhenUserExists_ShouldReturnUserData() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        UserDetails result = userService.loadUserByUsername("testuser");

        assertNotNull(result);
        assertInstanceOf(UserData.class, result);
        assertEquals("testuser", result.getUsername());
        assertEquals("encodedPassword", result.getPassword());
        verify(userRepository).findByUsername("testuser");
    }

    @Test
    void testLoadUserByUsername_WhenUserNotFound_ShouldThrowException() {
        when(userRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername("nonexistent"));
        verify(userRepository).findByUsername("nonexistent");
    }

    @Test
    void testFindByUsername_WhenUserExists_ShouldReturnUser() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        User result = userService.findByUsername("testuser");

        assertNotNull(result);
        assertEquals(user, result);
        verify(userRepository).findByUsername("testuser");
    }

    @Test
    void testFindByUsername_WhenUserNotFound_ShouldThrowException() {
        when(userRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.findByUsername("nonexistent"));
        verify(userRepository).findByUsername("nonexistent");
    }

    @Test
    void testUserHasBusiness_WhenUserHasBusiness_ShouldReturnTrue() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        boolean result = userService.userHasBusiness("testuser");

        assertTrue(result);
        verify(userRepository).findByUsername("testuser");
    }

    @Test
    void testUserHasBusiness_WhenUserHasNoBusiness_ShouldReturnFalse() {
        user.setBusiness(null);
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        boolean result = userService.userHasBusiness("testuser");

        assertFalse(result);
        verify(userRepository).findByUsername("testuser");
    }

    @Test
    void testAddBusiness_ShouldAddBusinessToUser() {
        Business newBusiness = Business.builder()
                .id(UUID.randomUUID())
                .name("New Business")
                .businessType(BusinessType.SUPERMARKET)
                .build();

        user.setBusiness(null);
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        userService.addBusiness(newBusiness, "testuser");

        verify(userRepository).findByUsername("testuser");
        verify(userRepository).save(user);
        assertEquals(newBusiness, user.getBusiness());
    }

    @Test
    void testGetAllUsers_ShouldReturnPageOfUsers() {
        List<User> users = new ArrayList<>();
        users.add(user);
        Page<User> userPage = new PageImpl<>(users);
        Pageable pageable = PageRequest.of(0, 10);

        when(userRepository.findAll(pageable)).thenReturn(userPage);

        Page<User> result = userService.getAllUsers(0);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(user, result.getContent().get(0));
        verify(userRepository).findAll(pageable);
    }

    @Test
    void testGetById_WhenUserExists_ShouldReturnUser() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        User result = userService.getById(userId);

        assertNotNull(result);
        assertEquals(user, result);
        verify(userRepository).findById(userId);
    }

    @Test
    void testGetById_WhenUserNotFound_ShouldThrowException() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getById(userId));
        verify(userRepository).findById(userId);
    }

    @Test
    void testUpdateProfile_ShouldUpdateUserProfile() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        userService.updateProfile(editProfileRequest, userId);

        verify(userRepository).findById(userId);
        verify(userRepository).save(user);
        assertEquals("Updated", user.getFirstName());
        assertEquals("Name", user.getLastName());
        assertEquals("updated@example.com", user.getEmail());
        assertEquals("0987654321", user.getPhoneNumber());
        assertNotNull(user.getUpdatedOn());
    }

    @Test
    void testGetAllEmployees_ShouldReturnPageOfEmployees() {
        List<User> employees = new ArrayList<>();
        employees.add(user);
        Page<User> employeePage = new PageImpl<>(employees);
        Pageable pageable = PageRequest.of(0, 10);
        UUID businessId = business.getId();

        when(userRepository.findAllByBusinessId(businessId, pageable)).thenReturn(employeePage);

        Page<User> result = userService.getAllEmployees(0, businessId);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(user, result.getContent().get(0));
        verify(userRepository).findAllByBusinessId(businessId, pageable);
    }

    @Test
    void testCreateEmployee_ShouldCreateEmployee() {
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User savedUser = invocation.getArgument(0);
            savedUser.setId(userId);
            return savedUser;
        });

        userService.createEmployee(newEmployeeRequest, business);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(passwordEncoder).encode("password123");
        verify(userRepository).save(userCaptor.capture());

        User capturedUser = userCaptor.getValue();
        assertEquals("employee", capturedUser.getUsername());
        assertEquals("encodedPassword", capturedUser.getPassword());
        assertEquals("Employee", capturedUser.getFirstName());
        assertEquals("Name", capturedUser.getLastName());
        assertEquals("employee@example.com", capturedUser.getEmail());
        assertEquals("1111111111", capturedUser.getPhoneNumber());
        assertEquals(UserRole.EMPLOYEE, capturedUser.getRole());
        assertEquals(business, capturedUser.getBusiness());
        assertNotNull(capturedUser.getCreatedOn());
        assertNotNull(capturedUser.getUpdatedOn());
    }

    @Test
    void testDelete_ShouldDeleteUser() {
        doNothing().when(userRepository).deleteById(userId);

        userService.delete(userId);

        verify(userRepository).deleteById(userId);
    }
}

