package logic.controller;

import java.util.ArrayList;
import java.util.List;

import logic.adapter.BattleActionAdapter;
import logic.domain.BattleResult;
import logic.domain.Unit;
import logic.domain.UnitState;
import logic.gundom.BattleLogic;
import logic.service.BattleService;

/**
 * Controller layer for the game.
 * Manages the state of the battle and executes turns.
 */
public class GameEngine {
	private final BattleService battleService;
	private final BattleLogic playerALogic;
	private final BattleLogic playerBLogic;
	private final Unit unitA;
	private final Unit unitB;
	private final List<BattleResult> turnResults = new ArrayList<>();

	public GameEngine(BattleService battleService, BattleLogic playerALogic, BattleLogic playerBLogic, Unit unitA, Unit unitB) {
		this.battleService = battleService;
		this.playerALogic = playerALogic;
		this.playerBLogic = playerBLogic;
		this.unitA = unitA;
		this.unitB = unitB;
	}

	/**
	 * Runs one full turn of the battle, with actions for both Unit A and Unit B.
	 */
	public void runBattleTurn() {
		turnResults.clear();

		// --- Pre-turn phase ---
		battleService.executeStartTurn(unitA);
		battleService.executeStartTurn(unitB);

		// --- Player A's Turn ---
		if (unitA.getCurrentHp() > 0) {
			UnitState stateA = new UnitState(unitA);
			UnitState stateB = new UnitState(unitB);
			BattleActionAdapter adapterA = new BattleActionAdapter(battleService, unitA, unitB);
			playerALogic.decideAction(stateA, stateB, adapterA);
			if (adapterA.getLastResult() != null) {
				turnResults.add(adapterA.getLastResult());
			}
		}

		// --- Player B's Turn ---
		if (unitB.getCurrentHp() > 0) {
			UnitState stateA = new UnitState(unitA);
			UnitState stateB = new UnitState(unitB);
			BattleActionAdapter adapterB = new BattleActionAdapter(battleService, unitB, unitA);
			playerBLogic.decideAction(stateB, stateA, adapterB);
			if (adapterB.getLastResult() != null) {
				turnResults.add(adapterB.getLastResult());
			}
		}
	}

	public Unit getUnitA() {
		return unitA;
	}

	public Unit getUnitB() {
		return unitB;
	}

	public List<BattleResult> getTurnResults() {
		return turnResults;
	}

	public boolean isGameOver() {
		return unitA.getCurrentHp() <= 0 || unitB.getCurrentHp() <= 0;
	}
}
