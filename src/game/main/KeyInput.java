package game.main;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.LinkedList;

import game.inventory.InventorySystem;
import game.inventory.Inventory_OLD;
import game.world.World;

public class KeyInput extends KeyAdapter {

	private Handler handler;
	private InventorySystem inventorySystem;
	private World world;

	// public Map<Integer, Boolean> keysDown = new HashMap<Integer, Boolean>();
	/*
	 * 0 = W 1 = S 2 = A 3 = D 4 = Space 5 = Shift 6 = Ctrl 7 = Tab
	 */
	public boolean[] keysDown = { false, false, false, false, false, false, false, false };

	public KeyInput(Handler handler) {
		this.handler = handler;
	}

	public void setInventory(InventorySystem inventorySystem) {
		this.inventorySystem = inventorySystem;
	}

	public void setWorld(World world) {
		this.world = world;
	}

	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();

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
							Game.showHitboxes = !Game.showHitboxes;
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
					double dis = Math.sqrt((obj.getX() - Game.player.getX())
							* (obj.getX() - Game.player.getX())
							+ (obj.getY() - Game.player.getY()) * (obj.getY() - Game.player.getY()));
					// System.out.println(dis);
					if (dis < 75) {
						obj.interact();
					}
				}
				// }
			}
		}

		/*if (key == KeyEvent.VK_I) {
			inventoryOLD.switchInventoryState();
		}

		if (!inventoryOLD.getInventoryOpen()) {
			if (key == KeyEvent.VK_1)
				inventoryOLD.setHotbarSelected(0);
			if (key == KeyEvent.VK_2)
				inventoryOLD.setHotbarSelected(1);
			if (key == KeyEvent.VK_3)
				inventoryOLD.setHotbarSelected(2);
			if (key == KeyEvent.VK_4)
				inventoryOLD.setHotbarSelected(3);
			if (key == KeyEvent.VK_5)
				inventoryOLD.setHotbarSelected(4);
			if (key == KeyEvent.VK_6)
				inventoryOLD.setHotbarSelected(5);
			if (key == KeyEvent.VK_7)
				inventoryOLD.setHotbarSelected(6);
			if (key == KeyEvent.VK_8)
				inventoryOLD.setHotbarSelected(7);
			if (key == KeyEvent.VK_9)
				inventoryOLD.setHotbarSelected(8);
		}*/

		/*
		 * if(Game.game_state == GAMESTATES.Menu) { if(key == KeyEvent.VK_ESCAPE)
		 * System.exit(1); } if(key == KeyEvent.VK_ESCAPE && !Game.pauzed) { Game.pauzed
		 * = true; } else if(key == KeyEvent.VK_ESCAPE && Game.pauzed) { Game.pauzed =
		 * false; }
		 */
		if (key == KeyEvent.VK_ESCAPE) {
			// saving
			// try {
			// // saving a world
			// Game.world.saveChunks("saves/test_save.txt");
			// } catch (FileNotFoundException e1) {
			// // TODO Auto-generated catch block
			// e1.printStackTrace();
			// }
			System.exit(1);
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
