package game.assets.tiles.dirt;

import game.assets.tiles.tile.Tile;
import game.assets.tiles.tile.UpdateAble;
import game.enums.BIOME;
import game.system.helpers.TransitionHelpers;
import game.system.world.Chunk;
import game.textures.TEXTURE_LIST;
import game.textures.Texture;

import java.awt.*;
import java.util.HashMap;

public class Tile_Dirt_Transition extends Tile implements UpdateAble {
    private HashMap<Integer, Texture> textures = new HashMap<>();
    public Tile_Dirt_Transition(int x, int y, int chunk_x, int chunk_y, int z_index, BIOME biome, Chunk chunk) {
        super(x, y, chunk_x, chunk_y, z_index, biome, chunk);

        textures.put(0b00000000, new Texture(TEXTURE_LIST.forest_list, 6, 10));

        textures.put(0b00001000, new Texture(TEXTURE_LIST.forest_list, 7, 10));
        textures.put(0b00000001, new Texture(TEXTURE_LIST.forest_list, 6, 11));
        textures.put(0b00001001, new Texture(TEXTURE_LIST.forest_list, 8, 9));
        textures.put(0b00000010, new Texture(TEXTURE_LIST.forest_list, 5, 10));
        textures.put(0b00000011, new Texture(TEXTURE_LIST.forest_list, 9, 9));
        textures.put(0b00000100, new Texture(TEXTURE_LIST.forest_list, 6, 9));
        textures.put(0b00001100, new Texture(TEXTURE_LIST.forest_list, 8, 10));
        textures.put(0b00000110, new Texture(TEXTURE_LIST.forest_list, 9, 10));

        textures.put(0b00010000, new Texture(TEXTURE_LIST.forest_list, 5, 11));
        textures.put(0b00100000, new Texture(TEXTURE_LIST.forest_list, 5, 9));
        textures.put(0b01000000, new Texture(TEXTURE_LIST.forest_list, 7, 9));
        textures.put(0b10000000, new Texture(TEXTURE_LIST.forest_list, 7, 11));

        texture = new Texture(TEXTURE_LIST.forest_list, 6, 10);
    }

    @Override
    public void update() {
        int type = TransitionHelpers.getTransition(this, chunk, z_index);
        if(textures.containsKey(type)) {
            texture = textures.get(type);
        }
    }

    @Override
    public void tick() {

    }

    @Override
    public void render(Graphics g) {
        g.drawImage(texture.getTexure(), x, y, tileSize, tileSize, null);
    }
}
