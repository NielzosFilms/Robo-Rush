package game.system.main;

import game.textures.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.Buffer;

import javax.swing.JFrame;

public class Window extends Canvas {

	private static final long serialVersionUID = 492636734070584756L;
	
	public Window(int width, int height, String title, Game game) {
		/*BufferedImage cursor = new BufferedImageLoader().loadImage("assets/main/hud/cursor.png");
		Cursor woodCursor = Toolkit.getDefaultToolkit().createCustomCursor(
				cursor, new Point(10, 10), "wood cursor");*/

		Cursor transp_cursor = Toolkit.getDefaultToolkit().createCustomCursor(
				new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB), new Point(0, 0), "transp cursor");

		JFrame f = new JFrame(title);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setPreferredSize(new Dimension(width, height));
		f.setMaximumSize(new Dimension(width, height));
		f.setMinimumSize(new Dimension(width, height));
		if(!Game.WINDOWED) {
			f.setResizable(false);
			f.setExtendedState(JFrame.MAXIMIZED_BOTH);
			f.setUndecorated(true);
		}
		f.setLocationRelativeTo(null);
		f.add(game);
		f.getContentPane().setCursor(transp_cursor);
		f.pack();
		f.setVisible(true);
		game.start();
	}

}
