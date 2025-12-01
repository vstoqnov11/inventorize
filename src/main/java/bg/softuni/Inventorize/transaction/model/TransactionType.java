package bg.softuni.Inventorize.transaction.model;

public enum TransactionType {

    SALE("Sale"),
    DAMAGE("Damage"),
    RESTOCK("Restock");

    private final String displayName;

    TransactionType (String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName () {
        return displayName;
    }
}
