package game.system.systems.levelGeneration;

import game.textures.COLOR_PALETTE;
import game.textures.Fonts;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class LevelGenerator {

    private Long seed;
    private static Integer DEPTH = 4;

    // sizes in tiles = 16
    private static Integer START_WIDTH = 300;
    private static Integer START_HEIGHT = 300;

    private static Integer ROOM_MARGIN = 2;
    private static Integer MIN_ROOMSIZE = 5;

    LinkedList<SplitRectangle> splitRectangles;
    LinkedList<SplitRectangle> rooms;
    LinkedList<Rectangle> hallways;

    private Random r = new Random();
    private DirectionGenerator dirGen = new DirectionGenerator(2);

    public LevelGenerator() {
        this.splitRectangles = new LinkedList<>();
        this.rooms = new LinkedList<>();
        this.hallways = new LinkedList<>();
        // this.seed = new Random().nextLong();
        this.seed = -7022676857325916674L;
        if (seed != null) {
            setSeed(this.seed);
        }
        System.out.println("Level Seed: " + seed);
    }

    public void setSeed(long seed) {
        this.r.setSeed(seed);
        this.dirGen.setSeed(seed);
    }

    public void generate() {
        splitRectangles(0, new Rectangle(0, 0, START_WIDTH, START_HEIGHT), UUID.randomUUID());

        createRooms();

        createHallways();

        System.out.println(splitRectangles);
        System.out.println(rooms);
        // visualizeRooms();
    }

    private void splitRectangles(int currentDepth, Rectangle rect, UUID parentUuid) {
        LinkedList<Rectangle> generatedRects = getSplitRectangles(rect, dirGen.getNextDirection());

        for (Rectangle generatedRect : generatedRects) {
            SplitRectangle splitRect = new SplitRectangle(currentDepth, parentUuid, generatedRect);
            splitRectangles.add(splitRect);
            if (currentDepth < DEPTH - 1) {
                splitRectangles(currentDepth + 1, generatedRect, splitRect.uuid);
            }
        }
    }

    private void createRooms() {
        for (SplitRectangle splitRect : this.splitRectangles) {
            if (splitRect.depth == DEPTH - 1) {
                // System.out.println(splitRect);
                Rectangle rect = splitRect.rect;
                int x = getRandNumBetw(rect.x + ROOM_MARGIN, rect.x + (rect.width - ROOM_MARGIN) / 2);
                int y = getRandNumBetw(rect.y + ROOM_MARGIN, rect.y + (rect.height - ROOM_MARGIN) / 2);

                int xDiff = Math.abs(rect.x - x);
                int yDiff = Math.abs(rect.y - y);

                int width = getRandNumBetw(Math.round((rect.width - xDiff - ROOM_MARGIN) / 1.5f),
                        (rect.width - xDiff - ROOM_MARGIN));
                int height = getRandNumBetw(Math.round((rect.height - yDiff - ROOM_MARGIN) / 1.5f),
                        (rect.height - yDiff - ROOM_MARGIN));

                SplitRectangle newSplitRect = new SplitRectangle(splitRect.depth, splitRect.parentUuid,
                        new Rectangle(x, y, width, height));
                newSplitRect.setUuid(splitRect.uuid);
                rooms.add(newSplitRect);
            }
        }
    }

    private LinkedList<Rectangle> getSplitRectangles(Rectangle rect, boolean horizontal) {
        LinkedList<Rectangle> ret = new LinkedList<>();

        if (horizontal) {
            int offset = Math.round(rect.width * 0.1f) > 0 ? r.nextInt(Math.round(rect.width * 0.1f)) : 0;
            int splitPos = rect.width / 2 + (offset - offset / 2);

            ret.add(new Rectangle(rect.x, rect.y, splitPos, rect.height));
            ret.add(new Rectangle(rect.x + splitPos, rect.y, rect.width - splitPos, rect.height));
        } else {
            int offset = Math.round(rect.height * 0.1f) > 0 ? r.nextInt(Math.round(rect.height * 0.1f)) : 0;
            int splitPos = rect.height / 2 + (offset - offset / 2);

            ret.add(new Rectangle(rect.x, rect.y, rect.width, splitPos));
            ret.add(new Rectangle(rect.x, rect.y + splitPos, rect.width, rect.height - splitPos));
        }
        return ret;
    }

    private void createHallways() {
        for (int currDepth = DEPTH - 1; currDepth >= 0; currDepth--) {
            if (currDepth == DEPTH - 1) {
                LinkedList<UUID> connectedUuids = new LinkedList<>();
                // for(SplitRectangle splitRect : rooms) {
                SplitRectangle splitRect = rooms.get(0);
                if (!connectedUuids.contains(splitRect.uuid)) {
                    for (SplitRectangle splitRect2 : rooms) {
                        if (splitRect2.parentUuid == splitRect.parentUuid && splitRect2.uuid != splitRect.uuid) {
                            // create hallway between 2 sides of the rooms
                            Rectangle rect1 = splitRect.rect;
                            Rectangle rect2 = splitRect2.rect;

                            int x1 = rect1.x + rect1.width / 2;
                            int y1 = rect1.y + rect1.height / 2;
                            int x2 = rect2.x + rect2.width / 2;
                            int y2 = rect2.y + rect2.height / 2;

                            int xDiff = Math.abs(x1 - x2);
                            int yDiff = Math.abs(y1 - y2);

                            int x = x1 < x2 ? x1 : x2;
                            int y = y1 < y2 ? y1 : y2;

                            int width = xDiff;
                            int height = yDiff;

                            if (xDiff > yDiff) {
                                height = Math.round(width / 2);
                            } else {
                                width = Math.round(height / 2);
                            }

                            Rectangle hallway = new Rectangle(x, y, width, height);
                            hallways.add(hallway);
                            connectedUuids.add(splitRect.uuid);
                            connectedUuids.add(splitRect2.uuid);

                            // get the center of the two rooms
                        }
                    }
                }
                // }
            }
        }
    }

    public void render(Graphics g) {
        g.setColor(COLOR_PALETTE.cherry_red.color);
        for (SplitRectangle splitRect : this.splitRectangles) {
            if (splitRect.depth == DEPTH - 1) {
                Rectangle rect = splitRect.rect;
                g.drawRect(rect.x, rect.y, rect.width, rect.height);
            }
        }

        g.setFont(Fonts.default_fonts.get(4));
        g.setColor(COLOR_PALETTE.light_green.color);
        for (SplitRectangle splitRect : this.rooms) {
            Rectangle rect = splitRect.rect;
            g.drawString(splitRect.toString(), rect.x, rect.y);
            g.drawString(splitRect.rect.toString(), rect.x, rect.y + 5);
            g.drawRect(rect.x, rect.y, rect.width, rect.height);
        }

        g.setColor(COLOR_PALETTE.orange.color);
        for (Rectangle hallway : this.hallways) {
            g.drawRect(hallway.x, hallway.y, hallway.width, hallway.height);
        }
    }

    private int getRandNumBetw(int low, int high) {
        return this.r.nextInt(high - low) + low;
    }

}
