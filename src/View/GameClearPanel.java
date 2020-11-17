package View;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import Controller.GameManager;
import Controller.Rank;
import Model.Button;
import Model.MyIcon;
import Model.Model;

public class GameClearPanel extends JPanel {
	private static GameClearPanel imgGameClearPanel;

	private JLabel lblGameClear;
	private JTextField scoreInputTextField;
	private JButton btnScoreInput, btnMain;
	private ImageIcon imgBeforeHoveringMain, imgAfterHoveringMain, imgBeforeHoveringInput, imgAfterHoveringInput,
			imgGameClear;

	public static GameClearPanel getInstance() {
		if (imgGameClearPanel == null)
			imgGameClearPanel = new GameClearPanel();

		return imgGameClearPanel;
	}

	private GameClearPanel() {
		Game game = GameManager.getInstance().getGame();
		Color backColor = new Color(195, 224, 166);

		setPreferredSize(new Dimension(600, 700));
		setLayout(null);

		imgGameClear = new MyIcon("GameClear.png").getIcon(400, 200);
		lblGameClear = new JLabel(imgGameClear, lblGameClear.CENTER);
		lblGameClear.setOpaque(false);
		lblGameClear.setBounds(55, 100, 500, 350);
		add(lblGameClear);

		scoreInputTextField = new JTextField(3);
		scoreInputTextField.setBounds(75, 503, 150, 65);
		scoreInputTextField.setFont(new Font("Verdana", Font.BOLD, 30));
		scoreInputTextField.setAlignmentX(CENTER_ALIGNMENT);
		scoreInputTextField.setDocument(new JTextFieldLimit(3));
		add(scoreInputTextField);

		imgBeforeHoveringInput = new MyIcon("input1.png").getIcon(150, 70);
		imgAfterHoveringInput = new MyIcon("input2.png").getIcon(150, 70);

		btnScoreInput = new Button("imgBeforeHoveringInput", imgBeforeHoveringInput, imgAfterHoveringInput)
				.getButton(backColor, 240, 500, 160, 75);
		btnScoreInput.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Model barObject = GameManager.getInstance().getModel();
				String playingName = scoreInputTextField.getText();
				scoreInputTextField.setText("");

				if (playingName.length() != 3) {
					scoreInputTextField.setText("");
					JOptionPane.showMessageDialog(null, "닉네임을 3글자로 입력해주세요.");
				} else {
					playingName = playingName.toUpperCase();
					Rank.getInstance().setNewRank(barObject.getScore(), playingName);
					barObject.initAll();
				}
			}
		});
		add(btnScoreInput);

		imgBeforeHoveringMain = new MyIcon("main1.png").getIcon(150, 75);
		imgAfterHoveringMain = new MyIcon("main2.png").getIcon(150, 75);

		btnMain = new Button("main1", imgBeforeHoveringMain, imgAfterHoveringMain).getButton(backColor, 410, 500, 160,
				75);
		btnMain.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				game.mainButton();
			}
		});
		add(btnMain);
	}

	public class JTextFieldLimit extends PlainDocument {
		private int limit;

		public JTextFieldLimit(int limit) {
			super();
			this.limit = limit;
		}

		public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
			if (str == null)
				return;
			if (getLength() + str.length() <= limit)
				super.insertString(offset, str, attr);
		}
	}
}