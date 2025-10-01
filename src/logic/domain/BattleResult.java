package logic.domain;

//アプリケーション層: DTO
public class BattleResult {
	private final boolean success;
	private final String message;

	public BattleResult(boolean success, String message) {
		this.success = success;
		this.message = message;
	}

	/**
	 * @return success
	 */
	public boolean isSuccess() {
		return success;
	}

	/**
	 * @return message
	 */
	public String getMessage() {
		return message;
	}

	// ゲッターメソッド
}