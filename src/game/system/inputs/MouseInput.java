package game.system.inputs;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import game.assets.entities.player.Player;
import game.system.systems.hud.HUD;
import game.system.main.*;
import game.system.systems.menu.MenuSystem;

public class MouseInput extends MouseAdapter implements MouseMotionListener, MouseWheelListener {
	private Game game;
//	private InventorySystem inventorySystem;
	private MenuSystem menuSystem;
	private Camera cam;
	private HUD hud;
	private Handler handler;
	private GameController gameController;
	private Player player;

	public int mouse_x, mouse_y;
	public boolean mouseDown_left = false;
	public boolean mouseDown_right = false;

	public MouseInput() {
		this.mouse_x = 0;
		this.mouse_y = 0;
	}

	public void setRequirements(Game game, GameController gameController) {
		this.game = game;
//		this.inventorySystem = gameController.getInventorySystem();
		this.menuSystem = Game.menuSystem;
		this.cam = gameController.getCam();
		this.hud = gameController.getHud();
		this.handler = gameController.getHandler();
		this.gameController = gameController;
		this.player = gameController.getPlayer();
	}

	public void tick() {

	}

	public void mousePressed(MouseEvent e) {
		if(e.getButton() == MouseEvent.BUTTON1) mouseDown_left = true;
		if(e.getButton() == MouseEvent.BUTTON3) mouseDown_right = true;
		switch(Game.game_state) {
			case Game:
				hud.mousePressed(e);
//				inventorySystem.mouseClicked(e);
				break;
			case Pauzed:
			case Menu:
				menuSystem.mousePressed(e);
				break;
		}
	}

	public void mouseReleased(MouseEvent e) {
		if(e.getButton() == MouseEvent.BUTTON1) mouseDown_left = false;
		if(e.getButton() == MouseEvent.BUTTON3) mouseDown_right = false;
		switch(Game.game_state) {
			case Game:
				hud.mouseReleased(e);
				break;
			case Menu:
			case Pauzed:
				menuSystem.mouseReleased(e);
				break;
		}
	}

	public void mouseClicked(MouseEvent e) {}

	public void mouseMoved(MouseEvent e) {
		if(e.getButton() == MouseEvent.BUTTON1) mouseDown_left = false;
		if(e.getButton() == MouseEvent.BUTTON3) mouseDown_right = false;
		this.mouse_x = (int) (e.getX() / Game.SCALE_WIDTH);
		this.mouse_y = (int) (e.getY() / Game.SCALE_HEIGHT);
	}

	public void mouseDragged(MouseEvent e) {
		if(e.getButton() == MouseEvent.BUTTON1) mouseDown_left = true;
		if(e.getButton() == MouseEvent.BUTTON3) mouseDown_right = true;
		this.mouse_x = (int) (e.getX() / Game.SCALE_WIDTH);
		this.mouse_y = (int) (e.getY() / Game.SCALE_HEIGHT);
	}

	public void mouseWheelMoved(MouseWheelEvent e) {
//		inventorySystem.mouseWheelMoved(e);
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

	public void setGameController(GameController gameController) {
//		this.inventorySystem = gameController.getInventorySystem();
		this.cam = gameController.getCam();
		this.hud = gameController.getHud();
		this.handler = gameController.getHandler();
		this.gameController = gameController;
		this.player = gameController.getPlayer();
	}

	public boolean leftMouseDown() {
		return mouseDown_left;
	}

	public boolean rightMouseDown() {
		return mouseDown_right;
	}
}
