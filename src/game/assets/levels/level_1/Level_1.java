package game.assets.levels.level_1;

import game.assets.entities.enemies.Boss_Enemy;
import game.assets.entities.enemies.Homing_Enemy;
import game.assets.entities.enemies.Shooting_Enemy;
import game.assets.entities.enemies.Shotgun_Enemy;
import game.assets.entities.player.Player;
import game.assets.levels.def.*;
import game.assets.objects.Door;
import game.assets.tiles.Tile_Static;
import game.enums.ID;
import game.system.helpers.Helpers;
import game.system.helpers.Logger;
import game.system.main.Game;
import game.system.main.Handler;
import game.system.systems.gameObject.Bounds;
import game.system.systems.gameObject.GameObject;
import game.system.systems.levelGeneration.Room;
import game.textures.TEXTURE_LIST;
import game.textures.Texture;

import java.awt.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Random;

public class Level_1 extends Level {
    Room bossRoom;
    Player player;
    Handler handler;
    Room currentRoom = null;
    boolean bossRoomOpen = false;

    public Level_1() {
        super();
        this.player = Game.gameController.getPlayer();
        this.handler = Game.gameController.getHandler();
    }

    @Override
    public void tick() {
        bossRoomOpen = handler.getObjectsWithIds(ID.Enemy).size() == 0;
        var doors = handler.getObjectsWithIds(ID.Door);
        Door bossDoor = null;
        for(Room rm : this.generator.getRooms()) {
            if(!Objects.equals(rm.roomType, "boss")) continue;
            Rectangle roomWorld = Helpers.marginRectangle(Helpers.convertRectToWorld(rm.rect), 16);
            for(GameObject _door : doors) {
                Door door = (Door) _door;
                if(roomWorld.contains(new Point(door.getX() + 8, door.getY() + 8))) {
                    bossDoor = door;
                    break;
                }
            }
        }
        if(bossDoor != null) {
            bossDoor.setLocked(!bossRoomOpen);
        }

        Rectangle playerBounds = this.player.getBounds();
        for(Room rm : this.generator.getRooms()) {
            Rectangle roomWorld = Helpers.convertRectToWorld(rm.rect);
            if(roomWorld.contains(playerBounds)) {
                currentRoom = rm;
                break;
            } else {
                currentRoom = null;
            }
        }

        if(currentRoom != null) {
            Rectangle roomWorld = Helpers.convertRectToWorld(currentRoom.rect);
            roomWorld = Helpers.marginRectangle(roomWorld, 16);

            for(GameObject _door : doors) {
                Door door = (Door) _door;
                if(roomWorld.contains(new Point(door.getX() + 8, door.getY() + 8))) {
                    if(roomHasEnemies(currentRoom, true)) {
                        door.close();
                        door.setLocked(true);
                    } else {
                        door.setLocked(false);
                        if(door.isDiscovered()) door.open();
                    }
                }
            }
        }
    }

    @Override
    protected void generateLevel(Random rand) {
        this.generator.generate();

        addBossRoom();

//        this.generator.changeWallHeight(2);

        this.objects.addAll(this.generator.getDungeonInTiles(TEXTURE_LIST.dungeon));

        this.objects.add(new Boss_Enemy((bossRoom.rect.x + bossRoom.rect.width / 2) * 16, (bossRoom.rect.y + bossRoom.rect.height / 2) * 16));

        addDoors();

        addEnemies();
    }

    private void addBossRoom() {
        LinkedList<Room> rooms = new LinkedList<>(this.generator.getRooms());
        Room spawnRoom = this.generator.getMainRoom();
        rooms.remove(spawnRoom);

        Room furthest = null;
        double distance = -1;
        // get the furthest room from spawn
        for(Room rm : rooms) {
            double dist = Helpers.getDistanceBetweenBounds(spawnRoom.rect, rm.rect);
            if(dist > distance) {
                distance = dist;
                furthest = rm;
            }
        }
        rooms.remove(furthest);

        this.bossRoom = new Room(new Rectangle(0, 0, 21, 21), "boss");
        this.generator.addExtraStraightHallwayRoom(this.bossRoom, furthest, 5, 10);
        rooms.remove(this.bossRoom);
        distance = -1;
        furthest = null;

        // get the furthest room from spawn
        for(Room rm : rooms) {
            double dist = Helpers.getDistanceBetweenBounds(spawnRoom.rect, rm.rect);
            if(dist > distance) {
                distance = dist;
                furthest = rm;
            }
        }
        rooms.remove(furthest);

        Room itemRoom1 = new Room(new Rectangle(0, 0, 10, 10), "item");
        this.generator.addExtraStraightHallwayRoom(itemRoom1, furthest, 5, 10);
        rooms.remove(itemRoom1);
        distance = -1;
        furthest = null;

        // get the furthest room from spawn
        for(Room rm : rooms) {
            double dist = Helpers.getDistanceBetweenBounds(itemRoom1.rect, rm.rect);
            if(dist > distance) {
                distance = dist;
                furthest = rm;
            }
        }

        Room itemRoom2 = new Room(new Rectangle(0, 0, 10, 10), "item");
        this.generator.addExtraStraightHallwayRoom(itemRoom2, furthest, 5, 10);
        rooms.remove(itemRoom2);
        distance = -1;
        furthest = null;
    }

    private void addEnemies() {
        Random r = this.generator.getRandom();
        int maxEnemies = 5;
        int minEnemies = 1;

        LinkedList<Room> rooms = new LinkedList<Room>(this.generator.getRooms());
        Room spawnRoom = this.generator.getMainRoom();
        rooms.remove(spawnRoom);

        for(Room room : rooms) {
            if(!Objects.equals(room.roomType, "normal")) continue;
            Rectangle rect = Helpers.convertRectToWorld(room.rect);
            int numEnemies = getRandNumBetw(minEnemies, maxEnemies, r);
            for (int i = 0; i < numEnemies; i++) {
                Point pos = getRandomPosInRect(rect, 0, r);
                switch (r.nextInt(3)) {
                    case 0 -> this.objects.add(new Shooting_Enemy(pos.x, pos.y));
                    case 1 -> this.objects.add(new Shotgun_Enemy(pos.x, pos.y));
                    case 2 -> this.objects.add(new Homing_Enemy(pos.x, pos.y));
                }
            }
        }
    }

    private void addDoors() {
        HashMap<Point, Integer> cells = this.generator.getDungeonInCells();
        for(Room rm : this.generator.getRooms()) {
            for(int x = rm.rect.x; x < rm.rect.x + rm.rect.width; x++) {
                Point top = new Point(x, rm.rect.y-1);
                Point bot = new Point(x, rm.rect.y + rm.rect.height);
                if(cells.containsKey(top) && cells.get(top) == 0) {
                    Door door = new Door(top.x * 16, top.y * 16, 10, false);
                    door.setLocked(rm.roomType == "boss");
                    this.objects.add(door);
                }
                if(cells.containsKey(bot) && cells.get(bot) == 0) {
                    Door door = new Door(bot.x * 16, bot.y * 16, 10, false);
                    door.setLocked(rm.roomType == "boss");
                    this.objects.add(door);
                }
            }
            for(int y = rm.rect.y; y < rm.rect.y + rm.rect.height; y++) {
                Point lft = new Point(rm.rect.x-1, y);
                Point rgt = new Point(rm.rect.x + rm.rect.width, y);
                if(cells.containsKey(lft) && cells.get(lft) == 0) {
                    Door door = new Door(lft.x * 16, lft.y * 16, 10, true);
                    door.setLocked(rm.roomType == "boss");
                    this.objects.add(door);
                }
                if(cells.containsKey(rgt) && cells.get(rgt) == 0) {
                    Door door = new Door(rgt.x * 16, rgt.y * 16, 10, true);
                    door.setLocked(rm.roomType == "boss");
                    this.objects.add(door);
                }
            }
        }
    }

    private Point getRandomPosInRect(Rectangle rect, int margin, Random r) {
        int x = getRandNumBetw(rect.x + margin, rect.x + rect.width - margin, r);
        int y = getRandNumBetw(rect.y + margin, rect.y + rect.height - margin, r);

        return new Point(x, y);
    }

    private int getRandNumBetw(int low, int high, Random r) {
        return r.nextInt(high - low) + low;
    }

    private boolean roomHasEnemies(Room rm, boolean includeBoss) {
        var enemies = includeBoss ? handler.getObjectsWithIds(ID.Enemy, ID.Boss) : handler.getObjectsWithIds(ID.Enemy);
        Rectangle roomWorld = Helpers.convertRectToWorld(rm.rect);
        roomWorld = Helpers.marginRectangle(roomWorld, 16);
        boolean currRoomHasEnemies = false;
        for(GameObject enemy : enemies) {
            if(roomWorld.contains(new Point(enemy.getX(), enemy.getY()))) {
                currRoomHasEnemies = true;
                break;
            }
        }
        return currRoomHasEnemies;
    }
}
