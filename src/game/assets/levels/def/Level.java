package game.assets.levels.def;

import game.system.helpers.Logger;
import game.system.helpers.Offsets;
import game.system.systems.gameObject.GameObject;

import java.awt.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

public abstract class Level {

    protected int room_count;
    protected HashMap<Point, Room> rooms = new HashMap<>();
    protected Point active_room = new Point(0, 0);

    public Level(int room_count) {
        this.room_count = room_count;
    }

    public abstract void tick();
    public abstract void render(Graphics g);

    public void generate() {
        long seed = new Random().nextLong();
        Logger.print("[seed]: " + seed);
        this.generateRooms(new Random(seed));
    }
    public void generate(Long seed) {
        Logger.print("[seed]: " + seed);
        this.generateRooms(new Random(seed));
    }
    protected abstract void generateRooms(Random rand);

//    public int getRoomKey(Room room) {
//        return rooms.(room);
//    }

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
}
