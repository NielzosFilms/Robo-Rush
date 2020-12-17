package game.assets.tiles;

import java.awt.*;
import java.util.HashMap;
import java.util.Random;

import game.assets.items.item.Item;
import game.assets.objects.rock.Pebble;
import game.assets.objects.tree.Tree;
import game.enums.ID;
import game.textures.TEXTURE_LIST;
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

    public void findAndSetEdgeTexture() {
        tile_type = TileHelperFunctions.getTileType8DirTileOrBiome(this, chunk, z_index);
        if(getConnections() >= 2) {
            this.texture = textures.get(tile_type);

            // Add water tile in background
            if(this.texture != textures.get(TILE_TYPE.center)) {
                TileWater water = new TileWater(x, y, chunk_x, chunk_y, 2, biome, chunk);
                water.setWaterType(tile_type);
                chunk.addTile(water);
                this.texture = textures.get(TILE_TYPE.center);
            }
        } else {
            TileWater water = new TileWater(x, y, chunk_x, chunk_y, 1, BIOME.Ocean, chunk);
            chunk.addTile(water);
            chunk.removeTile(this);
            //chunk.updateTiles();
        }
        if(tile_type == TILE_TYPE.center) {
            placeEntityOnTile();
        }
    }

    public void update() {
        findAndSetEdgeTexture();
    }

    private int getConnections() {
        int ret = 0;
        if(TileHelperFunctions.checkSameNeighbourTileOrBiome(this, chunk, 0, -1, z_index)) ret++;
        if(TileHelperFunctions.checkSameNeighbourTileOrBiome(this, chunk, 1, -1, z_index)) ret++;
        if(TileHelperFunctions.checkSameNeighbourTileOrBiome(this, chunk, 1, 0, z_index)) ret++;
        if(TileHelperFunctions.checkSameNeighbourTileOrBiome(this, chunk, 1, 1, z_index)) ret++;
        if(TileHelperFunctions.checkSameNeighbourTileOrBiome(this, chunk, 0, 1, z_index)) ret++;
        if(TileHelperFunctions.checkSameNeighbourTileOrBiome(this, chunk, -1, 1, z_index)) ret++;
        if(TileHelperFunctions.checkSameNeighbourTileOrBiome(this, chunk, -1, 0, z_index)) ret++;
        if(TileHelperFunctions.checkSameNeighbourTileOrBiome(this, chunk, -1, -1, z_index)) ret++;
        return ret;
    }

    private void placeEntityOnTile() {
        Random r = new Random();
        if(r.nextInt(101) == 0) {
            if(r.nextInt(2) == 0) {
                chunk.addEntity(new Tree(x, y, 10, ID.Tree, biome));
            } else {
                chunk.addEntity(new Pebble(x, y, 10, ID.Pebble));
            }
        }
    }

    public Rectangle getBounds() {
        return null;
    }

    public Rectangle getSelectBounds() {
        return null;
    }

    public Item getItem() { return null; }

}
