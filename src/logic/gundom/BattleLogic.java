package logic.gundom;

import logic.adapter.ActionIssuer;
import logic.domain.UnitState;

public interface BattleLogic {
    void decideAction(UnitState self, UnitState opponent, ActionIssuer issuer);
}
