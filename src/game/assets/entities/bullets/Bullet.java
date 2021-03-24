package game.assets.entities.bullets;

import game.enums.ID;
import game.system.helpers.Logger;
import game.system.main.Game;
import game.system.systems.gameObject.Bounds;
import game.system.systems.gameObject.Destroyable;
import game.system.systems.gameObject.GameObject;
import game.system.systems.hitbox.Hitbox;
import game.system.systems.hitbox.HitboxContainer;
import game.textures.Animation;
import game.textures.TEXTURE_LIST;
import game.textures.Texture;

import java.awt.*;
import java.util.LinkedList;

public class Bullet extends GameObject implements game.system.systems.gameObject.Bullet {
    protected float max_vel = 4f;
    protected int lifeTime = 300;
    protected int angle;
    protected int damage = 2;
    protected LinkedList<GameObject> hit_objects = new LinkedList<>();
    protected GameObject created_by;

    public Bullet(int x, int y, int z_index, int angle, GameObject created_by) {
        super(x, y, z_index, ID.Bullet);
        this.tex = new Texture(TEXTURE_LIST.bullets, 1, 0);
        this.angle = angle;
        updateVelocity();
        this.created_by = created_by;
        hit_objects.add(created_by);
    }

    @Override
    public void tick() {
        if(lifeTime <= 0) {
            destroy();
        } else {
            buffer_x += velX;
            buffer_y += velY;

            x = Math.round(buffer_x);
            y = Math.round(buffer_y);
        }

        lifeTime--;
    }

    @Override
    public void render(Graphics g) {
        g.drawImage(tex.getTexure(), x-8, y-8, null);
        //Rectangle b = getBounds();
//        g.setColor(Color.white);
//        g.drawRect(b.x, b.y, b.width, b.height);
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(x - 5, y - 5, 10, 10);
    }

    @Override
    public int getDamage() {
        return damage;
    }

    @Override
    public void addHitObject(GameObject object) {
        hit_objects.add(object);
    }

    @Override
    public LinkedList<GameObject> getHitObjects() {
        return hit_objects;
    }

    @Override
    public GameObject getCreatedBy() {
        return created_by;
    }

    @Override
    public void destroy() {
        Game.gameController.getHandler().addObject(new BulletBreak(x, y));
        Game.gameController.getHandler().removeObject(this);
    }

    protected void updateVelocity() {
        velX = (float) (max_vel*Math.cos(Math.toRadians(angle)));
        velY = (float) (max_vel*Math.sin(Math.toRadians(angle)));
    }
}
