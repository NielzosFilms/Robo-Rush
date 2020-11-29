package game.system.hitbox;

import game.assets.items.Item;
import game.enums.ID;
import game.system.main.Game;
import game.system.main.GameObject;

import java.awt.*;
import java.util.LinkedList;

public class Hitbox extends GameObject {
    private HitboxContainer parent;
    private int creation_delay;
    private int lifetime;
    private int damage;
    public Hitbox(int x, int y, int width, int height, int creation_delay, int lifetime, int damage ) {
        super(x, y, 1, ID.Hitbox);
        this.creation_delay = creation_delay;
        this.lifetime = lifetime;
        this.width = width;
        this.height = height;
        this.damage = damage;
    }

    public void setParent(HitboxContainer parent) {
        this.parent = parent;
    }

    public void tick() {
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
        if(Game.DEDUG_MODE) {
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

    public boolean active() {
        return creation_delay <= 0;
    }
}
