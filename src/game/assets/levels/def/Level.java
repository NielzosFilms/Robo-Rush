package game.assets.levels.def;

import game.system.systems.gameObject.GameObject;

import java.awt.*;
import java.util.LinkedList;
import java.util.Random;

public abstract class Level {

    protected int room_count;
    protected LinkedList<Room> rooms = new LinkedList<>();
    protected int active_room = 0;

    public Level(int room_count) {
        this.room_count = room_count;
    }

    public abstract void tick();
    public abstract void render(Graphics g);

    public void generate() {
        this.generateRooms(new Random());
    }
    public void generate(Long seed) {
        this.generateRooms(new Random(seed));
    }
    protected abstract void generateRooms(Random rand);

    public int getRoomIndex(Room room) {
        return rooms.indexOf(room);
    }

    public void setActiveRoomIndex(int active_room) {
        this.active_room = active_room;
    }
    public int getActiveRoomIndex() {
        return active_room;
    }

    public Room getActiveRoom() {
        return rooms.get(active_room);
    }

    public LinkedList<LinkedList<GameObject>> getObjects() {
        return getActiveRoom().getObjects();
    }
}
