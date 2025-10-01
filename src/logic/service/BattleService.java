package logic.service;

import logic.domain.ActionType;
import logic.domain.BattleResult;
import logic.domain.Unit;

//アプリケーション層: UseCase
//ターンごとの行動処理、およびバトルロジックの実行
public class BattleService { // UseCaseは〇〇Serviceや〇〇Interactorといった命名もされます

	private static final int VULCAN_EP_COST = 10;
	private static final int SABER_EP_COST = 5; // バルカンよりEP消費少ない
	private static final int DEFEND_EP_COST = 3;
	private static final int MOVE_EP_COST_PER_DISTANCE = 1; // 移動距離に応じて消費

	// 必殺技に必要なSPを計算するロジック (積載量に依存)
	public int calculateUltimateSkillSpCost(Unit unit) {
		// 例: 必殺技に必要なSP = 100 - (積載量 * 5)
		return 100 - (unit.getStatus().getPayloadCapacity() * 5);
	}

	// ターン開始処理
	public void executeStartTurn(Unit unit) {
		unit.startTurn();
	}

	// 行動実行のメインロジック
	public BattleResult executeAction(Unit self, Unit opponent, ActionType action, int value) {
		// 外部への依存(ログ出力など)はOutputPort/Gatewayに委譲すべきだが、簡略化のため直接記述

		switch (action) {
			case DO_NOTHING :
				// なにもしない: EP追加回復 (簡易的に回復速度の半分を追加)
				int additionalRecovery = self.getStatus().getEpRecoveryRate() / 2;
				// self.currentEp += additionalRecovery;
				self.setCurrentEp(self.getCurrentEp() + additionalRecovery);
				return new BattleResult(true, "なにもせず、EPを" + additionalRecovery + "追加回復");

			case MOVE :
				int distance = value; // valueは移動距離
				int cost = distance * MOVE_EP_COST_PER_DISTANCE;
				self.consumeEp(cost);
				self.move(distance);
				return new BattleResult(true, distance + "移動");

			case VULCAN :
				self.consumeEp(VULCAN_EP_COST);
				// 攻撃力(遠隔)を使用
				int damage = self.getStatus().getRangedAttackPower();
				opponent.receiveDamage(damage);
				return new BattleResult(true, "バルカン攻撃! " + damage + "ダメージ");

			case BEAM_SABER :
				// 近接攻撃なので、距離チェックは外部（Controller/Presenter）またはより上位のUseCaseで実施すべき
				self.consumeEp(SABER_EP_COST);
				// 攻撃力(近接)を使用 (バルカンより攻撃力高い)
				int saberDamage = self.getStatus().getCloseAttackPower() * 2; // 近接ボーナス
				opponent.receiveDamage(saberDamage);
				return new BattleResult(true, "ビームサーベル攻撃! " + saberDamage + "ダメージ");

			case DEFEND_EVADE :
				self.consumeEp(DEFEND_EP_COST);
				self.startDefending();
				return new BattleResult(true, "防御・回避態勢に移行。次回ダメージを大幅カット。");

			case ULTIMATE_SKILL :
				int spCost = calculateUltimateSkillSpCost(self);
				if (self.getCurrentSp() < spCost) {
					return new BattleResult(false, "SPが不足しています。");
				}
				// EP全消費
				int epConsumed = self.getCurrentEp();
				self.consumeEp(epConsumed);
				// self.currentSp -= spCost;
				self.setCurrentSp(self.getCurrentEp() - epConsumed);

				// 必殺技ダメージはEP消費量と攻撃力(近接/遠隔)の合計に依存するなど...
				int ultimateDamage = (epConsumed + self.getStatus().getCloseAttackPower()
						+ self.getStatus().getRangedAttackPower()) * 3;
				opponent.receiveDamage(ultimateDamage);
				return new BattleResult(true, "必殺技発動! " + ultimateDamage + "ダメージ。EP全消費。");

			default :
				return new BattleResult(false, "不明な行動");
		}
	}
}