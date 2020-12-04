package game.assets.tiles;

import java.awt.*;

import game.enums.TEXTURE_LIST;
import game.system.helpers.TileHelperFunctions;
import game.textures.Texture;
import game.enums.BIOME;
import game.system.world.Chunk;

public class TileGrass extends Tile {
    private boolean
            top = true,
            right = true,
            bottom = true,
            left = true;
    private boolean
            top_left = true,
            top_right = true,
            bottom_left = true,
            bottom_right = true;

    private final Texture
            tex_top_left = new Texture(TEXTURE_LIST.forest_list, 0, 0),
            tex_top = new Texture(TEXTURE_LIST.forest_list, 1, 0),
            tex_top_right = new Texture(TEXTURE_LIST.forest_list, 2, 0),
            tex_left = new Texture(TEXTURE_LIST.forest_list, 0, 1),
            tex_center = new Texture(TEXTURE_LIST.forest_list, 1, 1),
            tex_right = new Texture(TEXTURE_LIST.forest_list, 2, 1),
            tex_bot_left = new Texture(TEXTURE_LIST.forest_list, 0, 2),
            tex_bot = new Texture(TEXTURE_LIST.forest_list, 1, 2),
            tex_bot_right = new Texture(TEXTURE_LIST.forest_list, 2, 2),
            tex_inverse_bot_right = new Texture(TEXTURE_LIST.forest_list, 3, 0),
            tex_inverse_bot_left = new Texture(TEXTURE_LIST.forest_list, 4, 0),
            tex_inverse_top_right = new Texture(TEXTURE_LIST.forest_list, 3, 1),
            tex_inverse_top_left = new Texture(TEXTURE_LIST.forest_list, 4, 1);

    public TileGrass(int x, int y, int chunk_x, int chunk_y, int z_index, BIOME biome, Chunk chunk) {
        super(x, y, chunk_x, chunk_y, z_index, biome, chunk);
        this.texture = tex_center;
    }

    public void render(Graphics g) {
        g.drawImage(this.texture.getTexure(), x, y, this.tileSize, this.tileSize, null);
    }

    public void findAndSetEdgeTexture(int tilemap_index) {

        top = TileHelperFunctions.checkSameNeighbourTileOrBiome(this, chunk, 0, -1, tilemap_index);
        right = TileHelperFunctions.checkSameNeighbourTileOrBiome(this, chunk, 1, 0, tilemap_index);
        bottom = TileHelperFunctions.checkSameNeighbourTileOrBiome(this, chunk, 0, 1, tilemap_index);
        left = TileHelperFunctions.checkSameNeighbourTileOrBiome(this, chunk, -1, 0, tilemap_index);

        top_left = TileHelperFunctions.checkSameNeighbourTileOrBiome(this, chunk, -1, -1, tilemap_index);
        top_right = TileHelperFunctions.checkSameNeighbourTileOrBiome(this, chunk, 1, -1, tilemap_index);
        bottom_left = TileHelperFunctions.checkSameNeighbourTileOrBiome(this, chunk, -1, 1, tilemap_index);
        bottom_right = TileHelperFunctions.checkSameNeighbourTileOrBiome(this, chunk, 1, 1, tilemap_index);

        if(enoughConnections()) {
            this.texture = getTextureWithBooleans();

            // Add water tile in background
            if(this.texture != tex_center) {
                TileWater water = new TileWater(x, y, chunk_x, chunk_y, 2, biome, chunk);
                water.setWaterType(top, right, bottom, left, top_left, top_right, bottom_left, bottom_right);
                chunk.addTile(water);
                this.texture = tex_center;
            }
        } else {
            TileWater water = new TileWater(x, y, chunk_x, chunk_y, 1, BIOME.Ocean, chunk);
            chunk.addTile(water);
            chunk.removeTile(this);
            chunk.updateTiles(1);
        }
    }

    public void update(int tilemap_index) {
        findAndSetEdgeTexture(tilemap_index);
    }

    public Texture getTextureWithBooleans() {
        if (top && right && bottom && left) {
            if (!top_left) {
                return tex_inverse_top_left;
            } else if (!top_right) {
                return tex_inverse_top_right;
            } else if (!bottom_left) {
                return tex_inverse_bot_left;
            } else if (!bottom_right) {
                return tex_inverse_bot_right;
            }
        }

        if (!top && !right && !bottom && !left) {
            return tex_center;
        } else if (!top && !right) {
            return tex_top_right;
        } else if (!right && !bottom) {
            return tex_bot_right;
        } else if (!bottom && !left) {
            return tex_bot_left;
        } else if (!left && !top) {
            return tex_top_left;
        } else if (!top) {
            return tex_top;
        } else if (!right) {
            return tex_right;
        } else if (!bottom) {
            return tex_bot;
        } else if (!left) {
            return tex_left;
        }

        return tex_center;
    }

    private boolean enoughConnections() {
        if(!top && !right && !bottom && !left) return false;
        if(top && !right && !bottom && !left) return false;
        if(!top && right && !bottom && !left) return false;
        if(!top && !right && bottom && !left) return false;
        if(!top && !right && !bottom && left) return false;
        if(!top && right && !bottom && left) return false;
        if(top && !right && bottom && !left) return false;
        return true;
    }

    public Rectangle getBounds() {
        return null;
    }

    public Rectangle getSelectBounds() {
        return null;
    }

}
