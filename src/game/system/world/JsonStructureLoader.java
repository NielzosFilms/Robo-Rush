package game.system.world;

import game.assets.objects.puzzle_objects.PuzzleReciever;
import game.assets.objects.puzzle_objects.PuzzleTrigger;
import game.assets.tiles.Tile_Animation;
import game.assets.tiles.tile.Tile;
import game.assets.tiles.Tile_Static;
import game.system.helpers.StructureLoaderHelpers;
import game.system.helpers.Logger;
import game.system.main.Game;
import game.system.systems.gameObject.GameObject;
import game.textures.Animation;
import game.textures.TEXTURE_LIST;
import game.textures.Texture;
import game.textures.Textures;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.awt.*;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;

public class JsonStructureLoader {
    JSONParser parser = new JSONParser();
    private HashMap<Point, Chunk> chunks = new HashMap<>();

    private final int TO_TILE_SIZE = 16;
    private int tileSize = 16;
    private int division = 1;

    private HashMap<Integer, TEXTURE_LIST> texture_list_indexes = new HashMap<>();

    private LinkedList<Tile> static_tiles = new LinkedList<>();
    private LinkedList<GameObject> objects = new LinkedList<>();
    private LinkedList<Rectangle> bounds = new LinkedList<>();
    private Rectangle player_spawn;
    private GameObject exit_object;

    public JsonStructureLoader(String filepath) {
        try {
            JSONObject map = (JSONObject) parser.parse(new FileReader(filepath));
            tileSize = Integer.parseInt(map.get("tilewidth").toString());
            division = tileSize / TO_TILE_SIZE;
            if(!(boolean)map.get("infinite")) {
                throw new Exception("Loader only supports infinite tilemaps. file: " + filepath);
            }
            decodeTextureLists((JSONArray)map.get("tilesets"));
            decodeLayers((JSONArray) map.get("layers"));
            setLinkages();
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        } catch (Exception e) {
            Logger.printError(e.getMessage());
        }
    }

    private void decodeTextureLists(JSONArray tilesets) {
        for(Object o : tilesets) {
            JSONObject tileset = (JSONObject) o;
            int firstgid = StructureLoaderHelpers.getIntProp(tileset, "firstgid");
            String list_name = StructureLoaderHelpers.getCustomProp(tileset, "list_name");
            try {
                texture_list_indexes.put(firstgid, TEXTURE_LIST.valueOf(list_name));
                if(tileset.containsKey("tiles")) {
                    for(Object t : (JSONArray)tileset.get("tiles")) {
                        JSONObject tile = (JSONObject)t;
                        if(tile.containsKey("animation")) {
                            int id = StructureLoaderHelpers.getIntProp(tile, "id");
                            int animation_id = id + firstgid;
                            LinkedList<Texture> frames = new LinkedList<>();
                            for(Object a : (JSONArray) tile.get("animation")) {
                                JSONObject tile_frame = (JSONObject)a;
                                int tex_index = StructureLoaderHelpers.getIntProp(tile_frame, "tileid");
                                frames.add(new Texture(TEXTURE_LIST.valueOf(list_name), tex_index));
                            }
                            Animation animation = new Animation(20, frames.toArray(new Texture[0]));
                            if(Textures.generated_animations.containsKey(animation_id)) System.out.println("ALREADY HAS KEY: " + animation_id);
                            Textures.generated_animations.put(animation_id, animation);
                        }
                    }
                }
            } catch (Exception e) {
                Logger.printError("Tileset prop: 'list_name' not found, Tileset 'list_name': " + list_name);
                e.printStackTrace();
            }
        }
        System.out.println(Textures.generated_animations);
    }

    private void decodeLayers(JSONArray layers) {
        int layer_index = 0;
        for(Object o : layers) {
            JSONObject layer = (JSONObject) o;
            if((Boolean)layer.get("visible")) {
                if(layer.get("type").toString().equals("tilelayer")) {
                    decodeTileLayer(layer, layer_index);
                } else if(layer.get("type").toString().equals("objectgroup")) {
                    decodeObjectLayer(layer, layer_index);
                }
            }
            layer_index++;
        }
    }

    private void setLinkages() {
        for(GameObject obj : objects) {
            if(obj instanceof PuzzleTrigger) {
                PuzzleReciever reciever = getRecieverWithId(((PuzzleTrigger) obj).getRecieverId());
                if(reciever != null) {
                    ((PuzzleTrigger) obj).setReciever(reciever);
                }
            }
        }
    }
    private PuzzleReciever getRecieverWithId(int id) {
        for(GameObject obj : objects) {
            if(obj instanceof PuzzleReciever) {
                if(((PuzzleReciever) obj).getRevieverId() == id) {
                    return (PuzzleReciever) obj;
                }
            }
        }
        return null;
    }

    private void decodeTileLayer(JSONObject layer, int layer_index) {
        if(StructureLoaderHelpers.hasCustomProp(layer, "z_index")) {
            layer_index = Integer.parseInt(StructureLoaderHelpers.getCustomProp(layer, "z_index"));
        }
        for(Object c : (JSONArray)layer.get("chunks")) {
            JSONObject file_chunk = (JSONObject) c;
            int chunk_width = StructureLoaderHelpers.getIntProp(file_chunk, "width");
            int chunk_height = StructureLoaderHelpers.getIntProp(file_chunk, "height");

            int chunk_x = StructureLoaderHelpers.getIntProp(file_chunk, "x");
            int chunk_y = StructureLoaderHelpers.getIntProp(file_chunk, "y");

            Chunk chunk = getOrCreateChunk(chunk_x, chunk_y);

            int x = 0;
            int y = 0;
            for(Object o : (JSONArray)file_chunk.get("data")) {
                int tex_index = Integer.parseInt(o.toString());
                int tile_x = (chunk_x * TO_TILE_SIZE) + x * TO_TILE_SIZE;
                int tile_y = (chunk_y * TO_TILE_SIZE) + y * TO_TILE_SIZE;
                if(tex_index > 0) {
                    if(Textures.generated_animations.containsKey(tex_index)) {
                        chunk.addTile(new Tile_Animation(tile_x, tile_y, x, y, layer_index, chunk,
                                Textures.generated_animations.get(tex_index)));
                    } else {
                        chunk.addTile(new Tile_Static(tile_x, tile_y, x, y, layer_index, chunk,
                                new Texture(getTextureList(tex_index), tex_index-1 - getTextureListGid(tex_index))));
                    }
                }
                x++;
                if(x >= chunk_width) {
                    x = 0;
                    y++;
                }
            }
        }
    }

    private void decodeObjectLayer(JSONObject layer, int layer_index) {
        if(StructureLoaderHelpers.hasCustomProp(layer, "z_index")) {
            layer_index = Integer.parseInt(StructureLoaderHelpers.getCustomProp(layer, "z_index"));
        }
        for(Object o : (JSONArray)layer.get("objects")) {
            JSONObject object = (JSONObject) o;

            if(object.get("type").toString().equals("bounds")) {
                this.bounds.add(getRectangle(object));
            } else if(object.get("type").toString().equals("player_spawn")) {
                player_spawn = getRectangle(object);
            } else {
                try {
                    Class<?> clazz = Class.forName(StructureLoaderHelpers.getFullClassname(object));
                    Constructor<?> ctor = clazz.getConstructor(JSONObject.class, int.class, int.class, JsonStructureLoader.class);
                    addObjectToChunk((GameObject) ctor.newInstance(object, layer_index, division, this));
                } catch (ClassNotFoundException | NoSuchMethodException e) {
                    Logger.printError("Class not found: " + object.get("type").toString());
                    e.printStackTrace();
                } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
                    Logger.printError("Class constructor not found: " + object.get("type").toString());
                    e.printStackTrace();
                }
            }
        }
    }

    private void addObjectToChunk(GameObject object) {
        Point chunk_coords = Game.world.getChunkPointWithCoords(object.getX(), object.getY());
        getOrCreateChunk(chunk_coords.x, chunk_coords.y).addEntity(object);
    }

    private Chunk getOrCreateChunk(int chunk_x, int chunk_y) {
        Chunk chunk;
        if(chunks.containsKey(new Point(chunk_x, chunk_y))) {
            chunk = chunks.get(new Point(chunk_x, chunk_y));
        } else {
            chunk = new Chunk(chunk_x, chunk_y);
            chunks.put(new Point(chunk_x, chunk_y), chunk);
        }
        return chunk;
    }

    private Rectangle getRectangle(JSONObject bounds) {
        int x = Integer.parseInt(bounds.get("x").toString()) / division;
        int y = Integer.parseInt(bounds.get("y").toString()) / division;
        int width = Integer.parseInt(bounds.get("width").toString()) / division;
        int height = Integer.parseInt(bounds.get("height").toString()) / division;
        return new Rectangle(x, y, width, height);
    }

    public HashMap<Point, Chunk> getChunks() {
        return chunks;
    }

    private TEXTURE_LIST getTextureList(int index) {
        TEXTURE_LIST ret = null;
        for(int firstgid : getGidsOrdered()) {
            if(index >= firstgid) ret = texture_list_indexes.get(firstgid);
        }
        return ret;
    }

    private int getTextureListGid(int index) {
        int ret = 1;
        for(int firstgid : getGidsOrdered()) {
            if(index >= firstgid) ret = firstgid;
        }
        return ret-1;
    }

    private LinkedList<Integer> getGidsOrdered() {
        LinkedList<Integer> gids = new LinkedList<>(texture_list_indexes.keySet());
        Comparator<Integer> order = Integer::compare;
        gids.sort(order);
        return gids;
    }

    public LinkedList<Tile> getStatic_tiles() {
        return static_tiles;
    }

    public LinkedList<GameObject> getObjects() {
        return objects;
    }

    public LinkedList<Rectangle> getBounds() {
        return bounds;
    }

    public Rectangle getPlayerSpawn() {
        return player_spawn;
    }

    public GameObject getExitObject() {
        return exit_object;
    }
}
