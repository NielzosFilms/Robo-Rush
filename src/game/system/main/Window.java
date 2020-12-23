package game.system.main;

import game.textures.TEXTURE_LIST;
import game.textures.Texture;

import java.awt.*;

import javax.swing.JFrame;

public class Window extends Canvas {

	private static final long serialVersionUID = 492636734070584756L;

	private Texture cursor_tex = new Texture(TEXTURE_LIST.hud_list, 2, 0);
	
	public Window(int width, int height, String title, Game game) {
		/*Cursor woodCursor = Toolkit.getDefaultToolkit().createCustomCursor(
				cursor_tex.getTexure(), new Point(0, 0), "wood cursor");*/

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
		//f.getContentPane().setCursor(woodCursor);
		f.pack();
		f.setVisible(true);
		game.start();
	}

}
