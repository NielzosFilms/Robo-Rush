package game.system.inputs;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;

import game.enums.GAMESTATES;
import game.enums.MENUSTATES;
import game.system.helpers.Helpers;
import game.system.helpers.Logger;
import game.system.main.*;
import game.system.systems.menu.MenuSystem;

public class KeyInput extends KeyAdapter {

	public static HashMap<Integer, Boolean> keys_down = new HashMap<>();

	private Handler handler;
	private GameController gameController;
	private MenuSystem menuSystem;

	public KeyInput() {
		keys_down.put(KeyEvent.VK_W, false);
		keys_down.put(KeyEvent.VK_S, false);
		keys_down.put(KeyEvent.VK_A, false);
		keys_down.put(KeyEvent.VK_D, false);
		keys_down.put(KeyEvent.VK_SPACE, false);
		keys_down.put(KeyEvent.VK_SHIFT, false);
		keys_down.put(KeyEvent.VK_CONTROL, false);
		keys_down.put(KeyEvent.VK_TAB, false);
	}

	public void setRequirements(GameController gameController) {
		this.handler = gameController.getHandler();
//		this.inventorySystem = gameController.getInventorySystem();
		this.gameController = gameController;
		this.menuSystem = Game.menuSystem;
	}

	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();

		keys_down.put(key, true);

		menuSystem.keyPressed(e);
		if(key == KeyEvent.VK_T) {
			gameController.getPlayer().setX(Helpers.getWorldCoords(Game.mouseInput.mouse_x, Game.mouseInput.mouse_y, gameController.getCam()).x);
			gameController.getPlayer().setY(Helpers.getWorldCoords(Game.mouseInput.mouse_x, Game.mouseInput.mouse_y, gameController.getCam()).y);
		}
	}

	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		keys_down.put(key, false);

		if (key == KeyEvent.VK_F4) Game.DEBUG_MODE = !Game.DEBUG_MODE;
		if(key == KeyEvent.VK_F11) Game.window.setFullscreen(!Game.window.isFullscreen());
		if(key == KeyEvent.VK_B) {
			Logger.print("Tab");
			if(Game.game_state == GAMESTATES.Game) {
				Game.game_state = GAMESTATES.Pauzed;
				menuSystem.setState(MENUSTATES.Inventory);
			} else {
				Game.game_state = GAMESTATES.Game;
			}
		}
		if (key == KeyEvent.VK_ESCAPE) {
			switch(Game.game_state) {
				case Game:
					Game.game_state = GAMESTATES.Pauzed;
					Game.menuSystem.setState(MENUSTATES.Pauzed);
					break;
				case Pauzed:
					Game.game_state = GAMESTATES.Game;
					break;
				case Menu:
					System.exit(1);
					break;
			}
		}
		if (key == KeyEvent.VK_R) {
			Game.gameController.generate();
		}

		devKeyActions(key);
	}

	private void devKeyActions(int keycode) {
//		if(keycode == KeyEvent.VK_F5) {
//			for(Point roomkey : Game.gameController.getActiveLevel().getRooms().keySet()) {
//				Room room = Game.gameController.getActiveLevel().getRooms().get(roomkey);
//				room.setDiscovered(true);
//			}
//		}
	}

	public void tick() {}

}
