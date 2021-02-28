package game.assets.levels.level_1;

import game.assets.levels.def.Level;

import java.awt.*;
import java.util.Random;

public class Level_1 extends Level {

    public Level_1() {
        super(1);
    }

    @Override
    public void tick() {
        getActiveRoom().tick();
    }

    @Override
    public void render(Graphics g) {
        getActiveRoom().render(g);
    }

    @Override
    public void generateRooms(Random rand) {
        for(int i=0; i<room_count; i++) {
            rooms.add(new Room_Test());
        }
    }
}
