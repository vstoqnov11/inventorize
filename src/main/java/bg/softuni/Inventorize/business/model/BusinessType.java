package bg.softuni.Inventorize.business.model;

public enum BusinessType {

    RESTAURANT("Restaurant"),
    CAFE("Cafe"),
    BAKERY("Bakery"),
    SUPERMARKET("Supermarket"),

    RETAIL("Retail"),
    BOOKSTORE("Bookstore"),
    ELECTRONICS("Electronics"),
    CLOTHING("Clothing"),
    FURNITURE("Furniture"),

    CINEMA("Cinema"),
    BARBERSHOP("Barbershop"),
    GAS_STATION("Gas Station"),
    PET_STORE("Pet Store"),
    AUTO_SERVICE("Auto Service"),
    BEAUTY_SALON("Beauty Salon"),

    INVENTORIZE("Inventorize");


    private final String displayName;

    BusinessType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
