package bto.model.project;

/**
 * Enum representing the types of flats available in the BTO project.
 */
public enum FlatType {
    /**
     * Represents a 2-room flat.
     */
    TWO_ROOM_FLAT("2 Room Flat"),
    /**
     * Represents a 3-room flat.
     */
    THREE_ROOM_FLAT("3 Room Flat");

    private final String displayName;

    FlatType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
