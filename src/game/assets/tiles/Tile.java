package game.assets.tiles;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.Serializable;

import game.enums.BIOME;
import game.system.world.Chunk;
import game.textures.Texture;

public abstract class Tile implements Serializable {

    protected int x, y, chunk_x, chunk_y;
    protected BIOME biome;
    protected Chunk chunk;
    protected int z_index;
    protected int tileSize = 16;

    protected Texture texture;

    public Tile(int x, int y, int chunk_x, int chunk_y, int z_index, BIOME biome, Chunk chunk) {
        this.x = x;
        this.y = y;
        this.chunk_x = chunk_x;
        this.chunk_y = chunk_y;
        this.z_index = z_index;
        this.biome = biome;
        this.chunk = chunk;
    }

    // public abstract void tick();

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

    public void setChunkX(int chunk_x) {
        this.chunk_x = chunk_x;
    }

    public int getChunkX() {
        return this.chunk_x;
    }

    public void setChunkY(int chunk_y) {
        this.chunk_y = chunk_y;
    }

    public int getChunkY() {
        return this.chunk_y;
    }

    public void setBiome(BIOME biome) {
        this.biome = biome;
    }

    public BIOME getBiome() {
        return this.biome;
    }

    public Chunk getChunk() {
        return this.chunk;
    }

    public void setChunk(Chunk chunk) {
        this.chunk = chunk;
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

    public abstract Rectangle getBounds();

    public abstract Rectangle getSelectBounds();

    public abstract void findAndSetEdgeTexture();

    public abstract void update();
}
