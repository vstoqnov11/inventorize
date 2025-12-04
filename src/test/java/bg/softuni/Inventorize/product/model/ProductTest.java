package bg.softuni.Inventorize.product.model;

import bg.softuni.Inventorize.business.model.Business;
import bg.softuni.Inventorize.business.model.BusinessType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    private UUID productId;
    private UUID businessId;
    private Business business;
    private Product product;

    @BeforeEach
    void setUp() {
        productId = UUID.randomUUID();
        businessId = UUID.randomUUID();

        business = Business.builder()
                .id(businessId)
                .name("Test Business")
                .businessType(BusinessType.RETAIL)
                .build();
    }

    @Test
    void testProductBuilder_ShouldCreateProductWithAllFields() {
        LocalDate arrivalDate = LocalDate.now();
        LocalDate expiryDate = LocalDate.now().plusYears(1);

        product = Product.builder()
                .id(productId)
                .name("Test Product")
                .brand("Test Brand")
                .category(ProductCategory.ELECTRONICS)
                .quantity(100)
                .minStockThreshold(10)
                .maxStockThreshold(200)
                .unit(ProductUnit.PIECE)
                .price(29.99)
                .arrivalDate(arrivalDate)
                .expiryDate(expiryDate)
                .business(business)
                .build();

        assertNotNull(product);
        assertEquals(productId, product.getId());
        assertEquals("Test Product", product.getName());
        assertEquals("Test Brand", product.getBrand());
        assertEquals(ProductCategory.ELECTRONICS, product.getCategory());
        assertEquals(100, product.getQuantity());
        assertEquals(10, product.getMinStockThreshold());
        assertEquals(200, product.getMaxStockThreshold());
        assertEquals(ProductUnit.PIECE, product.getUnit());
        assertEquals(29.99, product.getPrice(), 0.01);
        assertEquals(arrivalDate, product.getArrivalDate());
        assertEquals(expiryDate, product.getExpiryDate());
        assertEquals(business, product.getBusiness());
    }

    @Test
    void testProductNoArgsConstructor_ShouldCreateEmptyProduct() {
        product = new Product();

        assertNotNull(product);
        assertNull(product.getId());
        assertNull(product.getName());
        assertNull(product.getBrand());
        assertNull(product.getCategory());
        assertEquals(0, product.getQuantity());
        assertEquals(0, product.getMinStockThreshold());
        assertEquals(0, product.getMaxStockThreshold());
    }

    @Test
    void testProductSettersAndGetters_ShouldSetAndGetValues() {
        product = new Product();
        LocalDate arrivalDate = LocalDate.now();
        LocalDate expiryDate = LocalDate.now().plusMonths(6);

        product.setId(productId);
        product.setName("Updated Product");
        product.setBrand("Updated Brand");
        product.setCategory(ProductCategory.FOOD_AND_BEVERAGES);
        product.setQuantity(150);
        product.setMinStockThreshold(20);
        product.setMaxStockThreshold(300);
        product.setUnit(ProductUnit.KG);
        product.setPrice(49.99);
        product.setArrivalDate(arrivalDate);
        product.setExpiryDate(expiryDate);
        product.setBusiness(business);

        assertEquals(productId, product.getId());
        assertEquals("Updated Product", product.getName());
        assertEquals("Updated Brand", product.getBrand());
        assertEquals(ProductCategory.FOOD_AND_BEVERAGES, product.getCategory());
        assertEquals(150, product.getQuantity());
        assertEquals(20, product.getMinStockThreshold());
        assertEquals(300, product.getMaxStockThreshold());
        assertEquals(ProductUnit.KG, product.getUnit());
        assertEquals(49.99, product.getPrice(), 0.01);
        assertEquals(arrivalDate, product.getArrivalDate());
        assertEquals(expiryDate, product.getExpiryDate());
        assertEquals(business, product.getBusiness());
    }

    @Test
    void testProductWithNullValues_ShouldHandleNulls() {
        product = Product.builder()
                .id(productId)
                .name("Test Product")
                .brand("Test Brand")
                .category(ProductCategory.ELECTRONICS)
                .build();

        assertNotNull(product);
        assertNull(product.getUnit());
        assertNull(product.getArrivalDate());
        assertNull(product.getExpiryDate());
        assertNull(product.getBusiness());
        assertEquals(0, product.getPrice(), 0.01);
    }

    @Test
    void testProductBusinessRelationship_ShouldSetBusiness() {
        product = Product.builder()
                .id(productId)
                .name("Test Product")
                .brand("Test Brand")
                .category(ProductCategory.ELECTRONICS)
                .business(business)
                .build();

        assertNotNull(product.getBusiness());
        assertEquals(businessId, product.getBusiness().getId());
        assertEquals("Test Business", product.getBusiness().getName());
    }
}

