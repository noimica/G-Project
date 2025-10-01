import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

// JFrame を継承し、KeyListenerを実装
public class BattleScreenLayout extends JFrame implements KeyListener {

	private final CommandPanel commandPanel;
	private final LogPanel logPanel;

	// constructor. フレームの設定関係を行う
	BattleScreenLayout(String title) {
		setTitle(title);
		setSize(800, 550);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Container cp = getContentPane();
		cp.setLayout(new BorderLayout(10, 10));

		// 1. メイン描画エリア (画面上部)
		MainDrawingPanel drawingPanel = new MainDrawingPanel();
		cp.add(drawingPanel, BorderLayout.CENTER);

		// 2. コマンドとログエリアを格納するコンテナ (画面下部)
		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new GridLayout(1, 2, 10, 0));
		bottomPanel.setPreferredSize(new Dimension(800, 180));

		// 2.1. コマンド選択エリア (画面下部左)
		// LogPanelを渡して、コマンド選択時にログを更新できるようにする
		logPanel = new LogPanel();
		commandPanel = new CommandPanel(logPanel);
		bottomPanel.add(commandPanel);

		// 2.2. ログ表示エリア (画面下部右)
		bottomPanel.add(logPanel);

		cp.add(bottomPanel, BorderLayout.SOUTH);

		// フレームにKeyListenerをセット
		// KeyListenerをJFrameに登録する場合は、setFocusable(true)とrequestFocusInWindow()が必要です
		addKeyListener(this);
		setFocusable(true);
		requestFocusInWindow();
	}

	// KeyListenerの実装
	@Override
	public void keyPressed(KeyEvent e) {
		char keyChar = e.getKeyChar();

		// 数字キー '1' から '9' の入力をチェック
		if (keyChar >= '1' && keyChar <= '9') {
			int commandNumber = Character.getNumericValue(keyChar);
			// CommandPanelのキーボード入力処理を呼び出す
			commandPanel.executeCommand(commandNumber);
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	public static void main(String args[]) {
		javax.swing.SwingUtilities.invokeLater(() -> {
			new BattleScreenLayout("1D コマンドバトル（インタラクティブ）").setVisible(true);
		});
	}
}

// -------------------------------------------------------------
/**
 * ログ表示パネルクラス
 */
class LogPanel extends JPanel {
	private final JLabel logArea;
	private final StringBuilder logHistory = new StringBuilder();
	private int turn = 1;

	public LogPanel() {
		setLayout(new BorderLayout());
		setBorder(new TitledBorder("■ バトルログ"));

		logArea = new JLabel();
		logArea.setVerticalAlignment(SwingConstants.TOP);
		logArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

		// 初期ログ
		appendLog("ターン " + turn + ": 戦闘開始！コマンドを選択してください。");

		add(logArea, BorderLayout.CENTER);
	}

	/**
	 * ログに新しいメッセージを追加し、表示を更新する
	 * 
	 * @param message
	 *            追加するメッセージ
	 */
	public void appendLog(String message) {
		turn++;
		// HTMLを使用して改行と色付け（ここでは単純なHTML）
		logHistory.insert(0, String.format("<font color='black'>[T%d] %s</font><br>", turn, message));

		// ログが長くなりすぎたら古いものを削除（任意）
		int maxLines = 10;
		String fullLog = logHistory.toString();
		if (fullLog.split("<br>").length > maxLines) {
			int lastBrIndex = fullLog.lastIndexOf("<br>", fullLog.lastIndexOf("<br>") - 1);
			if (lastBrIndex != -1) {
				logHistory.delete(lastBrIndex, logHistory.length());
			}
		}

		// JLabelはHTMLでラップする必要がある
		logArea.setText("<html><body>" + logHistory.toString() + "</body></html>");
	}
}

// -------------------------------------------------------------
/**
 * コマンド選択パネルクラス
 */
class CommandPanel extends JPanel {

	private final LogPanel logPanel;

	// 実際のコマンド名
	private static final String[] COMMAND_NAMES = {
			"攻撃", "防御", "チャージ",
			"必殺技", "アイテム", "逃走",
			"待機", "???", "設定"
	};

	public CommandPanel(LogPanel logPanel) {
		this.logPanel = logPanel;
		setBorder(new TitledBorder("■ コマンド選択 (1～9)"));
		setLayout(new GridLayout(3, 3, 5, 5));

		for (int i = 0; i < 9; i++) {
			final int commandIndex = i + 1; // コマンド番号は 1 から 9

			JLabel commandLabel = new JLabel(commandIndex + ". " + COMMAND_NAMES[i], SwingConstants.CENTER);
			commandLabel.setFont(new Font("Monospaced", Font.BOLD, 14));

			// クリック可能なUIとして見せるためのスタイル
			commandLabel.setBackground(Color.LIGHT_GRAY);
			commandLabel.setForeground(Color.BLACK);
			commandLabel.setOpaque(true);

			// マウスリスナーを追加して、クリックでコマンド実行
			commandLabel.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					executeCommand(commandIndex);
				}

				// クリック可能なことを示すため、ホバー時に色を変える
				@Override
				public void mouseEntered(MouseEvent e) {
					commandLabel.setBackground(Color.CYAN);
				}

				@Override
				public void mouseExited(MouseEvent e) {
					commandLabel.setBackground(Color.LIGHT_GRAY);
				}
			});

			add(commandLabel);
		}
	}

	/**
	 * コマンド番号に基づき、コマンドを実行する（ここではログ出力のみ）
	 * 
	 * @param commandNumber
	 *            選択されたコマンドの番号 (1-9)
	 */
	public void executeCommand(int commandNumber) {
		if (commandNumber >= 1 && commandNumber <= 9) {
			String commandName = COMMAND_NAMES[commandNumber - 1];

			// メインロジックをここに記述する代わりに、ログに出力
			logPanel.appendLog("ガンダムがコマンド " + commandNumber + " [" + commandName + "] を選択しました。");
		}
	}
}

// -------------------------------------------------------------
/**
 * 画面上部の自機と敵機の描画を行うパネル (描画特化)
 */
class MainDrawingPanel extends JPanel {
	private static final int GROUND_Y = 250;
	private static final int UNIT_SIZE = 60;
	private final int gundamX = 100;

	MainDrawingPanel() {
		setBackground(Color.DARK_GRAY);
		setDoubleBuffered(true);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		int panelWidth = getWidth();
		int zakuX = panelWidth - 100 - UNIT_SIZE; // 敵機X座標

		// 1. 地面 (一次元のバトルフィールド) の描画
		g.setColor(new Color(100, 100, 100));
		g.fillRect(0, GROUND_Y, panelWidth, getHeight() - GROUND_Y);

		// 2. ステータス表示エリア
		g.setColor(Color.WHITE);
		g.setFont(new Font("Monospaced", Font.BOLD, 18));
		g.drawString("== BATTLE FIELD (1D) ==", panelWidth / 2 - 150, 20);

		// 3. ユニットの描画

		// ガンダム (自機: 青)
		g.setColor(new Color(0, 100, 255));
		g.fillRect(gundamX, GROUND_Y - UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);
		g.setColor(Color.WHITE);
		g.setFont(new Font("Serif", Font.BOLD, 24));
		g.drawString("G", gundamX + 20, GROUND_Y - 25);
		g.setFont(new Font("SansSerif", Font.PLAIN, 12));
		g.drawString("HP: XXX", gundamX, GROUND_Y + 15);

		// ザク (敵機: 緑)
		g.setColor(new Color(50, 150, 50));
		g.fillRect(zakuX, GROUND_Y - UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);
		g.setColor(Color.BLACK);
		g.setFont(new Font("Serif", Font.BOLD, 24));
		g.drawString("Z", zakuX + 20, GROUND_Y - 25);
		g.setFont(new Font("SansSerif", Font.PLAIN, 12));
		g.drawString("HP: YYY", zakuX, GROUND_Y + 15);
	}
}