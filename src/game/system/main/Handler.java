package game.system.main;

import game.assets.tiles.Tile;
import game.enums.ID;
import game.system.lighting.Light;
import game.system.particles.ParticleSystem;
import game.system.world.Chunk;
import game.system.world.World;

import java.awt.*;
import java.io.Serializable;
import java.util.LinkedList;

public class Handler implements Serializable {
	private transient World world;
	private transient Camera cam;
	private transient ParticleSystem ps;

	public LinkedList<LinkedList<GameObject>> object_entities = new LinkedList<LinkedList<GameObject>>();
	// public LinkedList<LinkedList<Tile>> object_tiles = new
	// LinkedList<LinkedList<Tile>>();

	public LinkedList<Light> lights = new LinkedList<Light>();

	public Handler() {
		for (int i = 0; i < 4; i++) {
			this.object_entities.add(new LinkedList<GameObject>());
		}
	}

	public void setRequirements(World world, Camera cam, ParticleSystem ps) {
		this.world = world;
		this.cam = cam;
		this.ps = ps;
	}

	public void tick() {
		for (LinkedList<GameObject> list : object_entities) {
			for (int i = 0; i < list.size(); i++) {
				GameObject tempObject = list.get(i);
				if (tempObject.getId() == ID.Player) {
					tempObject.tick();
				} else {
					if(world.addEntityToChunk(tempObject)) list.remove(tempObject);
				}
			}
		}

		for (Light light : lights) {
			if(world.addLightToChunk(light)) lights.remove(light);
		}
	}

	public void render(Graphics g, int width, int height) {
		int camX = (int)-cam.getX();
		int camY = (int)-cam.getY();
		LinkedList<LinkedList<GameObject>> entities = new LinkedList<LinkedList<GameObject>>();
		LinkedList<LinkedList<Tile>> tiles = new LinkedList<LinkedList<Tile>>();

		LinkedList<Chunk> chunks_on_screen = world.getChunksOnScreen();

		for (int i = 0; i < object_entities.size(); i++) {
			entities.add(new LinkedList<GameObject>());
		}

		for (LinkedList<GameObject> list : object_entities) {
			for (int i = 0; i < list.size(); i++) {
				GameObject tempObject = list.get(i);
				if (tempObject.getX() + 16 > camX && tempObject.getY() + 16 > camY
						&& tempObject.getX() - 16 < camX + width && tempObject.getY() - 16 < camY + height) {
					entities.get(tempObject.getZIndex()).add(tempObject);
				}
			}
		}

		// chunks
		for (Chunk chunk : chunks_on_screen) {
			for (Tile tmp_tile : chunk.getTiles()) {
				int zIndex = tmp_tile.getZIndex();
				if (zIndex < tiles.size()) {
					tiles.get(zIndex).add(tmp_tile);
				} else {
					for (int i = 0; i <= zIndex; i++) {
						tiles.add(new LinkedList<Tile>());
					}
					tiles.get(zIndex).add(tmp_tile);
				}
			}

			for (GameObject obj : chunk.getEntities()) {
				entities.get(obj.getZIndex()).add(obj);
			}

		}

		// RENDER
		for (LinkedList<Tile> list : tiles) {
			for (Tile tile : list) {
				tile.render(g);
			}
		}
		for (LinkedList<GameObject> list : entities) {
			for (GameObject obj : list) {
				obj.render(g);
			}
		}

		if (Game.DEBUG_MODE) {
			for (Chunk chunk : chunks_on_screen) {
				chunk.renderBorder(g);
			}
		}

	}

	public void addLight(Light light) {
		this.lights.add(light);
	}

	public void addObject(GameObject object) {
		int z_index = object.getZIndex();
		while (z_index >= this.object_entities.size()) {// add new layers if it doesnt exist
			this.object_entities.add(new LinkedList<GameObject>());
		}

		if(!world.addEntityToChunk(object)) {
			this.object_entities.get(z_index).add(object);
		}
	}

	public void removeObject(GameObject object) {
		this.object_entities.get(object.getZIndex()).remove(object);
	}

	public void addTile(int z_index, Tile tile) {
		// add tile
	}

	public void removeTile(int z_index, Tile tile) {
		// remove tile
	}

	public LinkedList<GameObject> getBoundsObjects() {
		LinkedList<Chunk> chunks_on_screen = world.getChunksOnScreen();
		LinkedList<GameObject> objs = new LinkedList<GameObject>();

		for (LinkedList<GameObject> list : object_entities) {
			for (int i = 0; i < list.size(); i++) {
				GameObject tempObject = list.get(i);
				if (tempObject.getBounds() != null) {
					objs.add(tempObject);
				}
			}
		}

		for (Chunk chunk : chunks_on_screen) {
			for (GameObject tempObject : chunk.getEntities()) {
				if (tempObject.getBounds() != null) {
					objs.add(tempObject);
				}
			}
		}

		return objs;
	}

	public LinkedList<GameObject> getSelectableObjects() {
		LinkedList<Chunk> chunks_on_screen = world.getChunksOnScreen();
		LinkedList<GameObject> objs = new LinkedList<GameObject>();
		// ID[] ids = { ID.Tree, ID.Mushroom, ID.Item, ID.Pebble };

		for (LinkedList<GameObject> list : object_entities) {
			for (int i = 0; i < list.size(); i++) {
				GameObject tempObject = list.get(i);
				if (tempObject.getSelectBounds() != null) {
					objs.add(tempObject);
				}
			}
		}

		// chunks
		for (Chunk chunk : chunks_on_screen) {
			for (GameObject tempObject : chunk.getEntities()) {
				if (tempObject.getSelectBounds() != null) {
					objs.add(tempObject);
				}
			}
		}

		return objs;
	}

	public LinkedList<GameObject> getShadowObjects() {
		LinkedList<Chunk> chunks_on_screen = world.getChunksOnScreen();
		LinkedList<GameObject> objs = new LinkedList<GameObject>();
		ID[] ids = { ID.Tree, ID.Player };

		for (LinkedList<GameObject> list : object_entities) {
			for (int i = 0; i < list.size(); i++) {
				GameObject tempObject = list.get(i);
				if (isInArray(ids, tempObject.getId())) {
					objs.add(tempObject);
				}
			}
		}

		// chunks
		for (Chunk chunk : chunks_on_screen) {
			for (GameObject obj : chunk.getEntities()) {
				if (isInArray(ids, obj.getId())) {
					objs.add(obj);
				}
			}
		}

		return objs;
	}

	public LinkedList<Light> getLights() {
		LinkedList<Chunk> chunks_on_screen = world.getChunksOnScreen();
		LinkedList<Light> lights = new LinkedList<Light>();

		for (Chunk chunk : chunks_on_screen) {
			LinkedList<Light> chunk_content = chunk.lights;
			for (Light obj : chunk_content) {
				lights.add(obj);
			}
		}

		return lights;
	}

	private Boolean isInArray(ID[] arr, ID val) {
		for (ID tmp : arr) {
			if (tmp == val) {
				return true;
			}
		}
		return false;
	}

	public void findAndRemoveObject(GameObject item) {
		LinkedList<Chunk> chunks_on_screen = world.getChunksOnScreen();

		for (LinkedList<GameObject> list : object_entities) {
			list.remove(item);
		}

		// chunks
		for (Chunk chunk : chunks_on_screen) {
			world.getChunkWithCoords(chunk.x, chunk.y).removeEntity(item);
		}
		item.destroyed();
	}

	public boolean objectExistsAtCoords(Point coords, int z_index) {
		LinkedList<Chunk> chunks_on_screen = world.getChunksOnScreen();

		for(LinkedList<GameObject> list : object_entities) {
			for(GameObject obj : list) {
				if(obj.getBounds() != null) {
					if(obj.getBounds().contains(coords) || obj.getX() == coords.x && obj.getY() == coords.y) {
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

		for(Chunk chunk : chunks_on_screen) {
			for(GameObject obj : chunk.getEntities()) {
				if(obj.getBounds() != null) {
					if(obj.getBounds().contains(coords) || obj.getX() == coords.x && obj.getY() == coords.y) {
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
