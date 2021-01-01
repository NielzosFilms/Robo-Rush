package game.assets.tiles.grass;

import game.assets.tiles.tile.Tile;
import game.assets.tiles.tile.UpdateAble;
import game.enums.BIOME;
import game.system.helpers.TransitionHelpers;
import game.system.systems.gameObject.Bounds;
import game.system.world.Chunk;
import game.textures.TEXTURE_LIST;
import game.textures.Texture;

import java.awt.*;
import java.util.HashMap;

public class Tile_Grass_Plateau_Transition extends Tile implements UpdateAble, Bounds {
    private HashMap<Integer, Texture> textures = new HashMap<>();
    private int type;

    public Tile_Grass_Plateau_Transition(int x, int y, int chunk_x, int chunk_y, int z_index, BIOME biome, Chunk chunk) {
        super(x, y, chunk_x, chunk_y, z_index, biome, chunk);

        textures.put(0b00000000, new Texture(TEXTURE_LIST.grass_plateau, 1, 2));

        textures.put(0b00001000, new Texture(TEXTURE_LIST.grass_plateau, 2, 2));
        textures.put(0b00000001, new Texture(TEXTURE_LIST.grass_plateau, 1, 3));
        //textures.put(0b00001001, new Texture(TEXTURE_LIST.grass_plateau, 3, 1));
        textures.put(0b00000010, new Texture(TEXTURE_LIST.grass_plateau, 0, 2));
        //textures.put(0b00000011, new Texture(TEXTURE_LIST.grass_plateau, 4, 1));

        //textures.put(0b00000100, new Texture(TEXTURE_LIST.grass_plateau, 1, 1));

        //textures.put(0b00001100, new Texture(TEXTURE_LIST.grass_plateau, 3, 2));
        //textures.put(0b00000110, new Texture(TEXTURE_LIST.grass_plateau, 4, 2));

        textures.put(0b00010000, new Texture(TEXTURE_LIST.grass_plateau, 0, 3));
        textures.put(0b00100000, new Texture(TEXTURE_LIST.grass_plateau, 0, 1));
        textures.put(0b01000000, new Texture(TEXTURE_LIST.grass_plateau, 2, 1));
        textures.put(0b10000000, new Texture(TEXTURE_LIST.grass_plateau, 2, 3));

        texture = textures.get(0b00000000);
    }

    @Override
    public void tick() {

    }

    @Override
    public void render(Graphics g) {
        g.drawImage(texture.getTexure(), x, y, tileSize, tileSize, null);
        if(type == 0b00010000) {
            g.drawImage(new Texture(TEXTURE_LIST.grass_plateau, 0, 4).getTexure(), x, y+tileSize, tileSize, tileSize, null);
            g.drawImage(new Texture(TEXTURE_LIST.grass_plateau, 0, 5).getTexure(), x, y+tileSize*2, tileSize, tileSize, null);
        }
        if(type == 0b10000000) {
            g.drawImage(new Texture(TEXTURE_LIST.grass_plateau, 2, 4).getTexure(), x, y+tileSize, tileSize, tileSize, null);
            g.drawImage(new Texture(TEXTURE_LIST.grass_plateau, 2, 5).getTexure(), x, y+tileSize*2, tileSize, tileSize, null);
        }
        if(type == 0b00000001) {
            g.drawImage(new Texture(TEXTURE_LIST.grass_plateau, 1, 4).getTexure(), x, y+tileSize, tileSize, tileSize, null);
            g.drawImage(new Texture(TEXTURE_LIST.grass_plateau, 1, 5).getTexure(), x, y+tileSize*2, tileSize, tileSize, null);
        }
    }

    @Override
    public void update() {
        int type = TransitionHelpers.getTransition(this, chunk, z_index);
        if(textures.containsKey(type)) {
            texture = textures.get(type);
            this.type = type;
        }
    }

    @Override
    public Rectangle getBounds() {
        if(type != 0b00000000) {
            if(type == 0b00010000 || type == 0b10000000 || type == 0b00000001) {
                return new Rectangle(x, y, tileSize, tileSize*3);
            }
            return new Rectangle(x, y, tileSize, tileSize);
        }
        return null;
    }

    @Override
    public Rectangle getTopBounds() {
        return null;
    }

    @Override
    public Rectangle getBottomBounds() {
        return null;
    }

    @Override
    public Rectangle getLeftBounds() {
        return null;
    }

    @Override
    public Rectangle getRightBounds() {
        return null;
    }
}
