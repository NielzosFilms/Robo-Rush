package game.system.systems.levelGeneration;

import game.system.helpers.Helpers;
import game.system.main.Game;
import game.textures.COLOR_PALETTE;
import game.textures.Fonts;

import java.awt.*;
import java.awt.geom.Line2D;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class LevelGenerator {

    private Long seed;

    // sizes in tiles = 16
    private static Integer GEN_WIDTH = 300;
    private static Integer GEN_HEIGHT = 300;

    private static Integer ROOM_MIN_SIZE = 15, ROOM_MAX_SIZE = 30, ROOM_ATTEMPTS = 100;

    LinkedList<Rectangle> rooms;
    LinkedList<LinkedList<Line>> hallways;

    private Random r = new Random();

    public LevelGenerator() {
        this.rooms = new LinkedList<>();
        this.hallways = new LinkedList<>();
        this.seed = new Random().nextLong();
//        this.seed = -7022676857325916674L;
        if (seed != null) {
            setSeed(this.seed);
        }
        System.out.println("Level Seed: " + seed);
    }

    public void setSeed(long seed) {
        this.r.setSeed(seed);
    }

    public void generate() {
        createRooms();
    }

    private void createRooms() {
        for(int i = 0; i < ROOM_ATTEMPTS; i++) {
            int randW = getRandNumBetw(ROOM_MIN_SIZE, ROOM_MAX_SIZE);
            int randH = getRandNumBetw(ROOM_MIN_SIZE, ROOM_MAX_SIZE);
            int randX = getRandNumBetw(0, GEN_WIDTH - randW);
            int randY = getRandNumBetw(0, GEN_WIDTH - randH);
            Rectangle room = new Rectangle(randX, randY, randW, randH);

            if(isInsideGeneration(room) && !isOverlapping(room)) {
                this.rooms.add(room);
            }
        }
    }

    public void render(Graphics g) {
        g.setFont(Fonts.default_fonts.get(4));
        g.setColor(COLOR_PALETTE.light_green.color);
        for (Rectangle room : this.rooms) {
            if (Game.DEBUG_MODE) {
                g.drawString(room.toString(), room.x, room.y);
            }
            g.drawRect(room.x, room.y, room.width, room.height);
        }
//        for (Rectangle room : this.rooms) {
//            if (Game.DEBUG_MODE) {
//                g.drawString(room.toString(), room.x * 16, room.y * 16);
//            }
//            g.drawRect(room.x * 16, room.y * 16, room.width * 16, room.height * 16);
//        }

        for (LinkedList<Line> hallway : this.hallways) {
            for (Line path : hallway) {
                switch (path.depth) {
                    case 3:
                        g.setColor(COLOR_PALETTE.red.color);
                        break;
                    case 2:
                        g.setColor(COLOR_PALETTE.orange.color);
                        break;
                    case 1:
                        g.setColor(COLOR_PALETTE.green.color);
                        break;
                    default:
                        g.setColor(COLOR_PALETTE.white.color);
                        break;
                }
                g.drawLine(path.x1, path.y1, path.x2, path.y2);
            }
        }
    }

    private void connectRooms(SplitRectangle room1, SplitRectangle room2, int depth) {
        room1.setConnectedTo(room2.uuid);
        room2.setConnectedTo(room1.uuid);

        int room1CenX = (int) room1.rect.getCenterX();
        int room1CenY = (int) room1.rect.getCenterY();

        int room2CenX = (int) room2.rect.getCenterX();
        int room2CenY = (int) room2.rect.getCenterY();

        LinkedList<Line> hallwayPath = new LinkedList();
        if (r.nextBoolean()) {
            hallwayPath.add(new Line(room1CenX, room1CenY, room2CenX, room1CenY, depth)); // horizontal
            hallwayPath.add(new Line(room2CenX, room1CenY, room2CenX, room2CenY, depth)); // vertical
        } else {
            hallwayPath.add(new Line(room1CenX, room1CenY, room1CenX, room2CenY, depth)); // vertical
            hallwayPath.add(new Line(room1CenX, room2CenY, room2CenX, room2CenY, depth)); // horizontal
        }

        hallways.add(hallwayPath);
    }

    private boolean isInsideGeneration(Rectangle rect) {
        return rect.x > 0 && rect.y > 0 && rect.x + rect.width < GEN_WIDTH && rect.y + rect.height < GEN_HEIGHT;
    }

    private boolean isOverlapping(Rectangle rect) {
        for(Rectangle room : this.rooms) {
            if(room.intersects(rect)) return true;
        }
        return false;
    }

    private int getRandNumBetw(int low, int high) {
        return this.r.nextInt(high - low) + low;
    }

}
