package game.assets.levels.level_1;

import game.assets.levels.def.*;
import game.system.helpers.Helpers;
import game.system.helpers.Logger;

import java.awt.*;
import java.util.LinkedList;
import java.util.Random;

public class Level_1 extends Level {
    private RoomSelector roomSelector;

    private int room_count;

    public Level_1() {
        this.room_count = 8;

        this.roomSelector = new RoomSelector();
    }

    @Override
    public void tick() {
        getActiveRoom().tick();
//        if(rooms.size() == getDiscoveredRoomCount()) {
//            System.out.println("Level discovered!");
//        }
    }

    @Override
    public void render(Graphics g) {
        for(Point key : rooms.keySet()) {
            rooms.get(key).render(g);
        }
    }

    @Override
    public void generateRooms(Random rand) {
        Point origin = new Point(0, 0);
        rooms.put(origin, new Room_Spawn(origin));
        LinkedList<RoomSpawner> spawners = new LinkedList<>(rooms.get(origin).getSpawners());

        this.active_room = origin;

        while(rooms.size() < room_count) {
            LinkedList<RoomSpawner> new_spawners = new LinkedList<>(spawners);
            for (RoomSpawner spawner : spawners) {
                if (!this.roomExists(spawner.location) && rooms.size() < room_count) {
                    ROOM_TYPE room_type = roomSelector.getRoomType(spawner, rand, rooms);
                    rooms.put(spawner.location, new Room_Basic(spawner.location, room_type));
                    new_spawners.remove(spawner);

                    for (RoomSpawner room_spawner : rooms.get(spawner.location).getSpawners()) {
                        if (!roomExists(room_spawner.location)) {
                            new_spawners.add(room_spawner);
                        }
                    }
                }
            }
            if(spawners == new_spawners) break;
            spawners = new_spawners;
        }

        /*
         * Enclose dungeon rooms
         */
        for(RoomSpawner room_spawner : spawners) {
            if(!this.roomExists(room_spawner.location)) {
                ROOM_TYPE room_type = roomSelector.getClosingRoomType(room_spawner, rooms);
                rooms.put(room_spawner.location, new Room_Basic(room_spawner.location, room_type));
            }
        }


        createKeyRoom(origin);

        createTreasureRoom(origin);
        createTreasureRoom(origin);

        createBossRoom(origin);

        Logger.printRoomMatrix(rooms, room_count, spawners);
    }

    private void createBossRoom(Point origin) {
        Point best_boss_room_key = null;
        double dist = 0;
        for(Point room_key : rooms.keySet()) {
            Room room = rooms.get(room_key);
            if(room.getRoomType().toString().length() != 1) continue;
            double temp_dist = Helpers.getDistance(origin, room_key);
            if(temp_dist > dist && room instanceof Room_Basic) {
                dist = temp_dist;
                best_boss_room_key = room_key;
            }
        }
        Room_Boss boss_room = new Room_Boss(best_boss_room_key, rooms.get(best_boss_room_key).getRoomType());
        rooms.put(best_boss_room_key, boss_room);

        // update room before to use key
        Point before_offset = boss_room.getRoomType().getSpawners(best_boss_room_key).get(0).door_direction;
        Point room_before = new Point(best_boss_room_key.x + before_offset.x, best_boss_room_key.y + before_offset.y);
        rooms.get(room_before).getDoor(new Point(-before_offset.x, -before_offset.y)).setNeedsKey(true);
    }

    private void createTreasureRoom(Point origin) {
        LinkedList<Point> single_door_keys = new LinkedList<>();
        for(Point room_key : rooms.keySet()) {
            Room room = rooms.get(room_key);
            if((room.getRoomType().toString().length() == 1 || room.getRoomType().toString().length() == 2) && room instanceof Room_Basic) {
                if(room.getRoomType().toString().length() == 1) {
                    single_door_keys.add(room_key);
                    single_door_keys.add(room_key);
                    single_door_keys.add(room_key);
                    single_door_keys.add(room_key);
                } else {
                    single_door_keys.add(room_key);
                }
            }
        }
        Point room_key = single_door_keys.get(new Random().nextInt(single_door_keys.size()));
        Room_Treasure treasure_room = new Room_Treasure(room_key, rooms.get(room_key).getRoomType());
        rooms.put(room_key, treasure_room);
    }

    private void createKeyRoom(Point origin) {
        LinkedList<Point> single_door_keys = new LinkedList<>();
        for(Point room_key : rooms.keySet()) {
            Room room = rooms.get(room_key);
            if(room.getRoomType().toString().length() == 1 && room instanceof Room_Basic) {
                single_door_keys.add(room_key);
            }
        }
        Point room_key = single_door_keys.get(new Random().nextInt(single_door_keys.size()));
        Room_Key key_room = new Room_Key(room_key, rooms.get(room_key).getRoomType());
        rooms.put(room_key, key_room);
    }
}
