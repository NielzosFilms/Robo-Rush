package game.tiles;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.List;

import game.textures.Textures;
import game.world.BIOME;

public class TileGrass extends Tile {

    private BufferedImage texture;
    private List<BufferedImage> TEXTURE_LIST = Textures.tileSetForestBlocks;

    public TileGrass(int x, int y, int z_index, BIOME biome, int tex_id) {
        super(x, y, z_index, biome, tex_id);
    }

    public void render(Graphics g) {
        g.drawImage(this.texture, x, y, this.tileSize, this.tileSize, null);
    }

    public void findAndSetEdgeTexture() {

    }

    public Rectangle getBounds() {
        return null;
    }

    public Rectangle getSelectBounds() {
        return null;
    }

    public void setTextureWithId() {
        this.texture = TEXTURE_LIST.get(this.tex_id);
    }

}
