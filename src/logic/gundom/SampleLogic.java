package logic.gundom;

import logic.adapter.ActionIssuer;
import logic.domain.UnitState;

/**
 * A sample AI logic.
 * It attacks with the beam saber if close, otherwise it uses the vulcan.
 */
public class SampleLogic implements BattleLogic {
    @Override
    public void decideAction(UnitState self, UnitState opponent, ActionIssuer issuer) {
        // Simple logic: if distance is 1 or less, use beam saber. Otherwise, use vulcan.
        int distance = Math.abs(self.getPosition() - opponent.getPosition());
        if (distance <= 1) { 
            issuer.beamSaber();
        } else {
            issuer.vulcan();
        }
    }
}