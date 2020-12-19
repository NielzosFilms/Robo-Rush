package game.assets.tiles;

import java.awt.Graphics;
import java.awt.Rectangle;

import game.assets.items.item.Item;
import game.assets.tiles.tile.Tile;
import game.enums.TILE_TYPE;
import game.textures.Animation;
import game.textures.Textures;
import game.enums.BIOME;
import game.system.world.Chunk;

public class Tile_Water extends Tile {
    private TILE_TYPE water_type;

    public Tile_Water(int x, int y, int chunk_x, int chunk_y, int z_index, BIOME biome, Chunk chunk) {
        super(x, y, chunk_x, chunk_y, z_index, biome, chunk);
        water_type = TILE_TYPE.center;
    }

    public void tick() {

    }

    public void render(Graphics g) {
        getWaterAnimation().drawAnimation(g, x, y, this.tileSize, this.tileSize);
    }

    private Animation getWaterAnimation() {
        return Textures.water_red.get(water_type);
    }

    public void update() {

    }
    public void setWaterType(TILE_TYPE tileType) {
        this.water_type = tileType;
    }

}
