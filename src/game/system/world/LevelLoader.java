package game.system.world;

import com.sun.jdi.IntegerType;
import game.assets.objects.crate.Crate;
import game.assets.tiles.Tile;
import game.assets.tiles.Tile_Static;
import game.enums.ID;
import game.system.helpers.LevelLoaderHelpers;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import game.assets.objects.crate.Crate;

public class LevelLoader {
    JSONParser parser = new JSONParser();

    private int tileSize = 16;

    private LinkedList<Tile> static_tiles = new LinkedList<>();
    private LinkedList<GameObject> objects = new LinkedList<>();
    private LinkedList<Rectangle> bounds = new LinkedList<>();

    public LevelLoader(String filepath) {
        try {
            JSONObject map = (JSONObject) parser.parse(new FileReader(filepath));
            decodeLayers((JSONArray) map.get("layers"));
            tileSize = Integer.parseInt(map.get("tilewidth").toString());
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        System.out.println(objects);
        System.out.println(bounds);
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
        int width_tiles = LevelLoaderHelpers.getIntProp(layer, "width");
        int height_tiles = LevelLoaderHelpers.getIntProp(layer, "height");
        int layer_x = LevelLoaderHelpers.getIntProp(layer, "x");
        int layer_y = LevelLoaderHelpers.getIntProp(layer, "y");
        int layer_id = LevelLoaderHelpers.getIntProp(layer, "id");

        int x = 0;
        int y = 0;
        for(Object o : (JSONArray)layer.get("data")) {
            Integer data = (Integer) o;
            x++;
            if(x == width_tiles) {
                x = 0;
                y++;
            }

            // TODO decode texture lists with data indexes
            static_tiles.add(new Tile_Static(layer_x + x * tileSize, layer_y + y * tileSize, x, y, layer_id, null,
                    new Texture(null, data)));
        }
    }

    private void decodeObjectLayer(JSONObject layer) {
        for(Object o : (JSONArray)layer.get("objects")) {
            JSONObject object = (JSONObject) o;

            if(object.get("type").toString().equals("bounds")) {
                addBounds(object);
            } else {
                try {
                    Class<?> clazz = Class.forName(LevelLoaderHelpers.getFullClassname(object));
                    Constructor<?> ctor = clazz.getConstructor(JSONObject.class, Integer.class);
                    objects.add((GameObject) ctor.newInstance(object, LevelLoaderHelpers.getIntProp(layer, "id")));
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
        int x = Integer.parseInt(bounds.get("x").toString());
        int y = Integer.parseInt(bounds.get("y").toString());
        int width = Integer.parseInt(bounds.get("width").toString());
        int height = Integer.parseInt(bounds.get("height").toString());
        this.bounds.add(new Rectangle(x, y, width, height));
    }
}
