package logic.controller;

import logic.domain.ActionType;
import logic.domain.BattleResult;
import logic.domain.Unit;
import logic.service.BattleService;

/**
 * A stateless controller that executes actions.
 * It acts as a facade over the BattleService.
 */
public class GameEngine {
    private final BattleService battleService;

    public GameEngine(BattleService battleService) {
        this.battleService = battleService;
    }

    /**
     * Executes a single action for an actor against an opponent.
     * This includes the pre-turn preparations for the actor.
     * @param actor The unit taking the action.
     * @param opponent The opposing unit.
     * @param action The action to perform.
     * @param value An optional value for the action (e.g., distance).
     * @return The result of the action.
     */
    public BattleResult executeAction(Unit actor, Unit opponent, ActionType action, int value) {
        // Pre-turn phase for the current actor.
        battleService.executeStartTurn(actor);
        
        // Execute the action and return the result.
        return battleService.executeAction(actor, opponent, action, value);
    }
}