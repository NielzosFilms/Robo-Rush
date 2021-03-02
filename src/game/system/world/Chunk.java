package game.system.world;

import java.awt.*;
import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;

import game.assets.tiles.tile.Transition;
import game.assets.tiles.tile.UpdateAble;
import game.system.helpers.Logger;
import game.system.main.Game;
import game.system.systems.gameObject.*;
import game.system.systems.lighting.Light;
import game.assets.tiles.tile.Tile;
import game.assets.structures.Structure;
import game.textures.Fonts;

public class Chunk implements Serializable {

	private static Random r = new Random();

	public LinkedList<LinkedList<GameObject>> entities = new LinkedList<>();
	public LinkedList<Light> lights = new LinkedList<>();
	public LinkedList<HashMap<Point, Tile>> tiles = new LinkedList<>();
	public LinkedList<Rectangle> extra_bounds = new LinkedList<>();
	public HashMap<Point, Structure> structures = new HashMap<>();
	public LinkedList<GameObject> objects_on_hud = new LinkedList<>();

	public static int tile_width = 16, tile_height = 16;
	public int x, y;

	public Chunk(int x, int y) {
		this.tiles.add(new HashMap<Point, Tile>());
		this.entities.add(new LinkedList<GameObject>());
		this.x = x;
		this.y = y;
		// entities.add(new Enemy((x+8)*16, (y+8)*16, ID.Enemy));
		// generate chunk tiles 16x16 then add to world

		//GenerateTiles(world);
		/*createTransitions();
		update();*/
	}

	public void tick() {
		// move entity from chunk to chunk
		objects_on_hud = new LinkedList<>();
		for (int j = 0; j < entities.size(); j++) {
			LinkedList<GameObject> list = entities.get(j);
			for (int i = 0; i < list.size(); i++) {
				GameObject entity = list.get(i);
				if (entity.getX() > (this.x + 16) * 16) {
					Chunk chunk = getNeighbourChunk(1, 0);
					if(chunk == null) continue;
					chunk.addEntity(entity);
					list.remove(entity);
				} else if (entity.getX() < (this.x) * 16) {
					Chunk chunk = getNeighbourChunk(-1, 0);
					if(chunk == null) continue;
					chunk.addEntity(entity);
					list.remove(entity);
				} else if (entity.getY() > (this.y + 16) * 16) {
					Chunk chunk = getNeighbourChunk(0, 1);
					if(chunk == null) continue;
					chunk.addEntity(entity);
					list.remove(entity);
				} else if (entity.getY() < (this.y - 16) * 16) {
					Chunk chunk = getNeighbourChunk(0, -1);
					if(chunk == null) continue;
					chunk.addEntity(entity);
					list.remove(entity);
				} else {
					entity.tick();
					if(entity instanceof Health) {
						objects_on_hud.add(((Health) entity).getHealthBar());
						if(((Health) entity).dead()) {
							removeObjectOnHud(((Health) entity).getHealthBar());
							if(entity instanceof Destroyable) {
								if(!((Destroyable) entity).destroyedCalled()) {
									((Destroyable) entity).destroyed();
								}
								if(((Destroyable) entity).canRemove()) {
									list.remove(entity);
								}
							} else {
								list.remove(entity);
							}
						}
					}
				}
			}
		}
		for(HashMap<Point, Tile> list : new LinkedList<>(tiles)) {
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
		for(Rectangle bounds : extra_bounds) {
			g.drawRect(bounds.x, bounds.y, bounds.width, bounds.height);
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

	public void createTransitions() {
		for(int i=0; i<tiles.size(); i++) {
			HashMap<Point, Tile> map = new HashMap<>(tiles.get(i));
			for(Point key : map.keySet()) {
				if (map.containsKey(key)) {
					Tile tile = map.get(key);
					if(tile instanceof Transition) {
						((Transition) tile).createTransitionTiles();
					}
				}
			}
		}
	}

	public void update() {
		// change texture of tile
		/*for(int i=0; i<tiles.size(); i++) {
			HashMap<Point, Tile> map = new HashMap<>(tiles.get(i));
			for(Point key : map.keySet()) {
				if (map.containsKey(key)) {
					Tile tile = map.get(key);
					if(tile instanceof UpdateAble) {
						((UpdateAble) tile).update();
					}
				}
			}
		}*/
	}

	/*public void updateSameTiles(Tile tile) {
		int tilemap_index = tile.getZIndex();
		HashMap<Point, Tile> tmp = new HashMap<>(tiles.get(tilemap_index));

		for (Map.Entry<Point, Tile> pointTileEntry : tmp.entrySet()) {
			if (tmp.containsKey(pointTileEntry.getKey())) {
				if (tmp.get(pointTileEntry.getKey()).getClass() == tile.getClass()) {
					Tile tile_2 = tmp.get(pointTileEntry.getKey());
					if(tile_2 instanceof Transition) {
						((Transition) tile_2).findAndSetEdgeTexture();
					}
				}
			}
		}
		tiles.set(tilemap_index, tmp);
	}*/

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

//	public void addTile(Tile tile) {
//		int zIndex = tile.getZIndex();
//		for (int i = tiles.size(); i <= zIndex; i++) {
//			tiles.add(new HashMap<Point, Tile>());
//		}
//		tiles.get(zIndex).put(new Point(tile.getChunkX(), tile.getChunkY()), tile);
//	}
//
//	public void removeTile(Tile tile) {
//		int zIndex = tile.getZIndex();
//		tiles.get(zIndex).remove(new Point(tile.getChunkX(), tile.getChunkY()), tile);
//	}
//
//	public boolean tileExists(Tile tile) {
//		if(tile.getZIndex() >= tiles.size()) return false;
//		return tiles.get(tile.getZIndex()).containsKey(new Point(tile.getChunkX(), tile.getChunkY()));
//	}

	public boolean tileExistsCoords(int index, Point tile_coords) {
		if(index >= tiles.size()) return false;
		return tiles.get(index).containsKey(tile_coords);
	}

	public void addEntity(GameObject ent) {
		//Logger.print(ent.getId() + " added to zindex: " + ent.getZIndex());
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
//		x_offset *= 16;
//		y_offset *= 16;
//		return Game.world.getChunkWithCoordsPoint(new Point(x + x_offset, y + y_offset));
		return null;
	}

	public LinkedList<Rectangle> getAllTileBounds() {
		LinkedList<Rectangle> bounds = new LinkedList<>();
		for(HashMap<Point, Tile> list : tiles) {
			for(Point key : list.keySet()) {
				Tile tile = list.get(key);
				if(tile instanceof Bounds) {
					Rectangle rect = ((Bounds) tile).getBounds();
					if(rect != null) bounds.add(rect);
				}
			}
		}
		bounds.addAll(extra_bounds);
		return bounds;
	}

	public LinkedList<Rectangle> getExtraBounds() {
		return this.extra_bounds;
	}

	public void addExtraBound(Rectangle rect) {
		this.extra_bounds.add(rect);
	}

	public void addObjectOnHud(GameObject object) {
		objects_on_hud.add(object);
	}

	public void removeObjectOnHud(GameObject object) {
		objects_on_hud.remove(object);
	}

	public LinkedList<GameObject> getObjectsOnHud() {
		return objects_on_hud;
	}
}
