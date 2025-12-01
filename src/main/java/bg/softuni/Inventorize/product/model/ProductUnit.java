package bg.softuni.Inventorize.product.model;

public enum ProductUnit {

    PIECE("Piece"),
    KG("Kilogram"),
    L("Liter"),
    BOX("Box"),
    CASE("Case"),
    PACK("Pack"),
    BOTTLE("Bottle"),
    CARTON("Carton");

    private final String displayName;

    ProductUnit(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
