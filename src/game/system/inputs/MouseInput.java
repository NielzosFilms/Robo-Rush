package game.system.inputs;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.function.Function;

import game.assets.entities.Player;
import game.system.hud.HUD;
import game.system.inventory.InventorySystem;
import game.system.main.*;
import game.system.menu.MenuSystem;
import game.system.world.World;

public class MouseInput extends MouseAdapter implements MouseMotionListener, MouseWheelListener {
	private Game game;
	private InventorySystem inventorySystem;
	private MenuSystem menuSystem;
	private Camera cam;
	private HUD hud;
	private Handler handler;
	private World world;
	private Player player;

	public int mouse_x, mouse_y;
	public boolean dragging;

	public MouseInput() {
		this.mouse_x = 0;
		this.mouse_y = 0;
		this.dragging = false;
	}

	public void setRequirements(Game game, World world) {
		this.game = game;
		this.inventorySystem = world.getInventorySystem();
		this.menuSystem = Game.menuSystem;
		this.cam = world.getCam();
		this.hud = world.getHud();
		this.handler = world.getHandler();
		this.world = world;
		this.player = world.getPlayer();
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
		inventorySystem.mouseWheelMoved(e);
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

	public void setWorld(World world) {
		this.inventorySystem = world.getInventorySystem();
		this.cam = world.getCam();
		this.hud = world.getHud();
		this.handler = world.getHandler();
		this.world = world;
		this.player = world.getPlayer();
	}
}
