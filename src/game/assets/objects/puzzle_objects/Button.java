package game.assets.objects.puzzle_objects;

import game.enums.ID;
import game.system.helpers.JsonLoader;
import game.system.helpers.StructureLoaderHelpers;
import game.system.systems.gameObject.GameObject;
import game.system.systems.gameObject.Interactable;
import org.json.simple.JSONObject;

import java.awt.*;

public class Button extends GameObject implements PuzzleTrigger, Interactable {
    private PuzzleReciever reciever;
    private int reciever_id;
    public Button(int x, int y, int z_index) {
        super(x, y, z_index, ID.Button);
    }

    public Button(JSONObject json, int z_index, int division, JsonLoader loader) {
        super(StructureLoaderHelpers.getIntProp(json, "x") / division,
                StructureLoaderHelpers.getIntProp(json, "y") / division,
                z_index,
                ID.Button);
        width = StructureLoaderHelpers.getIntProp(json, "width") / division;
        height = StructureLoaderHelpers.getIntProp(json, "height") / division;

        reciever_id = Integer.parseInt(StructureLoaderHelpers.getCustomProp(json, "connected_object"));
    }

    public void tick() {

    }

    public void render(Graphics g) {

    }

    public Rectangle getSelectBounds() {
        return new Rectangle(x, y, width, height);
    }

    public void interact() {
        //if(connectedObject != null) connectedObject.interact();
        triggered();
    }

    public void setReciever(PuzzleReciever object) {
        reciever = object;
    }

    public PuzzleReciever getReciever() {
        return reciever;
    }

    public int getRecieverId() {
        return reciever_id;
    }

    public void triggered() {
        reciever.triggered();
    }
}
