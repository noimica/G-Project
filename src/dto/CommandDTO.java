package dto;

/**
 * A Data Transfer Object for command information needed by the UI.
 */
public class CommandDTO {
    private final String id; // e.g., the enum name "ATTACK"
    private final String displayName;

    public CommandDTO(String id, String displayName) {
        this.id = id;
        this.displayName = displayName;
    }

    public String getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }
}
