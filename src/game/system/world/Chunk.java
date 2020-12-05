package game.system.world;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;

import game.assets.entities.Player;
import game.enums.BIOME;
import game.system.systems.lighting.Light;
import game.system.systems.GameObject;
import game.enums.ID;
import game.assets.objects.stick.Branch;
import game.assets.objects.rock.Pebble;
import game.assets.tiles.Tile;
import game.assets.tiles.TileGrass;
import game.assets.tiles.TileWater;
import game.assets.objects.tree.Tree;
import game.assets.structures.Structure;
import game.textures.Fonts;
import game.textures.Textures;

public class Chunk implements Serializable {

	private static Random r = new Random();

	public LinkedList<LinkedList<GameObject>> entities = new LinkedList<LinkedList<GameObject>>();
	public LinkedList<Light> lights = new LinkedList<Light>();
	public LinkedList<HashMap<Point, Tile>> tiles = new LinkedList<HashMap<Point, Tile>>();
	public LinkedList<HashMap<Point, Structure>> structures = new LinkedList<HashMap<Point, Structure>>();

	public static int tile_width = 16, tile_height = 16;
	public int x, y;
	private Long seed;
	private Long temp_seed;
	private Long moist_seed;
	private World world;
	private transient Textures textures;

	public Chunk(int x, int y, Long seed, Long temp_seed, Long moist_seed, World world, Player player,
			Textures textures) {
		this.tiles.add(new HashMap<Point, Tile>());
		this.entities.add(new LinkedList<GameObject>());
		this.x = x;
		this.y = y;
		this.seed = seed;
		this.temp_seed = temp_seed;
		this.moist_seed = moist_seed;
		this.world = world;
		this.textures = textures;
		// entities.add(new Enemy((x+8)*16, (y+8)*16, ID.Enemy));
		// generate chunk tiles 16x16 then add to world
		GenerateTiles(world, player);
	}

	public void tick() {
		// move entity from chunk to chunk
		for (LinkedList<GameObject> list : entities) {
			for (int i = 0; i < list.size(); i++) {
				GameObject entity = list.get(i);
				if (entity.getX() > (this.x + 16) * 16) {
					Chunk chunk = this.world.getChunkWithCoords(this.x + 16, this.y);
					if(chunk == null) continue;
					chunk.entities.get(0).add(entity);
					list.remove(i);
					return;
				} else if (entity.getX() < (this.x) * 16) {
					Chunk chunk = this.world.getChunkWithCoords(this.x - 16, this.y);
					if(chunk == null) continue;
					chunk.entities.get(0).add(entity);
					list.remove(i);
					return;
				} else if (entity.getY() > (this.y + 16) * 16) {
					Chunk chunk = this.world.getChunkWithCoords(this.x, this.y + 16);
					if(chunk == null) continue;
					chunk.entities.get(0).add(entity);
					list.remove(i);
					return;
				} else if (entity.getY() < (this.y - 16) * 16) {
					Chunk chunk = this.world.getChunkWithCoords(this.x, this.y - 16);
					if(chunk == null) continue;
					chunk.entities.get(0).add(entity);
					list.remove(i);
					return;
				} else {
					entity.tick();
				}
			}
		}
	}

	public void renderBorder(Graphics g) {
		g.setColor(Color.decode("#70deff"));
		g.drawRect(x * 16, y * 16, 16 * 16, 16 * 16);
		g.setFont(Fonts.default_fonts.get(5));
		g.drawString(x + "," + y, x * 16 + 2, y * 16 + 8);
	}

	public void renderTiles(Graphics g) {
		for (HashMap<Point, Tile> list : tiles) {
			for (Map.Entry<Point, Tile> pointTileEntry : list.entrySet()) {
				Tile tile = (Tile) pointTileEntry.getValue();
				tile.render(g);
			}
		}
	}

	public void renderEntities(Graphics g) {
		for (LinkedList<GameObject> list : entities) {
			for (GameObject entity : list) {
				entity.render(g);
			}
		}
	}

	public void updateTiles(int tilemap_index) {
		// change texture of tile
		HashMap<Point, Tile> tmp = new HashMap<>(tiles.get(tilemap_index));
		for (Map.Entry<Point, Tile> pointTileEntry : tmp.entrySet()) {
			if (tmp.containsKey(pointTileEntry.getKey())) {
				tmp.get(pointTileEntry.getKey()).findAndSetEdgeTexture(tilemap_index);
			}
		}
	}

	public void updateSameTiles(Tile tile) {
		int tilemap_index = tile.getZIndex();
		HashMap<Point, Tile> tmp = new HashMap<>(tiles.get(tilemap_index));

		for (Map.Entry<Point, Tile> pointTileEntry : tmp.entrySet()) {
			if (tmp.containsKey(pointTileEntry.getKey())) {
				if (tmp.get(pointTileEntry.getKey()).getClass() == tile.getClass()) {
					tmp.get(pointTileEntry.getKey()).findAndSetEdgeTexture(tilemap_index);
				}
			}
		}
		tiles.set(tilemap_index, tmp);
	}

	private void GenerateTiles(World world, Player player) {
		float[][] osn = world.getOsn(x, y, tile_width, tile_height);
		float[][] temp_osn = world.getTemperatureOsn(x, y, tile_width, tile_height);
		float[][] moist_osn = world.getMoistureOsn(x, y, tile_width, tile_height);

		// create simple tiles
		for (int yy = 0; yy < osn.length; yy++) {
			for (int xx = 0; xx < osn[yy].length; xx++) {
				float val = osn[xx][yy];

				float temp_val = temp_osn[xx][yy];
				float moist_val = moist_osn[xx][yy];

				int resized_x = xx * 16;
				int world_x = resized_x + x * 16;

				int resized_y = yy * 16;
				int world_y = resized_y + y * 16;

				if (World.getBiome(val, temp_val, moist_val) == BIOME.Forest) {
					addTile(new TileGrass(world_x, world_y, xx, yy, 1, BIOME.Forest, this));
					int num = r.nextInt(100);
					if (num == 0) {
						addEntity(new Tree(world_x, world_y, 1, ID.Tree, BIOME.Forest, player));
						int stick = r.nextInt(5);
						if (stick == 0) {
							int placement = r.nextInt(2);
							if (placement == 0) {
								addEntity(new Branch(world_x - 16, world_y, 0, ID.Branch));
							} else {
								addEntity(new Branch(world_x + 16, world_y, 0, ID.Branch));
							}
						}
					} else if (num == 1) {
						addEntity(new Pebble(world_x, world_y, 0, ID.Pebble));
					}
				} else {
					addTile(new TileWater(world_x, world_y, xx, yy, 1, BIOME.Ocean, this));
				}
			}
		}
		updateTiles(1);
	}

	public LinkedList<GameObject> getEntities() {
		LinkedList<GameObject> tmp = new LinkedList<GameObject>();
		for (LinkedList<GameObject> list : entities) {
			tmp.addAll(list);
		}
		return tmp;
	}

	public LinkedList<Tile> getTiles() {
		LinkedList<Tile> tmp = new LinkedList<>();
		LinkedList<HashMap<Point, Tile>> tiles = new LinkedList<>(this.tiles);
		for (HashMap<Point, Tile> list : tiles) {
			for(Point key : list.keySet()) {
				tmp.add(list.get(key));
			}
		}
		return tmp;
	}

	public HashMap<Point, Tile> getTileMap(int index) {
		for (int i = tiles.size(); i <= index; i++) {
			tiles.add(new HashMap<Point, Tile>());
		}
		return tiles.get(index);
	}

	// OLD?
	public void removeEntity(GameObject object) {
		for (LinkedList<GameObject> list : entities) {
			if (list.contains(object)) {
				list.remove(object);
				return;
			}
		}
		// this.entities.remove(object);
	}

	public void addTile(Tile tile) {
		int zIndex = tile.getZIndex();
		for (int i = tiles.size(); i <= zIndex; i++) {
			tiles.add(new HashMap<Point, Tile>());
		}
		tiles.get(zIndex).put(new Point(tile.getChunkX(), tile.getChunkY()), tile);
	}

	public void removeTile(Tile tile) {
		int zIndex = tile.getZIndex();
		tiles.get(zIndex).remove(new Point(tile.getChunkX(), tile.getChunkY()), tile);
	}

	public boolean tileExists(Tile tile) {
		if(tile.getZIndex() >= tiles.size()) return false;
		return tiles.get(tile.getZIndex()).containsKey(new Point(tile.getChunkX(), tile.getChunkY()));
	}

	public boolean tileExistsCoords(int index, Point tile_coords) {
		if(index >= tiles.size()) return false;
		return tiles.get(index).containsKey(tile_coords);
	}

	public void addEntity(GameObject ent) {
		int zIndex = ent.getZIndex();
		for (int i = entities.size(); i <= zIndex; i++) {
			entities.add(new LinkedList<GameObject>());
		}
		entities.get(zIndex).add(ent);
	}

	public void addLight(Light light) {
		lights.add(light);
	}

	public Chunk getNeighbourChunk(int x_offset, int y_offset) {
		x_offset *= 16;
		y_offset *= 16;
		return this.world.getChunkWithCoordsPoint(new Point(x + x_offset, y + y_offset));
	}
}
