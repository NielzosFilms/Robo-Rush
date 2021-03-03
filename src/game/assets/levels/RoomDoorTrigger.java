package game.assets.levels;

import game.assets.levels.def.Room;
import game.enums.ID;
import game.system.helpers.JsonLoader;
import game.system.helpers.Logger;
import game.system.helpers.StructureLoaderHelpers;
import game.system.main.Game;
import game.system.systems.gameObject.Bounds;
import game.system.systems.gameObject.GameObject;
import org.json.simple.JSONObject;

import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

public class RoomDoorTrigger extends GameObject implements Bounds {
    private static final HashMap<String, Point> MAPPING = new HashMap<String, Point>() {{
        put("N", new Point(0, -1));
        put("E", new Point(1, 0));
        put("S", new Point(0, 1));
        put("W", new Point(-1, 0));
    }};

    private Point door_direction;

    public RoomDoorTrigger(int x, int y, Point door_direction) {
        super(x, y, 0, ID.RoomDoorTrigger);
        this.door_direction = door_direction;
    }

    public RoomDoorTrigger(JSONObject json, int z_index, int division, JsonLoader loader) {
        super(
                StructureLoaderHelpers.getIntProp(json, "x") / division,
                StructureLoaderHelpers.getIntProp(json, "y") / division,
                z_index, ID.RoomDoorTrigger);

        this.width = StructureLoaderHelpers.getIntProp(json, "width") / division;
        this.height = StructureLoaderHelpers.getIntProp(json, "height") / division;

        String direction = StructureLoaderHelpers.getCustomProp(json, "direction");
        this.door_direction = MAPPING.get(direction);
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(this.x, this.y, this.width, this.height);
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
        g.setColor(Color.magenta);
        g.drawRect(this.x, this.y, this.width, this.height);
    }

    public void triggered() {
        Logger.print("[DOOR TRIGGERED] >> " + door_direction);

        Point current_room = Game.gameController.getActiveLevel().getActiveRoomKey();
        Point next_room_key = new Point(current_room.x + door_direction.x, current_room.y + door_direction.y);
        Room next_room = Game.gameController.getActiveLevel().getRooms().get(next_room_key);

        RoomDoorTrigger exit_door = getExitDoor(next_room);
        Point offset = new Point(48*door_direction.x, 48*door_direction.y);
        Point exit_position = new Point(
                (int) exit_door.getBounds().getCenterX() + offset.x,
                (int) exit_door.getBounds().getCenterY() + offset.y);
        Game.gameController.updatePlayerPosition(exit_position.x, exit_position.y);

        Game.gameController.getActiveLevel().gotoRoom(this.door_direction);
    }

    private RoomDoorTrigger getExitDoor(Room next_room) {
        for(LinkedList<GameObject> layer : next_room.getObjects()) {
            for(GameObject object : layer) {
                if(object instanceof RoomDoorTrigger) {
                    RoomDoorTrigger temp = (RoomDoorTrigger) object;
                    if(temp.getDoorDirection().equals(new Point(-door_direction.x, -door_direction.y))) {
                        return temp;
                    }
                }
            }
        }
        return this;
    }

//    private Point getPlayerExitPosition() {
//
//    }

    public Point getDoorDirection() {
        return door_direction;
    }
}
