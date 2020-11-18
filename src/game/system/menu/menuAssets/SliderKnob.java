package game.system.menu.menuAssets;

import java.awt.*;

public class SliderKnob {
	private SliderInput parent;
	private int x, y;
	private final int width = 8;
	private final int height = 8;

	public SliderKnob(int x, int y, SliderInput parent) {
		this.x = x - (width / 2);
		this.y = y - (height / 2) + 1;
		this.parent = parent;
	}

	public void setX(int x) {
		this.x = x - (width / 2);
	}

	public void setY(int y) {
		this.y = y - (height / 2) + 1;
	}

	public Rectangle getBounds() {
		return new Rectangle(x, y, width, height);
	}

	public void render(Graphics g) {
		g.setColor(new Color(255, 255, 255, 100));
		g.fillRect(x, y, width, width);
	}

	public int getWidth() {
		return this.width;
	}
	public int getHeight() {
		return this.height;
	}
}
