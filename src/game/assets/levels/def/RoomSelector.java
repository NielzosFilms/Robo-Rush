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
        // TODO remove occupied directions

        Room room_ret = null;
        switch(level) {
            case 1:
//                if(spawner.door_direction.equals(new Point(0, 1)) || spawner.door_direction.equals(new Point(0, -1))) {
//                    room_ret = new Room_Test(spawner.location, ROOM_TYPE.TB);
//                } else if(spawner.door_direction.equals(new Point(1, 0)) || spawner.door_direction.equals(new Point(-1, 0))) {
//                    room_ret = new Room_Test(spawner.location, ROOM_TYPE.LR);
//                }
                room_ret = new Room_Test(spawner.location, getRoomType(spawner, rand, rooms));
                break;
        }
        return room_ret;
    }

    private ROOM_TYPE getRoomType(RoomSpawner spawner, Random rand, HashMap<Point, Room> rooms) {
        LinkedList<ROOM_TYPE> possible_room_types = new LinkedList<>();

        LinkedList<Point> blocked_offsets = getBlockedOffsets(spawner, rooms);
        LinkedList<Point> extra_required_offsets = getExtraRequiredOffsets(spawner, rooms);
        extra_required_offsets.add(spawner.door_direction);

        for(int i=0; i<ROOM_TYPE.values().length; i++) {
            ROOM_TYPE type = ROOM_TYPE.values()[i];
            if(checkRoomType(type, spawner.door_direction)) {
                if(!isOffsetBlocked(blocked_offsets, type)) {
//                    if(extra_required_offsets.size() > 0) {
//                        if(!roomHasOffset(type, extra_required_offsets)) {
//                            continue;
//                        }
//                    }
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
        }

        return possible_room_types.get(rand.nextInt(possible_room_types.size()));
    }

    private LinkedList<Point> getBlockedOffsets(RoomSpawner spawner, HashMap<Point, Room> rooms) {
        LinkedList<Point> blocked_offsets = new LinkedList<>();
        for(Point offset : mapping.keySet()) {
            if(offset.x != spawner.door_direction.x && offset.y != spawner.door_direction.y) {
                Point location = new Point(spawner.location.x + offset.x, spawner.location.y + offset.y);
                if(rooms.containsKey(location)) {
                    blocked_offsets.add(offset);
                }
            }

        }
        return blocked_offsets;
    }

    private LinkedList<Point> getExtraRequiredOffsets(RoomSpawner spawner, HashMap<Point, Room> rooms) {
        LinkedList<Point> required_offsets = new LinkedList<>();
        for(Point offset : mapping.keySet()) {
            if(offset.x != spawner.door_direction.x && offset.y != spawner.door_direction.y) {
                Point location = new Point(spawner.location.x + offset.x, spawner.location.y + offset.y);
                if(rooms.containsKey(location)) {
                    LinkedList<RoomSpawner> room_spawners = rooms.get(location).room_type.getSpawners(location);
                    for(RoomSpawner room_spawner : room_spawners) {
                        if(room_spawner.location.x == spawner.location.x && room_spawner.location.y == -spawner.location.y) {
                            required_offsets.add(new Point(-room_spawner.door_direction.x, -room_spawner.door_direction.y));
                        }
                    }
                }
            }
        }
        System.out.println(required_offsets);
        return required_offsets;
    }

    private boolean roomHasOffset(ROOM_TYPE type, LinkedList<Point> offsets) {
        LinkedList<RoomSpawner> room_spawners = type.getSpawners(new Point(0, 0));
        for(Point offset : offsets) {
            for(RoomSpawner room_spawner : room_spawners) {
                if(offset.x == room_spawner.door_direction.x && offset.y == room_spawner.door_direction.y) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isOffsetBlocked(LinkedList<Point> blocked_offsets, ROOM_TYPE type) {
        LinkedList<RoomSpawner> room_spawners = type.getSpawners(new Point(0, 0));
        for(Point blocked_offset : blocked_offsets) {
            for(RoomSpawner room_spawner : room_spawners) {
                if(blocked_offset.x == room_spawner.door_direction.x && blocked_offset.y == room_spawner.door_direction.y) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkRoomType(ROOM_TYPE room_type, Point door_direction) {
        LinkedList<RoomSpawner> room_spawners = room_type.getSpawners(new Point(0, 0));
        for(RoomSpawner room_spawner : room_spawners) {
            if(room_spawner.door_direction.x == -door_direction.x && room_spawner.door_direction.y == -door_direction.y) {
                return true;
            }
        }
        return false;
    }
}
