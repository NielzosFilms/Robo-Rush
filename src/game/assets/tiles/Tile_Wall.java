package game.assets.tiles;

import game.assets.items.item.Item;
import game.assets.tiles.tile.Tile;
import game.enums.TILE_TYPE;
import game.system.helpers.TileHelperFunctions;
import game.system.systems.gameObject.Bounds;
import game.textures.TEXTURE_LIST;
import game.system.world.Chunk;
import game.textures.Texture;

import java.awt.*;
import java.util.HashMap;

public class Tile_Wall extends Tile implements Bounds {
    private HashMap<TILE_TYPE, Texture> textures = new HashMap<>();
    private TILE_TYPE type;

    public Tile_Wall(int x, int y, int chunk_x, int chunk_y, int z_index, Chunk chunk) {
        super(x, y, chunk_x, chunk_y, z_index, null, chunk);

        texture = new Texture(TEXTURE_LIST.walls_list, 9, 3);

        textures.put(TILE_TYPE.bottom, new Texture(TEXTURE_LIST.cave_list, 9, 3));
        textures.put(TILE_TYPE.top, new Texture(TEXTURE_LIST.cave_list, 9, 0));
        textures.put(TILE_TYPE.left, new Texture(TEXTURE_LIST.cave_list, 7, 1));
        textures.put(TILE_TYPE.right, new Texture(TEXTURE_LIST.cave_list, 11, 1));
    }

    public void tick() {
    }

    public void render(Graphics g) {
        g.drawImage(texture.getTexure(), x, y, tileSize, tileSize, null);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, tileSize, tileSize);
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

    public void update() {

    }
}
