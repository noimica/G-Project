package logic.controller;

import logic.adapter.BattleActionAdapter;
import logic.domain.Unit;
import logic.domain.UnitState;
import logic.gundom.BattleLogic;
import logic.service.BattleService;

//インターフェース層: Controller (ゲームエンジン部分)
public class GameEngine {
	private final BattleService battleService;
	private final BattleLogic playerALogic; // ユーザーが実装するAI
	private final Unit unitA;
	private final Unit unitB;

	public GameEngine(BattleService battleService, BattleLogic playerALogic, Unit unitA, Unit unitB) {
		this.battleService = battleService;
		this.playerALogic = playerALogic;
		this.unitA = unitA;
		this.unitB = unitB;
	}

	public void runBattleTurn() {
		// 1. ターン開始処理 (ドメイン層への呼び出し)
		battleService.executeStartTurn(unitA);
		battleService.executeStartTurn(unitB);

		// 2. AIへの入力 (Unit -> UnitStateへの変換)
		UnitState stateA = new UnitState(unitA);
		UnitState stateB = new UnitState(unitB);

		// 3. AIが実装するロジックを呼び出し、ActionIssuer (Adapter)を注入
		BattleActionAdapter adapterA = new BattleActionAdapter(battleService, unitA, unitB);

		// AIのロジック実行: 状態を見て、adapterAを通じて行動を発行
		playerALogic.decideAction(stateA, stateB, adapterA);

		// 4. 結果の表示
		if (adapterA.getLastResult() != null) {
			System.out.println("プレイヤーAの行動: " + adapterA.getLastResult().getMessage());
		}

		// 5. 勝敗判定など...
	}
}