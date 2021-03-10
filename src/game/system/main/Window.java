package game.system.main;

import java.awt.*;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

public class Window extends Canvas {

	private static final long serialVersionUID = 492636734070584756L;

	private int width, height, min_width;
	private float screen_ratio;
	private String title;
	private Game game;

	private boolean fullscreen = true;

	public JFrame f;
	
	public Window(int width, int height, int min_width, float screen_ratio, String title, Game game) {
		this.width = width;
		this.height = height;
		this.min_width = min_width;
		this.screen_ratio = screen_ratio;
		this.title = title;
		this.game = game;

		this.f = new JFrame(title);
		createWindow();
		game.start();
	}

	private void createWindow() {
		Cursor transp_cursor = Toolkit.getDefaultToolkit().createCustomCursor(
				new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB), new Point(0, 0), "transp cursor");
		this.f = new JFrame(title);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setMinimumSize(new Dimension(min_width, Math.round(min_width / screen_ratio)));
		f.setMaximumSize(new Dimension(width, height));

		if(fullscreen) {
			f.setPreferredSize(new Dimension(width, height));
			f.setResizable(false);
			f.setExtendedState(JFrame.MAXIMIZED_BOTH);
			f.setUndecorated(true);
		} else {
			f.setPreferredSize(new Dimension(Math.round(width / 1.2f), Math.round(height / 1.2f)));
			f.setResizable(true);
			f.setUndecorated(false);
		}

		f.add(game);
		f.getContentPane().setCursor(transp_cursor);
		f.pack();
		f.setVisible(true);
		f.toFront();
		f.requestFocus();
	}

	public void setWindowed() {
		this.fullscreen = false;
		f.dispose();
		createWindow();
	}

	public void setFullscreen(){
		this.fullscreen = true;
		f.dispose();
		createWindow();
	}

	public boolean isFullscreen() {
		return fullscreen;
	}

}
