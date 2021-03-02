package game.assets.levels.level_1;

import game.assets.levels.Room_TRBL_Test;
import game.assets.levels.def.*;
import game.system.helpers.Logger;

import java.awt.*;
import java.util.LinkedList;
import java.util.Random;

public class Level_1 extends Level {
    private RoomSelector roomSelector;

    public Level_1() {
        super(13);
        this.roomSelector = new RoomSelector(1);
    }

    @Override
    public void tick() {
        getActiveRoom().tick();
    }

    @Override
    public void render(Graphics g) {
        for(Point key : rooms.keySet()) {
            rooms.get(key).render(g);
        }
    }

    @Override
    public void generateRooms(Random rand) {
        rooms.put(new Point(0, 0), new Room_TRBL_Test(new Point(0, 0)));
        LinkedList<RoomSpawner> spawners = new LinkedList<>(rooms.get(new Point(0, 0)).getSpawners());

        while(rooms.size() < room_count) {
            LinkedList<RoomSpawner> new_spawners = new LinkedList<>(spawners);
            for (RoomSpawner spawner : spawners) {
                if (!this.roomExists(spawner.location) && rooms.size() < room_count) {
                    ROOM_TYPE room_type = roomSelector.getRoomType(spawner, rand, rooms);
                    rooms.put(spawner.location, new Room_Test(spawner.location, room_type));
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
                rooms.put(room_spawner.location, new Room_Test(room_spawner.location, room_type));
            }
        }

        Logger.printRoomMatrix(rooms, room_count, spawners);
    }
}
