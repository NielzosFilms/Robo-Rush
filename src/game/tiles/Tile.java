package game.tiles;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import game.world.BIOME;
import game.world.Chunk;

public abstract class Tile {

    protected int x, y, chunk_x, chunk_y;
    protected BIOME biome;
    protected Chunk chunk;
    protected int z_index;
    protected int tex_id; // this really needed?
    protected int tileSize = 16;

    protected BufferedImage texture;

    public Tile(int x, int y, int chunk_x, int chunk_y, int z_index, BIOME biome, Chunk chunk, int tex_id) {
        this.x = x;
        this.y = y;
        this.chunk_x = chunk_x;
        this.chunk_y = chunk_y;
        this.z_index = z_index;
        this.biome = biome;
        this.tex_id = tex_id;
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

    public int getTexId() {
        return this.tex_id;
    }

    public void setTexId(int tex_id) {
        this.tex_id = tex_id;
    }

    public BufferedImage getTexture() {
        return this.texture;
    }

    public abstract void setTextureWithId();

    public abstract void findAndSetEdgeTexture();

    public int getTileSize() {
        return this.tileSize;
    }

    public void setTileSize(int tileSize) {
        this.tileSize = tileSize;
    }

    public abstract Rectangle getBounds();

    public abstract Rectangle getSelectBounds();
}
