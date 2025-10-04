package view;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;
import javax.swing.text.BadLocationException;

/**
 * ログ表示パネルクラス
 * JTextAreaとJScrollPaneを使い、下にスクロールする形式に変更
 */
public class LogPanel extends JPanel {
    private final JTextArea logArea;
    private final int maxLines = 100; // スクロール可能なビューのため、行数を増やす
    private int logCounter = 1;

    public LogPanel() {
        setLayout(new BorderLayout());
        setBorder(new TitledBorder("■ バトルログ"));

        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        logArea.setLineWrap(true);
        logArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(logArea);
        add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * ログの末尾に新しいメッセージを追加し、一番下にスクロールする
     * @param message 追加するメッセージ
     */
    public void appendLog(String message) {
        // カウンターをメッセージに含める
        final String fullMessage = String.format("[%d] %s\n", logCounter, message);
        logArea.append(fullMessage);
        logCounter++;

        // 古い行を削除する
        try {
            if (logArea.getLineCount() > maxLines) {
                int endOfFirstLine = logArea.getLineEndOffset(0);
                logArea.getDocument().remove(0, endOfFirstLine);
            }
        } catch (BadLocationException e) {
            // 無視するか、ロギングする
            e.printStackTrace();
        }

        // 自動で一番下にスクロール
        logArea.setCaretPosition(logArea.getDocument().getLength());
    }
}