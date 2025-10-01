// ドメイン層: Entity
// バトルにおける機体の静的な能力値
package logic.domain;

public class Status {
	private final int maxHp;
	private final int epRecoveryRate; // EPの回復速度
	private final int closeAttackPower; // 攻撃力(近接)
	private final int rangedAttackPower; // 攻撃力(遠隔)
	// 防御力は今回は「なし」とする仕様のため除外
	private final int mobility; // 機動性
	private final int payloadCapacity; // 積載量 (必殺技の積む量)

	public Status(int maxHp, int epRecoveryRate, int closeAttackPower,
			int rangedAttackPower, int mobility, int payloadCapacity) {
		// ... (省略: バリデーションなど)
		this.maxHp = maxHp;
		this.epRecoveryRate = epRecoveryRate;
		this.closeAttackPower = closeAttackPower;
		this.rangedAttackPower = rangedAttackPower;
		this.mobility = mobility;
		this.payloadCapacity = payloadCapacity;
	}

	// ゲッターメソッド
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
	public int getMobility() {
		return mobility;
	}
	public int getPayloadCapacity() {
		return payloadCapacity;
	}
}