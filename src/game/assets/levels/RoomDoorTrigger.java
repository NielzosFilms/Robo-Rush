package game.assets.levels;

import game.assets.entities.player.Player;
import game.assets.levels.def.Room;
import game.enums.ID;
import game.enums.ITEM_ID;
import game.system.helpers.JsonLoader;
import game.system.helpers.Logger;
import game.system.helpers.StructureLoaderHelpers;
import game.system.main.Game;
import game.system.systems.gameObject.Bounds;
import game.system.systems.gameObject.GameObject;
import game.system.systems.gameObject.Trigger;
import game.textures.Fonts;
import org.json.simple.JSONObject;

import java.awt.*;
import java.util.HashMap;
import java.util.LinkedList;

public class RoomDoorTrigger extends GameObject implements Bounds, Trigger {
    private static final HashMap<String, Point> MAPPING = new HashMap<String, Point>() {{
        put("N", new Point(0, -1));
        put("E", new Point(1, 0));
        put("S", new Point(0, 1));
        put("W", new Point(-1, 0));
    }};

    private Point door_direction;

    private boolean open = true;
    private boolean trigger_active = true;

    private boolean need_key = false;
    private int need_key_id = 0;

    public RoomDoorTrigger(int x, int y, Point door_direction) {
        super(x, y, 0, ID.RoomDoorTrigger);
        this.door_direction = door_direction;
    }

    public RoomDoorTrigger(JSONObject json, int z_index, int division, JsonLoader loader) {
        super(
                StructureLoaderHelpers.getIntProp(json, "x") / division,
                StructureLoaderHelpers.getIntProp(json, "y") / division,
                0, ID.RoomDoorTrigger);

        this.width = StructureLoaderHelpers.getIntProp(json, "width") / division;
        this.height = StructureLoaderHelpers.getIntProp(json, "height") / division;

        this.need_key = Boolean.parseBoolean(StructureLoaderHelpers.getCustomProp(json, "need_key"));
        this.need_key_id = Integer.parseInt(StructureLoaderHelpers.getCustomProp(json, "need_key_id"));

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
        if(this.need_key) {
            this.open = Game.gameController.getPlayer().hasItem(ITEM_ID.key);
        }
    }

    @Override
    public void render(Graphics g) {
        g.setColor(Color.magenta);
        g.drawRect(this.x, this.y, this.width, this.height);
        g.setFont(Fonts.default_fonts.get(5));
        g.drawString(String.valueOf(open), this.x, this.y + 8);
    }

    @Override
    public void triggered(Player player) {
//        Logger.print("[DOOR TRIGGERED] >> " + door_direction);

//        if(this.need_key) {
//            if(!Game.gameController.getPlayer().hasKey(this.need_key_id)) return;
//        }

        Point current_room = Game.gameController.getActiveLevel().getActiveRoomKey();
        Point next_room_key = new Point(current_room.x + door_direction.x, current_room.y + door_direction.y);
        Room next_room = Game.gameController.getActiveLevel().getRooms().get(next_room_key);

        RoomDoorTrigger exit_door = getExitDoor(next_room);
        exit_door.setTriggerActive(false);
        Point offset = new Point(16*door_direction.x - 8, 16*door_direction.y - 12);
        //Point exit_position = getExitPosition(exit_door.getBounds(), offset);

        Point exit_position = new Point((int)exit_door.getBounds().getCenterX() - 8, (int)exit_door.getBounds().getCenterY() - 12);

        Game.gameController.updatePlayerPosition(exit_position.x, exit_position.y);
        Game.gameController.getActiveLevel().gotoRoom(this.door_direction);
        Game.gameController.getActiveLevel().getActiveRoom().setDiscovered(true);

        if(this.need_key) {
            Game.gameController.getPlayer().removeItemKey(this.need_key_id);
            this.need_key = false;
        }
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

    private Point getExitPosition(Rectangle exit_door_bounds, Point offset) {
        int exit_x = 0;
        int exit_y = 0;
        if(new Point(0, -1).equals(door_direction)) {
            exit_x = (int) exit_door_bounds.getCenterX() + offset.x;
            exit_y = (int) (exit_door_bounds.getY() + offset.y);
        } else if(new Point(0, 1).equals(door_direction)) {
            exit_x = (int) exit_door_bounds.getCenterX() + offset.x;
            exit_y = (int) (exit_door_bounds.getY() + exit_door_bounds.getHeight() + offset.y);
        } else if(new Point(-1, 0).equals(door_direction)) {
            exit_x = (int) exit_door_bounds.getX() + offset.x;
            exit_y = (int) exit_door_bounds.getCenterY() + offset.y;
        } else if(new Point(1, 0).equals(door_direction)) {
            exit_x = (int) (exit_door_bounds.getX() + exit_door_bounds.getHeight() + offset.x);
            exit_y = (int) exit_door_bounds.getCenterY() + offset.y;
        }
        return new Point(exit_x, exit_y);
    }

    public Point getDoorDirection() {
        return door_direction;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    @Override
    public boolean canTrigger() {
        return trigger_active;
    }

    @Override
    public void setTriggerActive(boolean trigger_active) {
        this.trigger_active = trigger_active;
    }

    @Override
    public boolean triggerCollision() {
        return !open;
    }

    public boolean needsKey() {
        return need_key;
    }

    public void setNeedsKey(boolean needs_key) {
        Logger.printStackStrace();
        this.need_key = needs_key;
    }

    public void setNeedsKeyId(int need_key_id) {
        this.need_key_id = need_key_id;
    }
}
