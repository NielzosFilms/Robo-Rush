package game.tiles;

import java.awt.Graphics;
import java.awt.Rectangle;

import game.textures.Textures;
import game.world.BIOME;

public class TileWater extends Tile {

    public TileWater(int x, int y, int z_index, BIOME biome, int tex_id) {
        super(x, y, z_index, biome, tex_id);
    }

    public void render(Graphics g) {
        Textures.water.drawAnimation(g, x, y, this.tileSize, this.tileSize);
    }

    public Rectangle getBounds() {
        return null;
    }

    public Rectangle getSelectBounds() {
        return null;
    }

    public void findAndSetEdgeTexture() {
    }

    public void setTextureWithId() {
    }

}
