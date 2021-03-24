package game.assets.levels.def;

import game.system.helpers.Logger;
import game.system.helpers.Offsets;
import game.system.systems.gameObject.GameObject;

import java.awt.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

public abstract class Level {
    protected long level_seed;
    protected HashMap<Point, Room> rooms = new HashMap<>();
    protected Point active_room = new Point(0, 0);

    public Level() {
    }

    public abstract void tick();
    public abstract void render(Graphics g);

    public void generate() {
        this.level_seed = new Random().nextLong();
        Logger.print("[seed]: " + level_seed);
        this.generateRooms(new Random(level_seed));
    }
    public void generate(Long level_seed) {
        this.level_seed = level_seed;
        Logger.print("[seed]: " + level_seed);
        this.generateRooms(new Random(level_seed));
    }
    protected abstract void generateRooms(Random rand);

    public void gotoRoom(Point door_direction) {
        active_room = new Point(active_room.x + door_direction.x, active_room.y + door_direction.y);
    }

    public void setActiveRoomKey(Point point) {
        this.active_room = point;
    }
    public Point getActiveRoomKey() {
        return active_room;
    }

    public Room getActiveRoom() {
        return rooms.get(active_room);
    }

    public LinkedList<LinkedList<GameObject>> getObjects() {
        return getActiveRoom().getObjects();
    }

    protected boolean roomExists(Point coords) {
        return rooms.containsKey(coords);
    }

    protected Point getRandomEdgeOffset(Random rand) {
        return Offsets.edge_offsets[rand.nextInt(Offsets.edge_offsets.length)];
    }

    protected Point invertOffset(Point offset) {
        return new Point(-offset.x, -offset.y);
    }

    public HashMap<Point, Room> getRooms() {
        return rooms;
    }

    public void openDoors() {
        this.getActiveRoom().openDoors();
    }

    public void closeDoors() {
        this.getActiveRoom().closeDoors();
    }

    public int getDiscoveredRoomCount() {
        int count = 0;
        for(Point key : rooms.keySet()) {
            if(rooms.get(key).isDiscovered()) count++;
        }
        return count;
    }
}
