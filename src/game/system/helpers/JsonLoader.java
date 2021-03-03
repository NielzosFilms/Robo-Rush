package game.system.helpers;

import game.assets.tiles.Tile_Animation;
import game.assets.tiles.Tile_Static;
import game.system.systems.gameObject.GameObject;
import game.system.world.Chunk;
import game.textures.Animation;
import game.textures.TEXTURE_LIST;
import game.textures.Texture;
import game.textures.Textures;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;

public class JsonLoader {
    private JSONParser parser = new JSONParser();
    private LinkedList<LinkedList<GameObject>> loaded_objects = new LinkedList<>();

    private final int TO_TILE_SIZE = 16;
    private int tileSize = 16;
    private int division = 1;

    private HashMap<Integer, TEXTURE_LIST> texture_list_indexes = new HashMap<>();

    public JsonLoader(String filepath) {
        try {
            Reader reader = new InputStreamReader(ClassLoader.getSystemResourceAsStream(filepath));
            JSONObject map = (JSONObject) parser.parse(reader);
            tileSize = StructureLoaderHelpers.getIntProp(map, "tilewidth");
            division = tileSize / TO_TILE_SIZE;
            if((boolean)map.get("infinite")) {
                throw new Exception("JsonLoader does not support infinite tilemaps. filepath: " + filepath);
            }
            decodeTextureLists((JSONArray)map.get("tilesets"));
            decodeLayers((JSONArray) map.get("layers"));
            // setLinkages();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void decodeTextureLists(JSONArray tilesets) {
        for(Object o : tilesets) {
            JSONObject tileset = (JSONObject) o;
            if(tileset == null) continue;
            int firstgid = StructureLoaderHelpers.getIntProp(tileset, "firstgid");
            String list_name = "";
            try {
                if(StructureLoaderHelpers.hasCustomProp(tileset, "list_name")) {
                    list_name = StructureLoaderHelpers.getCustomProp(tileset, "list_name");
                } else throw new Exception("Cant find 'list_name' on a tileset, please embed the tileset properties.");
                texture_list_indexes.put(firstgid, TEXTURE_LIST.valueOf(list_name));
                if(tileset.get("tiles") != null) {
                    for(Object t : (JSONArray)tileset.get("tiles")) {
                        JSONObject tile = (JSONObject)t;
                        if(tile.get("animation") != null) {
                            int id = StructureLoaderHelpers.getIntProp(tile, "id");
                            int animation_id = id + firstgid;
                            LinkedList<Texture> frames = new LinkedList<>();
                            int frame_duration = 300;
                            for(Object a : (JSONArray) tile.get("animation")) {
                                JSONObject tile_frame = (JSONObject)a;
                                int tex_index = StructureLoaderHelpers.getIntProp(tile_frame, "tileid");
                                frame_duration = StructureLoaderHelpers.getIntProp(tile_frame, "duration");
                                frames.add(new Texture(TEXTURE_LIST.valueOf(list_name), tex_index));
                            }
                            Animation animation = new Animation(frame_duration, frames.toArray(new Texture[0]));
                            Textures.generated_animations.put(animation_id, animation);
                        }
                    }
                }
            } catch (Exception e) {
                Logger.printError("Tileset prop: 'list_name' not found, Tileset 'list_name': " + list_name);
                e.printStackTrace();
            }
        }
    }

    private void decodeLayers(JSONArray layers) {
        int layer_index = 0;
        for(Object o : layers) {
            JSONObject layer = (JSONObject) o;
            if((Boolean)layer.get("visible")) {
                if(layer.get("type").toString().equals("tilelayer")) {
                    decodeTileLayer(layer, layer_index);
                } else if(layer.get("type").toString().equals("objectgroup")) {
                    //decodeObjectLayer(layer, layer_index);
                }
            }
            layer_index++;
        }
    }

    private void decodeTileLayer(JSONObject layer, int layer_index) {
        if(StructureLoaderHelpers.hasCustomProp(layer, "z_index")) {
            layer_index = Integer.parseInt(StructureLoaderHelpers.getCustomProp(layer, "z_index"));
        }
        int x = 0;
        int y = 0;
        int width = Integer.parseInt(layer.get("width").toString());
        System.out.println("width: " + width);
        for(Object data : (JSONArray)layer.get("data")) {
            int tex_index = Integer.parseInt(data.toString());
            int tile_x = x * TO_TILE_SIZE;
            int tile_y = y * TO_TILE_SIZE;
            if(tex_index > 0) {
                // BROKEN
                if(Textures.generated_animations.containsKey(tex_index)) {
                    addObject(new Tile_Animation(tile_x, tile_y, layer_index,
                            Textures.generated_animations.get(tex_index)));
                } else {
                    addObject(new Tile_Static(tile_x, tile_y, layer_index,
                            new Texture(getTextureList(tex_index), tex_index-1 - getTextureListGid(tex_index))));
                }
            }
            x++;
            if(x >= width) {
                x = 0;
                y++;
            }
        }
    }

    private void addObject(GameObject object) {
        int z_index = object.getZIndex();
        for(int i=loaded_objects.size(); i<=z_index; i++) {
            this.loaded_objects.add(new LinkedList<>());
        }
        loaded_objects.get(z_index).add(object);
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

    public LinkedList<LinkedList<GameObject>> getLoadedObjects() {
        return loaded_objects;
    }
}
