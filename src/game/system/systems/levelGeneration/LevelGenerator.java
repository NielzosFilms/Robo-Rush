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
    private static Integer DEPTH = 4;

    // sizes in tiles = 16
    private static Integer START_WIDTH = 300;
    private static Integer START_HEIGHT = 300;

    private static Integer ROOM_MARGIN = 2;
    private static Integer MIN_ROOMSIZE = 5;

    LinkedList<SplitRectangle> splitRectangles;
    LinkedList<SplitRectangle> rooms;
    LinkedList<LinkedList<Line>> hallways;

    private Random r = new Random();
    private DirectionGenerator dirGen = new DirectionGenerator(2);

    public LevelGenerator() {
        this.splitRectangles = new LinkedList<>();
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
            LinkedList<UUID> connectedRooms = new LinkedList<>();
            if (currDepth == DEPTH - 1) {
                for (SplitRectangle room1 : rooms) {
                    if (!connectedRooms.contains(room1.uuid)) {
                        SplitRectangle room2 = null;
                        for (SplitRectangle rm : rooms) {
                            if (rm.parentUuid == room1.parentUuid && rm.uuid != room1.uuid) {
                                room2 = rm;
                                break;
                            }
                        }

                        connectedRooms.add(room1.uuid);
                        connectedRooms.add(room2.uuid);
                        connectRooms(room1, room2, currDepth);
                    }
                }
            } else {
//                if(currDepth <= 0) return;
                LinkedList<SplitRectangle> depthRooms = new LinkedList<>();
                for (SplitRectangle rm : splitRectangles) {
                    if (rm.depth == currDepth) {
                        depthRooms.add(rm);
                    }
                }

                System.out.println("depthRooms");
                System.out.println(depthRooms);

                LinkedList<UUID> connected = new LinkedList<>();

                for (SplitRectangle depthRoom : depthRooms) {
                    if(connected.contains(depthRoom.uuid)) continue;
                    SplitRectangle parent1 = depthRoom;
                    SplitRectangle parent2 = null;
                    for (SplitRectangle rm : splitRectangles) {
                        if (rm.parentUuid == depthRoom.parentUuid) {
                            parent2 = rm;
                            break;
                        }
                    }
                    LinkedList<SplitRectangle> group1 = getMaxDepthChildren(parent1);
                    LinkedList<SplitRectangle> group2 = getMaxDepthChildren(parent2);
                    connected.add(parent1.uuid);
                    connected.add(parent2.uuid);

                    System.out.println("group1");
                    System.out.println(group1);
                    System.out.println("group2");
                    System.out.println(group2);


                    SplitRectangle newRoom1 = getClosestRoom(group1, new Point((int)parent2.rect.getCenterX(), (int)parent2.rect.getCenterY()));
                    SplitRectangle newRoom2 = getClosestRoom(group2, new Point((int)parent1.rect.getCenterX(), (int)parent1.rect.getCenterY()));

                    System.out.println("newRoom1");
                    System.out.println(newRoom1);
                    System.out.println("newRoom2");
                    System.out.println(newRoom2);

                    connectRooms(newRoom1, newRoom2, currDepth);
                }
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

    private SplitRectangle getClosestRoom(LinkedList<SplitRectangle> rooms, Point coord) {
        SplitRectangle ret = null;
        double dist = -1;

        for(SplitRectangle room : rooms) {
            double cenX = room.rect.getCenterX();
            double cenY = room.rect.getCenterY();
            double calcDist = Helpers.getDistance(new Point((int)cenX, (int)cenY), coord);
            if(dist == -1 || calcDist < dist) {
                dist = calcDist;
                ret = room;
            }
        }
        return ret;
    }

    private LinkedList<SplitRectangle> getMaxDepthChildren(SplitRectangle splitParent) {
        LinkedList<SplitRectangle> parentRooms = new LinkedList<>();
        parentRooms.add(splitParent);
        int parentDepth = splitParent.depth;
        while (parentDepth < DEPTH - 1) {
            LinkedList<SplitRectangle> newParents = new LinkedList<>();
            for (SplitRectangle parent : parentRooms) {
                for (SplitRectangle rect : splitRectangles) {
                    if (rect.parentUuid == parent.uuid) {
                        newParents.add(rect);
                    }
                }
            }
            parentRooms = newParents;
            parentDepth++;
        }
        LinkedList<SplitRectangle> ret = new LinkedList<>();
        for(SplitRectangle rm : parentRooms) {
            for(SplitRectangle rm2 : rooms) {
                if(rm.uuid == rm2.uuid) {
                    ret.add(rm2);
                }
            }
        }
        return ret;
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
            if (Game.DEBUG_MODE) {
                g.drawString(splitRect.toString(), rect.x, rect.y);
                g.drawString(splitRect.rect.toString(), rect.x, rect.y + 5);
            }
            g.drawRect(rect.x, rect.y, rect.width, rect.height);
        }

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

    private int getRandNumBetw(int low, int high) {
        return this.r.nextInt(high - low) + low;
    }

}
