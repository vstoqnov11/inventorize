package bg.softuni.Inventorize.user.model;

import bg.softuni.Inventorize.business.model.Business;
import bg.softuni.Inventorize.business.model.BusinessType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private UUID userId;
    private UUID businessId;
    private Business business;
    private User user;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        businessId = UUID.randomUUID();

        business = Business.builder()
                .id(businessId)
                .name("Test Business")
                .businessType(BusinessType.RETAIL)
                .build();
    }

    @Test
    void testUserBuilder_ShouldCreateUserWithAllFields() {
        LocalDateTime now = LocalDateTime.now();

        user = User.builder()
                .id(userId)
                .username("testuser")
                .password("encodedPassword")
                .firstName("Test")
                .lastName("User")
                .email("test@example.com")
                .phoneNumber("1234567890")
                .role(UserRole.MANAGER)
                .createdOn(now)
                .updatedOn(now)
                .business(business)
                .build();

        assertNotNull(user);
        assertEquals(userId, user.getId());
        assertEquals("testuser", user.getUsername());
        assertEquals("encodedPassword", user.getPassword());
        assertEquals("Test", user.getFirstName());
        assertEquals("User", user.getLastName());
        assertEquals("test@example.com", user.getEmail());
        assertEquals("1234567890", user.getPhoneNumber());
        assertEquals(UserRole.MANAGER, user.getRole());
        assertEquals(now, user.getCreatedOn());
        assertEquals(now, user.getUpdatedOn());
        assertEquals(business, user.getBusiness());
    }

    @Test
    void testUserNoArgsConstructor_ShouldCreateEmptyUser() {
        user = new User();

        assertNotNull(user);
        assertNull(user.getId());
        assertNull(user.getUsername());
        assertNull(user.getPassword());
        assertNull(user.getFirstName());
        assertNull(user.getLastName());
        assertNull(user.getRole());
    }

    @Test
    void testUserSettersAndGetters_ShouldSetAndGetValues() {
        user = new User();
        LocalDateTime now = LocalDateTime.now();

        user.setId(userId);
        user.setUsername("newuser");
        user.setPassword("newPassword");
        user.setFirstName("New");
        user.setLastName("User");
        user.setEmail("newuser@example.com");
        user.setPhoneNumber("0987654321");
        user.setRole(UserRole.EMPLOYEE);
        user.setCreatedOn(now);
        user.setUpdatedOn(now);
        user.setBusiness(business);

        assertEquals(userId, user.getId());
        assertEquals("newuser", user.getUsername());
        assertEquals("newPassword", user.getPassword());
        assertEquals("New", user.getFirstName());
        assertEquals("User", user.getLastName());
        assertEquals("newuser@example.com", user.getEmail());
        assertEquals("0987654321", user.getPhoneNumber());
        assertEquals(UserRole.EMPLOYEE, user.getRole());
        assertEquals(now, user.getCreatedOn());
        assertEquals(now, user.getUpdatedOn());
        assertEquals(business, user.getBusiness());
    }

    @Test
    void testUserWithNullValues_ShouldHandleNulls() {
        user = User.builder()
                .id(userId)
                .username("testuser")
                .password("password")
                .firstName("Test")
                .lastName("User")
                .role(UserRole.ADMIN)
                .build();

        assertNotNull(user);
        assertNull(user.getEmail());
        assertNull(user.getPhoneNumber());
        assertNull(user.getCreatedOn());
        assertNull(user.getUpdatedOn());
        assertNull(user.getBusiness());
    }

    @Test
    void testUserBusinessRelationship_ShouldSetBusiness() {
        user = User.builder()
                .id(userId)
                .username("testuser")
                .password("password")
                .firstName("Test")
                .lastName("User")
                .role(UserRole.MANAGER)
                .business(business)
                .build();

        assertNotNull(user.getBusiness());
        assertEquals(businessId, user.getBusiness().getId());
        assertEquals("Test Business", user.getBusiness().getName());
    }

    @Test
    void testUserRoles_ShouldSetAllRoles() {
        user = User.builder()
                .id(userId)
                .username("admin")
                .password("password")
                .firstName("Admin")
                .lastName("User")
                .role(UserRole.ADMIN)
                .build();

        assertEquals(UserRole.ADMIN, user.getRole());

        user.setRole(UserRole.MANAGER);
        assertEquals(UserRole.MANAGER, user.getRole());

        user.setRole(UserRole.EMPLOYEE);
        assertEquals(UserRole.EMPLOYEE, user.getRole());
    }
}

