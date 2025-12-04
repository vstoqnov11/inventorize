package bg.softuni.Inventorize.product.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductCategoryTest {

    @Test
    void testProductCategoryDisplayNames_ShouldReturnCorrectDisplayNames() {
        assertEquals("Electronics", ProductCategory.ELECTRONICS.getDisplayName());
        assertEquals("Food & Beverages", ProductCategory.FOOD_AND_BEVERAGES.getDisplayName());
        assertEquals("Clothing", ProductCategory.CLOTHING.getDisplayName());
        assertEquals("Furniture", ProductCategory.FURNITURE.getDisplayName());
        assertEquals("Health & Beauty", ProductCategory.HEALTH_AND_BEAUTY.getDisplayName());
        assertEquals("Sports & Outdoors", ProductCategory.SPORTS_AND_OUTDOORS.getDisplayName());
        assertEquals("Books & Media", ProductCategory.BOOKS_AND_MEDIA.getDisplayName());
        assertEquals("Automotive", ProductCategory.AUTOMOTIVE.getDisplayName());
        assertEquals("Tools & Hardware", ProductCategory.TOOLS_AND_HARDWARE.getDisplayName());
        assertEquals("Other", ProductCategory.OTHER.getDisplayName());
    }

    @Test
    void testProductCategoryValues_ShouldContainAllCategories() {
        ProductCategory[] values = ProductCategory.values();
        assertEquals(10, values.length);
    }

    @Test
    void testProductCategoryValueOf_ShouldReturnCorrectCategory() {
        assertEquals(ProductCategory.ELECTRONICS, ProductCategory.valueOf("ELECTRONICS"));
        assertEquals(ProductCategory.FOOD_AND_BEVERAGES, ProductCategory.valueOf("FOOD_AND_BEVERAGES"));
        assertEquals(ProductCategory.OTHER, ProductCategory.valueOf("OTHER"));
    }
}

