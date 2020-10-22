package game.tiles;

import java.awt.Graphics;
import java.awt.Rectangle;

import game.textures.Animation;
import game.textures.Textures;
import game.world.BIOME;
import game.world.Chunk;

public class TileWater extends Tile {
    private boolean top = true, right = true, bottom = true, left = true;
    private boolean top_left = true, top_right = true, bottom_left = true, bottom_right = true;

    public TileWater(int x, int y, int chunk_x, int chunk_y, int z_index, BIOME biome, Chunk chunk, int tex_id) {
        super(x, y, chunk_x, chunk_y, z_index, biome, chunk, tex_id);
    }

    public void render(Graphics g) {
        getWaterAnimation().drawAnimation(g, x, y, this.tileSize, this.tileSize);
    }

    public Rectangle getBounds() {
        return null;
    }

    public Rectangle getSelectBounds() {
        return null;
    }

    private Animation getWaterAnimation() {
        if (top && right && bottom && left) {
            if (!top_left) {
                return Textures.water_r_tl;
            } else if (!top_right) {
                return Textures.water_r_tr;
            } else if (!bottom_left) {
                return Textures.water_r_bl;
            } else if (!bottom_right) {
                return Textures.water_r_br;
            }
        }

        if (!top && !right && !bottom && !left) {
            return Textures.water;
        } else if (!top && !right) {
            return Textures.water_r_t_r;
        } else if (!right && !bottom) {
            return Textures.water_r_b_r;
        } else if (!bottom && !left) {
            return Textures.water_r_b_l;
        } else if (!left && !top) {
            return Textures.water_r_t_l;
        } else if (!top) {
            return Textures.water_r_t;
        } else if (!right) {
            return Textures.water_r_r;
        } else if (!bottom) {
            return Textures.water_r_b;
        } else if (!left) {
            return Textures.water_r_l;
        }
        return Textures.water;
    }

    public void findAndSetEdgeTexture() {
    }

    public void setTextureWithId() {
    }

    public void setWaterType(boolean top, boolean right, boolean bottom, boolean left, boolean top_left,
            boolean top_right, boolean bottom_left, boolean bottom_right) {
        this.top = top;
        this.right = right;
        this.bottom = bottom;
        this.left = left;
        this.top_left = top_left;
        this.top_right = top_right;
        this.bottom_left = bottom_left;
        this.bottom_right = bottom_right;
    }

}
