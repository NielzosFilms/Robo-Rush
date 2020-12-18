package game.assets.tiles;

import game.assets.items.item.Item;
import game.assets.tiles.tile.Tile;
import game.system.world.Chunk;
import game.textures.Texture;

import java.awt.*;

public class Tile_Static extends Tile {
    public Tile_Static(int x, int y, int chunk_x, int chunk_y, int z_index, Chunk chunk, Texture texture) {
        super(x, y, chunk_x, chunk_y, z_index, null, chunk);
        this.texture = texture;
    }

    public void tick() {

    }

    public void render(Graphics g) {
        g.drawImage(texture.getTexure(), x, y, tileSize, tileSize, null);
    }

    public void update() {

    }
}
