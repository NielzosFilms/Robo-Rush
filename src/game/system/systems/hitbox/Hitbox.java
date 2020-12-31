package game.system.systems.hitbox;

import game.assets.items.item.Item;
import game.enums.ID;
import game.system.main.Game;
import game.system.systems.gameObject.GameObject;

import java.awt.*;

public class Hitbox extends GameObject {
    private HitboxContainer parent;
    private int creation_delay;
    private int lifetime;
    private int damage;
    private float knockback;

    private int x_diff = 0, y_diff = 0;
    public Hitbox(int x, int y, int width, int height, int creation_delay, int lifetime, int damage, float knockback ) {
        super(x, y, 1, ID.Hitbox);
        this.creation_delay = creation_delay;
        this.lifetime = lifetime;
        this.width = width;
        this.height = height;
        this.damage = damage;
        this.knockback = knockback;
    }

    public void setParent(HitboxContainer parent) {
        this.parent = parent;
        this.x_diff = x - parent.getCreated_by().getX();
        this.y_diff = y - parent.getCreated_by().getY();
    }

    public void tick() {
        x = parent.getCreated_by().getX() + x_diff;
        y = parent.getCreated_by().getY() + y_diff;
        if(active()) {
            if(lifetime > 0) {
                lifetime--;
            } else {
                parent.removeHitbox(this);
            }
        } else {
            if(creation_delay > 0) creation_delay--;
        }
    }

    public void render(Graphics g) {
        if(Game.DEBUG_MODE) {
            g.setColor(new Color(255, 255, 0, 128));
            g.fillRect(x, y, width, height);
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public Rectangle getSelectBounds() {
        return null;
    }

    public Item getItem() {
        return null;
    }

    public void interact() {}

    public void destroyed() {}

    public void hit(int damage) {}

    public int getDamage() {
        return this.damage;
    }
    public float getKnockback() {return this.knockback;}

    public boolean active() {
        return creation_delay <= 0;
    }
}
