package game.system.inputs;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.LinkedList;

import game.enums.GAMESTATES;
import game.enums.MENUSTATES;
import game.system.inventory.Inventory;
import game.system.inventory.InventorySystem;
import game.system.main.Game;
import game.system.main.GameObject;
import game.system.main.Handler;
import game.enums.ID;
import game.system.main.Logger;
import game.system.menu.MenuSystem;
import game.system.world.World;

public class KeyInput extends KeyAdapter {

	private Handler handler;
	private InventorySystem inventorySystem;
	private World world;
	private MenuSystem menuSystem;

	public KeyInput() {}

	/*
	 * 0 = W 1 = S 2 = A 3 = D 4 = Space 5 = Shift 6 = Ctrl 7 = Tab
	 */
	public boolean[] keysDown = { false, false, false, false, false, false, false, false };

	public void setRequirements(Handler handler, InventorySystem inventorySystem, World world, MenuSystem menuSystem) {
		this.handler = handler;
		this.inventorySystem = inventorySystem;
		this.world = world;
		this.menuSystem = menuSystem;
	}

	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();

		if(Game.game_state == GAMESTATES.Game) {
			for (LinkedList<GameObject> list : handler.object_entities) {
				for (GameObject tempObject : list) {
					if (tempObject.getId() == ID.Player) {
						switch (key) {
							case KeyEvent.VK_W -> keysDown[0] = true;
							case KeyEvent.VK_S -> keysDown[1] = true;
							case KeyEvent.VK_A -> keysDown[2] = true;
							case KeyEvent.VK_D -> keysDown[3] = true;
							case KeyEvent.VK_SPACE -> keysDown[4] = true;
							case KeyEvent.VK_SHIFT -> keysDown[5] = true;
							case KeyEvent.VK_CONTROL -> {
								keysDown[6] = true;
							}
							case KeyEvent.VK_I -> tempObject.interact();
						}
						// inventory.pickupItem(handler, world);
					}
				}
			}

			if (key == KeyEvent.VK_E) {
				LinkedList<GameObject> objs = handler.getSelectableObjects(world);
				for (GameObject obj : objs) {
					// if (obj.getSelectBounds() != null) {
					if (Game.mouseInput.mouseOverWorldVar(obj.getSelectBounds().x, obj.getSelectBounds().y,
							obj.getSelectBounds().width, obj.getSelectBounds().height)) {
						// Math.(Game.player);
						Rectangle obj_bounds = obj.getSelectBounds();
						Rectangle player_bounds = Game.player.getBounds();
						double dis = Math.sqrt((obj_bounds.getCenterX() - player_bounds.getCenterX())
								* (obj_bounds.getCenterX() - player_bounds.getCenterX())
								+ (obj_bounds.getCenterY() - player_bounds.getCenterY()) * (obj_bounds.getCenterY() - player_bounds.getCenterY()));
						// System.out.println(dis);
						if (dis < 50) {
							// TODO distance is from top left?
							obj.interact();
							return;
						}
					}
					// }
				}
			}
			inventorySystem.keyPressed(e);
		} else {
			menuSystem.keyPressed(e);
		}
		if (key == KeyEvent.VK_ESCAPE) {
			// saving
			// try {
			// // saving a world
			// Game.world.saveChunks("saves/test_save.txt");
			// } catch (FileNotFoundException e1) {
			// // TODO Auto-generated catch block
			// e1.printStackTrace();
			// }

			switch(Game.game_state) {
				case Game -> {
					if(inventorySystem.inventoryIsOpen()) {
						inventorySystem.closeAll();
					} else {
						Game.game_state = GAMESTATES.Pauzed;
						Game.menuSystem.setState(MENUSTATES.Pauzed);
					}
				}
				case Pauzed -> Game.game_state = GAMESTATES.Game;
				case Menu -> System.exit(1);
			}
		}

	}

	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();

		for (LinkedList<GameObject> list : handler.object_entities) {
			for (GameObject tempObject : list) {
				if (tempObject.getId() == ID.Player) {
					switch (key) {
						case KeyEvent.VK_W -> keysDown[0] = false;
						case KeyEvent.VK_S -> keysDown[1] = false;
						case KeyEvent.VK_A -> keysDown[2] = false;
						case KeyEvent.VK_D -> keysDown[3] = false;
						case KeyEvent.VK_SPACE -> keysDown[4] = false;
						case KeyEvent.VK_SHIFT -> keysDown[5] = false;
						case KeyEvent.VK_CONTROL -> keysDown[6] = false;
					}
				}
			}
		}

		if (key == KeyEvent.VK_F4) Game.DEDUG_MODE = !Game.DEDUG_MODE;
	}

	public void tick() {
//		for (LinkedList<GameObject> list : handler.object_entities) {
//			for (int i = 0; i < list.size(); i++) {
//				GameObject tempObject = list.get(i);
//				if (tempObject.getId() == ID.Player) {
//					/*
//					 * if(keysDown.get(KeyEvent.VK_W) == true) tempObject.setVelY(-5);
//					 * if(keysDown.get(KeyEvent.VK_S) == true) tempObject.setVelY(5);
//					 * if((keysDown.get(KeyEvent.VK_W) == true && keysDown.get(KeyEvent.VK_S) ==
//					 * true) || (keysDown.get(KeyEvent.VK_W) == false && keysDown.get(KeyEvent.VK_S)
//					 * == false)) tempObject.setVelY(0);
//					 */
//
//				}
//			}
//		}
	}

}
