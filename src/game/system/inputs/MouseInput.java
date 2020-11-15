package game.system.inputs;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.function.Function;

import game.system.hud.HUD;
import game.system.inventory.InventorySystem;
import game.system.main.Camera;
import game.system.main.Game;
import game.system.menu.MenuSystem;

public class MouseInput extends MouseAdapter implements MouseMotionListener, MouseWheelListener {
	private Game game;
	private InventorySystem inventorySystem;
	private MenuSystem menuSystem;
	private Camera cam;
	private HUD hud;

	public int mouse_x, mouse_y;
	public boolean dragging;

	public static ArrayList<Function> functions = new ArrayList<>();

	public MouseInput() {
		this.mouse_x = 0;
		this.mouse_y = 0;
		this.dragging = false;
	}

	public void setRequirements(Game game, InventorySystem inventorySystem, MenuSystem menuSystem, Camera cam, HUD hud) {
		this.game = game;
		this.inventorySystem = inventorySystem;
		this.menuSystem = menuSystem;
		this.cam = cam;
		this.hud = hud;
	}

	public void tick() {

	}

	public void mousePressed(MouseEvent e) {
		switch(Game.game_state) {
			case Game -> {
				hud.mousePressed(e);
				inventorySystem.mouseClicked(e);
			}
			case Menu, Pauzed -> menuSystem.mousePressed(e);
		}
	}

	public void mouseReleased(MouseEvent e) {
		switch(Game.game_state) {
			case Game -> hud.mouseReleased(e);
			case Menu, Pauzed -> menuSystem.mouseReleased(e);
		}
	}

	public void mouseClicked(MouseEvent e) {}

	public void mouseMoved(MouseEvent e) {
		this.mouse_x = (int) (e.getX() / Game.SCALE_WIDTH);
		this.mouse_y = (int) (e.getY() / Game.SCALE_HEIGHT);
		this.dragging = false;
	}

	public void mouseDragged(MouseEvent e) {
		this.mouse_x = (int) (e.getX() / Game.SCALE_WIDTH);
		this.mouse_y = (int) (e.getY() / Game.SCALE_HEIGHT);
		this.dragging = true;
		// System.out.println("Mouse dragged"+ e.getX()+ " | "+e.getY());
	}

	public void mouseWheelMoved(MouseWheelEvent e) {
		//System.out.println("Mouse Wheel: "+e.getWheelRotation());
		if (!inventorySystem.inventoryIsOpen()) {
			int new_index = inventorySystem.hotbar_selected + e.getWheelRotation();
			if (new_index > inventorySystem.player_hotbar.getSizeX() - 1) {
				new_index = 0;
			} else if (new_index < 0) {
				new_index = inventorySystem.player_hotbar.getSizeX() - 1;
			}
			inventorySystem.hotbar_selected = new_index;
		}
	}

	public boolean mouseOverLocalVar(int x, int y, int width, int height) {
		if (mouse_x > x && mouse_x < x + width) {
			return mouse_y > y && mouse_y < y + height;
		} else
			return false;
	}
	public boolean mouseOverLocalRect(Rectangle rect) {
		if (mouse_x > rect.x && mouse_x < rect.x + rect.width) {
			return mouse_y > rect.y && mouse_y < rect.y + rect.height;
		} else
			return false;
	}
	public boolean mouseOverWorldVar(int x, int y, int width, int height) {
		int mx = (int) (mouse_x + -cam.getX());
		int my = (int) (mouse_y + -cam.getY());
		return (mx > x && mx < x + width) && (my > y && my < y + height);
	}
	public boolean mouseOverWorldRect(Rectangle rect) {
		int mx = (int) (mouse_x + -cam.getX());
		int my = (int) (mouse_y + -cam.getY());
		return (mx > rect.x && mx < rect.x + rect.width) && (my > rect.y && my < rect.y + rect.height);
	}
	public Point getMouseWorldCoords() {
		return new Point((int) (mouse_x + -cam.getX()), (int) (mouse_y + -cam.getY()));
	}

}
