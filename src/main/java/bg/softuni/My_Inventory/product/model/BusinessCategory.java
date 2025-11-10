package bg.softuni.My_Inventory.product.model;

public enum BusinessCategory {

    FOOD("Food"),
    MEDICINE("Medicine"),
    RETAIL("Retail"),
    SERVICES("Services"),
    ENTERTAINMENT("Entertainment"),
    PETCARE("Petcare");

    private final String displayName;

    BusinessCategory (String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName () {
        return displayName;
    }
}
