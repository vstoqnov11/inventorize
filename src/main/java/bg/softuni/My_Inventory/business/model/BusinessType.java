package bg.softuni.My_Inventory.business.model;

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
    BEAUTY_SALON("Beauty Salon");

    private final String displayName;

    BusinessType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
