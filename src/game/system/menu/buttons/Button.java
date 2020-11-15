package game.system.menu.buttons;

import game.enums.BUTTONS;
import game.system.inputs.MouseInput;
import game.system.main.Game;
import game.textures.Fonts;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;

public abstract class Button {
	protected int x, y, width, height;
	protected BUTTONS btn_type;
	protected boolean hover, click;
	protected Color bg_color = new Color(0, 0, 0, 0);
	protected Color hover_color = new Color(255, 255, 255, 50);
	protected Color pressed_color = new Color(0, 0, 0, 50);

	public Button(int x, int y, int width, int height) {
		super();
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	public abstract void render(Graphics g, Graphics2D g2d);

	public abstract void handleClick(MouseEvent e);

	public void setColor(Graphics g) {
		if(this.hover) {
			if(this.click) {
				g.setColor(pressed_color);
			} else {
				g.setColor(hover_color);
			}
		} else {
			g.setColor(bg_color);
		}
	}

	public void alignCenterX(int screenWidth) {
		this.x = screenWidth / 2 - width / 2;
	}
	public void alignCenterY(int screenHeight) {
		this.y = screenHeight / 2 - height / 2;
	}

	public Rectangle getBounds() {
		return new Rectangle(x, y, width, height);
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public BUTTONS getBtn_type() {
		return btn_type;
	}

	public void setBtn_type(BUTTONS btn_type) {
		this.btn_type = btn_type;
	}

	public boolean isHover() {
		return hover;
	}

	public void setHover(boolean hover) {
		this.hover = hover;
	}

	public boolean isClick() {
		return click;
	}

	public void setClick(boolean click) {
		this.click = click;
	}

	public Color getBg_color() {
		return bg_color;
	}

	public void setBg_color(Color bg_color) {
		this.bg_color = bg_color;
	}

	public Color getHover_color() {
		return hover_color;
	}

	public void setHover_color(Color hover_color) {
		this.hover_color = hover_color;
	}

	public Color getPressed_color() {
		return pressed_color;
	}

	public void setPressed_color(Color pressed_color) {
		this.pressed_color = pressed_color;
	}
}
