package game.system.systems.menu.buttons;

import game.enums.BUTTONS;
import game.textures.Fonts;

import java.awt.*;
import java.awt.event.MouseEvent;

public abstract class Button {
	protected int x, y, width, height;
	protected String text;
	protected BUTTONS btn_type;
	protected boolean hover, click;
	protected Color bg_color = new Color(24, 20, 37, 0);
	protected Color hover_color = new Color(192, 203, 220, 50);
	protected Color pressed_color = new Color(24, 20, 37, 50);

	public Button(int x, int y, int width, int height, String text) {
		super();
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.text = text;
	}

	public void render(Graphics g) {
		g.setFont(Fonts.default_fonts.get(10));
		g.setColor(new Color(24, 20, 37));
		g.drawString(text, x+1, y + height - 4);
		g.setColor(new Color(90, 105, 136));
		g.drawString(text, x, y + height - 5);
		this.setColor(g);
		g.fillRect(x, y, width, height);
	}

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
