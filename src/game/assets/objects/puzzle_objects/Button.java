package game.assets.objects.puzzle_objects;

import game.assets.items.Item;
import game.enums.ID;
import game.system.helpers.Logger;
import game.system.helpers.StructureLoaderHelpers;
import game.system.main.Game;
import game.system.systems.GameObject;
import game.system.world.JsonStructureLoader;
import org.json.simple.JSONObject;

import java.awt.*;

public class Button extends GameObject implements PuzzleObject {
    private GameObject connectedObject;
    private int connectedObject_id;
    public Button(int x, int y, int z_index) {
        super(x, y, z_index, ID.Button);
    }

    public Button(JSONObject json, int z_index, int division, JsonStructureLoader loader) {
        super(StructureLoaderHelpers.getIntProp(json, "x") / division,
                StructureLoaderHelpers.getIntProp(json, "y") / division,
                z_index,
                ID.Button);
        width = StructureLoaderHelpers.getIntProp(json, "width") / division;
        height = StructureLoaderHelpers.getIntProp(json, "height") / division;

        connectedObject_id = Integer.parseInt(StructureLoaderHelpers.getCustomProp(json, "connected_object"));
    }

    public void tick() {

    }

    public void render(Graphics g) {

    }

    public Rectangle getBounds() {
        return null;
    }

    public Rectangle getSelectBounds() {
        return new Rectangle(x, y, width, height);
    }

    public Item getItem() {
        return null;
    }

    public void interact() {
        if(connectedObject != null) connectedObject.interact();
    }

    public void destroyed() {

    }

    public void hit(int damage) {

    }

    public void setConnectedObject(GameObject object) {
        connectedObject = object;
    }

    public GameObject getConnectedObject() {
        return null;
    }

    public int getConnectedObject_id() {
        return connectedObject_id;
    }

    public int getConnection_id() {
        return 0;
    }

    public void getConnectedObject(GameObject object) {

    }

    public boolean hasConnection() {
        return true;
    }
}
