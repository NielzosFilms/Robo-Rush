package game.system.menu;

import game.enums.MENUSTATES;
import game.system.inputs.MouseInput;
import game.system.main.Game;

import java.awt.*;

public class Menu {
	private MENUSTATES state;
	private MouseInput mouse;

	public Menu() {
		this.state = MENUSTATES.Main;
		this.mouse = Game.mouseInput;
	}

	public void tick() {

	}

	public void render(Graphics g, Graphics2D g2d) {

	}

	public MENUSTATES getState() {
		return state;
	}
	public void setState(MENUSTATES state) {
		this.state = state;
	}
}
