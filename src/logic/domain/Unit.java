package logic.domain;

//ドメイン層: Entity
//バトル中の機体の状態を管理するアグリゲートルート
public class Unit {
	private final Status status;
	private int currentHp;
	private int currentEp; // Energy Power
	private int currentSp; // Special Point
	private int position; // 軸が1方向のバトルなので、位置情報（距離）
	private boolean isDefending; // 防御・回避状態か

	// 初期HP, EP, SPは別途初期設定サービスなどで決定
	public Unit(Status status, int initialHp, int initialEp, int initialSp, int initialPosition) {
		this.status = status;
		this.currentHp = initialHp;
		this.currentEp = initialEp;
		this.currentSp = initialSp;
		this.position = initialPosition;
		this.isDefending = false;
	}

	// ターン開始時の処理 (EP回復など)
	public void startTurn() {
		// EP回復 (ターン経過で回復)
		this.currentEp += status.getEpRecoveryRate();
		this.isDefending = false; // 防御状態をリセット
		// ... (上限チェックなど)
	}

	// ダメージを受ける処理
	public void receiveDamage(int rawDamage) {
		// 防御・回避によるダメージ軽減ロジック (簡易的な例)
		double damageMultiplier = this.isDefending ? 0.3 : 1.0;
		int effectiveDamage = (int) (rawDamage * damageMultiplier);

		this.currentHp -= effectiveDamage;
		if (this.currentHp < 0)
			this.currentHp = 0;

		// SP回復 (ダメージを受けると回復)
		int spRecovery = calculateSpRecovery(effectiveDamage);
		this.currentSp += spRecovery;
		// ... (上限チェックなど)
	}

	// SP回復量の計算ロジック (HPが低いほど回復量が増える)
	private int calculateSpRecovery(int receivedDamage) {
		// 例: 受けたダメージ * (基本係数 + 低HPボーナス)
		double lowHpBonus = 1.0 - ((double) this.currentHp / this.status.getMaxHp()); // HPが低いほど0から1に近づく
		return (int) (receivedDamage * 0.1 * (1 + lowHpBonus)); // SP回復の最適解防止のため係数は抑える
	}

	// EP消費の抽象的なメソッド
	public void consumeEp(int cost) {
		if (this.currentEp < cost) {
			throw new IllegalStateException("EPが不足しています");
		}
		this.currentEp -= cost;
	}

	// 移動処理
	public void move(int distance) {
		this.position += distance;
		// ... (位置の有効範囲チェックなど)
	}

	// 防御/回避状態にする
	public void startDefending() {
		this.isDefending = true;
		// EP消費ロジックはUseCase側で実行
	}

	// ゲッターメソッド (カプセル化のため、必要に応じて提供)
	public Status getStatus() {
		return status;
	}
	public int getCurrentHp() {
		return currentHp;
	}
	public int getCurrentEp() {
		return currentEp;
	}
	public int getCurrentSp() {
		return currentSp;
	}
	public int getPosition() {
		return position;
	}
	public boolean isDefending() {
		return isDefending;
	}

	/**
	 * @param currentHp
	 *            セットする currentHp
	 */
	public void setCurrentHp(int currentHp) {
		this.currentHp = currentHp;
	}

	/**
	 * @param currentEp
	 *            セットする currentEp
	 */
	public void setCurrentEp(int currentEp) {
		this.currentEp = currentEp;
	}

	/**
	 * @param currentSp
	 *            セットする currentSp
	 */
	public void setCurrentSp(int currentSp) {
		this.currentSp = currentSp;
	}

	/**
	 * @param position
	 *            セットする position
	 */
	public void setPosition(int position) {
		this.position = position;
	}

	/**
	 * @param isDefending
	 *            セットする isDefending
	 */
	public void setDefending(boolean isDefending) {
		this.isDefending = isDefending;
	}
}