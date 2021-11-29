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
    private static Integer GEN_WIDTH = 301; // needs to be odd
    private static Integer GEN_HEIGHT = 301; // needs to be odd

    private static Integer ROOM_MIN_SIZE = 15, ROOM_MAX_SIZE = 31, ROOM_ATTEMPTS = 1000;

    LinkedList<Rectangle> rooms;

    /**
     * Integer is the state of the tile
     * 0 = open
     * 1 = wall
     */
    HashMap<Point, Integer> generatedTiles;

    private Random r = new Random();

    public LevelGenerator() {
        this.rooms = new LinkedList<>();
        this.generatedTiles = new HashMap<>();

        for(int y = 0; y < GEN_HEIGHT; y++) {
            for(int x = 0; x < GEN_WIDTH; x++) {
                generatedTiles.put(new Point(x, y), 1);
            }
        }

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
        carveRoomsInTiles();

        carveMaze();
    }

    private void createRooms() {
        for(int i = 0; i < ROOM_ATTEMPTS; i++) {
            int randW = getRandNumBetw(ROOM_MIN_SIZE, ROOM_MAX_SIZE - 1);
            int randH = getRandNumBetw(ROOM_MIN_SIZE, ROOM_MAX_SIZE - 1);
            int randX = getRandNumBetw(0, GEN_WIDTH - randW - 1);
            int randY = getRandNumBetw(0, GEN_WIDTH - randH - 1);
            // Values need to be all odd for maze to be valid
            if(randW % 2 == 0) randW += 1;
            if(randH % 2 == 0) randH += 1;
            if(randX % 2 == 0) randX += 1;
            if(randY % 2 == 0) randY += 1;
            Rectangle room = new Rectangle(randX, randY, randW, randH);

            if(isInsideGeneration(room) && !isOverlapping(room)) {
                this.rooms.add(room);
            }
        }
    }

    private void carveRoomsInTiles() {
        for(Point coord : this.generatedTiles.keySet()) {
            for(Rectangle room : this.rooms) {
                if(room.contains(coord)) {
                    this.generatedTiles.put(coord, 0);
                }
            }
        }
    }

    private void carveMaze() {
        LinkedList<Point> stack = new LinkedList<>();
        stack.add(new Point(1, 1));

        while(!stack.isEmpty()) {
            Point tile = stack.pop();
            LinkedList<Point> unvisitedNeighbours = getUnvisitedNeighbours(tile);
            if(!unvisitedNeighbours.isEmpty()) {
                stack.add(tile);
                Point neighbourTile = unvisitedNeighbours.get(getRandNumBetw(0, unvisitedNeighbours.size()));
                int xOffset = neighbourTile.x - tile.x;
                int yOffset = neighbourTile.y - tile.y;

                if(xOffset > 0) {
                    this.generatedTiles.put(new Point(tile.x + 1, tile.y), 0);
                } else if(xOffset < 0) {
                    this.generatedTiles.put(new Point(tile.x - 1, tile.y), 0);
                } else if(yOffset > 0) {
                    this.generatedTiles.put(new Point(tile.x, tile.y + 1), 0);
                } else if(yOffset < 0) {
                    this.generatedTiles.put(new Point(tile.x, tile.y - 1), 0);
                }
                this.generatedTiles.put(neighbourTile, 0);
                stack.add(neighbourTile);
            }
        }
    }

    private LinkedList<Point> getUnvisitedNeighbours(Point coord) {
        Point[] directions = {
                new Point(0, -2),
                new Point(2, 0),
                new Point(0, 2),
                new Point(-2, 0),
        };
        LinkedList<Point> ret = new LinkedList<>();

        for(Point dirOffset : directions) {
            Point offsetCoord = new Point(coord.x + dirOffset.x, coord.y + dirOffset.y);
            if(this.generatedTiles.containsKey(offsetCoord)) {
                if(this.generatedTiles.get(offsetCoord) == 1) {
                    ret.add(offsetCoord);
                }
            }
        }
        return ret;
    }

    public void render(Graphics g) {
        g.setFont(Fonts.default_fonts.get(4));
        g.setColor(COLOR_PALETTE.light_green.color);
//        for (Rectangle room : this.rooms) {
//            if (Game.DEBUG_MODE) {
//                g.drawString(room.toString(), room.x, room.y);
//            }
//            g.drawRect(room.x, room.y, room.width, room.height);
//        }
//        for (Rectangle room : this.rooms) {
//            if (Game.DEBUG_MODE) {
//                g.drawString(room.toString(), room.x * 16, room.y * 16);
//            }
//            g.drawRect(room.x * 16, room.y * 16, room.width * 16, room.height * 16);
//        }

        for(Point coord : this.generatedTiles.keySet()) {
            if(this.generatedTiles.get(coord) == 1) {
                g.drawLine(coord.x, coord.y, coord.x, coord.y);
            }
        }
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
