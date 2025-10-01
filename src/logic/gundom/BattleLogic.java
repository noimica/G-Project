package logic.gundom;

import logic.adapter.ActionIssuer;
import logic.domain.UnitState;

//アプリケーション層: Input Port
//ユーザー (最強のガンダムのコード) が実装するAIのロジック
public interface BattleLogic {
	/**
	 * ターン開始時に、機体の状態を見て、次にとるべき行動を決定し、ActionIssuerを通じて発行する。 * @param
	 * selfUnitState 自身の読み取り専用の状態
	 * 
	 * @param opponentUnitState
	 *            相手の読み取り専用の状態
	 * @param actionIssuer
	 *            決定した行動をアプリケーションに伝えるためのインターフェース
	 */
	void decideAction(UnitState selfUnitState, UnitState opponentUnitState, ActionIssuer actionIssuer);
}