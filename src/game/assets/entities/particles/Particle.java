package game.assets.entities.particles;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import game.assets.items.Item;
import game.system.main.Game;
import game.system.main.GameObject;
import game.system.main.Handler;
import game.enums.ID;

public class Particle extends GameObject {

	private int lifetime;
	private float velX, velY;
	private float buffer_x, buffer_y;
	private ParticleSystem ps;
	private int alpha;

	public Particle(int x, int y, float velX, float velY, int lifetime) {
		super(x, y, 0, ID.NULL);
		this.buffer_x = x;
		this.buffer_y = y;
		this.lifetime = lifetime;
		this.velX = velX;
		this.velY = velY;
		this.ps = Game.ps;
		this.alpha = 255;
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

	public void interact() {

	}

	@Override
	public void destroyed() {

	}

	@Override
	public void hit(int damage) {

	}

}
