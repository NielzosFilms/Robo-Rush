package game.assets.objects;

import game.enums.ID;
import game.system.helpers.JsonLoader;
import game.system.helpers.StructureLoaderHelpers;
import game.system.main.Game;
import game.system.systems.gameObject.Bounds;
import game.system.systems.gameObject.GameObject;
import game.textures.COLOR_PALETTE;
import org.json.simple.JSONObject;

import java.awt.*;

public class BoundsObject extends GameObject implements Bounds {
    public BoundsObject(int x, int y, int width, int height) {
        super(x, y, 1, ID.BoundsObject);
        this.width = width;
        this.height = height;
    }

    public BoundsObject(JSONObject json, int z_index, int division, JsonLoader loader) {
        super(
                StructureLoaderHelpers.getIntProp(json, "x") / division,
                StructureLoaderHelpers.getIntProp(json, "y") / division,
                1, ID.BoundsObject);

        this.width = StructureLoaderHelpers.getIntProp(json, "width") / division;
        this.height = StructureLoaderHelpers.getIntProp(json, "height") / division;
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
        g.setColor(COLOR_PALETTE.gray_2.color);
        g.fillRect(this.x, this.y, this.width, this.height);
        if(Game.DEBUG_MODE) {
            g.setColor(Color.decode("#c0cbdc"));
            g.drawRect(this.x, this.y, this.width, this.height);
        }
    }
}
