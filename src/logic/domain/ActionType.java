package logic.domain;

public enum ActionType {
    DO_NOTHING("何もしない"),
    MOVE("移動"),
    VULCAN("バルカン"),
    BEAM_SABER("ビームサーベル"),
    DEFEND_EVADE("防御・回避"),
    WEAPON_DEPLOYMENT("武装展開"),
    ULTIMATE_SKILL("必殺技");

    private final String displayName;

    ActionType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
