package game.assets.objects.puzzle_objects;

import game.enums.ID;
import game.system.helpers.JsonLoader;
import game.system.helpers.StructureLoaderHelpers;
import game.system.systems.gameObject.Bounds;
import game.system.systems.gameObject.GameObject;
import org.json.simple.JSONObject;

import java.awt.*;

public class Door extends GameObject implements Bounds, PuzzleReciever {
    private int reciever_id;
    private boolean open = false;
    public Door(int x, int y, int z_index) {
        super(x, y, z_index, ID.Door);
    }

    public Door(JSONObject json, int z_index, int division, JsonLoader loader) {
        super(
                StructureLoaderHelpers.getIntProp(json, "x") / division,
                StructureLoaderHelpers.getIntProp(json, "y") / division,
                z_index,
                ID.Door);
        width = StructureLoaderHelpers.getIntProp(json, "width") / division;
        height = StructureLoaderHelpers.getIntProp(json, "height") / division;
        reciever_id = StructureLoaderHelpers.getIntProp(json, "id");
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

    public int getRevieverId() {
        return reciever_id;
    }

    public void triggered() {
        open = !open;
    }
}
