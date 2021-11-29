package game.system.systems.levelGeneration;

import java.awt.*;
import java.util.LinkedList;

public class Room {
    public Rectangle rect;
    public LinkedList<Point> connectors;
    public Point connector;

    public Room(Rectangle rect) {
        this.rect = rect;
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
