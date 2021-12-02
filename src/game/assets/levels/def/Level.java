package game.assets.levels.def;

import game.system.helpers.Logger;
import game.system.helpers.Offsets;
import game.system.systems.gameObject.GameObject;
import game.system.systems.levelGeneration.LevelGenerator;

import java.awt.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

public abstract class Level {
    protected long level_seed;
    protected LinkedList<GameObject> objects;
    protected LevelGenerator generator;

    public Level() {
        this.objects = new LinkedList<>();
    }

//    public abstract void tick();
//    public abstract void render(Graphics g);

    public void generate() {
        this.level_seed = new Random().nextLong();
        this.generator = new LevelGenerator();
        this.generator.setSeed(this.level_seed);
        Logger.print("[seed]: " + level_seed);
        this.generateLevel(new Random(level_seed));
    }
    public void generate(Long level_seed) {
        this.level_seed = level_seed;
        this.generator = new LevelGenerator();
        this.generator.setSeed(this.level_seed);
        Logger.print("[seed]: " + level_seed);
        this.generateLevel(new Random(level_seed));
    }
    protected abstract void generateLevel(Random rand);

    public LinkedList<GameObject> getObjects() {
        return this.objects;
    }
}
