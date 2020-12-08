package game.system.world;

import java.awt.*;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;

import game.assets.entities.Player;
import game.enums.BIOME;
import game.system.helpers.Logger;
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

	public LinkedList<LinkedList<GameObject>> entities = new LinkedList<>();
	public LinkedList<Light> lights = new LinkedList<>();
	public LinkedList<HashMap<Point, Tile>> tiles = new LinkedList<>();
	public HashMap<Point, Structure> structures = new HashMap<>();

	public static int tile_width = 16, tile_height = 16;
	public int x, y;
	private World world;

	public Chunk(int x, int y, World world) {
		this.tiles.add(new HashMap<Point, Tile>());
		this.entities.add(new LinkedList<GameObject>());
		this.x = x;
		this.y = y;
		this.world = world;
		// entities.add(new Enemy((x+8)*16, (y+8)*16, ID.Enemy));
		// generate chunk tiles 16x16 then add to world
		GenerateTiles(world);
	}

	public void tick() {
		// move entity from chunk to chunk
		for (LinkedList<GameObject> list : entities) {
			for (int i = 0; i < list.size(); i++) {
				GameObject entity = list.get(i);
				if (entity.getX() > (this.x + 16) * 16) {
					Chunk chunk = this.world.getChunkWithCoords(this.x + 16, this.y);
					if(chunk == null) continue;
					addEntity(entity);
					list.remove(i);
					return;
				} else if (entity.getX() < (this.x) * 16) {
					Chunk chunk = this.world.getChunkWithCoords(this.x - 16, this.y);
					if(chunk == null) continue;
					addEntity(entity);
					list.remove(i);
					return;
				} else if (entity.getY() > (this.y + 16) * 16) {
					Chunk chunk = this.world.getChunkWithCoords(this.x, this.y + 16);
					if(chunk == null) continue;
					addEntity(entity);
					list.remove(i);
					return;
				} else if (entity.getY() < (this.y - 16) * 16) {
					Chunk chunk = this.world.getChunkWithCoords(this.x, this.y - 16);
					if(chunk == null) continue;
					addEntity(entity);
					list.remove(i);
					return;
				} else {
					entity.tick();
				}
			}
		}
		for(HashMap<Point, Tile> list : tiles) {
			for(Point key : list.keySet()) {
				list.get(key).tick();
			}
		}
	}

	public void renderBorder(Graphics g) {
		g.setColor(new Color(232, 57, 57));
		for(Rectangle tile_bounds : getAllTileBounds()) {
			g.drawRect(tile_bounds.x, tile_bounds.y, tile_bounds.width, tile_bounds.height);
		}
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

	public void updateTiles() {
		// change texture of tile
		for(HashMap<Point, Tile> map : new LinkedList<>(tiles)) {
			for (Map.Entry<Point, Tile> pointTileEntry : map.entrySet()) {
				if (map.containsKey(pointTileEntry.getKey())) {
					map.get(pointTileEntry.getKey()).findAndSetEdgeTexture();
				}
			}
		}
	}

	public void updateSameTiles(Tile tile) {
		int tilemap_index = tile.getZIndex();
		HashMap<Point, Tile> tmp = new HashMap<>(tiles.get(tilemap_index));

		for (Map.Entry<Point, Tile> pointTileEntry : tmp.entrySet()) {
			if (tmp.containsKey(pointTileEntry.getKey())) {
				if (tmp.get(pointTileEntry.getKey()).getClass() == tile.getClass()) {
					tmp.get(pointTileEntry.getKey()).findAndSetEdgeTexture();
				}
			}
		}
		tiles.set(tilemap_index, tmp);
	}

	private void GenerateTiles(World world) {
		float[][] osn = world.getGeneration().getHeightOsn(x, y, tile_width, tile_height);
		float[][] temp_osn = world.getGeneration().getTemperatureOsn(x, y, tile_width, tile_height);
		float[][] moist_osn = world.getGeneration().getMoistureOsn(x, y, tile_width, tile_height);

		// create simple tiles
		int tilemap_index = 1;
		for (int yy = 0; yy < osn.length; yy++) {
			for (int xx = 0; xx < osn[yy].length; xx++) {
				float val = osn[xx][yy];

				float temp_val = temp_osn[xx][yy];
				float moist_val = moist_osn[xx][yy];

				int resized_x = xx * 16;
				int world_x = resized_x + x * 16;

				int resized_y = yy * 16;
				int world_y = resized_y + y * 16;

				Tile tile = world.getGeneratedTile(xx, yy, val, temp_val, moist_val, this, world_x, world_y);
				addTile(tile);
			}
		}
		updateTiles();
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
		Logger.print(ent.getId() + " added to zindex: " + ent.getZIndex());
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

	public LinkedList<Rectangle> getAllTileBounds() {
		LinkedList<Rectangle> bounds = new LinkedList<>();
		for(HashMap<Point, Tile> list : tiles) {
			for(Point key : list.keySet()) {
				Tile tile = list.get(key);
				if(tile.getBounds() != null) bounds.add(tile.getBounds());
			}
		}
		return bounds;
	}
}
