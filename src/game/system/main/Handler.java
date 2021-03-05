package game.system.main;

import game.assets.tiles.tile.Tile;
import game.enums.ID;
import game.system.helpers.Logger;
import game.system.systems.gameObject.*;
import game.system.systems.lighting.Light;
import game.system.systems.particles.ParticleSystem;
import game.system.world.Chunk;
import game.system.world.World;

import java.awt.*;
import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedList;

public class Handler implements Serializable {
	private transient GameController gameController;
	private transient Camera cam;
	private transient ParticleSystem ps;

	public LinkedList<LinkedList<GameObject>> object_entities = new LinkedList<>();
	public LinkedList<LinkedList<GameObject>> hud_objects = new LinkedList<>();

	public Handler() {
		for (int i = 0; i < 4; i++) {
			this.object_entities.add(new LinkedList<GameObject>());
		}
	}

	public void setRequirements(GameController gameController, Camera cam, ParticleSystem ps) {
		this.gameController = gameController;
		this.cam = cam;
		this.ps = ps;
	}

	public void tick() {
		LinkedList<LinkedList<GameObject>> all_game_objects = gameController.getAllGameObjects();

		for (LinkedList<GameObject> list : object_entities) {
			for (int i = 0; i < list.size(); i++) {
				GameObject tempObject = list.get(i);
				tempObject.tick();
//				if (tempObject.getId() == ID.Player) {
//					tempObject.tick();
//				} else {
//					gameController.addEntityToActiveRoom(tempObject);
//					list.remove(tempObject);
//				}
			}
		}

		for (LinkedList<GameObject> list : all_game_objects) {
			for (int i = 0; i < list.size(); i++) {
				GameObject tempObject = list.get(i);
				tempObject.tick();
			}
		}

//		for (Light light : lights) {
//			if(world.addLightToChunk(light)) lights.remove(light);
//		}

//		for (int i=0; i<tiles.size(); i++) {
//			Tile tile = tiles.get(i);
//			Chunk chunk = world.getChunkWithCoordsPoint(world.getChunkPointWithCoords(tile.getX(), tile.getY()));
//			if(chunk != null) {
//				tile.setChunk(chunk);
//				if(!chunk.tileExists(tile)) {
//					chunk.addTile(tile);
//				}
//				tiles.remove(tile);
//			}
//		}
	}

	public void render(Graphics g, int width, int height) {
		int camX = (int)-cam.getX();
		int camY = (int)-cam.getY();
		LinkedList<LinkedList<GameObject>> all_game_objects = gameController.getAllGameObjects();

		// RENDER

		for (int i=0; i < all_game_objects.size(); i++) {
			LinkedList<GameObject> layer_game_objects = new LinkedList<>(all_game_objects.get(i));

			if(i == gameController.getPlayer().getZIndex()) {
				layer_game_objects.add(gameController.getPlayer());
			}

			if(i < object_entities.size()) {
				layer_game_objects.addAll(object_entities.get(i));
			}

			LinkedList<GameObject> y_sorted = new LinkedList<>();
			while (layer_game_objects.size() > 0) {
//				GameObject lowest = layer_game_objects.get(0);
//				int lowestY = lowest.getY();
//				if (lowest instanceof Bounds) {
//					if (((Bounds) lowest).getBounds() != null) {
//						lowestY = (int) (((Bounds) lowest).getBounds().getY() + ((Bounds) lowest).getBounds().getHeight());
//					}
//				}
				GameObject lowest = null;
				int lowestY = 0;
				for (int y = 0; y < layer_game_objects.size(); y++) {
					GameObject new_ent = layer_game_objects.get(y);
					int newY = new_ent.getY();
//					if (new_ent instanceof Bounds) {
//						if (((Bounds) new_ent).getBounds() != null) {
//							newY = (int) (((Bounds) new_ent).getBounds().getY() + ((Bounds) new_ent).getBounds().getHeight());
//						}
//					}
					if (newY < lowestY || lowest == null) {
						lowest = new_ent;
						lowestY = newY;
					}
				}
				y_sorted.add(lowest);
				layer_game_objects.remove(lowest);
			}
			for (GameObject entity : y_sorted) {
				entity.render(g);
			}
		}
	}

	public void addObject(GameObject object) {
		int z_index = object.getZIndex();
		for(int i=object_entities.size(); i<=z_index; i++) {
			this.object_entities.add(new LinkedList<GameObject>());
		}

		this.object_entities.get(z_index).add(object);
	}

	public void removeObject(GameObject object) {
		this.object_entities.get(object.getZIndex()).remove(object);
	}

	public void addHudObjects(GameObject object) {
		int z_index = object.getZIndex();
		for(int i=hud_objects.size(); i<=z_index; i++) {
			this.hud_objects.add(new LinkedList<>());
		}

		this.hud_objects.get(z_index).add(object);
	}

	public void removeHudObject(GameObject object) {
		this.hud_objects.get(object.getZIndex()).remove(object);
	}

	public LinkedList<LinkedList<GameObject>> getHudObjects() {
		return this.hud_objects;
	}

	/*public void removeTile(Tile tile) {
		// remove tile
	}*/

	public LinkedList<GameObject> getBoundsObjects() {
		LinkedList<LinkedList<GameObject>> all_game_objects = gameController.getAllGameObjects();
		LinkedList<GameObject> ret = new LinkedList<>();

		for (LinkedList<GameObject> list : object_entities) {
			for (int i = 0; i < list.size(); i++) {
				GameObject tempObject = list.get(i);
				if (tempObject instanceof Bounds) {
					if(((Bounds) tempObject).getBounds() != null) {
						ret.add(tempObject);
					}
				}
			}
		}

		for (LinkedList<GameObject> layer_game_objects : all_game_objects) {
			for (int i = 0; i < layer_game_objects.size(); i++) {
				GameObject tempObject = layer_game_objects.get(i);
				if (tempObject instanceof Bounds) {
					if(((Bounds) tempObject).getBounds() != null) {
						ret.add(tempObject);
					}
				}
			}
		}

		return ret;
	}

	public LinkedList<GameObject> getObjectsWithIds(ID... args) {
		ID[] ids = new ID[args.length];
		for(int i = 0; i < args.length; i++) {
			ids[i] = args[i];
		}
		LinkedList<LinkedList<GameObject>> all_game_objects = gameController.getAllGameObjects();
		LinkedList<GameObject> ret = new LinkedList<>();

		for (LinkedList<GameObject> list : object_entities) {
			for (int i = 0; i < list.size(); i++) {
				GameObject tempObject = list.get(i);
				if (isInArray(ids, tempObject.getId())) {
					ret.add(tempObject);
				}
			}
		}

		for (LinkedList<GameObject> layer_game_objects : all_game_objects) {
			for (int i = 0; i < layer_game_objects.size(); i++) {
				GameObject tempObject = layer_game_objects.get(i);
				if (isInArray(ids, tempObject.getId())) {
					ret.add(tempObject);
				}
			}
		}
 		return ret;
	}

	public LinkedList<GameObject> getSelectableObjects() {
		LinkedList<LinkedList<GameObject>> all_game_objects = gameController.getAllGameObjects();
		LinkedList<GameObject> ret = new LinkedList<GameObject>();
		// ID[] ids = { ID.Tree, ID.Mushroom, ID.Item, ID.Pebble };

		for (LinkedList<GameObject> list : object_entities) {
			for (int i = 0; i < list.size(); i++) {
				GameObject tempObject = list.get(i);
				if (tempObject instanceof Interactable) {
					if(((Interactable) tempObject).getSelectBounds() != null) {
						ret.add(tempObject);
					}
				}
			}
		}

		for (LinkedList<GameObject> list : all_game_objects) {
			for (int i = 0; i < list.size(); i++) {
				GameObject tempObject = list.get(i);
				if (tempObject instanceof Interactable) {
					if(((Interactable) tempObject).getSelectBounds() != null) {
						ret.add(tempObject);
					}
				}
			}
		}

		return ret;
	}

	public LinkedList<GameObject> getShadowObjects() {
		LinkedList<LinkedList<GameObject>> all_game_objects = gameController.getAllGameObjects();
		LinkedList<GameObject> ret = new LinkedList<GameObject>();
		ID[] ids = { ID.Tree, ID.Player };

		for (LinkedList<GameObject> list : object_entities) {
			for (int i = 0; i < list.size(); i++) {
				GameObject tempObject = list.get(i);
				if (isInArray(ids, tempObject.getId())) {
					ret.add(tempObject);
				}
			}
		}

		for (LinkedList<GameObject> list : all_game_objects) {
			for (int i = 0; i < list.size(); i++) {
				GameObject tempObject = list.get(i);
				if (isInArray(ids, tempObject.getId())) {
					ret.add(tempObject);
				}
			}
		}

		return ret;
	}

//	public LinkedList<Light> getLights() {
//		LinkedList<Chunk> chunks_on_screen = world.getChunksOnScreen();
//		LinkedList<Light> lights = new LinkedList<Light>();
//
//		for (Chunk chunk : chunks_on_screen) {
//			LinkedList<Light> chunk_content = chunk.lights;
//			for (Light obj : chunk_content) {
//				lights.add(obj);
//			}
//		}
//
//		return lights;
//	}

	private Boolean isInArray(ID[] arr, ID val) {
		for (ID tmp : arr) {
			if (tmp == val) {
				return true;
			}
		}
		return false;
	}

	public void findAndRemoveObject(GameObject object) {
		LinkedList<LinkedList<GameObject>> all_game_objects = gameController.getAllGameObjects();

		object_entities.get(object.getZIndex()).remove(object);

		all_game_objects.get(object.getZIndex()).remove(object);

		if(object instanceof Destroyable) {
			((Destroyable) object).destroyed();
		}
	}

	public boolean objectExistsAtCoords(Point coords) {
		LinkedList<LinkedList<GameObject>> all_game_objects = gameController.getAllGameObjects();

		for(LinkedList<GameObject> list : object_entities) {
			for(GameObject obj : list) {
				if(obj instanceof Bounds) {
					if(((Bounds) obj).getBounds().contains(coords) || obj.getX() == coords.x && obj.getY() == coords.y) {
						return true;
					}
				} else {
					if(obj.getX() == coords.x && obj.getY() == coords.y) {
						return true;
					}
				}
			}
		}


//		for(GameObject obj : chunks.get(z_index)) {
//			if(obj.x == tileCoords.x && obj.y == tileCoords.y) {
//				return true;
//			}
//		}

		for(LinkedList<GameObject> list : all_game_objects) {
			for(GameObject obj : list) {
				if(obj instanceof Bounds) {
					if(((Bounds) obj).getBounds().contains(coords) || obj.getX() == coords.x && obj.getY() == coords.y) {
						return true;
					}
				} else {
					if(obj.getX() == coords.x && obj.getY() == coords.y) {
						return true;
					}
				}
			}
		}
		return false;
	}

}
