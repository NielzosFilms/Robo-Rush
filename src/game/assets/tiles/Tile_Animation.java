package game.assets.tiles;

import game.assets.tiles.tile.Tile;
import game.enums.ID;
import game.textures.Animation;

import java.awt.*;

public class Tile_Animation extends Tile {
    private Animation animation;
    public Tile_Animation(int x, int y, int z_index, Animation animation) {
        super(x, y, z_index, ID.Animation_tile);
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
