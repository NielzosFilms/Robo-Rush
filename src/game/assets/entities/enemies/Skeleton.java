package game.assets.entities.enemies;

import game.assets.HealthBar;
import game.enums.DIRECTIONS;
import game.enums.ID;
import game.system.helpers.Helpers;
import game.system.helpers.Logger;
import game.system.helpers.StructureLoaderHelpers;
import game.system.main.Game;
import game.system.systems.gameObject.Bounds;
import game.system.systems.gameObject.GameObject;
import game.system.systems.gameObject.Hitable;
import game.system.systems.hitbox.HitboxContainer;
import game.system.systems.particles.Particle_String;
import game.system.world.JsonStructureLoader;
import game.textures.Animation;
import game.textures.TEXTURE_LIST;
import game.textures.Texture;
import game.textures.Textures;
import org.json.simple.JSONObject;

import java.awt.*;

public class Skeleton extends GameObject implements Bounds, Hitable {
    private Animation idle = new Animation(200,
            new Texture(TEXTURE_LIST.skeleton_list, 0, 0),
            new Texture(TEXTURE_LIST.skeleton_list, 1, 0),

            new Texture(TEXTURE_LIST.skeleton_list, 0, 1),
            new Texture(TEXTURE_LIST.skeleton_list, 1, 1)
            );

    //private HealthBar healthBar = new HealthBar(0, 0, 0, 50);

    private boolean facing = true;

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
        buffer_x += velX;
        buffer_y += velY;
        x = Math.round(buffer_x);
        y = Math.round(buffer_y);

        Point mouse = Helpers.getWorldCoords(Game.mouseInput.mouse_x, Game.mouseInput.mouse_y, Game.world.getCam());

        if(Game.mouseInput.leftMouseDown()) {
            velX += (mouse.x - x) * 0.01f;
            velY += (mouse.y - y) * 0.01f;
        }

        velX -= (velX) * 0.05f;
        velY -= (velY) * 0.05f;
        /*healthBar.setXY(x, y);
        if(healthBar.dead()) {
            Game.world.getPs().addParticle(new Particle_String(x, y + (getBounds().height / 2), 0, -0.2f, 60, "HE FOKIN DEAD"));
            Game.world.getHandler().findAndRemoveObject(this);
            healthBar.kill();
        }*/

        if(velX > 0) {
            facing = true;
        } else if(velX < 0) {
            facing = false;
        }

        idle.runAnimation();
    }

    public void render(Graphics g) {
        g.drawImage(Textures.entity_shadow, x + ((getBounds().width / 2) - 8), y + getBounds().height - 8, 16, 16, null);
        if(facing) {
            idle.drawAnimation(g, x, y);
        } else {
            idle.drawAnimationMirroredH(g, x, y);
        }
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
    public void hit(HitboxContainer hitboxContainer, int hit_hitbox_index) {
        //healthBar.subtractHealth(damage);
    }
}
