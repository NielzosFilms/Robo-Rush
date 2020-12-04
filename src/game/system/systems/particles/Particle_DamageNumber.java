package game.system.systems.particles;

import game.assets.items.Item;
import game.enums.ID;
import game.system.main.Game;
import game.system.systems.GameObject;
import game.textures.Fonts;

import java.awt.*;

public class Particle_DamageNumber extends GameObject {
    private int lifetime;
    private float velX, velY;
    private float buffer_x, buffer_y;
    private ParticleSystem ps;
    private int alpha;
    private int damage_amount;

    public Particle_DamageNumber(int x, int y, float velX, float velY, int lifetime, int damage_amount) {
        super(x, y, 0, ID.NULL);
        this.buffer_x = x;
        this.buffer_y = y;
        this.lifetime = lifetime;
        this.velX = velX;
        this.velY = velY;
        this.ps = Game.world.getPs();
        this.alpha = 255;
        this.damage_amount = damage_amount;
    }

    public void tick() {
        buffer_x += velX;
        buffer_y += velY;
        x = Math.round(buffer_x);
        y = Math.round(buffer_y);
        lifetime--;
        alpha = alpha - (alpha / lifetime);
        if (lifetime <= 1) {
            ps.removeParticle(this);
        }

    }

    public void render(Graphics g) {
        g.setColor(new Color(255, 255, 255, alpha));
        g.setFont(Fonts.default_fonts.get(5));
        g.drawString("" + damage_amount, x, y);
    }

    public Rectangle getBounds() {
        return null;
    }

    public Rectangle getSelectBounds() {
        return null;
    }

    public Item getItem() {
        return null;
    }

    public void interact() {

    }

    @Override
    public void destroyed() {

    }

    @Override
    public void hit(int damage) {

    }
}
