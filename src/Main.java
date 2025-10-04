import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import dto.CommandDTO;
import dto.UnitDTO;
import logic.adapter.BattleActionAdapter;
import logic.domain.ActionType;
import logic.domain.BattleResult;
import logic.domain.Status;
import logic.domain.Unit;
import logic.service.BattleService;
import view.BattleScreenLayout;

/**
 * Main class to start the battle application.
 * This class also acts as a simple Game Engine for demonstration purposes.
 */
public class Main {

	// Hold the application state here for simplicity.
	private static Unit playerDomain;
	private static Unit enemyDomain;
	private static BattleScreenLayout screen;
	private static boolean gameOver = false;

	public static void main(String[] args) {
		// =================================================================
		// 1. Initialize Logic Layer Objects
		// =================================================================
		// name, maxHp, epRecovery, closeAttack, rangedAttack, payload
		Status gundamStatus = new Status("Gundam", 100, 5, 15, 10, 10);
		Status zakuStatus = new Status("Zaku", 80, 3, 10, 5, 5);

		playerDomain = new Unit(gundamStatus, 100, 50, 0, 0); // status, hp, ep, sp, pos
		enemyDomain = new Unit(zakuStatus, 80, 30, 0, 10);

		List<ActionType> domainCommands = new ArrayList<>(Arrays.asList(ActionType.values()));
		BattleService battleService = new BattleService();
		BattleActionAdapter actionAdapter = new BattleActionAdapter(battleService, playerDomain, enemyDomain);

		// =================================================================
		// 2. Map Domain Objects to DTOs for the initial UI display
		// =================================================================
		UnitDTO playerDTO = new UnitDTO(playerDomain.getStatus().getName(), playerDomain.getCurrentHp(), playerDomain.getCurrentEp(), playerDomain.getCurrentSp());
		UnitDTO enemyDTO = new UnitDTO(enemyDomain.getStatus().getName(), enemyDomain.getCurrentHp(), enemyDomain.getCurrentEp(), enemyDomain.getCurrentSp());

		List<CommandDTO> commandDTOs = domainCommands.stream()
				.map(actionType -> new CommandDTO(actionType.name(), actionType.getDisplayName()))
				.collect(Collectors.toList());

		// =================================================================
		// 3. Create a Consumer to bridge UI events back to the Logic Layer
		// =================================================================
		Consumer<CommandDTO> commandConsumer = (commandDto) -> {
			if (gameOver) return;

			// --- Player's Turn ---
			boolean playerActionTaken = true;
			switch (commandDto.getId()) {
				case "VULCAN":
					actionAdapter.vulcan();
					break;
				case "BEAM_SABER":
					actionAdapter.beamSaber();
					break;
				case "DEFEND_EVADE":
					actionAdapter.defendEvade();
					break;
				case "MOVE":
					// For now, hardcode a move distance of 2. A real UI would ask for input.
					actionAdapter.move(2);
					break;
				default:
					playerActionTaken = false;
					System.out.println("Command not yet handled: " + commandDto.getDisplayName());
					break;
			}

			// Log the result of the player's action
			BattleResult playerResult = actionAdapter.getLastResult();
			if (playerResult != null) {
				screen.getLogPanel().appendLog(playerResult.getMessage());
			}

			// --- Enemy's Turn (Simple AI) ---
			// Only if player took a valid action and enemy is alive
			if (playerActionTaken && enemyDomain.getCurrentHp() > 0) {
				BattleActionAdapter enemyAdapter = new BattleActionAdapter(battleService, enemyDomain, playerDomain);
				enemyAdapter.vulcan(); // Enemy always attacks with Vulcan
				BattleResult enemyResult = enemyAdapter.getLastResult();
				if (enemyResult != null) {
					screen.getLogPanel().appendLog(enemyResult.getMessage());
				}
			}

			// --- Update UI ---
			UnitDTO newPlayerDTO = new UnitDTO(playerDomain.getStatus().getName(), playerDomain.getCurrentHp(), playerDomain.getCurrentEp(), playerDomain.getCurrentSp());
			UnitDTO newEnemyDTO = new UnitDTO(enemyDomain.getStatus().getName(), enemyDomain.getCurrentHp(), enemyDomain.getCurrentEp(), enemyDomain.getCurrentSp());
			screen.updateUnits(newPlayerDTO, newEnemyDTO);

			// --- Check for Game Over ---
			if (playerDomain.getCurrentHp() <= 0) {
				screen.getLogPanel().appendLog("Gundam has been defeated. YOU LOSE.");
				gameOver = true;
			} else if (enemyDomain.getCurrentHp() <= 0) {
				screen.getLogPanel().appendLog("Zaku has been defeated. YOU WIN!");
				gameOver = true;
			}
		};

		// =================================================================
		// 4. Start the UI on the Event Dispatch Thread
		// =================================================================
		javax.swing.SwingUtilities.invokeLater(() -> {
			screen = new BattleScreenLayout(
					"1D Command Battle (DTO)",
					playerDTO,
					enemyDTO,
					commandConsumer,
					commandDTOs);
			screen.setVisible(true);
			screen.getLogPanel().appendLog("Battle Start! Choose a command.");
		});
	}
}