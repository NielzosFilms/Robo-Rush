package game.assets.tiles;

import java.awt.Graphics;
import java.awt.Rectangle;

import game.assets.items.Item;
import game.enums.TILE_TYPE;
import game.textures.Animation;
import game.textures.Textures;
import game.enums.BIOME;
import game.system.world.Chunk;

public class TileWater extends Tile {
    private TILE_TYPE water_type;

    public TileWater(int x, int y, int chunk_x, int chunk_y, int z_index, BIOME biome, Chunk chunk) {
        super(x, y, chunk_x, chunk_y, z_index, biome, chunk);
        water_type = TILE_TYPE.center;
    }

    public void tick() {

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
        return Textures.water_red.get(water_type);
    }

    public void findAndSetEdgeTexture() {
    }

    public void update() {

    }
    public void setWaterType(TILE_TYPE tileType) {
        this.water_type = tileType;
    }

    public Item getItem() { return null; }

}
