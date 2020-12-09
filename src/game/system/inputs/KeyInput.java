package game.system.inputs;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.Random;

import game.assets.tiles.Tile;
import game.assets.tiles.floor.wood.Tile_FloorWood;
import game.enums.GAMESTATES;
import game.enums.MENUSTATES;
import game.system.helpers.Helpers;
import game.system.systems.GameObject;
import game.system.systems.inventory.InventorySystem;
import game.system.main.*;
import game.enums.ID;
import game.system.systems.menu.MenuSystem;
import game.system.world.Chunk;
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

	public void setRequirements(World world) {
		this.handler = world.getHandler();
		this.inventorySystem = world.getInventorySystem();
		this.world = world;
		this.menuSystem = Game.menuSystem;
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
							case KeyEvent.VK_U -> this.world.getChunkWithCoordsPoint(this.world.getChunkPointWithCoords(tempObject.getX(), tempObject.getY())).updateTiles();
						}
						// inventory.pickupItem(handler, world);
					}
				}
			}

			if (key == KeyEvent.VK_E) {
				LinkedList<GameObject> objs = handler.getSelectableObjects();
				for (GameObject obj : objs) {
					if (Game.mouseInput.mouseOverWorldVar(obj.getSelectBounds().x, obj.getSelectBounds().y,
							obj.getSelectBounds().width, obj.getSelectBounds().height)) {
						if (Helpers.getDistanceBetweenBounds(Game.world.getPlayer().getBounds(), obj.getSelectBounds()) < Game.world.getPlayer().REACH) {
							obj.interact();
							return;
						}
					}
				}
			}
			if(key == KeyEvent.VK_N) {
				if(world.structureActive()) {
					world.getGeneration().setNewSeed(world.getNextSeed());
					world.getActive_structure().generate(world);
				}else world.generate(new Random().nextLong());
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

		if (key == KeyEvent.VK_F4) Game.DEBUG_MODE = !Game.DEBUG_MODE;
	}

	public void tick() {}

}
