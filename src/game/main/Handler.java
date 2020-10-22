package game.main;

import java.awt.Graphics;
import java.awt.Point;
import java.util.LinkedList;

import game.entities.particles.Particle;
import game.entities.particles.ParticleSystem;
import game.lighting.Light;
import game.tiles.Tile;
import game.world.Chunk;
import game.world.World;

public class Handler {

	public LinkedList<LinkedList<GameObject>> object_entities = new LinkedList<LinkedList<GameObject>>();
	// public LinkedList<LinkedList<Tile>> object_tiles = new
	// LinkedList<LinkedList<Tile>>();

	public LinkedList<Light> lights = new LinkedList<Light>();
	public LinkedList<LinkedList<GameObject>> chunks = new LinkedList<LinkedList<GameObject>>(); // add everything to
																									// their
																									// corresponding
																									// chunks and loop
																									// throught the
																									// chunks

	public Handler() {
		for (int i = 0; i < 4; i++) {
			this.object_entities.add(new LinkedList<GameObject>());
		}
	}

	public void tick(World world) {
		for (LinkedList<GameObject> list : object_entities) {
			for (int i = 0; i < list.size(); i++) {
				GameObject tempObject = list.get(i);
				if (tempObject.getId() != ID.Player) {
					int x = tempObject.getX();
					int y = tempObject.getY();
					if (world.getChunkPointWithCoords(x, y) != null) { // adds enemy to a chunk to be unloaded
						world.chunks.get(world.getChunkPointWithCoords(x, y)).entities.get(0).add(tempObject);
						list.remove(i);
					}
				} else {
					tempObject.tick();
				}
			}
		}

		for (Light light : lights) {
			int x = light.getX();
			int y = light.getY();
			if (world.getChunkPointWithCoords(x, y) != null) { // adds enemy to a chunk to be unloaded
				world.chunks.get(world.getChunkPointWithCoords(x, y)).lights.add(light);
				lights.remove(light);
			}
		}
	}

	public void render(Graphics g, int camX, int camY, int width, int height, World world, ParticleSystem ps) {
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

		for (LinkedList<GameObject> chunk : chunks) {
			for (GameObject object : chunk) {
				entities.get(object.getZIndex()).add(object);
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

		// particles
		LinkedList<Particle> particles = ps.getParticles();
		for (Particle particle : particles) {
			entities.get(particle.getZIndex()).add(particle);
		}

		// RENDER
		for (LinkedList<Tile> list : tiles) {
			for (Tile tile : list) {
				tile.render(g);
			}
		}
		for (LinkedList<GameObject> list : entities) {
			for (GameObject obj : list) {
				obj.render(g); // klopt niet meer
			}
		}

		if (Game.DEDUG_MODE) {
			for (Chunk chunk : chunks_on_screen) {
				chunk.renderBorder(g);
			}
		}

	}

	public void addLight(Light light) {
		this.lights.add(light);
	}

	public void addObject(int z_index, GameObject object) {
		while (z_index >= this.object_entities.size()) {// add new layers if it doesnt exist
			this.object_entities.add(new LinkedList<GameObject>());
		}
		this.object_entities.get(z_index).add(object);
	}

	public void removeObject(int z_index, GameObject object) {
		this.object_entities.get(z_index).remove(object);
	}

	public void addTile(int z_index, Tile tile) {
		// add tile
	}

	public void removeTile(int z_index, Tile tile) {
		// remove tile
	}

	public LinkedList<GameObject> getSelectableObjects(World world) {
		LinkedList<Chunk> chunks_on_screen = world.getChunksOnScreen();
		LinkedList<GameObject> objs = new LinkedList<GameObject>();
		ID[] ids = { ID.Tree, ID.Mushroom, ID.Item, ID.Pebble };

		for (LinkedList<GameObject> list : object_entities) {
			for (int i = 0; i < list.size(); i++) {
				GameObject tempObject = list.get(i);
				if (isInArray(ids, tempObject.getId())) {
					objs.add(tempObject);
				}
			}
		}

		for (LinkedList<GameObject> chunk : chunks) {
			for (GameObject object : chunk) {
				if (isInArray(ids, object.getId())) {
					objs.add(object);
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

	public LinkedList<GameObject> getShadowObjects(World world) {
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

		for (LinkedList<GameObject> chunk : chunks) {
			for (GameObject object : chunk) {
				if (isInArray(ids, object.getId())) {
					objs.add(object);
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

	public LinkedList<Light> getLights(World world) {
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

	public void findAndRemoveObject(GameObject item, World world) {
		LinkedList<Chunk> chunks_on_screen = world.getChunksOnScreen();

		for (LinkedList<GameObject> list : object_entities) {
			list.remove(item);
		}

		for (LinkedList<GameObject> chunk : chunks) {
			chunk.remove(item);
		}

		// chunks
		for (Chunk chunk : chunks_on_screen) {
			// chunk.removeFromTilesEntities(item);
			world.chunks.get(new Point(chunk.x, chunk.y)).removeFromTilesEntities(item);
		}
	}

}
