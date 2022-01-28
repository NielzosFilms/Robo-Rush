package game.system.systems.levelGeneration;

import game.assets.entities.player.Player;
import game.assets.objects.BoundsObject;
import game.assets.tiles.Tile_Static;
import game.assets.tiles.tile.Tile;
import game.system.helpers.Offsets;
import game.system.main.Game;
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

    LinkedList<Point> connections;

    private Random r = new Random();

    public LevelGenerator() {
        this.rooms = new LinkedList<>();
        this.cells = new HashMap<>();
        this.mainRegion = new LinkedList<>();
        this.connections = new LinkedList<>();

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
                this.connections.add(connector);
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

    public HashMap<Point, Integer> getDungeonInCellsMinimal() {
        HashMap<Point, Integer> filteredCells = new HashMap<>();
        for(Point cell : this.cells.keySet()) if(shouldBeTile(cell)) filteredCells.put(cell, this.cells.get(cell));
        return filteredCells;
    }

    public Room getMainRoom() {
        return mainRoom;
    }

    public LinkedList<GameObject> getDungeonInTiles(TEXTURE_LIST tileSheet) {
        LinkedList<GameObject> ret = new LinkedList<>();

        HashMap<Point, Integer> filteredCells = getDungeonInCellsMinimal();

        Player player = Game.gameController.getPlayer();

        for(Point cell : filteredCells.keySet()) {
            if(filteredCells.get(cell) == 1) {
                ret.add(new BoundsObject(cell.x * 16, cell.y * 16, 16, 16));
                boolean top = cellHasSameNeighbour(filteredCells, cell, 0, -1);
                boolean bot = cellHasSameNeighbour(filteredCells, cell, 0, 1);
                boolean rgt = cellHasSameNeighbour(filteredCells, cell, 1, 0);
                boolean lft = cellHasSameNeighbour(filteredCells, cell, -1, 0);

                Texture texture = new Texture(tileSheet, 4, 1);
                if(top && bot && rgt) {
                    texture.setX(0);
                    texture.setY(1);
                    ret.add(new Tile_Static(cell.x * 16, cell.y * 16 - 16, player.getZIndex()+1, new Texture(tileSheet, 3, 1)));
                } else if(top && bot && lft) {
                    texture.setX(0);
                    texture.setY(1);
                    ret.add(new Tile_Static(cell.x * 16, cell.y * 16 - 16, player.getZIndex()+1, new Texture(tileSheet, 3, 0)));
                } else if(lft && rgt && top) {
//                    boolean wallAbove = cellHasSameNeighbour(filteredCells, cell, 0, 1);
                    texture.setX(1);
                    texture.setY(1);
//                        ret.add(new BoundsObject(cell.x * 16, cell.y * 16 + 16, 16, 16));
                    ret.add(new Tile_Static(cell.x * 16, cell.y * 16 - 16, player.getZIndex()+1, new Texture(tileSheet, 1, 0)));
                } else if(lft && rgt && bot) {
                    texture.setX(0);
                    texture.setY(1);
                    ret.add(new Tile_Static(cell.x * 16, cell.y * 16 - 16, player.getZIndex()+1, new Texture(tileSheet, 4, 0)));
                } else if(lft && rgt) {
                    texture.setX(1);
                    texture.setY(1);
                    // wall
//                    ret.add(new BoundsObject(cell.x * 16, cell.y * 16 - 16, 16, 16));
                    ret.add(new Tile_Static(cell.x * 16, cell.y * 16 - 16, player.getZIndex()+1, new Texture(tileSheet, 1, 0)));
                } else if(top && bot) {
                    texture.setX(0);
                    texture.setY(1);
                } else if(top && rgt) {
                    texture.setX(1);
                    texture.setY(1);
                    ret.add(new Tile_Static(cell.x * 16, cell.y * 16 - 16, player.getZIndex()+1, new Texture(tileSheet, 3, 3)));

                } else if(top && lft) {
                    texture.setX(1);
                    texture.setY(1);
                    ret.add(new Tile_Static(cell.x * 16, cell.y * 16 - 16, player.getZIndex()+1, new Texture(tileSheet, 3, 2)));

                } else if(bot && rgt) {
                    texture.setX(0);
                    texture.setY(1);
                    ret.add(new Tile_Static(cell.x * 16, cell.y * 16 - 16, player.getZIndex()+1, new Texture(tileSheet, 3, 1)));

                } else if(bot && lft) {
                    texture.setX(0);
                    texture.setY(1);
                    ret.add(new Tile_Static(cell.x * 16, cell.y * 16 - 16, player.getZIndex()+1, new Texture(tileSheet, 3, 0)));

                } else if(top) {
                    texture.setX(1);
                    texture.setY(1);
                    ret.add(new Tile_Static(cell.x * 16, cell.y * 16 - 16, player.getZIndex()+1, new Texture(tileSheet, 0, 2)));

                } else if(bot) {
                    texture.setX(0);
                    texture.setY(1);
                    ret.add(new Tile_Static(cell.x * 16, cell.y * 16 - 16, player.getZIndex()+1, new Texture(tileSheet, 0, 0)));

                } else if(lft) {
                    texture.setX(1);
                    texture.setY(1);
                    ret.add(new Tile_Static(cell.x * 16, cell.y * 16 - 16, player.getZIndex()+1, new Texture(tileSheet, 3, 2)));

                } else if(rgt) {
                    texture.setX(1);
                    texture.setY(1);
                    ret.add(new Tile_Static(cell.x * 16, cell.y * 16 - 16, player.getZIndex()+1, new Texture(tileSheet, 3, 3)));

                } else {
                    texture.setX(1);
                    texture.setY(1);
                    ret.add(new Tile_Static(cell.x * 16, cell.y * 16 - 16, player.getZIndex()+1, new Texture(tileSheet, 1, 0)));
                }
                ret.add(new Tile_Static(cell.x * 16, cell.y * 16, player.getZIndex(), texture));
            } else {
                boolean top = cellHasSameNeighbour(filteredCells, cell, 0, -1);
                boolean bot = cellHasSameNeighbour(filteredCells, cell, 0, 1);
                boolean rgt = cellHasSameNeighbour(filteredCells, cell, 1, 0);
                boolean lft = cellHasSameNeighbour(filteredCells, cell, -1, 0);

                Texture texture = new Texture(tileSheet, 13, 1);
                if(top && bot && lft && rgt) {
                    texture.setX(13);
                    texture.setY(1);
                } else if(top && bot && rgt) {
                    texture.setX(12);
                    texture.setY(1);
                } else if(top && bot && lft) {
                    texture.setX(14);
                    texture.setY(1);
                } else if(rgt && lft && top) {
                    texture.setX(13);
                    texture.setY(2);
                } else if(rgt && lft && bot) {
                    texture.setX(13);
                    texture.setY(0);
                } else if(lft && bot) {
                    texture.setX(14);
                    texture.setY(0);
                } else if(top && lft) {
                    texture.setX(14);
                    texture.setY(2);
                } else if(rgt && top) {
                    texture.setX(12);
                    texture.setY(2);
                } else if(bot && rgt) {
                    texture.setX(12);
                    texture.setY(0);
                }
                ret.add(new Tile_Static(cell.x * 16, cell.y * 16, 1, texture));
            }
        }
        return ret;
    }

    private boolean shouldBeTile(Point cell) {
        int neighbourWalls = 0;
        for(Point offset : Offsets.all_offsets) {
            Point offsetCell = new Point(cell.x + offset.x, cell.y + offset.y);
            if(this.cells.containsKey(offsetCell)) {
                if(this.cells.get(offsetCell) == 1) neighbourWalls ++;
            } else {
                neighbourWalls++;
            }
        }
        return neighbourWalls < 8;
    }

    private boolean cellHasSameNeighbour(HashMap<Point, Integer> cells, Point cell, int xOffset, int yOffset) {
        Point offsetCell = new Point(cell.x + xOffset, cell.y + yOffset);
        if(cells.containsKey(offsetCell)) {
            return cells.get(cell).equals(cells.get(offsetCell));
        }
        return false;
    }

    public LinkedList<Point> getConnections() {
        return this.connections;
    }

    public Random getRandom() {
        return this.r;
    }

    public void putCell(int x, int y, boolean isWall) {
        this.cells.put(new Point(x, y), isWall ? 1 : 0);
    }

    public void addExtraStraightHallwayRoom(Room extraRoom, Room roomToExtend, int minOffset, int maxOffset) {
        int roomCenX = roomToExtend.rect.x + roomToExtend.rect.width/2;
        int roomCenY = roomToExtend.rect.y + roomToExtend.rect.height/2;
        int startX = roomCenX - extraRoom.rect.width/2;
        int startY = roomCenY - extraRoom.rect.height/2;
        Rectangle roomRect = null;
        Point finalDirection = null;
        int usedOffset = minOffset;
        for(Point dir : DIRECTIONS) {
            for(int i = minOffset; i < maxOffset; i++) {
                if(dir.x > 0) {
                    startX = roomToExtend.rect.x + roomToExtend.rect.width;
                    startY = roomCenY - extraRoom.rect.height/2;
                } else if(dir.x < 0) {
                    startX = roomToExtend.rect.x - extraRoom.rect.width;
                    startY = roomCenY - extraRoom.rect.height/2;
                } else if(dir.y > 0) {
                    startX = roomCenX - extraRoom.rect.width/2;
                    startY = roomToExtend.rect.y + roomToExtend.rect.height;
                } else if(dir.y < 0) {
                    startX = roomCenX - extraRoom.rect.width/2;
                    startY = roomToExtend.rect.y - extraRoom.rect.height;
                }
                Rectangle tmpRect = new Rectangle(startX + dir.x * i, startY + dir.y *i, extraRoom.rect.width, extraRoom.rect.height);
                boolean collides = false;
                for(Point cell : this.cells.keySet()) {
                    if(tmpRect.contains(cell) && this.cells.containsKey(cell)) {
                        collides = true;
                        break;
                    }
                }
                if(!collides) {
                    roomRect = tmpRect;
                    finalDirection = dir;
                    usedOffset = i;
                    break;
                }
            }
            if(roomRect != null) break;
        }

        // add the room
        Rectangle roomMarginRect = new Rectangle(roomRect.x - 1, roomRect.y - 1, extraRoom.rect.width + 1, extraRoom.rect.height + 1);
        extraRoom.rect.setLocation(roomRect.x, roomRect.y);
        this.rooms.add(extraRoom);
        for(int y = roomMarginRect.y; y <= roomMarginRect.y + roomMarginRect.height; y++) {
            for(int x = roomMarginRect.x; x <= roomMarginRect.x + roomMarginRect.width; x++) {
                if(isOnEdge(new Point(x, y), roomMarginRect)) {
                    this.cells.put(new Point(x, y), 1);
                } else {
                    this.cells.put(new Point(x, y), 0);
                }
            }
        }

        // make the hallway
        Point inverseDirection = new Point(finalDirection.x * -1, finalDirection.y * -1);
        int hallwayStartX = extraRoom.rect.x + extraRoom.rect.width / 2;
        int hallwayStartY = extraRoom.rect.y + extraRoom.rect.height / 2;

        if(inverseDirection.x > 0) {
            hallwayStartX = extraRoom.rect.x + extraRoom.rect.width;
        } else if(inverseDirection.x < 0) {
            hallwayStartX = extraRoom.rect.x - 1;
        } else if(inverseDirection.y > 0) {
            hallwayStartY = extraRoom.rect.y + extraRoom.rect.height;
        } else if(inverseDirection.y < 0) {
            hallwayStartY = extraRoom.rect.y - 1;
        }

        for(int i = 0; i < usedOffset; i++) {
            if(inverseDirection.x == 0) {
                putExtraCellPathPreference(new Point(hallwayStartX - 1, hallwayStartY + inverseDirection.y * i), 1);
                putExtraCellPathPreference(new Point(hallwayStartX, hallwayStartY + inverseDirection.y * i), 0);
                putExtraCellPathPreference(new Point(hallwayStartX + 1, hallwayStartY + inverseDirection.y * i), 1);
            } else {
                putExtraCellPathPreference(new Point(hallwayStartX + inverseDirection.x * i, hallwayStartY - 1), 1);
                putExtraCellPathPreference(new Point(hallwayStartX + inverseDirection.x * i, hallwayStartY), 0);
                putExtraCellPathPreference(new Point(hallwayStartX + inverseDirection.x * i, hallwayStartY + 1), 1);
            }
        }

    }

    private void putExtraCellPathPreference(Point cell, int state) {
        if(this.cells.containsKey(cell)) {
            if(state == 0 && this.cells.get(cell) == 1) {
                this.cells.put(cell, state);
            }
        } else {
            this.cells.put(cell, state);
        }
    }

    public void changeWallHeight(int wallHeight) {
//        LinkedList<Integer> movedLayers = new LinkedList<>();
        for(Point cell : this.cells.keySet()) {
            if(this.cells.get(cell) == 1) {
                boolean top = cellHasSameNeighbour(this.cells, cell, 0, -1);
                boolean bot = cellHasSameNeighbour(this.cells, cell, 0, 1);
                boolean rgt = cellHasSameNeighbour(this.cells, cell, 1, 0);
                boolean lft = cellHasSameNeighbour(this.cells, cell, -1, 0);
                if((!top || !bot) && (lft || rgt)) {
//                    movedLayers.add(cell.y);
                    HashMap<Point, Integer> newCells = new HashMap<>(this.cells);
                    for(Point cell2 : this.cells.keySet()) {
                        if(cell2.y == cell.y) {
                            newCells.put(new Point(cell2.x, cell2.y - 1), this.cells.get(cell2));
//                            newCells.put(cell2, 1);
                        }
                    }
                    this.cells = newCells;
                }
            }
        }
    }
}
