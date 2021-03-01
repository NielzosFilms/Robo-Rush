package game.assets.levels.def;

import game.enums.DIRECTIONS;

import java.awt.*;

public class RoomSpawner {
    public Point door_direction;
    public Point location;

    public RoomSpawner(Point location, Point door_direction) {
        this.door_direction = door_direction;
        this.location = location;
    }

}
