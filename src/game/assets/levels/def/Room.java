package game.assets.levels.def;

import game.system.systems.gameObject.GameObject;

import java.awt.*;
import java.util.LinkedList;

public abstract class Room {
    protected LinkedList<LinkedList<GameObject>> objects = new LinkedList<>();
    protected Level parent_level;
    protected Point location;
    protected ROOM_TYPE room_type;

    public Room(Point location) {
        this.location = location;
    }

    public abstract void tick();
    public abstract void render(Graphics g);

    public LinkedList<LinkedList<GameObject>> getObjects(){
        return objects;
    }

    public void addObject(GameObject object) {
        int z_index = object.getZIndex();
        for(int i=objects.size(); i<=z_index; i++) {
            this.objects.add(new LinkedList<>());
        }
        this.objects.get(z_index).add(object);
    }

    public void removeObject(GameObject object) {
        objects.get(object.getZIndex()).remove(object);
    }

    public LinkedList<RoomSpawner> getSpawners() {
        return this.room_type.getSpawners(location);
    }

    public ROOM_TYPE getRoomType() {
        return room_type;
    }

    public void setRoomType(ROOM_TYPE room_type) {
        this.room_type = room_type;
    }
}