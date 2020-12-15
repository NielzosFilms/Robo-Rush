package game.assets.structures;

import game.assets.entities.Player;
import game.assets.tiles.Tile;
import game.system.main.Game;
import game.system.systems.GameObject;
import game.system.world.Chunk;
import game.system.world.Generation;
import game.system.world.World;
import game.system.world.biome_groups.BiomeGroup;

import java.awt.*;
import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

public abstract class Structure implements Serializable {
    protected HashMap<Point, Chunk> chunks = new HashMap<>();
    protected GameObject world_object;
    protected Long seed;
    protected Generation generation;
    protected boolean infinite = false;
    protected boolean generated = false;
    protected Rectangle player_spawn;

    public Structure(Long seed, GameObject world_object, BiomeGroup biomeGroup) {
        this.seed = seed;
        this.generation = new Generation(seed, biomeGroup);
        this.world_object = world_object;
    }

    public abstract void generate(World world);

    public abstract void entered(World world);

    public abstract LinkedList<Tile> getGeneratedTile(int x, int y, float height, float temp, float moist, Chunk chunk, int world_x, int world_y);

    public abstract void generateNewChunksOffScreen(int camX, int camY, int camW, int camH, World world);

    public HashMap<Point, Chunk> getChunks() {
        return this.chunks;
    }

    public boolean isInfinite() {
        return infinite;
    }

    public void setInfinite(boolean infinite) {
        this.infinite = infinite;
    }

    public Generation getGeneration() {
        return this.generation;
    }

    public boolean isGenerated() {
        return generated;
    }

    public Rectangle getPlayerSpawn() {
        return player_spawn;
    }

    public void setPlayerSpawn(Rectangle player_spawn) {
        this.player_spawn = player_spawn;
    }
}
