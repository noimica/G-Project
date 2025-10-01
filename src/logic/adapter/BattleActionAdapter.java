package logic.adapter;

import logic.domain.ActionType;
import logic.domain.BattleResult;
import logic.domain.Unit;
import logic.service.BattleService;

// ActionIssuerインターフェースを実装し、AIのコールをユースケースに変換するアダプタ
public class BattleActionAdapter implements ActionIssuer {
	private final BattleService battleService;
	private final Unit self;
	private final Unit opponent;
	private BattleResult lastResult;

	// EP消費コストは本来ユースケース/ドメイン層で定義すべきだが、分かりやすさのため一旦仮の値を設定
	// 実際の実装では、BattleServiceまたはUnitからこれらのコストを取得するのが望ましい
	private static final int MOVE_EP_COST_PER_DISTANCE = 1;

	public BattleActionAdapter(BattleService battleService, Unit self, Unit opponent) {
		this.battleService = battleService;
		this.self = self;
		this.opponent = opponent;
	}

	// 実行結果を保持する (Controller/Presenterでの表示に利用)
	public BattleResult getLastResult() {
		return lastResult;
	}

	/**
	 * ActionIssuerインターフェースの実装 AI (BattleLogic) から呼び出される抽象的な行動を、 具体的な ActionType
	 * と Value に変換し、BattleServiceに実行させる。
	 */

	@Override
	public void doNothing() {
		// DO_NOTHING は追加回復の特殊行動のため、Valueは0でOK
		this.lastResult = battleService.executeAction(self, opponent, ActionType.DO_NOTHING, 0);
	}

	@Override
	public void move(int distance) {
		// 移動行動には距離(Value)が必要
		// Note: EP消費チェックはBattleService内で行われる
		this.lastResult = battleService.executeAction(self, opponent, ActionType.MOVE, distance);
	}

	@Override
	public void vulcan() {
		// バルカン (遠隔) はValue不要
		this.lastResult = battleService.executeAction(self, opponent, ActionType.VULCAN, 0);
	}

	@Override
	public void beamSaber() {
		// ビームサーベル (近接) はValue不要
		this.lastResult = battleService.executeAction(self, opponent, ActionType.BEAM_SABER, 0);
	}

	@Override
	public void defendEvade() {
		// 防御・回避はValue不要
		this.lastResult = battleService.executeAction(self, opponent, ActionType.DEFEND_EVADE, 0);
	}

	@Override
	public void weaponDeployment() {
		// 武装展開の具体的な仕様がないため、Valueは0とする
		this.lastResult = battleService.executeAction(self, opponent, ActionType.WEAPON_DEPLOYMENT, 0);
	}

	@Override
	public void ultimateSkill() {
		// 必殺技はValue不要 (EP全消費はBattleService側で処理)
		this.lastResult = battleService.executeAction(self, opponent, ActionType.ULTIMATE_SKILL, 0);
	}
}