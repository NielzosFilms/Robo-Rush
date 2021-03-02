package game.assets.levels.def;

import game.assets.levels.Room_TRBL_Test;
import game.assets.levels.level_1.Room_Test;

import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

public class RoomSelector {
    private int level;
    private HashMap<Point, String> mapping = new HashMap<>();

    public RoomSelector(int level) {
        this.level = level;

        mapping.put(new Point(0, -1), "B");
        mapping.put(new Point(1, 0), "L");
        mapping.put(new Point(0, 1), "T");
        mapping.put(new Point(-1, 0), "R");
    }

    public Room getRoom(RoomSpawner spawner, HashMap<Point, Room> rooms, Random rand) {
        Room room_ret = null;
        switch(level) {
            case 1:
                room_ret = new Room_Test(spawner.location, getRoomType(spawner, rand, rooms));
                break;
        }
        return room_ret;
    }

    public Room getClosingRoom(RoomSpawner spawner, HashMap<Point, Room> rooms, Random rand) {
        Room room_ret = null;
        switch(level) {
            case 1:
                room_ret = new Room_Test(spawner.location, getClosingRoomType(spawner, rand, rooms));
                break;
        }
        return room_ret;
    }

    private ROOM_TYPE getRoomType(RoomSpawner spawner, Random rand, HashMap<Point, Room> rooms) {
        LinkedList<ROOM_TYPE> possible_room_types = new LinkedList<>();

        LinkedList<Point> blocked_offsets = getBlockedOffsets(spawner, rooms);
        LinkedList<Point> required_offsets = getRequiredOffsets(spawner, rooms);

        for(int i=0; i<ROOM_TYPE.values().length; i++) {
            ROOM_TYPE type = ROOM_TYPE.values()[i];
            if(roomHasOffsets(type, required_offsets) && !roomHasOffsets(type, blocked_offsets)) {
                if (type.toString().length() == 2) {
                    possible_room_types.add(type);
                    possible_room_types.add(type);
                    possible_room_types.add(type);
                    possible_room_types.add(type);
                } else if (type.toString().length() == 1) {
                    possible_room_types.add(type);
                    possible_room_types.add(type);
                } else {
                    possible_room_types.add(type);
                }
            }
        }

        return possible_room_types.get(rand.nextInt(possible_room_types.size()));
    }

    private ROOM_TYPE getClosingRoomType(RoomSpawner spawner, Random rand, HashMap<Point, Room> rooms) {
        LinkedList<Point> required_offsets = getRequiredOffsets(spawner, rooms);
        LinkedList<Point> other_offsets = new LinkedList<>();

        for(Point offset : mapping.keySet()) {
            boolean found = false;
            for(Point req_offset: required_offsets) {
                if(offset.x == req_offset.x && offset.y == req_offset.y) {
                    found = true;
                }
            }
            if(!found) other_offsets.add(offset);
        }

        for(int i=0; i<ROOM_TYPE.values().length; i++) {
            ROOM_TYPE type = ROOM_TYPE.values()[i];
            if(roomHasOffsets(type, required_offsets) && !roomHasOffsets(type, other_offsets)) {
                return type;
            }
        }
        return null;
    }

    private LinkedList<Point> getBlockedOffsets(RoomSpawner spawner, HashMap<Point, Room> rooms) {
        LinkedList<Point> blocked_offsets = new LinkedList<>();
        for(Point offset : mapping.keySet()) {
            if(offset.x != spawner.door_direction.x && offset.y != spawner.door_direction.y) {
                Point location = new Point(spawner.location.x + offset.x, spawner.location.y + offset.y);
                if(rooms.containsKey(location)) {
                    Room colliding_room = rooms.get(location);
                    if(!roomHasOffset(colliding_room.room_type, new Point(-offset.x, -offset.y))) {
                        blocked_offsets.add(offset);
                    }
                }
            }

        }
        return blocked_offsets;
    }

    private LinkedList<Point> getRequiredOffsets(RoomSpawner spawner, HashMap<Point, Room> rooms) {
        LinkedList<Point> required_offsets = new LinkedList<>();
        for(Point offset : mapping.keySet()) {
            if(offset.x == spawner.door_direction.x && offset.y == spawner.door_direction.y) continue;

            Point location = new Point(spawner.location.x + offset.x, spawner.location.y + offset.y);
            if(rooms.containsKey(location)) {
                LinkedList<RoomSpawner> room_spawners = rooms.get(location).room_type.getSpawners(location);
                for(RoomSpawner room_spawner : room_spawners) {
                    if(room_spawner.location.x == spawner.location.x && room_spawner.location.y == spawner.location.y) {
                        required_offsets.add(new Point(-room_spawner.door_direction.x, -room_spawner.door_direction.y));
                    }
                }
            }
        }
        System.out.println("extra required offsets: " + required_offsets);
        return required_offsets;
    }

    private boolean roomHasOffsets(ROOM_TYPE type, LinkedList<Point> offsets) {
        LinkedList<RoomSpawner> room_spawners = type.getSpawners(new Point(0, 0));
        int matches = 0;
        if(offsets.size() == 0) return false;
        for(Point offset : offsets) {
            for(RoomSpawner room_spawner : room_spawners) {
                if(offset.x == room_spawner.door_direction.x && offset.y == room_spawner.door_direction.y) {
                    matches++;
                }
            }
        }
        return matches == offsets.size();
    }

    private boolean roomHasOffset(ROOM_TYPE type, Point offset) {
        for(RoomSpawner room_spawner : type.getSpawners(new Point(0, 0))) {
            if(offset.x == room_spawner.door_direction.x && offset.y == room_spawner.door_direction.y) {
                return true;
            }
        }
        return false;
    }
}
