import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

public class FrameGame extends JFrame implements KeyListener {

	PanelGame panelGame = new PanelGame();

	public FrameGame() {
		setSize(900, 650);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
		setTitle("Artificial Intelligence in Games");
		add(panelGame, BorderLayout.CENTER);
		setFocusable(true);
		this.addKeyListener(this);
		if (GameInformation.onPlay == true) {
			if (GameInformation.level == "Easy")
				panelGame.playGameHard();
			if (GameInformation.level == "Medium")
				panelGame.playGameMedium();
			if (GameInformation.level == "Hard")
				panelGame.playGameHard();
		}

	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == e.VK_E && GameInformation.onPlay == false) {
			GameInformation.level = "Easy";
		}
		if (e.getKeyCode() == e.VK_M && GameInformation.onPlay == false) {
			GameInformation.level = "Medium";
		}
		if (e.getKeyCode() == e.VK_H && GameInformation.onPlay == false) {
			GameInformation.level = "Hard";
		}
		if (e.getKeyCode() == e.VK_SPACE) {
			GameInformation.onPlay = true;
		}
		if (e.getKeyCode() == e.VK_LEFT) {
			panelGame.playerControl("Left");
		}
		if (e.getKeyCode() == e.VK_UP) {
			panelGame.playerControl("Up");
		}
		if (e.getKeyCode() == e.VK_DOWN) {
			panelGame.playerControl("Down");
		}
		if (e.getKeyCode() == e.VK_RIGHT) {
			panelGame.playerControl("Right");
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

}
