package game.assets.levels.level_1;

import game.assets.entities.enemies.Boss_Enemy;
import game.assets.entities.enemies.Homing_Enemy;
import game.assets.entities.enemies.Shooting_Enemy;
import game.assets.entities.enemies.Shotgun_Enemy;
import game.assets.levels.def.*;
import game.assets.objects.Door;
import game.assets.tiles.Tile_Static;
import game.system.helpers.Helpers;
import game.system.helpers.Logger;
import game.system.systems.gameObject.GameObject;
import game.system.systems.levelGeneration.Room;
import game.textures.TEXTURE_LIST;
import game.textures.Texture;

import java.awt.*;
import java.util.LinkedList;
import java.util.Random;

public class Level_1 extends Level {

    public Level_1() {
        super();
    }

    @Override
    protected void generateLevel(Random rand) {
        this.generator.generate();

        addBossRoom();

        // convert the generation to tiles and objects;
        this.objects.addAll(this.generator.getDungeonInTiles(TEXTURE_LIST.dungeon));
        for (Point connection : this.generator.getConnections()) {
            this.objects.add(new Door(connection.x * 16, connection.y * 16, 10, false));
//            this.objects.add(new Tile_Static(connection.x * 16, connection.y * 16, 10, new Texture(TEXTURE_LIST.dungeon, 6, 7)));
        }

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

        Room bossRoom = new Room(new Rectangle(0, 0, 21, 21));
        this.generator.addExtraStraightHallwayRoom(bossRoom, furthest, 5, 10);
        rooms.remove(bossRoom);
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

        Room itemRoom1 = new Room(new Rectangle(0, 0, 10, 10));
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

        Room itemRoom2 = new Room(new Rectangle(0, 0, 10, 10));
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
//        Room bossRoom = null;
//        double distance = -1;
//        for(Room rm : rooms) {
//            double dist = Helpers.getDistanceBetweenBounds(spawnRoom.rect, spawnRoom.rect);
//            if(dist > distance || distance == -1) {
//                distance = dist;
//                bossRoom = rm;
//            }
//        }
//        rooms.remove(bossRoom);

        for(Room room : rooms) {
            Rectangle rect = convertRectToWorld(room.rect);
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

//        Rectangle bossRect = convertRectToWorld(bossRoom.rect);
//        Point cen = new Point(bossRect.x + bossRect.width / 2, bossRect.y + bossRect.height / 2);
//        this.objects.add(new Boss_Enemy(cen.x, cen.y));

//        for (Room room : this.generator.getRooms()) {
//            if(room != this.generator.getMainRoom()) {
//            Rectangle rect = convertRectToWorld(room.rect);
//            int numEnemies = getRandNumBetw(minEnemies, maxEnemies, r);
//            for (int i = 0; i < numEnemies; i++) {
//                Point pos = getRandomPosInRect(rect, 0, r);
//                switch (r.nextInt(3)) {
//                    case 0 -> this.objects.add(new Shooting_Enemy(pos.x, pos.y));
//                    case 1 -> this.objects.add(new Shotgun_Enemy(pos.x, pos.y));
//                    case 2 -> this.objects.add(new Homing_Enemy(pos.x, pos.y));
//                }
//            }
//            Point pos = getRandomPosInRect(rect, 0, r);
////            this.objects.add(new Shooting_Enemy(pos.x, pos.y));
//            }
//        }
    }

    private Point getRandomPosInRect(Rectangle rect, int margin, Random r) {
        int x = getRandNumBetw(rect.x + margin, rect.x + rect.width - margin, r);
        int y = getRandNumBetw(rect.y + margin, rect.y + rect.height - margin, r);

        return new Point(x, y);
    }

    private int getRandNumBetw(int low, int high, Random r) {
        return r.nextInt(high - low) + low;
    }

    private Rectangle convertRectToWorld(Rectangle rect) {
        return new Rectangle(rect.x * 16, rect.y * 16, rect.width * 16, rect.height * 16);
    }
}
