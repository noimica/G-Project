package logic.domain;

//ドメイン層: Entity
public enum ActionType {
	DO_NOTHING, MOVE, VULCAN, // バルカン (遠隔)
	BEAM_SABER, // ビームサーベル (近接)
	DEFEND_EVADE, // 防御・回避
	WEAPON_DEPLOYMENT, // 武装展開 (具体的な効果は省略)
	ULTIMATE_SKILL // 必殺技 (EP全消費)
}
