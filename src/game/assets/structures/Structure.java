package game.assets.structures;

import game.assets.entities.Player;
import game.assets.tiles.Tile;
import game.system.main.Game;
import game.system.systems.GameObject;
import game.system.world.Chunk;
import game.system.world.Generation;
import game.system.world.World;

import java.awt.*;
import java.util.HashMap;
import java.util.Random;

public abstract class Structure {
    private HashMap<Point, Chunk> chunks = new HashMap<>();
    private GameObject world_object;
    private Long seed;
    private Generation generation;

    public Structure(Long seed, GameObject world_object) {
        this.seed = seed;
        this.generation = new Generation(seed);
        this.world_object = world_object;
    }

    public void generate(World world, Player player) {
        chunks.put(new Point(0, 0), new Chunk(0, 0, seed, generation.getTemp_seed(), generation.getMoist_seed(),
                world, player));
    }

    public abstract Tile getGeneratedTile(int x, int y, float height, float temp, float moist, Chunk chunk, int world_x, int world_y);

    public HashMap<Point, Chunk> getChunks() {
        return this.chunks;
    }
}
