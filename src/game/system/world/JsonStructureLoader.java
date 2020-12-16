package game.system.world;

import game.assets.tiles.Tile;
import game.assets.tiles.Tile_Static;
import game.system.helpers.StructureLoaderHelpers;
import game.system.helpers.Logger;
import game.system.systems.GameObject;
import game.textures.TEXTURE_LIST;
import game.textures.Texture;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.w3c.dom.css.Rect;

import java.awt.*;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
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
            decodeTextureLists((JSONArray)map.get("tilesets"));
            decodeLayers((JSONArray) map.get("layers"));
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    private void decodeTextureLists(JSONArray tilesets) {
        for(Object o : tilesets) {
            JSONObject tileset = (JSONObject) o;
            int firstgid = StructureLoaderHelpers.getIntProp(tileset, "firstgid");
            String list_name = StructureLoaderHelpers.getCustomProp(tileset, "list_name");
            try {
                texture_list_indexes.put(firstgid, TEXTURE_LIST.valueOf(list_name));
            } catch (Exception e) {
                Logger.print("Tileset prop: 'list_name' not found, Tileset 'list_name': " + list_name);
                e.printStackTrace();
            }
        }
        Logger.print(texture_list_indexes.toString());
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

    private void decodeTileLayer(JSONObject layer, int layer_index) {
        int width_tiles = StructureLoaderHelpers.getIntProp(layer, "width");
        int height_tiles = StructureLoaderHelpers.getIntProp(layer, "height");
        int layer_x = StructureLoaderHelpers.getIntProp(layer, "x");
        int layer_y = StructureLoaderHelpers.getIntProp(layer, "y");
        if(StructureLoaderHelpers.hasCustomProp(layer, "z_index")) {
            layer_index = Integer.parseInt(StructureLoaderHelpers.getCustomProp(layer, "z_index"));
        }

        int x = 0;
        int y = 0;
        for(Object o : (JSONArray)layer.get("data")) {
            int tex_index = Integer.parseInt(o.toString());
            if(tex_index > 0) {
                static_tiles.add(new Tile_Static(layer_x + x * TO_TILE_SIZE, layer_y + y * TO_TILE_SIZE, x, y, layer_index, null,
                        new Texture(getTextureList(tex_index), tex_index-1 - getTextureListGid(tex_index))));
            }
            x++;
            if(x == width_tiles) {
                x = 0;
                y++;
            }
        }
    }

    private void decodeObjectLayer(JSONObject layer, int layer_index) {
        for(Object o : (JSONArray)layer.get("objects")) {
            JSONObject object = (JSONObject) o;
            if(StructureLoaderHelpers.hasCustomProp(layer, "z_index")) {
                layer_index = Integer.parseInt(StructureLoaderHelpers.getCustomProp(layer, "z_index"));
            }

            if(object.get("type").toString().equals("bounds")) {
                this.bounds.add(getRectangle(object));
            } else if(object.get("type").toString().equals("player_spawn")) {
                player_spawn = getRectangle(object);
            } else if(object.get("type").toString().equals("StructureExit")){
                // TODO make this object
            } else {
                try {
                    Class<?> clazz = Class.forName(StructureLoaderHelpers.getFullClassname(object));
                    Constructor<?> ctor = clazz.getConstructor(JSONObject.class, Integer.class, Integer.class);
                    objects.add((GameObject) ctor.newInstance(object, layer_index, division));
                } catch (ClassNotFoundException | NoSuchMethodException e) {
                    Logger.print("Class not found: " + object.get("type").toString());
                    e.printStackTrace();
                } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
                    Logger.print("Class constructor not found: " + object.get("type").toString());
                    e.printStackTrace();
                }
            }
        }
    }

    private Rectangle getRectangle(JSONObject bounds) {
        int x = Integer.parseInt(bounds.get("x").toString()) / division;
        int y = Integer.parseInt(bounds.get("y").toString()) / division;
        int width = Integer.parseInt(bounds.get("width").toString()) / division;
        int height = Integer.parseInt(bounds.get("height").toString()) / division;
        return new Rectangle(x, y, width, height);
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

    public HashMap<Point, Chunk> getChunks(World world) {
        Point origin = world.getChunkPointWithCoords(player_spawn.x, player_spawn.y);
        chunks.put(origin, new Chunk(origin.x, origin.y, world));
        for (Tile tile : static_tiles) {
            chunks.get(getContainingChunk(tile.getX(), tile.getY(), world)).addTile(tile);
        }
        for (GameObject entity : objects) {
            chunks.get(getContainingChunk(entity.getX(), entity.getY(), world)).addEntity(entity);
        }
        for (Rectangle bounds : bounds) {
            chunks.get(getContainingChunk(bounds.x, bounds.y, world)).addExtraBound(bounds);
        }
        return chunks;
    }

    public Point getContainingChunk(int x, int y, World world) {
        Point chunkPoint = world.getChunkPointWithCoords(x, y);
        if (!chunks.containsKey(chunkPoint)) {
            chunks.put(chunkPoint, new Chunk(chunkPoint.x, chunkPoint.y, world));
        }
        return chunkPoint;
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
