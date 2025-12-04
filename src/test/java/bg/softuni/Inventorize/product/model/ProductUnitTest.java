package bg.softuni.Inventorize.product.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductUnitTest {

    @Test
    void testProductUnitDisplayNames_ShouldReturnCorrectDisplayNames() {
        assertEquals("Piece", ProductUnit.PIECE.getDisplayName());
        assertEquals("Kilogram", ProductUnit.KG.getDisplayName());
        assertEquals("Liter", ProductUnit.L.getDisplayName());
        assertEquals("Box", ProductUnit.BOX.getDisplayName());
        assertEquals("Case", ProductUnit.CASE.getDisplayName());
        assertEquals("Pack", ProductUnit.PACK.getDisplayName());
        assertEquals("Bottle", ProductUnit.BOTTLE.getDisplayName());
        assertEquals("Carton", ProductUnit.CARTON.getDisplayName());
    }

    @Test
    void testProductUnitValues_ShouldContainAllUnits() {
        ProductUnit[] values = ProductUnit.values();
        assertEquals(8, values.length);
    }

    @Test
    void testProductUnitValueOf_ShouldReturnCorrectUnit() {
        assertEquals(ProductUnit.PIECE, ProductUnit.valueOf("PIECE"));
        assertEquals(ProductUnit.KG, ProductUnit.valueOf("KG"));
        assertEquals(ProductUnit.L, ProductUnit.valueOf("L"));
        assertEquals(ProductUnit.BOTTLE, ProductUnit.valueOf("BOTTLE"));
    }
}

