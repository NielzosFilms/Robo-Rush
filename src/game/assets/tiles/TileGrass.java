package game.assets.tiles;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.List;

import game.textures.Textures;
import game.enums.BIOME;
import game.system.world.Chunk;

public class TileGrass extends Tile {

    private boolean top = true, right = true, bottom = true, left = true;
    private boolean top_left = true, top_right = true, bottom_left = true, bottom_right = true;
    private transient List<BufferedImage> TEXTURE_LIST = Textures.tileSetForestBlocks;

    public TileGrass(int x, int y, int chunk_x, int chunk_y, int z_index, BIOME biome, Chunk chunk, int tex_id) {
        super(x, y, chunk_x, chunk_y, z_index, biome, chunk, tex_id);
    }

    public void render(Graphics g) {
        g.drawImage(this.texture, x, y, this.tileSize, this.tileSize, null);
    }

    public void findAndSetEdgeTexture() {
        top = TileHelperFunctions.checkSameNeighbourBiome(biome, chunk.getTileMap(), chunk, chunk_x, chunk_y, 0, -1);
        right = TileHelperFunctions.checkSameNeighbourBiome(biome, chunk.getTileMap(), chunk, chunk_x, chunk_y, 1, 0);
        bottom = TileHelperFunctions.checkSameNeighbourBiome(biome, chunk.getTileMap(), chunk, chunk_x, chunk_y, 0, 1);
        left = TileHelperFunctions.checkSameNeighbourBiome(biome, chunk.getTileMap(), chunk, chunk_x, chunk_y, -1, 0);

        top_left = TileHelperFunctions.checkSameNeighbourBiome(biome, chunk.getTileMap(), chunk, chunk_x, chunk_y, -1,
                -1);
        top_right = TileHelperFunctions.checkSameNeighbourBiome(biome, chunk.getTileMap(), chunk, chunk_x, chunk_y, 1,
                -1);
        bottom_left = TileHelperFunctions.checkSameNeighbourBiome(biome, chunk.getTileMap(), chunk, chunk_x, chunk_y,
                -1, 1);
        bottom_right = TileHelperFunctions.checkSameNeighbourBiome(biome, chunk.getTileMap(), chunk, chunk_x, chunk_y,
                1, 1);

        this.tex_id = getTexId();
        // add water as bg
        /**
         * 
         * TEMPORARY
         * 
         */
        if (this.tex_id != 16) {
            this.tex_id = 16;
            TileWater water = new TileWater(x, y, chunk_x, chunk_y, 2, biome, chunk, tex_id);
            water.setWaterType(top, right, bottom, left, top_left, top_right, bottom_left, bottom_right);
            chunk.addTile(water);
        }
        setTextureWithId();
    }

    public int getTexId() {
        if (top && right && bottom && left) {
            if (!top_left) {
                return 19;
            } else if (!top_right) {
                return 18;
            } else if (!bottom_left) {
                return 4;
            } else if (!bottom_right) {
                return 3;
            }
        }

        if (!top && !right && !bottom && !left) {
            return 16;
        } else if (!top && !right) {
            return 2;
        } else if (!right && !bottom) {
            return 32;
        } else if (!bottom && !left) {
            return 30;
        } else if (!left && !top) {
            return 0;
        } else if (!top) {
            return 1;
        } else if (!right) {
            return 17;
        } else if (!bottom) {
            return 31;
        } else if (!left) {
            return 15;
        }

        return 16;
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
