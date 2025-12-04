package bg.softuni.Inventorize.business.model;

import bg.softuni.Inventorize.product.model.Product;
import bg.softuni.Inventorize.user.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class BusinessTest {

    private UUID businessId;
    private Business business;

    @BeforeEach
    void setUp() {
        businessId = UUID.randomUUID();
    }

    @Test
    void testBusinessBuilder_ShouldCreateBusinessWithAllFields() {
        LocalDateTime now = LocalDateTime.now();
        business = Business.builder()
                .id(businessId)
                .name("Test Business")
                .businessType(BusinessType.RETAIL)
                .address("123 Test St")
                .phoneNumber("1234567890")
                .email("test@example.com")
                .createdOn(now)
                .updatedOn(now)
                .notificationsEnabled(true)
                .build();

        assertNotNull(business);
        assertEquals(businessId, business.getId());
        assertEquals("Test Business", business.getName());
        assertEquals(BusinessType.RETAIL, business.getBusinessType());
        assertEquals("123 Test St", business.getAddress());
        assertEquals("1234567890", business.getPhoneNumber());
        assertEquals("test@example.com", business.getEmail());
        assertEquals(now, business.getCreatedOn());
        assertEquals(now, business.getUpdatedOn());
        assertTrue(business.getNotificationsEnabled());
    }

    @Test
    void testBusinessNoArgsConstructor_ShouldCreateEmptyBusiness() {
        business = new Business();

        assertNotNull(business);
        assertNull(business.getId());
        assertNull(business.getName());
        assertNull(business.getBusinessType());
    }

    @Test
    void testBusinessSettersAndGetters_ShouldSetAndGetValues() {
        business = new Business();
        LocalDateTime now = LocalDateTime.now();

        business.setId(businessId);
        business.setName("New Business");
        business.setBusinessType(BusinessType.SUPERMARKET);
        business.setAddress("456 New St");
        business.setPhoneNumber("0987654321");
        business.setEmail("new@example.com");
        business.setCreatedOn(now);
        business.setUpdatedOn(now);
        business.setNotificationsEnabled(false);

        assertEquals(businessId, business.getId());
        assertEquals("New Business", business.getName());
        assertEquals(BusinessType.SUPERMARKET, business.getBusinessType());
        assertEquals("456 New St", business.getAddress());
        assertEquals("0987654321", business.getPhoneNumber());
        assertEquals("new@example.com", business.getEmail());
        assertEquals(now, business.getCreatedOn());
        assertEquals(now, business.getUpdatedOn());
        assertFalse(business.getNotificationsEnabled());
    }

    @Test
    void testBusinessRelationships_ShouldSetEmployeesAndProducts() {
        business = Business.builder()
                .id(businessId)
                .name("Test Business")
                .businessType(BusinessType.RETAIL)
                .build();

        List<User> employees = new ArrayList<>();
        List<Product> products = new ArrayList<>();

        User user = User.builder()
                .id(UUID.randomUUID())
                .username("testuser")
                .business(business)
                .build();

        Product product = Product.builder()
                .id(UUID.randomUUID())
                .name("Test Product")
                .brand("Test Brand")
                .business(business)
                .build();

        employees.add(user);
        products.add(product);

        business.setEmployees(employees);
        business.setProducts(products);

        assertNotNull(business.getEmployees());
        assertNotNull(business.getProducts());
        assertEquals(1, business.getEmployees().size());
        assertEquals(1, business.getProducts().size());
        assertEquals(user, business.getEmployees().get(0));
        assertEquals(product, business.getProducts().get(0));
    }

    @Test
    void testBusinessWithNullValues_ShouldHandleNulls() {
        business = Business.builder()
                .id(businessId)
                .name("Test Business")
                .businessType(BusinessType.RETAIL)
                .build();

        assertNotNull(business);
        assertNull(business.getAddress());
        assertNull(business.getPhoneNumber());
        assertNull(business.getEmail());
        assertNull(business.getCreatedOn());
        assertNull(business.getUpdatedOn());
        assertNull(business.getNotificationsEnabled());
    }
}

