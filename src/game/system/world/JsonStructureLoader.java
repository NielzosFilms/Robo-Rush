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

import java.awt.*;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.LinkedList;

public class JsonStructureLoader {
    JSONParser parser = new JSONParser();

    private final int TO_TILE_SIZE = 16;
    private int tileSize = 16;
    private int division = 1;

    private HashMap<Integer, TEXTURE_LIST> texture_list_indexes = new HashMap<>();

    private LinkedList<Tile> static_tiles = new LinkedList<>();
    private LinkedList<GameObject> objects = new LinkedList<>();
    private LinkedList<Rectangle> bounds = new LinkedList<>();

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
        System.out.println(objects);
        System.out.println(bounds);
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
    }

    private void decodeLayers(JSONArray layers) {
        for(Object o : layers) {
            JSONObject layer = (JSONObject) o;
            if((Boolean)layer.get("visible")) {
                if(layer.get("type").toString().equals("tilelayer")) {
                    decodeTileLayer(layer);
                } else if(layer.get("type").toString().equals("objectgroup")) {
                    decodeObjectLayer(layer);
                }
            }
        }
    }

    private void decodeTileLayer(JSONObject layer) {
        int width_tiles = StructureLoaderHelpers.getIntProp(layer, "width");
        int height_tiles = StructureLoaderHelpers.getIntProp(layer, "height");
        int layer_x = StructureLoaderHelpers.getIntProp(layer, "x");
        int layer_y = StructureLoaderHelpers.getIntProp(layer, "y");
        int layer_id = StructureLoaderHelpers.getIntProp(layer, "id");

        int x = 0;
        int y = 0;
        for(Object o : (JSONArray)layer.get("data")) {
            int tex_index = Integer.parseInt(o.toString());
            if(tex_index > 0) {
                static_tiles.add(new Tile_Static(layer_x + x * TO_TILE_SIZE, layer_y + y * TO_TILE_SIZE, x, y, layer_id, null,
                        new Texture(getTextureList(tex_index), tex_index-1 - getTextureListGid(tex_index))));
            }
            x++;
            if(x == width_tiles) {
                x = 0;
                y++;
            }
        }
    }

    private void decodeObjectLayer(JSONObject layer) {
        for(Object o : (JSONArray)layer.get("objects")) {
            JSONObject object = (JSONObject) o;

            if(object.get("type").toString().equals("bounds")) {
                addBounds(object);
            } else {
                try {
                    Class<?> clazz = Class.forName(StructureLoaderHelpers.getFullClassname(object));
                    Constructor<?> ctor = clazz.getConstructor(JSONObject.class, Integer.class, Integer.class);
                    objects.add((GameObject) ctor.newInstance(object, StructureLoaderHelpers.getIntProp(layer, "id"), division));
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

    private void addBounds(JSONObject bounds) {
        int x = Integer.parseInt(bounds.get("x").toString()) / division;
        int y = Integer.parseInt(bounds.get("y").toString()) / division;
        int width = Integer.parseInt(bounds.get("width").toString()) / division;
        int height = Integer.parseInt(bounds.get("height").toString()) / division;
        this.bounds.add(new Rectangle(x, y, width, height));
    }

    private TEXTURE_LIST getTextureList(int index) {
        TEXTURE_LIST ret = null;
        for(int firstgid : texture_list_indexes.keySet()) {
            if(index >= firstgid) ret = texture_list_indexes.get(firstgid);
        }
        return ret;
    }

    private int getTextureListGid(int index) {
        int ret = 1;
        for(int firstgid : texture_list_indexes.keySet()) {
            if(index >= firstgid) ret = firstgid;
        }
        return ret-1;
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
}
