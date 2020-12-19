package game.assets.tiles.grass;

import java.awt.*;
import java.util.HashMap;
import java.util.Random;

import game.assets.objects.rock.Pebble;
import game.assets.objects.tree.Tree;
import game.assets.tiles.tile.Tile;
import game.enums.ID;
import game.system.helpers.Logger;
import game.system.helpers.TransitionHelpers;
import game.textures.TEXTURE_LIST;
import game.textures.Texture;
import game.enums.BIOME;
import game.system.world.Chunk;

public class Tile_Grass extends Tile {
    //private HashMap<TILE_TYPE, Texture> textures = new HashMap<>();

    public Tile_Grass(int x, int y, int chunk_x, int chunk_y, int z_index, BIOME biome, Chunk chunk) {
        super(x, y, chunk_x, chunk_y, z_index, biome, chunk);

       /*textures.put(TILE_TYPE.top_left, new Texture(TEXTURE_LIST.forest_list, 0, 0));
        textures.put(TILE_TYPE.top, new Texture(TEXTURE_LIST.forest_list, 1, 0));
        textures.put(TILE_TYPE.top_right, new Texture(TEXTURE_LIST.forest_list, 2, 0));
        textures.put(TILE_TYPE.left, new Texture(TEXTURE_LIST.forest_list, 0, 1));
        textures.put(TILE_TYPE.center, new Texture(TEXTURE_LIST.forest_list, 1, 1));
        textures.put(TILE_TYPE.right, new Texture(TEXTURE_LIST.forest_list, 2, 1));
        textures.put(TILE_TYPE.bottom_left, new Texture(TEXTURE_LIST.forest_list, 0, 2));
        textures.put(TILE_TYPE.bottom, new Texture(TEXTURE_LIST.forest_list, 1, 2));
        textures.put(TILE_TYPE.bottom_right, new Texture(TEXTURE_LIST.forest_list, 2, 2));
        textures.put(TILE_TYPE.bottom_right_inverse, new Texture(TEXTURE_LIST.forest_list, 3, 0));
        textures.put(TILE_TYPE.bottom_left_inverse, new Texture(TEXTURE_LIST.forest_list, 4, 0));
        textures.put(TILE_TYPE.top_right_inverse, new Texture(TEXTURE_LIST.forest_list, 3, 1));
        textures.put(TILE_TYPE.top_left_inverse, new Texture(TEXTURE_LIST.forest_list, 4, 1));*/

        //this.texture = textures.get(TILE_TYPE.center);

        texture = new Texture(TEXTURE_LIST.forest_list, 1, 1);
    }

    public void tick() {

    }

    public void render(Graphics g) {
        g.drawImage(this.texture.getTexure(), x, y, this.tileSize, this.tileSize, null);
    }

    /*public void update() {
        Logger.printStackStrace();
    }*/

    private void placeEntityOnTile() {
        Random r = new Random();
        if(r.nextInt(101) == 0) {
            if(r.nextInt(2) == 0) {
                chunk.addEntity(new Tree(x, y, 10, ID.Tree, biome));
            } else {
                chunk.addEntity(new Pebble(x, y, 10, ID.Pebble));
            }
        }
    }
}
