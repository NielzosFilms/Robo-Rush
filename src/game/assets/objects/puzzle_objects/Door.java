package game.assets.objects.puzzle_objects;

import game.assets.items.Item;
import game.enums.ID;
import game.system.helpers.StructureLoaderHelpers;
import game.system.main.Game;
import game.system.systems.GameObject;
import game.system.world.JsonStructureLoader;
import org.json.simple.JSONObject;

import java.awt.*;

public class Door extends GameObject implements PuzzleObject {
    private int connection_id;
    private boolean open = false;
    public Door(int x, int y, int z_index) {
        super(x, y, z_index, ID.Door);
    }

    public Door(JSONObject json, int z_index, int division, JsonStructureLoader loader) {
        super(
                StructureLoaderHelpers.getIntProp(json, "x") / division,
                StructureLoaderHelpers.getIntProp(json, "y") / division,
                z_index,
                ID.Door);
        width = StructureLoaderHelpers.getIntProp(json, "width") / division;
        height = StructureLoaderHelpers.getIntProp(json, "height") / division;
        connection_id = StructureLoaderHelpers.getIntProp(json, "id");
    }

    public void tick() {

    }

    public void render(Graphics g) {
        if(!open) {
            g.setColor(Color.orange);
            g.drawRect(x, y, width, height);
        }
    }

    public Rectangle getBounds() {
        if(!open) {
            return new Rectangle(x, y, width, height);
        }
        return null;
    }

    public Rectangle getSelectBounds() {
        return null;
    }

    public Item getItem() {
        return null;
    }

    public void interact() {
        open = !open;
    }

    public void destroyed() {

    }

    public void hit(int damage) {

    }

    public void setConnectedObject(GameObject object) {

    }

    public GameObject getConnectedObject() {
        return null;
    }

    public int getConnectedObject_id() {
        return 0;
    }

    public int getConnection_id() {
        return connection_id;
    }

    public boolean hasConnection() {
        return false;
    }

    public void getConnectedObject(GameObject object) {

    }
}
