package game.assets.entities.enemies;

import game.assets.items.Item;
import game.enums.ID;
import game.system.helpers.StructureLoaderHelpers;
import game.system.systems.gameObject.GameObject;
import game.system.world.JsonStructureLoader;
import game.textures.Animation;
import game.textures.TEXTURE_LIST;
import game.textures.Texture;
import game.textures.Textures;
import org.json.simple.JSONObject;

import java.awt.*;

public class Skeleton extends GameObject {
    private Animation idle = new Animation(20,
            new Texture(TEXTURE_LIST.skeleton_list, 0, 0),
            new Texture(TEXTURE_LIST.skeleton_list, 1, 0),
            new Texture(TEXTURE_LIST.skeleton_list, 0, 1),
            new Texture(TEXTURE_LIST.skeleton_list, 1, 0)
            );
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
        idle.runAnimation();
    }

    public void render(Graphics g) {
        idle.drawAnimation(g, x, y);
    }
}
