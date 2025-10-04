package logic.domain;

public class Status {
    private String name;
    private int maxHp;
    private int epRecoveryRate;
    private int closeAttackPower;
    private int rangedAttackPower;
    private int payloadCapacity;

    public Status(String name, int maxHp, int epRecoveryRate, int closeAttackPower, int rangedAttackPower, int payloadCapacity) {
        this.name = name;
        this.maxHp = maxHp;
        this.epRecoveryRate = epRecoveryRate;
        this.closeAttackPower = closeAttackPower;
        this.rangedAttackPower = rangedAttackPower;
        this.payloadCapacity = payloadCapacity;
    }

    public String getName() {
        return name;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public int getEpRecoveryRate() {
        return epRecoveryRate;
    }

    public int getCloseAttackPower() {
        return closeAttackPower;
    }

    public int getRangedAttackPower() {
        return rangedAttackPower;
    }

    public int getPayloadCapacity() {
        return payloadCapacity;
    }
}
