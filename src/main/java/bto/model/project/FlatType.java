package bto.model.project;

/**
 * Enum representing the types of flats available in the BTO project.
 */
public enum FlatType {
    /**
     * Represents a 2-room flat.
     */
    TWO_ROOM("2-Room"),
    /**
     * Represents a 3-room flat.
     */
    THREE_ROOM("3-Room");

    private final String displayName;

    FlatType(String displayName) {
        this.displayName = displayName;
    }

    public static FlatType fromString(String type) {
        for (FlatType flatType : FlatType.values()) {
            if (flatType.displayName.equalsIgnoreCase(type)) {
                return flatType;
            }
        }
        throw new IllegalArgumentException("Unknown flat type: " + type);
    }

    public String getDisplayName() {
        return displayName;
    }
}
