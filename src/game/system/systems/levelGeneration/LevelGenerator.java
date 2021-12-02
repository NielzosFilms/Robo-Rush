package game.system.systems.levelGeneration;

import game.assets.tiles.Tile_Static;
import game.assets.tiles.tile.Tile;
import game.system.systems.gameObject.GameObject;
import game.textures.TEXTURE_LIST;
import game.textures.Texture;

import java.awt.*;
import java.util.*;

public class LevelGenerator {

    private Long seed;

    // sizes in tiles = 16
    private Integer GEN_WIDTH = 41; // needs to be odd
    private Integer GEN_HEIGHT = 41; // needs to be odd

    private Integer ROOM_MIN_SIZE = 7, ROOM_MAX_SIZE = 27, ROOM_ATTEMPTS = 2000;

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
        setSeed(this.seed);
    }

    public void setSeed(long seed) {
        this.r.setSeed(seed);
    }

    public void setGenerationParams(int genWidth, int genHeight, int minRoomSize, int maxRoomSize, int roomAttempts) {
        if(genWidth % 2 != 0) genWidth++;
        if(genHeight % 2 != 0) genHeight++;
        if(minRoomSize % 2 != 0) minRoomSize++;
        if(maxRoomSize % 2 != 0) maxRoomSize++;

        this.GEN_WIDTH = genWidth;
        this.GEN_HEIGHT = genHeight;
        this.ROOM_MIN_SIZE = minRoomSize;
        this.ROOM_MAX_SIZE = maxRoomSize;
        this.ROOM_ATTEMPTS = roomAttempts;
    }

    public void generate() {
        createRooms();
        carveRoomsInTiles();

        for (int y = 1; y < GEN_HEIGHT; y += 2) {
            for (int x = 1; x < GEN_WIDTH; x += 2) {
                Point cell = new Point(x, y);
                if (wallsOnAllSides(cell)) carveMaze(new Point(x, y));
            }
        }

        findAllConnectors();
        makeConnections();

        removeDeadEnds();
    }

    private void createRooms() {
        //create mainRoom
        int width = ROOM_MIN_SIZE;
        int height = ROOM_MIN_SIZE;
        int x = getRandNumBetw(0, GEN_WIDTH - width - 1);
        int y = getRandNumBetw(0, GEN_HEIGHT - height - 1);
        if (x % 2 == 0) x += 1;
        if (y % 2 == 0) y += 1;
        Rectangle mainRoomRect = new Rectangle(x, y, width, height);
        Room mainRoom = new Room(mainRoomRect);
        this.mainRoom = mainRoom;
        this.rooms.add(mainRoom);
        for (int i = 0; i < ROOM_ATTEMPTS; i++) {
            int randH = getRandNumBetw(ROOM_MIN_SIZE, ROOM_MAX_SIZE - 1);
            int randW = getRandNumBetw(randH == ROOM_MIN_SIZE ? ROOM_MIN_SIZE + 2 : ROOM_MIN_SIZE, ROOM_MAX_SIZE - 1);
            int randX = getRandNumBetw(0, GEN_WIDTH - randW - 1);
            int randY = getRandNumBetw(0, GEN_WIDTH - randH - 1);
            // Values need to be all odd for maze to be valid
            if (randH % 2 == 0) randH += 1;
            if (randW % 2 == 0) randW += 1;
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
        for (Point dir : DIRECTIONS) {
            Point offsetCoord = new Point(p.x + dir.x, p.y + dir.y);
            if (this.cells.containsKey(offsetCoord) && this.cells.get(offsetCoord) == 0) {
                return false;
            }
            if (!this.cells.containsKey(offsetCoord)) {
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
        for (Room rm : this.rooms) {
            Rectangle room = rm.rect;
            for (int x = room.x - 1; x < room.x + room.width; x++) {
                Point top = new Point(x, room.y - 1);
                Point bottom = new Point(x, room.y + room.height);
                if (isPossibleConnection(top) && !isCorner(room, top)) rm.addConnector(top);
                if (isPossibleConnection(bottom) && !isCorner(room, bottom)) rm.addConnector(bottom);
            }
            for (int y = room.y - 1; y < room.y + room.height; y++) {
                Point left = new Point(room.x - 1, y);
                Point right = new Point(room.x + room.width, y);
                if (isPossibleConnection(left) && !isCorner(room, left)) rm.addConnector(left);
                if (isPossibleConnection(right) && !isCorner(room, right)) rm.addConnector(right);
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
        return (p.x == rect.x - 1 && p.y == rect.y - 1) ||
                (p.x == rect.x + rect.width && p.y == rect.y - 1) ||
                (p.x == rect.x + rect.width && p.y == rect.y + rect.height) ||
                (p.x == rect.x - 1 && p.y == rect.y + rect.height);
    }

    private void makeConnections() {
//        this.mainRoom = this.rooms.get(getRandNumBetw(0, this.rooms.size()));
        Point connector = mainRoom.connectors.get(getRandNumBetw(0, mainRoom.connectors.size()));

        LinkedList<Point> newMainRoomConnectors = new LinkedList<>();
        for(Point conn : this.mainRoom.connectors) {
            if(conn.x == connector.x || conn.y == connector.y) newMainRoomConnectors.add(conn);
        }
        mainRoom.setConnectors(newMainRoomConnectors);
        mainRoom.setConnector(connector);

        this.mainRegion.add(connector);
        this.cells.put(connector, 0);

        LinkedList<Point> connectors = getAllConnectorsTouchingMainRegion();

        boolean firstIteration = true;

        while (!connectors.isEmpty() || firstIteration) {
            if(!firstIteration) {
                this.cells.put(connector, 0);
                LinkedList<Room> rooms = getRoomsWithConnector(connector);
                if(!rooms.isEmpty()) {
                    for(Room tmpRoom : rooms) {
                        LinkedList<Point> newConnectors = new LinkedList<>();
                        for(Point roomConnector : tmpRoom.connectors) {
                            if(!isTouchingMainRegion(roomConnector)) newConnectors.add(roomConnector);
                        }
                        tmpRoom.setConnectors(newConnectors);
                    }

                }
            }

            LinkedList<Point> stack = new LinkedList<>();
            stack.add(connector);

            while (!stack.isEmpty()) {
                Point currentCell = stack.pop();
                if (this.cells.get(currentCell) == 0 && !this.mainRegion.contains(currentCell))
                    this.mainRegion.add(currentCell);
                LinkedList<Point> neighbours = getAllWalkableNeighbours(currentCell, stack);
                if (!neighbours.isEmpty()) {
                    stack.addAll(neighbours);
                }
            }

            connectors = getAllConnectorsTouchingMainRegion();
            if(!connectors.isEmpty())connector = connectors.get(r.nextInt(connectors.size()));

            if (firstIteration) firstIteration = false;
        }

    }

    private LinkedList<Point> getAllWalkableNeighbours(Point cell, LinkedList<Point> stack) {
        LinkedList<Point> neighbours = new LinkedList<>();
        for (Point dir : DIRECTIONS) {
            Point offsetCoord = new Point(cell.x + dir.x, cell.y + dir.y);
            if (this.cells.containsKey(offsetCoord) && this.cells.get(offsetCoord) == 0 && !this.mainRegion.contains(offsetCoord) && !stack.contains(offsetCoord)) {
                neighbours.add(offsetCoord);
            }
        }
        return neighbours;
    }

    private LinkedList<Point> getAllConnectorsTouchingMainRegion() {
        LinkedList<Point> connectors = new LinkedList<>();
        for (Room room : this.rooms) {
            connectors.addAll(room.connectors);
        }

        LinkedList<Point> ret = new LinkedList<>();
        for (Point connector : connectors) {
            if (isTouchingMainRegion(connector)) ret.add(connector);
        }
        return ret;
    }

    private boolean isTouchingMainRegion(Point cell) {
        for (Point regionCell : this.mainRegion) {
            int xDiff = Math.abs(cell.x - regionCell.x);
            int yDiff = Math.abs(cell.y - regionCell.y);
            if (
                    (xDiff == 1 && yDiff == 0) ||
                            (yDiff == 1 && xDiff == 0)
            ) {
                return true;
            }
        }
        return false;
    }

    private LinkedList<Room> getRoomsWithConnector(Point connector) {
        LinkedList<Room> ret = new LinkedList<>();
        for (Room room : this.rooms) {
            if (room.connectors.contains(connector)) ret.add(room);
        }
        return ret;
    }

    private void removeDeadEnds() {
        while (deadEndsExist()) {
            for (int y = 1; y < GEN_HEIGHT; y += 1) {
                for (int x = 1; x < GEN_WIDTH; x += 1) {
                    Point p = new Point(x, y);
                    if (this.cells.get(p) == 0) {
                        if (isDeadEnd(p)) this.cells.put(p, 1);
                    }
                }
            }
        }
    }

    private boolean deadEndsExist() {
        for (int y = 1; y < GEN_HEIGHT; y += 1) {
            for (int x = 1; x < GEN_WIDTH; x += 1) {
                Point p = new Point(x, y);
                if (isDeadEnd(p)) return true;
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

    public LinkedList<Room> getRooms() {
        return this.rooms;
    }

    public HashMap<Point, Integer> getDungeonInCells() {
        return cells;
    }

    public Room getMainRoom() {
        return mainRoom;
    }

    public LinkedList<GameObject> getDungeonInTiles(TEXTURE_LIST tileSheet) {
        LinkedList<GameObject> ret = new LinkedList<>();
        for(Room room : this.rooms) {
            ret.addAll(getTilesFromRoom(tileSheet, room));
        }
        return ret;
    }

    private LinkedList<GameObject> getTilesFromRoom(TEXTURE_LIST tileSheet, Room room) {
        LinkedList<GameObject> ret = new LinkedList<>();
        Rectangle rect = room.rect;
        for (int y = rect.y; y < rect.y + rect.height; y++) {
            for (int x = rect.x; x < rect.x + rect.width; x++) {
                ret.add(new Tile_Static(x * 16, y * 16, 2, new Texture(tileSheet, 13, 1)));
            }
        }
        return ret;
    }
}
