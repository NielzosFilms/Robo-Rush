package game.assets.levels.def;

import game.assets.levels.RoomDoorTrigger;
import game.enums.ID;
import game.system.systems.gameObject.GameObject;
import game.system.systems.gameObject.Trigger;

import java.awt.*;
import java.util.LinkedList;

public abstract class Room {
    protected LinkedList<LinkedList<GameObject>> objects = new LinkedList<>();
    protected LinkedList<LinkedList<GameObject>> staged_waves = new LinkedList<>();
    protected Level parent_level;
    protected Point location;
    protected ROOM_TYPE room_type;
    protected int current_wave = 0, next_wave_threshold = 1;

    public Room(Point location) {
        this.location = location;
    }

    public abstract void tick();
    public abstract void render(Graphics g);
    public abstract void drawRoomMiniMap(Graphics g, int x, int y, int room_size, boolean active);

    public void spawnNextWave() {
        if(current_wave < staged_waves.size()) {
            for(GameObject enemy : staged_waves.get(current_wave)) {
                addObject(enemy);
            }
            current_wave++;
        }
    }

    public LinkedList<LinkedList<GameObject>> getObjects(){
        return objects;
    }

    public void addObject(GameObject object) {
        int z_index = object.getZIndex();
        for(int i=objects.size(); i<=z_index; i++) {
            this.objects.add(new LinkedList<>());
        }
        try {
            if(z_index == 0 && !(object instanceof Trigger)) throw new Exception("The z_index 0 in rooms is meant for trigger objects.");
            this.objects.get(z_index).add(object);
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    public void openDoors() {
        setDoors(true);
    }

    public void closeDoors() {
        setDoors(false);
    }

    private void setDoors(boolean door_state) {
        int door_count = 0;
        for(GameObject object : objects.get(0)) {
            if(object instanceof RoomDoorTrigger) {
                if(!((RoomDoorTrigger) object).needsKey()) {
                    ((RoomDoorTrigger) object).setOpen(door_state);
                }
                door_count++;
            }
            if(door_count >= room_type.toString().length()) return;
        }
    }

    public void addEnemyToWave(int wave, GameObject enemy) {
        for(int i=staged_waves.size(); i<=wave; i++) {
            this.staged_waves.add(new LinkedList<>());
        }
        this.staged_waves.get(wave).add(enemy);
    }

    public RoomDoorTrigger getDoor(Point door_direction) {
        for(GameObject object : objects.get(0)) {
            if(object instanceof RoomDoorTrigger) {
                if(((RoomDoorTrigger) object).getDoorDirection().equals(door_direction)) {
                    return (RoomDoorTrigger)object;
                }
            }
        }
        return null;
    }

    public int currentEnemyCount() {
        int count = 0;
        for(LinkedList<GameObject> layer : objects) {
            for (GameObject object : layer) {
                if(object.getId() == ID.Enemy || object.getId() == ID.Boss) count++;
            }
        }
        return count;
    }

    public boolean nextWaveExists() {
        return current_wave + 1 < staged_waves.size();
    }
}
