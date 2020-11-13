package game.system.inputs;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import game.system.inventory.InventorySystem;
import game.system.main.Camera;
import game.enums.GAMESTATES;
import game.system.main.Game;
import game.system.menu.Menu;

public class MouseInput extends MouseAdapter implements MouseMotionListener, MouseWheelListener {

	public int mouse_x, mouse_y;
	public boolean dragging;
	private InventorySystem inventorySystem;
	private Menu menu;
	private Camera cam;

	public MouseInput() {
		this.mouse_x = 0;
		this.mouse_y = 0;
		this.dragging = false;

	}

	public void setCam(Camera cam) {
		this.cam = cam;
	}

	public void tick() {

	}

	public void mousePressed(MouseEvent e) {
		int mx = e.getX();
		int my = e.getY();
		switch(Game.game_state) {
			case Game -> {
				inventorySystem.mouseClicked(e);
				Game.debug_hud.mousePressed(e);
			}
			case Menu, Pauzed -> menu.mouseClicked(e);
		}
	}

	public void mouseReleased(MouseEvent e) {
		int mx = e.getX();
		int my = e.getY();
		switch(Game.game_state) {
			case Game -> Game.debug_hud.mouseReleased(e);
			//case Menu, Pauzed -> menu.mouseClicked(e);
		}
	}

	public void mouseClicked(MouseEvent e) {
		// inventory.mouseClicked(e);
	}

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
		// System.out.println("Mouse Wheel: "+e.getWheelRotation());
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

	public boolean mouseOver(int mx, int my, int x, int y, int width, int height) {
		return (mx > x && mx < x + width) && (my > y && my < y + height);
	}

	public void setInventory(InventorySystem inventorySystem) {
		this.inventorySystem = inventorySystem;
	}

	public void setMenu(Menu menu) {
		this.menu = menu;
	}

}
