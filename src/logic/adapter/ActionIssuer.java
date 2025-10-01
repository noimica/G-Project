package logic.adapter;

//アプリケーション層: Output Port
//AI (BattleLogic) が実際にアクションを「発行」するためのインターフェース
public interface ActionIssuer {
	void doNothing();
	void move(int distance);
	void vulcan();
	void beamSaber();
	void defendEvade();
	void weaponDeployment();
	void ultimateSkill();
}