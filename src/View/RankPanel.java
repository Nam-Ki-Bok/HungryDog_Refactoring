package View;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import Controller.GameManager;
import Model.BarObject;
import Model.Button;
import Model.AllLabel;
import Model.GameFont;
import Model.GameIcon;
import Model.Rank;

public class RankPanel extends JPanel {

	private static RankPanel rankPanel;
	private JButton btnGoMain, btnExit;
	private Button buttonGoMain, buttonExit;
	private JLabel lblTitle, lblSubTitle;
	private AllLabel labelTitle, labelSubTitle;
	private JLabel lblName[] = new JLabel[5];
	private JLabel lblScore[] = new JLabel[5];
	private int[] lastScore = new int[2];

	private int rankCnt;
	private ImageIcon imgBeforeHoveringMain, imgAfterHoveringMain, imgBeforeHoveringExit, imgAfterHoveringExit;

	public static RankPanel getInstance() {
		if (rankPanel == null)
			rankPanel = new RankPanel();

		return rankPanel;
	}

	private RankPanel() {
		Game game = GameManager.getInstance().getGame();

		Color backColor = new Color(246, 223, 170);
		setPreferredSize(new Dimension(600, 700));
		setBackground(backColor);
		setLayout(null);

		labelTitle = new AllLabel("RANKING");
		labelTitle.setRankLabelWithFontNPosition(GameFont.getRankBIgFont(), 50, 50, 500, 90);
		lblTitle=labelTitle.getLabel();
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		add(lblTitle);

		labelSubTitle = new AllLabel("     NAME          SCORE");
		labelSubTitle.setRankLabelWithFontNPosition(GameFont.getRankSmallFont(), 50,165, 500, 60);
		lblSubTitle=labelSubTitle.getLabel();
		lblSubTitle.setOpaque(true);
		lblSubTitle.setForeground(Color.red);
		add(lblSubTitle);

		imgBeforeHoveringMain = new GameIcon("main1.png").getIcon(225, 100);
		imgAfterHoveringMain = new GameIcon("main2.png").getIcon(225, 100);

		imgBeforeHoveringExit = new GameIcon("exit1.png").getIcon(225, 100);
		imgAfterHoveringExit = new GameIcon("exit2.png").getIcon(225, 100);

		buttonGoMain = new Button("□ MAIN", imgBeforeHoveringMain, imgAfterHoveringMain);
		buttonGoMain.setButton(backColor, 50, 550, 235, 100);
		btnGoMain=buttonGoMain.getButton();
		btnGoMain.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				game.mainState();
			}
		});
		add(btnGoMain);

		buttonExit = new Button("Exit", imgBeforeHoveringExit, imgAfterHoveringExit);
		buttonExit.setButton(backColor, 325, 550, 235, 100);
		btnExit=buttonExit.getButton();
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		add(btnExit);
	}

	public void printRanking(RankPanel rankPanel) {
		rankCnt = 0;
		Statement stmt = null;
		ResultSet rs = null;

		try {
			stmt = Rank.getInstance().getCon().createStatement();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

		try {
			rs = stmt.executeQuery("SELECT * FROM Score_Board ORDER BY score DESC");
		} catch (SQLException e) {
			System.out.println(e);
			e.printStackTrace();
		}

		try {
			while (rs.next() && rankCnt < 5) {
				String name = rs.getString(1);
				int score = rs.getInt(2);

				// NAME 랭킹 라벨
				lblName[rankCnt] = new JLabel();
				lblName[rankCnt].setBounds(50, 225 + 60 * (rankCnt), 500, 60);
				lblName[rankCnt].setFont(GameFont.getRankSmallFont());
				lblName[rankCnt].setText(rankCnt + 1 + ".  " + name + "\r\n");
				rankPanel.add(lblName[rankCnt]);

				// SCORE 랭킹 라벨
				lblScore[rankCnt] = new JLabel();
				lblScore[rankCnt].setBounds(340, 225 + 60 * (rankCnt), 500, 60);
				lblScore[rankCnt].setFont(GameFont.getRankSmallFont());
				lblScore[rankCnt].setText(score + "\r\n");
				rankPanel.add(lblScore[rankCnt]);

				lastScore[0] = rankCnt;
				lastScore[1] = score;
				rankCnt += 1;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void hideLabelBeforePrintRanking() {
		for (int i = 0; i < rankCnt; i++) {
			lblName[i].setVisible(false);
			lblScore[i].setVisible(false);
		}
	}

	public void isRankIn() {
		BarObject barObject = GameManager.getInstance().getBarObject();
		if (lastScore[0] < 4 || lastScore[1] < barObject.getScore())
			JOptionPane.showMessageDialog(null, "랭킹에 진입했습니다.");
		else
			JOptionPane.showMessageDialog(null, "랭킹에 진입하지 못했습니다.");
	}
}