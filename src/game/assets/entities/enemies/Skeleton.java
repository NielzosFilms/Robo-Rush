package game.assets.entities.enemies;

import game.assets.HealthBar;
import game.enums.ID;
import game.system.helpers.Logger;
import game.system.helpers.StructureLoaderHelpers;
import game.system.main.Game;
import game.system.systems.gameObject.Collision;
import game.system.systems.gameObject.GameObject;
import game.system.systems.gameObject.Hitable;
import game.system.systems.particles.Particle_String;
import game.system.world.JsonStructureLoader;
import game.textures.Animation;
import game.textures.TEXTURE_LIST;
import game.textures.Texture;
import game.textures.Textures;
import org.json.simple.JSONObject;

import java.awt.*;

public class Skeleton extends GameObject implements Collision, Hitable {
    private Animation idle = new Animation(8,
            new Texture(TEXTURE_LIST.skeleton_list, 0, 0),
            new Texture(TEXTURE_LIST.skeleton_list, 1, 0),
            new Texture(TEXTURE_LIST.skeleton_list, 2, 0),
            new Texture(TEXTURE_LIST.skeleton_list, 3, 0),

            new Texture(TEXTURE_LIST.skeleton_list, 0, 1),
            new Texture(TEXTURE_LIST.skeleton_list, 1, 1),
            new Texture(TEXTURE_LIST.skeleton_list, 2, 1),
            new Texture(TEXTURE_LIST.skeleton_list, 3, 1),

            new Texture(TEXTURE_LIST.skeleton_list, 0, 2),
            new Texture(TEXTURE_LIST.skeleton_list, 1, 2),
            new Texture(TEXTURE_LIST.skeleton_list, 2, 2)
            );

    private HealthBar healthBar = new HealthBar(0, 0, 0, 50);

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
        healthBar.setXY(x, y);
        if(healthBar.dead()) {
            Game.world.getPs().addParticle(new Particle_String(x, y + (getBounds().height / 2), 0, -0.2f, 60, "HE FOKIN DEAD"));
            Game.world.getHandler().findAndRemoveObject(this);
            healthBar.kill();
        }
        idle.runAnimation();
    }

    public void render(Graphics g) {
        g.drawImage(Textures.entity_shadow, x + ((getBounds().width / 2) - 8), y + getBounds().height - 8, 16, 16, null);
        idle.drawAnimation(g, x, y);
        //healthBar.render(g);
        if(Game.DEBUG_MODE) {
            g.setColor(Color.red);
            Rectangle r = getBounds();
            g.drawRect(r.x, r.y, r.width, r.height);
        }
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(x, y, 32, 48);
    }

    @Override
    public void hit(int damage) {
        Logger.print("skeleton hit: " + damage);
        healthBar.subtractHealth(damage);
    }
}
