package dto;

/**
 * A Data Transfer Object for unit information needed by the UI.
 */
public class UnitDTO {
    private final String name;
    private final int hp;
    private final int ep;
    private final int sp;

    public UnitDTO(String name, int hp, int ep, int sp) {
        this.name = name;
        this.hp = hp;
        this.ep = ep;
        this.sp = sp;
    }

    public String getName() {
        return name != null ? name : "";
    }

    public int getHp() {
        return hp;
    }

    public int getEp() {
        return ep;
    }

    public int getSp() {
        return sp;
    }
}
