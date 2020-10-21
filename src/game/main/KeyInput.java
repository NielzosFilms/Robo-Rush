package game.main;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.FileNotFoundException;
import java.security.KeyException;
import java.util.LinkedList;

import game.audioEngine.AudioPlayer;
import game.inventory.Inventory;
import game.world.World;

public class KeyInput extends KeyAdapter {

	private Handler handler;
	private Inventory inventory;
	private World world;

	// public Map<Integer, Boolean> keysDown = new HashMap<Integer, Boolean>();
	/*
	 * 0 = W 1 = S 2 = A 3 = D 4 = Space 5 = Shift 6 = Ctrl 7 = Tab
	 */
	public boolean[] keysDown = { false, false, false, false, false, false, false, false };

	public KeyInput(Handler handler) {
		this.handler = handler;
	}

	public void setInventory(Inventory inventory) {
		this.inventory = inventory;
	}

	public void setWorld(World world) {
		this.world = world;
	}

	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();

		for (LinkedList<GameObject> list : handler.object_entities) {
			for (int i = 0; i < list.size(); i++) {
				GameObject tempObject = list.get(i);

				if (tempObject.getId() == ID.Player) {
					if (key == KeyEvent.VK_W)
						keysDown[0] = true;
					if (key == KeyEvent.VK_S)
						keysDown[1] = true;
					if (key == KeyEvent.VK_A)
						keysDown[2] = true;
					if (key == KeyEvent.VK_D)
						keysDown[3] = true;
					if (key == KeyEvent.VK_SPACE) {
						keysDown[4] = true;
					}
					if (key == KeyEvent.VK_SHIFT) {
						keysDown[5] = true;
					}
					if (key == KeyEvent.VK_CONTROL) {
						keysDown[6] = true;
						if (!Game.showHitboxes)
							Game.showHitboxes = true;
						else if (Game.showHitboxes)
							Game.showHitboxes = false;
					}
					if (key == KeyEvent.VK_E)
						inventory.pickupItem(handler, world);

				}
			}
		}

		if (key == KeyEvent.VK_I) {
			inventory.switchInventoryState();
		}

		if (!inventory.getInventoryOpen()) {
			if (key == KeyEvent.VK_1)
				inventory.setHotbarSelected(0);
			if (key == KeyEvent.VK_2)
				inventory.setHotbarSelected(1);
			if (key == KeyEvent.VK_3)
				inventory.setHotbarSelected(2);
			if (key == KeyEvent.VK_4)
				inventory.setHotbarSelected(3);
			if (key == KeyEvent.VK_5)
				inventory.setHotbarSelected(4);
			if (key == KeyEvent.VK_6)
				inventory.setHotbarSelected(5);
			if (key == KeyEvent.VK_7)
				inventory.setHotbarSelected(6);
			if (key == KeyEvent.VK_8)
				inventory.setHotbarSelected(7);
			if (key == KeyEvent.VK_9)
				inventory.setHotbarSelected(8);
		}

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
			for (int i = 0; i < list.size(); i++) {
				GameObject tempObject = list.get(i);

				if (tempObject.getId() == ID.Player) {
					if (key == KeyEvent.VK_W)
						keysDown[0] = false;
					if (key == KeyEvent.VK_S)
						keysDown[1] = false;
					if (key == KeyEvent.VK_A)
						keysDown[2] = false;
					if (key == KeyEvent.VK_D)
						keysDown[3] = false;
					if (key == KeyEvent.VK_SHIFT)
						keysDown[5] = false;
					if (key == KeyEvent.VK_CONTROL)
						keysDown[6] = false;
					if (key == KeyEvent.VK_SPACE)
						keysDown[4] = false;
				}
			}
		}

		if (key == KeyEvent.VK_F4) {
			Game.DEDUG_MODE = !Game.DEDUG_MODE;
		}
	}

	public void tick() {
		for (LinkedList<GameObject> list : handler.object_entities) {
			for (int i = 0; i < list.size(); i++) {
				GameObject tempObject = list.get(i);
				if (tempObject.getId() == ID.Player) {
					/*
					 * if(keysDown.get(KeyEvent.VK_W) == true) tempObject.setVelY(-5);
					 * if(keysDown.get(KeyEvent.VK_S) == true) tempObject.setVelY(5);
					 * if((keysDown.get(KeyEvent.VK_W) == true && keysDown.get(KeyEvent.VK_S) ==
					 * true) || (keysDown.get(KeyEvent.VK_W) == false && keysDown.get(KeyEvent.VK_S)
					 * == false)) tempObject.setVelY(0);
					 */

				}
			}
		}
	}

}
