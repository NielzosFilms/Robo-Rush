package game.system.systems.levelGeneration;

import game.assets.objects.Door;

import java.awt.*;
import java.util.LinkedList;

public class Room {
    public Rectangle rect;
    public LinkedList<Point> connectors;
    public Point connector;
    public LinkedList<Door> doors;
    public String roomType = "normal";

    public Room(Rectangle rect) {
        this.rect = rect;
        this.connectors = new LinkedList<>();
    }

    public Room(Rectangle rect, String roomType) {
        this.rect = rect;
        this.roomType = roomType;
        this.connectors = new LinkedList<>();
    }

    public void setConnectors(LinkedList<Point> connectors) {
        this.connectors = connectors;
    }

    public void clearConnectors() {
        this.connectors = new LinkedList<>();
    }

    public void addConnector(Point connector) {
        this.connectors.push(connector);
    }

    public void setConnector(Point connector) { this.connector = connector; }
}
