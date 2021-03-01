package game.assets.levels.def;

import game.enums.DIRECTIONS;

import java.awt.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

public class Head {
    private HashMap<DIRECTIONS, Point> offsets = new HashMap<>();
    private Point head;
    private DIRECTIONS direction;

    public Head(Point head, DIRECTIONS direction) {
        offsets.put(DIRECTIONS.down, new Point(0, -1));
        offsets.put(DIRECTIONS.left, new Point(1, 0));
        offsets.put(DIRECTIONS.up, new Point(0, 1));
        offsets.put(DIRECTIONS.right, new Point(-1, 0));
        this.head = head;
        this.direction = direction;
        offsets.remove(direction);
    }

    public Head(Point head, Random rand) {
        offsets.put(DIRECTIONS.down, new Point(0, -1));
        offsets.put(DIRECTIONS.left, new Point(1, 0));
        offsets.put(DIRECTIONS.up, new Point(0, 1));
        offsets.put(DIRECTIONS.right, new Point(-1, 0));
        this.head = head;

        LinkedList<DIRECTIONS> keys = new LinkedList<>(offsets.keySet());
        this.direction = keys.get(rand.nextInt(4));
        offsets.remove(direction);
    }

    public Point getNextHead(Random rand) {
        LinkedList<DIRECTIONS> keys = new LinkedList<>(offsets.keySet());
        Point offset = offsets.get(keys.get(rand.nextInt(3)));
        return new Point(head.x + offset.x, head.y + offset.y);
    }

    public void setHead(Point head) {
        this.head = head;
    }

    public Point getHead() {
        return head;
    }
}
