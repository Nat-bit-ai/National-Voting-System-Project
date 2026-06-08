package model;

public enum ElectionStatus {
    UPCOMING("Upcoming"),
    ACTIVE("Active"),
    CLOSED("Completed");

    private final String databaseValue;

    ElectionStatus(String databaseValue) {
        this.databaseValue = databaseValue;
    }

    public String databaseValue() {
        return databaseValue;
    }

    public static ElectionStatus fromDatabaseValue(String value) {
        for (ElectionStatus status : values()) {
            if (status.databaseValue.equalsIgnoreCase(value) || status.name().equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown election status: " + value);
    }
}
