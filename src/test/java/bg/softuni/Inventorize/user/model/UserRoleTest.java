package bg.softuni.Inventorize.user.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserRoleTest {

    @Test
    void testUserRoleDisplayNames_ShouldReturnCorrectDisplayNames() {
        assertEquals("Admin", UserRole.ADMIN.getDisplayName());
        assertEquals("Manager", UserRole.MANAGER.getDisplayName());
        assertEquals("Employee", UserRole.EMPLOYEE.getDisplayName());
    }

    @Test
    void testUserRoleValues_ShouldContainAllRoles() {
        UserRole[] values = UserRole.values();
        assertEquals(3, values.length);
    }

    @Test
    void testUserRoleValueOf_ShouldReturnCorrectRole() {
        assertEquals(UserRole.ADMIN, UserRole.valueOf("ADMIN"));
        assertEquals(UserRole.MANAGER, UserRole.valueOf("MANAGER"));
        assertEquals(UserRole.EMPLOYEE, UserRole.valueOf("EMPLOYEE"));
    }
}

