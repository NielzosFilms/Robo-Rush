package game.system.systems.menu;

import com.sun.tools.javac.Main;
import game.enums.MENUSTATES;
import game.system.inputs.MouseInput;
import game.system.main.Game;
import game.textures.Textures;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.HashMap;

public class MenuSystem {
	private static final int screenWidth = Game.WIDTH, screenHeight = Game.HEIGHT;
	public MENUSTATES state;
	public MENUSTATES previous_state;
	private MouseInput mouse;

	private HashMap<MENUSTATES, Menu> menus = new HashMap<>();

	public MenuSystem() {
		this.state = MENUSTATES.Main;
	}

	public void setRequirements(MouseInput mouseInput) {
		this.mouse = mouseInput;
		update();
	}

	public void update() {
		menus.put(MENUSTATES.Main, new MainMenu(mouse));
		menus.put(MENUSTATES.Pauzed, new PauzedMenu(mouse));
		menus.put(MENUSTATES.Settings, new SettingsMenu(mouse));
		menus.put(MENUSTATES.SaveSlotSelect, new MenuWorldSelect(mouse));
	}

	public void tick() {
		menus.get(state).tick();
	}

	public void render(Graphics g, Graphics2D g2d) {
		menus.get(state).render(g, g2d);
	}

	public void mousePressed(MouseEvent e) {
		menus.get(state).mousePressed(e);
	}

	public void mouseReleased(MouseEvent e) {
		menus.get(state).mouseReleased(e);
	}

	public void keyPressed(KeyEvent e) {
		menus.get(state).keyPressed(e);
	}

	public MENUSTATES getState() {
		return state;
	}

	public void setState(MENUSTATES state) {
		this.previous_state = this.state;
		this.state = state;
	}

	public MENUSTATES getPreviousState() { return this.previous_state; }
}
