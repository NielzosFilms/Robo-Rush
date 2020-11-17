package game.system.menu;

import game.enums.MENUSTATES;
import game.system.inputs.MouseInput;
import game.system.main.Game;
import game.system.menu.buttons.Button;
import game.system.menu.buttons.ButtonPlay;
import game.system.menu.buttons.ButtonQuit;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class MenuSystem {
	private static final int screenWidth = Game.WIDTH, screenHeight = Game.HEIGHT;
	private MENUSTATES state;
	private MouseInput mouse;

	private MainMenu mainMenu;
	private PauzedMenu pauzedMenu;

	public MenuSystem() {
		this.state = MENUSTATES.Main;
	}

	public void setRequirements(MouseInput mouseInput) {
		this.mouse = mouseInput;

		this.mainMenu = new MainMenu(mouse);
		this.pauzedMenu = new PauzedMenu(mouse);
	}

	public void tick() {
		Menu active = getActiveMenu();
		if(active != null) active.tick();
	}

	public void render(Graphics g, Graphics2D g2d) {
		Menu active = getActiveMenu();
		if(active != null) active.render(g, g2d);
	}

	public void mousePressed(MouseEvent e) {
		Menu active = getActiveMenu();
		if(active != null) active.mousePressed(e);
	}

	public void mouseReleased(MouseEvent e) {
		Menu active = getActiveMenu();
		if(active != null) active.mouseReleased(e);
	}

	public void keyPressed(KeyEvent e) {
		Menu active = getActiveMenu();
		if(active != null) active.keyPressed(e);
	}

	public Menu getActiveMenu() {
		Menu ret;
		switch(state) {
			case Main -> ret = mainMenu;
			case Pauzed -> ret = pauzedMenu;
			default -> ret = null;
		}
		return ret;
	}

	public MENUSTATES getState() {
		return state;
	}
	public void setState(MENUSTATES state) {
		this.state = state;
	}
}
