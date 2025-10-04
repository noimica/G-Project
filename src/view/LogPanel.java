package view;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

/**
 * ログ表示パネルクラス (変更なし)
 */
public class LogPanel extends JPanel {
	private final JLabel logArea;
	private final StringBuilder logHistory = new StringBuilder();
	private int turn = 1;

	public LogPanel() {
		setLayout(new BorderLayout());
		setBorder(new TitledBorder("■ バトルログ"));

		logArea = new JLabel();
		logArea.setVerticalAlignment(SwingConstants.TOP);
		logArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

		// 初期ログは外部から与えられる
		logArea.setText("<html><body></body></html>");

		add(logArea, BorderLayout.CENTER);
	}

	public void appendLog(String message) {
		turn++;
		logHistory.insert(0, String.format("<font color='black'>[T%d] %s</font><br>", turn, message));

		int maxLines = 10;
		String fullLog = logHistory.toString();
		if (fullLog.split("<br>").length > maxLines) {
			int lastBrIndex = fullLog.lastIndexOf("<br>", fullLog.lastIndexOf("<br>") - 1);
			if (lastBrIndex != -1) {
				logHistory.delete(lastBrIndex, logHistory.length());
			}
		}

		logArea.setText("<html><body>" + logHistory.toString() + "</body></html>");
	}
}
