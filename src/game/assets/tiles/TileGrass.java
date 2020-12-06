package game.assets.tiles;

import java.awt.*;
import java.util.HashMap;

import game.assets.items.Item;
import game.enums.TEXTURE_LIST;
import game.enums.TILE_TYPE;
import game.system.helpers.TileHelperFunctions;
import game.textures.Texture;
import game.enums.BIOME;
import game.system.world.Chunk;

public class TileGrass extends Tile {
    private HashMap<TILE_TYPE, Texture> textures = new HashMap<>();

    public TileGrass(int x, int y, int chunk_x, int chunk_y, int z_index, BIOME biome, Chunk chunk) {
        super(x, y, chunk_x, chunk_y, z_index, biome, chunk);

        textures.put(TILE_TYPE.top_left, new Texture(TEXTURE_LIST.forest_list, 0, 0));
        textures.put(TILE_TYPE.top, new Texture(TEXTURE_LIST.forest_list, 1, 0));
        textures.put(TILE_TYPE.top_right, new Texture(TEXTURE_LIST.forest_list, 2, 0));
        textures.put(TILE_TYPE.left, new Texture(TEXTURE_LIST.forest_list, 0, 1));
        textures.put(TILE_TYPE.center, new Texture(TEXTURE_LIST.forest_list, 1, 1));
        textures.put(TILE_TYPE.right, new Texture(TEXTURE_LIST.forest_list, 2, 1));
        textures.put(TILE_TYPE.bottom_left, new Texture(TEXTURE_LIST.forest_list, 0, 2));
        textures.put(TILE_TYPE.bottom, new Texture(TEXTURE_LIST.forest_list, 1, 2));
        textures.put(TILE_TYPE.bottom_right, new Texture(TEXTURE_LIST.forest_list, 2, 2));
        textures.put(TILE_TYPE.bottom_right_inverse, new Texture(TEXTURE_LIST.forest_list, 3, 0));
        textures.put(TILE_TYPE.bottom_left_inverse, new Texture(TEXTURE_LIST.forest_list, 4, 0));
        textures.put(TILE_TYPE.top_right_inverse, new Texture(TEXTURE_LIST.forest_list, 3, 1));
        textures.put(TILE_TYPE.top_left_inverse, new Texture(TEXTURE_LIST.forest_list, 4, 1));

        this.texture = textures.get(TILE_TYPE.center);
    }

    public void tick() {

    }

    public void render(Graphics g) {
        g.drawImage(this.texture.getTexure(), x, y, this.tileSize, this.tileSize, null);
    }

    public void findAndSetEdgeTexture(int tilemap_index) {
        TILE_TYPE tileType = TileHelperFunctions.getTileType8DirTileOrBiome(this, chunk, tilemap_index);

        if(enoughConnections()) {
            this.texture = textures.get(tileType);

            // Add water tile in background
            if(this.texture != textures.get(TILE_TYPE.center)) {
                TileWater water = new TileWater(x, y, chunk_x, chunk_y, 2, biome, chunk);
                water.setWaterType(tileType);
                chunk.addTile(water);
                this.texture = textures.get(TILE_TYPE.center);
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


    // TODO make this is tile helper functions?
    private boolean enoughConnections() {
        /*if(!top && !right && !bottom && !left) return false;
        if(top && !right && !bottom && !left) return false;
        if(!top && right && !bottom && !left) return false;
        if(!top && !right && bottom && !left) return false;
        if(!top && !right && !bottom && left) return false;
        if(!top && right && !bottom && left) return false;
        if(top && !right && bottom && !left) return false;*/
        return true;
    }

    public Rectangle getBounds() {
        return null;
    }

    public Rectangle getSelectBounds() {
        return null;
    }

    public Item getItem() { return null; }

}
