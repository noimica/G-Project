import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import dto.CommandDTO;
import dto.UnitDTO;
import logic.controller.GameEngine;
import logic.domain.ActionType;
import logic.domain.BattleResult;
import logic.domain.Status;
import logic.domain.Unit;
import logic.gundom.BattleLogic;
import logic.gundom.SampleLogic;
import logic.service.BattleService;
import view.BattleScreenLayout;

/**
 * Main class to start the battle application.
 * This class is responsible for wiring the logic and UI layers together.
 */
public class Main {

	private static GameEngine gameEngine;
	private static BattleScreenLayout screen;

	public static void main(String[] args) {
		// =================================================================
		// 1. Initialize Controller and Logic Layers
		// =================================================================
		// name, maxHp, epRecovery, closeAttack, rangedAttack, payload
		Status gundamStatus = new Status("Gundam", 100, 5, 15, 10, 10);
		Status zakuStatus = new Status("Zaku", 80, 3, 10, 5, 5);

		Unit playerUnit = new Unit(gundamStatus, 100, 50, 0, 0); // status, hp, ep, sp, pos
		Unit enemyUnit = new Unit(zakuStatus, 80, 30, 0, 10);

		BattleService battleService = new BattleService();
		BattleLogic playerLogic = new SampleLogic(); // AI for Player A
		BattleLogic enemyLogic = new SampleLogic(); // AI for Player B

		gameEngine = new GameEngine(battleService, playerLogic, enemyLogic, playerUnit, enemyUnit);

		// =================================================================
		// 2. Map Domain Objects to DTOs for the initial UI display
		// =================================================================
		UnitDTO playerDTO = new UnitDTO(playerUnit.getStatus().getName(), playerUnit.getCurrentHp(),
				playerUnit.getCurrentEp(), playerUnit.getCurrentSp());
		UnitDTO enemyDTO = new UnitDTO(enemyUnit.getStatus().getName(), enemyUnit.getCurrentHp(),
				enemyUnit.getCurrentEp(), enemyUnit.getCurrentSp());

		List<CommandDTO> commandDTOs = Arrays.stream(ActionType.values())
				.map(actionType -> new CommandDTO(actionType.name(), actionType.getDisplayName()))
				.collect(Collectors.toList());

		// =================================================================
		// 3. Define the action when a UI command is clicked
		// =================================================================
		Consumer<CommandDTO> commandConsumer = (commandDto) -> {
			if (gameEngine.isGameOver())
				return;

			// The UI click now simply advances the game by one turn.
			// The GameEngine handles both player and enemy AI actions.
			gameEngine.runBattleTurn();

			// --- Log results from the turn ---
			List<BattleResult> results = gameEngine.getTurnResults();
			if (results.size() > 0) {
				screen.getLogPanel().appendLog("--- My Turn ---");
				screen.getLogPanel().appendLog(results.get(0).getMessage());
			}
			if (results.size() > 1) {
				screen.getLogPanel().appendLog("--- Enemy Turn ---");
				screen.getLogPanel().appendLog(results.get(1).getMessage());
			}

			// --- Update UI with new state ---
			Unit newPlayer = gameEngine.getUnitA();
			Unit newEnemy = gameEngine.getUnitB();
			UnitDTO newPlayerDTO = new UnitDTO(newPlayer.getStatus().getName(), newPlayer.getCurrentHp(),
					newPlayer.getCurrentEp(), newPlayer.getCurrentSp());
			UnitDTO newEnemyDTO = new UnitDTO(newEnemy.getStatus().getName(), newEnemy.getCurrentHp(),
					newEnemy.getCurrentEp(), newEnemy.getCurrentSp());
			screen.updateUnits(newPlayerDTO, newEnemyDTO);

			// --- Check for Game Over ---
			if (gameEngine.isGameOver()) {
				if (newPlayer.getCurrentHp() <= 0) {
					screen.getLogPanel().appendLog("Gundam has been defeated. YOU LOSE.");
				} else if (newEnemy.getCurrentHp() <= 0) {
					screen.getLogPanel().appendLog("Zaku has been defeated. YOU WIN!");
				}
			}
		};

		// =================================================================
		// 4. Start the UI on the Event Dispatch Thread
		// =================================================================
		javax.swing.SwingUtilities.invokeLater(() -> {
			screen = new BattleScreenLayout(
					"1D Command Battle (Engine)",
					playerDTO,
					enemyDTO,
					commandConsumer,
					commandDTOs);
			screen.setVisible(true);
			screen.getLogPanel().appendLog("Battle Start! Click any command to advance turn.");
		});
	}
}
