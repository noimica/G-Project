package logic.gundom;

import logic.adapter.ActionIssuer;
import logic.domain.UnitState;

public class SampleLogic implements BattleLogic {
	@Override
	public void decideAction(UnitState self, UnitState opponent, ActionIssuer actionIssuer) {

		int distance = Math.abs(self.getPosition() - opponent.getPosition());

		// EPが少なく、HPが十分なら何もしないで回復
		if (self.getEp() < 10 && self.getHp() > self.getMaxHp() / 2) {
			actionIssuer.doNothing();
			return;
		}

		// 距離が近い場合 (近接攻撃)
		if (distance <= 5) {
			// SPが溜まっていたら必殺技
			if (self.getSp() >= 80) {
				actionIssuer.ultimateSkill();
			} else {
				// 通常はビームサーベル
				actionIssuer.beamSaber();
			}
		}
		// 距離が遠い場合 (遠隔攻撃 or 移動)
		else if (distance > 15) {
			// 距離を詰める
			actionIssuer.move(10);
		} else {
			// 中距離ならバルカン
			actionIssuer.vulcan();
		}
	}
}
