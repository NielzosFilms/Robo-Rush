package game.assets.tiles.tile;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.io.Serializable;

import game.assets.items.item.Item;
import game.enums.BIOME;
import game.enums.ID;
import game.enums.TILE_TYPE;
import game.system.systems.gameObject.GameObject;
import game.system.world.Chunk;
import game.textures.Texture;

public abstract class Tile extends GameObject implements Serializable {

    protected int tileSize = 16;

    protected Texture texture;

    public Tile(int x, int y, int z_index, ID id) {
        super(x, y, z_index, id);
    }

    public abstract void tick();

    public abstract void render(Graphics g);

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return this.y;
    }

    public int getZIndex() {
        return z_index;
    }

    public void setZIndex(int z_index) {
        this.z_index = z_index;
    }

    public Texture getTexture() {
        return this.texture;
    }

    public void setTexture(Texture texture) {this.texture = texture; }

    public int getTileSize() {
        return this.tileSize;
    }

    public void setTileSize(int tileSize) {
        this.tileSize = tileSize;
    }
}
