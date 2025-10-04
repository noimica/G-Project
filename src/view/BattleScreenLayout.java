package view;

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
import java.util.List;
import java.util.function.Consumer;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

import dto.CommandDTO;
import dto.UnitDTO;

// JFrame を継承し、KeyListenerを実装
public class BattleScreenLayout extends JFrame implements KeyListener {

	private final CommandPanel commandPanel;
	private final LogPanel logPanel;
	private final MainDrawingPanel drawingPanel; // drawingPanelをフィールドに

	// constructor. DTOを受け取るように変更
	public BattleScreenLayout(String title, UnitDTO player, UnitDTO enemy, Consumer<CommandDTO> commandConsumer,
			List<CommandDTO> commands) {
		setTitle(title);
		setSize(800, 550);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Container cp = getContentPane();
		cp.setLayout(new BorderLayout(10, 10));

		// 1. メイン描画エリア (画面上部)
		drawingPanel = new MainDrawingPanel(player, enemy); // DTOを渡す
		cp.add(drawingPanel, BorderLayout.CENTER);

		// 2. コマンドとログエリアを格納するコンテナ (画面下部)
		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new GridLayout(1, 2, 10, 0));
		bottomPanel.setPreferredSize(new Dimension(800, 180));

		// 2.1. コマンド選択エリア (画面下部左)
		logPanel = new LogPanel();
		// ConsumerとDTOリストを渡す
		commandPanel = new CommandPanel(logPanel, commandConsumer, commands);
		bottomPanel.add(commandPanel);

		// 2.2. ログ表示エリア (画面下部右)
		bottomPanel.add(logPanel);

		cp.add(bottomPanel, BorderLayout.SOUTH);

		// フレームにKeyListenerをセット
		addKeyListener(this);
		setFocusable(true);
		requestFocusInWindow();
	}

	/**
	 * ユニット情報を更新して再描画 (DTO版)
	 */
	public void updateUnits(UnitDTO player, UnitDTO enemy) {
		drawingPanel.updateUnits(player, enemy);
	}

	/**
	 * ログパネルを取得
	 */
	public LogPanel getLogPanel() {
		return logPanel;
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
}

// -------------------------------------------------------------
/**
 * コマンド選択パネルクラス (DTO版)
 */
class CommandPanel extends JPanel {

	private final LogPanel logPanel;
	private final Consumer<CommandDTO> commandConsumer;
	private final List<CommandDTO> commands;

	public CommandPanel(LogPanel logPanel, Consumer<CommandDTO> commandConsumer, List<CommandDTO> commands) {
		this.logPanel = logPanel;
		this.commandConsumer = commandConsumer;
		this.commands = commands;

		setBorder(new TitledBorder("■ コマンド選択 (1～9)"));
		setLayout(new GridLayout(3, 3, 5, 5));

		for (int i = 0; i < commands.size() && i < 9; i++) {
			final int commandIndex = i; // 0-indexed
			CommandDTO command = commands.get(i);

			JLabel commandLabel = new JLabel((i + 1) + ". " + command.getDisplayName(), SwingConstants.CENTER);
			commandLabel.setFont(new Font("Monospaced", Font.BOLD, 14));

			commandLabel.setBackground(Color.LIGHT_GRAY);
			commandLabel.setForeground(Color.BLACK);
			commandLabel.setOpaque(true);

			commandLabel.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					executeCommand(commandIndex + 1);
				}

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

	public void executeCommand(int commandNumber) {
		if (commandNumber >= 1 && commandNumber <= commands.size()) {
			CommandDTO command = commands.get(commandNumber - 1);

			// Consumerを通じてアクションを発行
			commandConsumer.accept(command);

			// UIの即時フィードバックとしてログ出力
			logPanel.appendLog("コマンド " + command.getDisplayName() + " を選択しました。");
		}
	}
}

// -------------------------------------------------------------
/**
 * 画面上部の自機と敵機の描画を行うパネル (DTO版)
 */
class MainDrawingPanel extends JPanel {
	private static final int GROUND_Y = 250;
	private static final int UNIT_SIZE = 60;
	private final int gundamX = 100;

	private UnitDTO player;
	private UnitDTO enemy;

	MainDrawingPanel(UnitDTO player, UnitDTO enemy) {
		this.player = player;
		this.enemy = enemy;
		setBackground(Color.DARK_GRAY);
		setDoubleBuffered(true);
	}

	public void updateUnits(UnitDTO player, UnitDTO enemy) {
		this.player = player;
		this.enemy = enemy;
		repaint(); // ユニット情報が更新されたら再描画
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		int panelWidth = getWidth();
		int zakuX = panelWidth - 100 - UNIT_SIZE; // 敵機X座標

		g.setColor(new Color(100, 100, 100));
		g.fillRect(0, GROUND_Y, panelWidth, getHeight() - GROUND_Y);

		g.setColor(Color.WHITE);
		g.setFont(new Font("Monospaced", Font.BOLD, 18));
		g.drawString("== BATTLE FIELD (1D) ==", panelWidth / 2 - 150, 20);

		// プレイヤー (自機: 青)
		g.setColor(new Color(0, 100, 255));
		g.fillRect(gundamX, GROUND_Y - UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);
		g.setColor(Color.WHITE);
		g.setFont(new Font("Serif", Font.BOLD, 24));

		String playerName = (player != null) ? player.getName() : "";
		if (!playerName.isEmpty()) {
			g.drawString(playerName.substring(0, 1), gundamX + 20, GROUND_Y - 25);
		} else {
			g.drawString("P", gundamX + 20, GROUND_Y - 25);
		}
		g.setFont(new Font("SansSerif", Font.PLAIN, 12));
		g.drawString("HP: " + (player != null ? player.getHp() : "XXX"), gundamX, GROUND_Y + 15);
		g.drawString("EP: " + (player != null ? player.getEp() : "XXX"), gundamX, GROUND_Y + 30);
		g.drawString("SP: " + (player != null ? player.getSp() : "XXX"), gundamX, GROUND_Y + 45);

		// 敵 (敵機: 緑)
		g.setColor(new Color(50, 150, 50));
		g.fillRect(zakuX, GROUND_Y - UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);
		g.setColor(Color.BLACK);
		g.setFont(new Font("Serif", Font.BOLD, 24));

		String enemyName = (enemy != null) ? enemy.getName() : "";
		if (!enemyName.isEmpty()) {
			g.drawString(enemyName.substring(0, 1), zakuX + 20, GROUND_Y - 25);
		} else {
			g.drawString("E", zakuX + 20, GROUND_Y - 25);
		}
		g.setFont(new Font("SansSerif", Font.PLAIN, 12));
		g.drawString("HP: " + (enemy != null ? enemy.getHp() : "YYY"), zakuX, GROUND_Y + 15);
		g.drawString("EP: " + (enemy != null ? enemy.getEp() : "YYY"), zakuX, GROUND_Y + 30);
		g.drawString("SP: " + (enemy != null ? enemy.getSp() : "YYY"), zakuX, GROUND_Y + 45);
	}
}
