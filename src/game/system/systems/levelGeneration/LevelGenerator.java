package game.system.systems.levelGeneration;

import game.textures.COLOR_PALETTE;
import game.textures.Fonts;

import java.awt.*;
import java.util.*;

public class LevelGenerator {

    private Long seed;

    // sizes in tiles = 16
    private static final Integer GEN_WIDTH = 71; // needs to be odd
    private static final Integer GEN_HEIGHT = 71; // needs to be odd

    private static final Integer ROOM_MIN_SIZE = 11, ROOM_MAX_SIZE = 31, ROOM_ATTEMPTS = 1000;

    private static final Point[] DIRECTIONS = {
            new Point(0, -1),
            new Point(1, 0),
            new Point(0, 1),
            new Point(-1, 0),
    };

    LinkedList<Room> rooms;

    /**
     * Integer is the state of the tile
     * 0 = open
     * 1 = wall
     */
    HashMap<Point, Integer> cells;

    HashMap<Room[], Point> connections;

    Room mainRoom;
    LinkedList<Point> mainRegion;

    private Random r = new Random();

    public LevelGenerator() {
        this.rooms = new LinkedList<>();
        this.cells = new HashMap<>();
        this.mainRegion = new LinkedList<>();

        for (int y = 0; y < GEN_HEIGHT; y++) {
            for (int x = 0; x < GEN_WIDTH; x++) {
                cells.put(new Point(x, y), 1);
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

        for (int y = 1; y < GEN_HEIGHT; y += 2) {
            for (int x = 1; x < GEN_WIDTH; x += 2) {
                Point cell = new Point(x, y);
                if(wallsOnAllSides(cell)) carveMaze(new Point(x, y));
            }
        }

        findAllConnectors();
        makeConnections();

//        removeDeadEnds();
    }

    private void createRooms() {
        for (int i = 0; i < ROOM_ATTEMPTS; i++) {
            int randW = getRandNumBetw(ROOM_MIN_SIZE, ROOM_MAX_SIZE - 1);
            int randH = getRandNumBetw(ROOM_MIN_SIZE, ROOM_MAX_SIZE - 1);
            int randX = getRandNumBetw(0, GEN_WIDTH - randW - 1);
            int randY = getRandNumBetw(0, GEN_WIDTH - randH - 1);
            // Values need to be all odd for maze to be valid
            if (randW % 2 == 0) randW += 1;
            if (randH % 2 == 0) randH += 1;
            if (randX % 2 == 0) randX += 1;
            if (randY % 2 == 0) randY += 1;
            Rectangle room = new Rectangle(randX, randY, randW, randH);

            if (isInsideGeneration(room) && !isOverlapping(room)) {
                this.rooms.add(new Room(room));
            }
        }
    }

    private void carveRoomsInTiles() {
        for (Point coord : this.cells.keySet()) {
            for (Room room : this.rooms) {
                if (room.rect.contains(coord)) {
                    this.cells.put(coord, 0);
                }
            }
        }
    }

    private void carveMaze(Point startingCell) {
        LinkedList<Point> stack = new LinkedList<>();
        stack.add(startingCell);

        while (!stack.isEmpty()) {
            Point tile = stack.pop();
            LinkedList<Point> unvisitedNeighbours = getUnvisitedNeighbours(tile);
            if (!unvisitedNeighbours.isEmpty()) {
                stack.add(tile);
                Point neighbourTile = unvisitedNeighbours.get(r.nextInt(unvisitedNeighbours.size()));
//                Point neighbourTile = unvisitedNeighbours.pop();
                int xOffset = neighbourTile.x - tile.x;
                int yOffset = neighbourTile.y - tile.y;

                if (xOffset > 0) {
                    this.cells.put(new Point(tile.x + 1, tile.y), 0);
                } else if (xOffset < 0) {
                    this.cells.put(new Point(tile.x - 1, tile.y), 0);
                } else if (yOffset > 0) {
                    this.cells.put(new Point(tile.x, tile.y + 1), 0);
                } else if (yOffset < 0) {
                    this.cells.put(new Point(tile.x, tile.y - 1), 0);
                }
                this.cells.put(neighbourTile, 0);
                stack.add(neighbourTile);
            }
        }
    }

    private LinkedList<Point> getUnvisitedNeighbours(Point coord) {
        LinkedList<Point> ret = new LinkedList<>();

        for (Point dirOffset : DIRECTIONS) {
            Point offsetCoord = new Point(coord.x + dirOffset.x * 2, coord.y + dirOffset.y * 2);
            if (this.cells.containsKey(offsetCoord)) {
                if (this.cells.get(offsetCoord) == 1) {
                    ret.add(offsetCoord);
                }
            }
        }
        return ret;
    }

    private boolean wallsOnAllSides(Point p) {
        for(Point dir : DIRECTIONS) {
            Point offsetCoord = new Point(p.x + dir.x, p.y + dir.y);
            if(this.cells.containsKey(offsetCoord) && this.cells.get(offsetCoord) == 0) {
                return false;
            }
            if(!this.cells.containsKey(offsetCoord)) {
                return false;
            }
        }
        return true;
    }

    private boolean isOnEdge(Point p, Rectangle rect) {
        return (p.x >= rect.x && p.x <= rect.x + rect.width && p.y == rect.y) ||
                (p.x >= rect.x && p.x <= rect.x + rect.width && p.y == rect.y + rect.height) ||
                (p.x == rect.x && p.y >= rect.y && p.y <= rect.y + rect.height) ||
                (p.x == rect.x + rect.width && p.y >= rect.y && p.y <= rect.y + rect.height);
    }

    private void findAllConnectors() {
        for(Room rm : this.rooms) {
            Rectangle room = rm.rect;
            for(int x = room.x - 1; x < room.x + room.width; x++){
                Point top = new Point(x, room.y - 1);
                Point bottom = new Point(x, room.y + room.height);
                if(isPossibleConnection(top) && !isCorner(room, top)) rm.addConnector(top);
                if(isPossibleConnection(bottom) && !isCorner(room, bottom)) rm.addConnector(bottom);
            }
            for(int y = room.y - 1; y < room.y + room.height; y++){
                Point left = new Point(room.x - 1, y);
                Point right = new Point(room.x + room.width, y);
                if(isPossibleConnection(left) && !isCorner(room, left)) rm.addConnector(left);
                if(isPossibleConnection(right) && !isCorner(room, right)) rm.addConnector(right);
            }
        }
    }

    private boolean isPossibleConnection(Point cell) {
        int possibleConnectionCount = 0;
        if (this.cells.get(cell) == 1) {
            for (Point dirOffset : DIRECTIONS) {
                Point offsetCoord = new Point(cell.x + dirOffset.x, cell.y + dirOffset.y);
                if (this.cells.containsKey(offsetCoord)) {
                    if (this.cells.get(offsetCoord) == 0) {
                        possibleConnectionCount++;
                    }
                }
            }
        }
        return possibleConnectionCount == 2;
    }

    private boolean isCorner(Rectangle rect, Point p) {
        return (p.x == rect.x-1 && p.y == rect.y-1) ||
                (p.x == rect.x + rect.width && p.y == rect.y-1) ||
                (p.x == rect.x + rect.width && p.y == rect.y + rect.height) ||
                (p.x == rect.x-1 && p.y == rect.y + rect.height);
    }

    private void makeConnections() {
        this.mainRoom = this.rooms.get(getRandNumBetw(0, this.rooms.size()));
        Point connector = mainRoom.connectors.get(getRandNumBetw(0, mainRoom.connectors.size()));

        mainRoom.setConnectors(new LinkedList<>());
        mainRoom.setConnector(connector);
        this.cells.put(connector, 0);
//        this.mainRegion.add(connector);

        LinkedList<Point> stack = new LinkedList<>();
        stack.add(connector);

        while(!stack.isEmpty()) {
            System.out.println(stack.size());
            Point currentCell = stack.pop();
            if(this.cells.get(currentCell) == 0) this.mainRegion.add(currentCell);
            LinkedList<Point> neighbours = getAllWalkableNeighbours(currentCell, stack);
            if(!neighbours.isEmpty()) {
                stack.addAll(neighbours);
            }
        }

//        LinkedList<Point> connectors = getAllConnectorsTouchingMainRegion();
//        Point connector

        // TODO: OLD CODE
//        for(Room room : this.rooms) {
//            Point connector = room.connectors.get(getRandNumBetw(0, room.connectors.size()));
//            this.cells.put(connector, 0);
//        }

    }

    private LinkedList<Point> getAllWalkableNeighbours(Point cell, LinkedList<Point> stack) {
        LinkedList<Point> neighbours = new LinkedList<>();
        for(Point dir : DIRECTIONS) {
            Point offsetCoord = new Point(cell.x + dir.x, cell.y + dir.y);
            if(this.cells.containsKey(offsetCoord) && this.cells.get(offsetCoord) == 0 && !this.mainRegion.contains(offsetCoord) && !stack.contains(offsetCoord)) {
                neighbours.add(offsetCoord);
            }
        }
        return neighbours;
    }

    private LinkedList<Point> getAllConnectorsTouchingMainRegion() {
        LinkedList<Point> connectors = new LinkedList<>();
        for(Room room : this.rooms) {
            connectors.addAll(room.connectors);
        }

        LinkedList<Point> ret = new LinkedList<>();
        for(Point connector : connectors) {
            if(isTouchingMainRegion(connector)) ret.add(connector);
        }
        return ret;
    }

    private boolean isTouchingMainRegion(Point cell) {
        for(Point regionCell : this.mainRegion) {
            int xDiff = Math.abs(cell.x - regionCell.x);
            int yDiff = Math.abs(cell.y - regionCell.y);
            if(
                    (xDiff == 1 && yDiff == 0) ||
                    (yDiff == 1 && xDiff == 0)
            ) {
                return true;
            }
        }
        return false;
    }

    private void removeDeadEnds() {
        while(deadEndsExist()) {
            for (int y = 1; y < GEN_HEIGHT; y += 1) {
                for (int x = 1; x < GEN_WIDTH; x += 1) {
                    Point p = new Point(x, y);
                    if(this.cells.get(p) == 0) {
                        if(isDeadEnd(p)) this.cells.put(p, 1);
                    }
                }
            }
        }
    }

    private boolean deadEndsExist() {
        for (int y = 1; y < GEN_HEIGHT; y += 1) {
            for (int x = 1; x < GEN_WIDTH; x += 1) {
                Point p = new Point(x, y);
                if(isDeadEnd(p)) return true;
            }
        }
        return false;
    }

    private boolean isDeadEnd(Point p) {
        int wallSides = 0;
        if (this.cells.get(p) == 0) {
            for (Point dirOffset : DIRECTIONS) {
                Point offsetCoord = new Point(p.x + dirOffset.x, p.y + dirOffset.y);
                if (this.cells.containsKey(offsetCoord)) {
                    if (this.cells.get(offsetCoord) == 1) {
                        wallSides++;
                    }
                }
            }
        }
        return wallSides >= 3;
    }

    public void render(Graphics g) {
        g.setColor(COLOR_PALETTE.yellow.color);
//        for (Room room : this.rooms) {
//            for(Point connection : room.connectors) {
//                g.drawLine(connection.x, connection.y, connection.x, connection.y);
//            }
//        }
        for(Point p : this.mainRegion) {
            g.drawLine(p.x, p.y, p.x, p.y);
        }

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

        for (Point coord : this.cells.keySet()) {
            if (this.cells.get(coord) == 1) {
                g.drawLine(coord.x, coord.y, coord.x, coord.y);
            }
        }
    }

    private boolean isInsideGeneration(Rectangle rect) {
        return rect.x > 0 && rect.y > 0 && rect.x + rect.width < GEN_WIDTH && rect.y + rect.height < GEN_HEIGHT;
    }

    private boolean isOverlapping(Rectangle rect) {
        for (Room room : this.rooms) {
            if (room.rect.intersects(rect)) return true;
        }
        return false;
    }

    private int getRandNumBetw(int low, int high) {
        return this.r.nextInt(high - low) + low;
    }

}
