import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import dto.CommandDTO;
import dto.UnitDTO;
import logic.adapter.BattleActionAdapter;
import logic.controller.GameEngine;
import logic.domain.ActionType;
import logic.domain.BattleResult;
import logic.domain.Status;
import logic.domain.Unit;
import logic.domain.UnitState;
import logic.gundom.BattleLogic;
import logic.gundom.SampleLogic;
import logic.service.BattleService;
import view.BattleScreenLayout;

/**
 * Main class to start the battle application.
 * This class is responsible for holding the game state and managing the game loop.
 */
public class Main {

	// --- Game State ---
	private static Unit playerUnit;
	private static Unit enemyUnit;
	private static GameEngine gameEngine;
	private static BattleLogic enemyLogic;
	private static BattleService battleService;
	private static BattleScreenLayout screen;

	public static void main(String[] args) {
		// =================================================================
		// 1. Initialize Game Objects
		// =================================================================
		Status gundamStatus = new Status("Gundam", 100, 5, 15, 10, 10);
		Status zakuStatus = new Status("Zaku", 80, 3, 10, 5, 5);

		playerUnit = new Unit(gundamStatus, 100, 50, 0, 0);
		enemyUnit = new Unit(zakuStatus, 80, 30, 0, 10);

		battleService = new BattleService();
		gameEngine = new GameEngine(battleService);
		enemyLogic = new SampleLogic();

		// =================================================================
		// 2. Create DTOs for initial UI display
		// =================================================================
		UnitDTO playerDTO = new UnitDTO(playerUnit.getStatus().getName(), playerUnit.getCurrentHp(),
				playerUnit.getCurrentEp(), playerUnit.getCurrentSp());
		UnitDTO enemyDTO = new UnitDTO(enemyUnit.getStatus().getName(), enemyUnit.getCurrentHp(),
				enemyUnit.getCurrentEp(), enemyUnit.getCurrentSp());

		List<CommandDTO> commandDTOs = Arrays.stream(ActionType.values())
				.map(actionType -> new CommandDTO(actionType.name(), actionType.getDisplayName()))
				.collect(Collectors.toList());

		// =================================================================
		// 3. Define the Game Loop triggered by a UI command
		// =================================================================
		Consumer<CommandDTO> commandConsumer = (commandDto) -> {
			if (isGameOver()) return;

			screen.getLogPanel().appendLog("コマンド " + commandDto.getDisplayName() + " を選択しました。");

			// --- Player's Turn ---
			screen.getLogPanel().appendLog("--- My Turn ---");
			ActionType playerAction = ActionType.valueOf(commandDto.getId());
			int value = (playerAction == ActionType.MOVE) ? 2 : 0; // Set value for MOVE action
			
			BattleResult playerResult = gameEngine.executeAction(playerUnit, enemyUnit, playerAction, value);
			screen.getLogPanel().appendLog(playerResult.getMessage());
			updateUi();
			if (isGameOver()) return; // Check game over after player's turn

			// --- Enemy's Turn ---
			screen.getLogPanel().appendLog("--- Enemy Turn ---");
			// The AI needs an ActionIssuer to commit its decided action.
			// This adapter calls our stateless gameEngine.
			BattleActionAdapter enemyAdapter = new BattleActionAdapter(battleService, enemyUnit, playerUnit);
			enemyLogic.decideAction(new UnitState(enemyUnit), new UnitState(playerUnit), enemyAdapter);
			BattleResult enemyResult = enemyAdapter.getLastResult();
			screen.getLogPanel().appendLog(enemyResult.getMessage());
			updateUi();
			if (isGameOver()) return; // Check game over after enemy's turn

			// --- End of Turn ---
			screen.getLogPanel().appendLog("--- Please select your action ---");
		};

		// =================================================================
		// 4. Start the UI
		// =================================================================
		javax.swing.SwingUtilities.invokeLater(() -> {
			screen = new BattleScreenLayout("1D Command Battle", playerDTO, enemyDTO, commandConsumer, commandDTOs);
			screen.setVisible(true);
			screen.getLogPanel().appendLog("Battle Start! Please select your action.");
		});
	}

	private static boolean isGameOver() {
		boolean isOver = playerUnit.getCurrentHp() <= 0 || enemyUnit.getCurrentHp() <= 0;
		if (isOver) {
			if (playerUnit.getCurrentHp() <= 0) {
				screen.getLogPanel().appendLog("Gundam has been defeated. YOU LOSE.");
			} else {
				screen.getLogPanel().appendLog("Zaku has been defeated. YOU WIN!");
			}
		}
		return isOver;
	}

	private static void updateUi() {
		UnitDTO newPlayerDTO = new UnitDTO(playerUnit.getStatus().getName(), playerUnit.getCurrentHp(),
				playerUnit.getCurrentEp(), playerUnit.getCurrentSp());
		UnitDTO newEnemyDTO = new UnitDTO(enemyUnit.getStatus().getName(), enemyUnit.getCurrentHp(),
				enemyUnit.getCurrentEp(), enemyUnit.getCurrentSp());
		screen.updateUnits(newPlayerDTO, newEnemyDTO);
	}
}
