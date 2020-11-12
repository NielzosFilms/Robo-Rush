package game.system.menu;

import game.enums.MENUSTATES;
import game.system.inputs.MouseInput;
import game.system.main.Game;

import java.awt.*;
import java.awt.event.MouseEvent;

public class Menu {
	private static final int screenWidth = Game.WIDTH, screenHeight = Game.HEIGHT;
	private MENUSTATES state;
	private MouseInput mouse;

	public Menu() {
		this.state = MENUSTATES.Main;
		this.mouse = Game.mouseInput;
	}

	public void tick() {

	}

	public void render(Graphics g, Graphics2D g2d) {
		if(state == MENUSTATES.Pauzed) {
			g.setColor(new Color(0, 0, 0, 0.5f));
			g.fillRect(0, 0, screenWidth, screenHeight);
		}
		//g.setColor(Color.BLUE);
		//g.fillRect(0, 0, screenWidth, screenWidth);
	}

	public void mouseClicked(MouseEvent e) {
		System.out.println(mouse.mouse_x);
		System.out.println(mouse.mouse_y);
	}

	public MENUSTATES getState() {
		return state;
	}
	public void setState(MENUSTATES state) {
		this.state = state;
	}
}
