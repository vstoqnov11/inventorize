package bg.softuni.Inventorize.business.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BusinessTypeTest {

    @Test
    void testBusinessTypeDisplayNames_ShouldReturnCorrectDisplayNames() {
        assertEquals("Restaurant", BusinessType.RESTAURANT.getDisplayName());
        assertEquals("Cafe", BusinessType.CAFE.getDisplayName());
        assertEquals("Bakery", BusinessType.BAKERY.getDisplayName());
        assertEquals("Supermarket", BusinessType.SUPERMARKET.getDisplayName());
        assertEquals("Retail", BusinessType.RETAIL.getDisplayName());
        assertEquals("Bookstore", BusinessType.BOOKSTORE.getDisplayName());
        assertEquals("Electronics", BusinessType.ELECTRONICS.getDisplayName());
        assertEquals("Clothing", BusinessType.CLOTHING.getDisplayName());
        assertEquals("Furniture", BusinessType.FURNITURE.getDisplayName());
        assertEquals("Cinema", BusinessType.CINEMA.getDisplayName());
        assertEquals("Barbershop", BusinessType.BARBERSHOP.getDisplayName());
        assertEquals("Gas Station", BusinessType.GAS_STATION.getDisplayName());
        assertEquals("Pet Store", BusinessType.PET_STORE.getDisplayName());
        assertEquals("Auto Service", BusinessType.AUTO_SERVICE.getDisplayName());
        assertEquals("Beauty Salon", BusinessType.BEAUTY_SALON.getDisplayName());
        assertEquals("Inventorize", BusinessType.INVENTORIZE.getDisplayName());
    }

    @Test
    void testBusinessTypeValues_ShouldContainAllTypes() {
        BusinessType[] values = BusinessType.values();
        assertEquals(16, values.length);
    }

    @Test
    void testBusinessTypeValueOf_ShouldReturnCorrectType() {
        assertEquals(BusinessType.RESTAURANT, BusinessType.valueOf("RESTAURANT"));
        assertEquals(BusinessType.RETAIL, BusinessType.valueOf("RETAIL"));
        assertEquals(BusinessType.INVENTORIZE, BusinessType.valueOf("INVENTORIZE"));
    }
}

