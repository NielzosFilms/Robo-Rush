package game.system.inputs;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.LinkedList;

import game.assets.entities.enemies.Enemy;
import game.enums.GAMESTATES;
import game.enums.MENUSTATES;
import game.system.helpers.Helpers;
import game.system.systems.gameObject.GameObject;
import game.system.systems.gameObject.Interactable;
import game.system.main.*;
import game.enums.ID;
import game.system.systems.menu.MenuSystem;
import game.textures.Textures;

public class KeyInput extends KeyAdapter {

	private Handler handler;
//	private InventorySystem inventorySystem;
	private GameController gameController;
	private MenuSystem menuSystem;

	public KeyInput() {}

	/*
	 * 0 = W 1 = S 2 = A 3 = D 4 = Space 5 = Shift 6 = Ctrl 7 = Tab
	 */
	public boolean[] keysDown = { false, false, false, false, false, false, false, false };

	public void setRequirements(GameController gameController) {
		this.handler = gameController.getHandler();
//		this.inventorySystem = gameController.getInventorySystem();
		this.gameController = gameController;
		this.menuSystem = Game.menuSystem;
	}

	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();

		if(Game.game_state == GAMESTATES.Game) {
			for (LinkedList<GameObject> list : handler.object_entities) {
				for (GameObject tempObject : list) {
					if (tempObject.getId() == ID.Player) {
						switch (key) {
							case KeyEvent.VK_W:
								keysDown[0] = true;
								break;
							case KeyEvent.VK_S:
								keysDown[1] = true;
								break;
							case KeyEvent.VK_A:
								keysDown[2] = true;
								break;
							case KeyEvent.VK_D:
								keysDown[3] = true;
								break;
							case KeyEvent.VK_SPACE:
								keysDown[4] = true;
								break;
							case KeyEvent.VK_SHIFT:
								keysDown[5] = true;
								break;
							case KeyEvent.VK_CONTROL:
								keysDown[6] = true;
								break;
							case KeyEvent.VK_I:
//								((Interactable)tempObject).interact();
								break;
							case KeyEvent.VK_R:
								gameController.generate();
								break;
							case KeyEvent.VK_O:
								gameController.getActiveLevel().openDoors();
								break;
							case KeyEvent.VK_P:
								gameController.getActiveLevel().closeDoors();
								break;
						}
						// inventory.pickupItem(handler, world);
					}
				}
			}

			if (key == KeyEvent.VK_E) {
				LinkedList<GameObject> objs = handler.getSelectableObjects();
				for (GameObject obj : objs) {
					if(obj instanceof Interactable) {
						Interactable object = (Interactable) obj;
						if (Game.mouseInput.mouseOverWorldVar(object.getSelectBounds().x, object.getSelectBounds().y,
								object.getSelectBounds().width, object.getSelectBounds().height)) {
//							if (Helpers.getDistanceBetweenBounds(Game.gameController.getPlayer().getBounds(), object.getSelectBounds()) < Game.gameController.getPlayer().REACH) {
//								object.interact();
//								return;
//							}
						}
					}
				}
			}
//			if(key == KeyEvent.VK_N) {
//				if(world.structureActive()) {
//					world.getGeneration().setNewSeed(world.getNextSeed());
//					world.getActive_structure().generate(world);
//				}else world.generate();
//			}
//			inventorySystem.keyPressed(e);
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
		if(key == KeyEvent.VK_F1) {
			int current_cursor = Game.settings.getCursor().getIndex();
			if(current_cursor + 1 >= Textures.texture_lists.get(Game.settings.getCursor().getTexture_list()).size()) {
				current_cursor = 0;
			} else {
				current_cursor += 1;
			}
			Game.settings.getCursor().setIndex(current_cursor);
		}
		if(key == KeyEvent.VK_R) handler.addObject( new Enemy(80, 64, 10, ID.Enemy));
		if(key == KeyEvent.VK_T) {
			gameController.getPlayer().setX(Helpers.getWorldCoords(Game.mouseInput.mouse_x, Game.mouseInput.mouse_y, gameController.getCam()).x);
			gameController.getPlayer().setY(Helpers.getWorldCoords(Game.mouseInput.mouse_x, Game.mouseInput.mouse_y, gameController.getCam()).y);
		}

		if(key == KeyEvent.VK_F11) {
			if(Game.window.isFullscreen()) {
				Game.window.setWindowed();
			} else {
				Game.window.setFullscreen();
			}
		}
	}

	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();

		for (LinkedList<GameObject> list : handler.object_entities) {
			for (GameObject tempObject : list) {
				if (tempObject.getId() == ID.Player) {
					switch (key) {
						case KeyEvent.VK_W:
							keysDown[0] = false;
							break;
						case KeyEvent.VK_S:
							keysDown[1] = false;
							break;
						case KeyEvent.VK_A:
							keysDown[2] = false;
							break;
						case KeyEvent.VK_D:
							keysDown[3] = false;
							break;
						case KeyEvent.VK_SPACE:
							keysDown[4] = false;
							break;
						case KeyEvent.VK_SHIFT:
							keysDown[5] = false;
							break;
						case KeyEvent.VK_CONTROL:
							keysDown[6] = false;
							break;
					}
				}
			}
		}

		if (key == KeyEvent.VK_F4) Game.DEBUG_MODE = !Game.DEBUG_MODE;
	}

	public void tick() {}

}
