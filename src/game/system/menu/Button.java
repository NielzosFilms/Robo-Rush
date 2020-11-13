package game.system.menu;

import game.system.inputs.MouseInput;

import java.awt.*;

public class Button {
	private int x, y;
	private String text;
	// static hovered pressed
	private String state = "static";
	private Color bg_color = new Color(66, 66, 66);
	private Color hover_color = new Color(175, 175, 175);
	private Color pressed_color = new Color(35, 35, 35);

	public Button(int x, int y, String text) {
		this.x = x;
		this.y = y;
		this.text = text;
	}

	public void tick() {

	}

	public void render(Graphics g, Graphics2D g2d) {
		switch(state) {
			case "hovered", "clicked" -> g.setColor(hover_color);
			case "pressed" -> g.setColor(pressed_color);
			default -> g.setColor(bg_color);
		}
		g.fillRect(x, y, 50, 50);
	}

	public String getState() {
		return state;
	}
	public void setState(String state) {
		if(this.state.equals("pressed")) {
			if(state.equals("clicked")) {
				this.state = state;
			}
		} else {
			this.state = state;
		}
	}

	public Rectangle getBounds() {
		return new Rectangle(x, y, 50, 50);
	}
}
