package game.assets.tiles;

import game.assets.items.item.Item;
import game.assets.tiles.tile.Tile;
import game.enums.ID;
import game.system.world.Chunk;
import game.textures.Texture;

import java.awt.*;

public class Tile_Static extends Tile {

    public Tile_Static(int x, int y, int z_index, Texture texture) {
        super(x, y, z_index, ID.Static_Tile);
        this.texture = texture;
    }

    public void tick() {

    }

    public void render(Graphics g) {
        g.drawImage(texture.getTexure(), x, y, tileSize, tileSize, null);
    }
}
