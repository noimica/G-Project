package logic.domain;

//インターフェース層: DTO (Presenter)
//AIに公開するUnitの読み取り専用情報
public class UnitState {
	private final int hp;
	private final int ep;
	private final int sp;
	private final int position;
	private final int maxHp;
	private final int closeAttackPower;
	// ... (必要なゲッターメソッドのみ提供)

	public UnitState(Unit unit) {
		this.hp = unit.getCurrentHp();
		this.ep = unit.getCurrentEp();
		this.sp = unit.getCurrentSp();
		this.position = unit.getPosition();
		this.maxHp = unit.getStatus().getMaxHp();
		this.closeAttackPower = unit.getStatus().getCloseAttackPower();
	}

	// ゲッターのみ提供 (AIから状態を変更できないようにする)
	public int getHp() {
		return hp;
	}
	public int getEp() {
		return ep;
	}
	public int getSp() {
		return sp;
	}
	public int getPosition() {
		return position;
	}
	public int getMaxHp() {
		return maxHp;
	}
	public int getCloseAttackPower() {
		return closeAttackPower;
	}
}