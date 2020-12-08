package game.assets.tiles;

import game.assets.items.Item;
import game.enums.BIOME;
import game.enums.TILE_TYPE;
import game.system.helpers.TileHelperFunctions;
import game.system.main.Game;
import game.system.world.Chunk;
import game.textures.Fonts;
import game.textures.TEXTURE_LIST;
import game.textures.Texture;

import java.awt.*;
import java.util.HashMap;

public class Tile_CaveWall extends Tile {
    private HashMap<TILE_TYPE, Texture> textures = new HashMap<>();
    private TILE_TYPE type;

    public Tile_CaveWall(int x, int y, int chunk_x, int chunk_y, int z_index, Chunk chunk) {
        super(x, y, chunk_x, chunk_y, z_index, null, chunk);

        texture = new Texture(TEXTURE_LIST.cave_list, 9, 3);

        textures.put(TILE_TYPE.bottom, new Texture(TEXTURE_LIST.cave_list, 9, 3));
        textures.put(TILE_TYPE.top, new Texture(TEXTURE_LIST.cave_list, 9, 0));
        textures.put(TILE_TYPE.right, new Texture(TEXTURE_LIST.cave_list, 11, 1));
        textures.put(TILE_TYPE.left, new Texture(TEXTURE_LIST.cave_list, 7, 1));

        textures.put(TILE_TYPE.top_left, new Texture(TEXTURE_LIST.cave_list, 7, 0));
        textures.put(TILE_TYPE.top_right, new Texture(TEXTURE_LIST.cave_list, 11, 0));
        textures.put(TILE_TYPE.bottom_right, new Texture(TEXTURE_LIST.cave_list, 11, 3));
        textures.put(TILE_TYPE.bottom_left, new Texture(TEXTURE_LIST.cave_list, 7, 3));
    }

    public void tick() {

    }

    public void render(Graphics g) {
        g.drawImage(texture.getTexure(), x, y, tileSize, tileSize, null);
        if(type != null && Game.DEBUG_MODE) {
            g.setColor(Color.WHITE);
            g.setFont(Fonts.default_fonts.get(5));
            g.drawString(type.name(), x, y);
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, tileSize, tileSize);
    }

    public Rectangle getSelectBounds() {
        return null;
    }

    public void findAndSetEdgeTexture() {
        type = TileHelperFunctions.getTileType4DirTile(this, chunk, z_index);
        if(textures.containsKey(type)) {
            texture = textures.get(type);
        }
    }

    public void update() {

    }

    public Item getItem() {
        return null;
    }
}
