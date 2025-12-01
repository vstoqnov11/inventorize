package bg.softuni.Inventorize.product.model;

public enum ProductCategory {

    ELECTRONICS("Electronics"),
    FOOD_AND_BEVERAGES("Food & Beverages"),
    CLOTHING("Clothing"),
    FURNITURE("Furniture"),
    HEALTH_AND_BEAUTY("Health & Beauty"),
    SPORTS_AND_OUTDOORS("Sports & Outdoors"),
    BOOKS_AND_MEDIA("Books & Media"),
    AUTOMOTIVE("Automotive"),
    TOOLS_AND_HARDWARE("Tools & Hardware"),
    OTHER("Other");

    private final String displayName;

    ProductCategory (String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName () {
        return displayName;
    }
}
