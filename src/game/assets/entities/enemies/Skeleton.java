package game.assets.entities.enemies;

import game.assets.items.Item;
import game.enums.ID;
import game.system.helpers.StructureLoaderHelpers;
import game.system.systems.gameObject.GameObject;
import game.system.world.JsonStructureLoader;
import game.textures.Textures;
import org.json.simple.JSONObject;

import java.awt.*;

public class Skeleton extends GameObject {
    public Skeleton(int x, int y, int z_index, ID id) {
        super(x, y, z_index, ID.Skeleton);
    }

    public Skeleton(JSONObject json, int z_index, int division, JsonStructureLoader loader) {
        super(
                StructureLoaderHelpers.getIntProp(json, "x") / division,
                StructureLoaderHelpers.getIntProp(json, "y") / division,
                z_index,
                ID.Skeleton);
    }

    public void tick() {

    }

    public void render(Graphics g) {
        g.drawImage(Textures.skeleton, x, y, null);
    }
}
