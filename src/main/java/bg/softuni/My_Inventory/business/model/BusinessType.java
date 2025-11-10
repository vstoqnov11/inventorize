package bg.softuni.My_Inventory.business.model;

import bg.softuni.My_Inventory.product.model.BusinessCategory;

public enum BusinessType {

    RESTAURANT("Restaurant", BusinessCategory.FOOD),
    CAFE("Cafe", BusinessCategory.FOOD),
    BAKERY("Bakery", BusinessCategory.FOOD),
    SUPERMARKET("Supermarket", BusinessCategory.FOOD),

    PHARMACY("Pharmacy", BusinessCategory.MEDICINE),
    HOSPITAL("Hospital", BusinessCategory.MEDICINE),

    RETAIL("Retail", BusinessCategory.RETAIL),
    BOOKSTORE("Bookstore", BusinessCategory.RETAIL),
    ELECTRONICS("Electronics", BusinessCategory.RETAIL),
    CLOTHING("Clothing", BusinessCategory.RETAIL),
    FURNITURE("Furniture", BusinessCategory.RETAIL),

    CINEMA("Cinema", BusinessCategory.ENTERTAINMENT),

    BARBERSHOP("Barbershop", BusinessCategory.SERVICES),
    GAS_STATION("Gas Station", BusinessCategory.SERVICES),
    PET_STORE("Pet Store", BusinessCategory.SERVICES),
    AUTO_SERVICE("Auto Service", BusinessCategory.SERVICES),
    BEAUTY_SALON("Beauty Salon", BusinessCategory.SERVICES);

    private final String displayName;
    private final BusinessCategory category;

    BusinessType(String displayName, BusinessCategory category) {
        this.displayName = displayName;
        this.category = category;
    }

    public String getDisplayName() {
        return displayName;
    }

    public BusinessCategory getCategory () {
        return category;
    }
}
