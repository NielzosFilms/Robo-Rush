package game.system.main;

import java.awt.Canvas;
import java.awt.Dimension;

import javax.swing.JFrame;

public class Window extends Canvas {

	private static final long serialVersionUID = 492636734070584756L;
	
	public Window(int width, int height, String title, Game game) {
		JFrame f = new JFrame(title);
		f.setPreferredSize(new Dimension(width, height));
		f.setMaximumSize(new Dimension(width, height));
		f.setMinimumSize(new Dimension(width, height));
		
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setResizable(false);
		f.setLocationRelativeTo(null);
		f.setExtendedState(JFrame.MAXIMIZED_BOTH);
		f.setUndecorated(true);
		f.add(game);
		f.setVisible(true);
		game.start();
	}

}
