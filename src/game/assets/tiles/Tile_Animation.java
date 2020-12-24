package game.assets.tiles;

import game.assets.tiles.tile.Tile;
import game.enums.BIOME;
import game.system.world.Chunk;
import game.textures.Animation;

import java.awt.*;

public class Tile_Animation extends Tile {
    private Animation animation;
    public Tile_Animation(int x, int y, int chunk_x, int chunk_y, int z_index, Chunk chunk, Animation animation) {
        super(x, y, chunk_x, chunk_y, z_index, null, chunk);
        this.animation = animation;
    }

    @Override
    public void tick() {

    }

    @Override
    public void render(Graphics g) {
        animation.drawAnimation(g, x, y, tileSize, tileSize);
    }
}
