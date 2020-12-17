package game.assets.structures;

import game.assets.items.Item;
import game.enums.ID;
import game.system.helpers.StructureLoaderHelpers;
import game.system.main.Game;
import game.system.systems.gameObject.GameObject;
import game.system.systems.gameObject.Interactable;
import game.system.world.JsonStructureLoader;
import org.json.simple.JSONObject;

import java.awt.*;

public class StructureExit extends GameObject implements Interactable {
    public StructureExit(int x, int y, int z_index, ID id) {
        super(x, y, z_index, id);
    }

    public StructureExit(JSONObject json, int z_index, int division, JsonStructureLoader loader) {
        super(
                StructureLoaderHelpers.getIntProp(json, "x") / division,
                StructureLoaderHelpers.getIntProp(json, "y") / division,
                z_index,
                ID.NULL
        );
        this.width = StructureLoaderHelpers.getIntProp(json, "width") / division;
        this.height = StructureLoaderHelpers.getIntProp(json, "height") / division;
    }

    public void tick() {

    }

    public void render(Graphics g) {
        Rectangle r = getSelectBounds();
        g.setColor(new Color(255, 122, 122, 128));
        g.drawRect(r.x, r.y, r.width, r.height);
    }

    public Rectangle getSelectBounds() {
        return new Rectangle(x, y, width, height);
    }

    public void interact() {
        Game.world.gotoLastEnteredStructure();
    }
}
