package game.entities.particles;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import game.items.Item;
import game.main.GameObject;
import game.main.Handler;
import game.main.ID;

public class Particle extends GameObject {

	private int lifetime;
	private float velX, velY;
	private Handler handler;
	private ParticleSystem ps;
	private int alpha;

	public Particle(int x, int y, int z_index, ID id, float velX, float velY, int lifetime, ParticleSystem ps) {
		super(x, y, z_index, id);
		this.lifetime = lifetime;
		this.velX = velX;
		this.velY = velY;
		this.handler = handler;
		this.ps = ps;
		this.alpha = 255;
	}

	public void tick() {

		y = Math.round(y + velY);
		x = Math.round(x + velX);
		lifetime--;
		alpha = alpha - (alpha / lifetime);
		if (lifetime <= 1) {
			ps.removeParticle(this);
		}

	}

	public void render(Graphics g) {
		g.setColor(new Color(255, 0, 0, alpha));
		g.fillRect(x, y, 16, 16);
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

}
