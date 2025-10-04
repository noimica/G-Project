package logic.domain;

public class BattleResult {
    private final boolean success;
    private final String message;

    public BattleResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
