package game.assets.levels;

import game.enums.ID;
import game.system.main.Game;
import game.system.systems.gameObject.Bounds;
import game.system.systems.gameObject.GameObject;

import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

public class RoomDoorTrigger extends GameObject implements Bounds {
//    private static final LinkedList<Point> DOOR_DIRECTIONS = new LinkedList<>(Arrays.asList(
//            new Point(0, -1),
//            new Point(0, 1),
//            new Point(-1, 0),
//            new Point(1, 0)
//    ));

    private Point door_direction;

    public RoomDoorTrigger(int x, int y, Point door_direction) {
        super(x, y, 0, ID.RoomDoorTrigger);
        this.door_direction = door_direction;
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(x, y, 16, 16);
    }

    @Override
    public Rectangle getTopBounds() {
        return null;
    }

    @Override
    public Rectangle getBottomBounds() {
        return null;
    }

    @Override
    public Rectangle getLeftBounds() {
        return null;
    }

    @Override
    public Rectangle getRightBounds() {
        return null;
    }

    @Override
    public void tick() {

    }

    @Override
    public void render(Graphics g) {
        g.setColor(Color.green);
        g.drawRect(x, y, 16, 16);
    }

    public void triggered() {
        System.out.println("triggered");
        Game.gameController.getActiveLevel().gotoRoom(this.door_direction);
    }
}
