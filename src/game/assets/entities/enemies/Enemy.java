package game.assets.entities.enemies;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import game.system.helpers.Logger;
import game.system.systems.gameObject.Collision;
import game.system.systems.gameObject.GameObject;
import game.enums.ID;
import game.system.systems.gameObject.Hitable;

public class Enemy extends GameObject implements Collision, Hitable {

	public Enemy(int x, int y, int z_index, ID id) {
		super(x, y, z_index, id);
	}

	public void tick() {
		buffer_x += velX;
		buffer_y += velY;
		x = Math.round(buffer_x);
		y = Math.round(buffer_y);
	}

	public void render(Graphics g) {
		g.setColor(Color.red);
		g.drawRect(x, y, 16, 16);
	}

	@Override
	public Rectangle getBounds() {
		return new Rectangle(x, y, 16, 16);
	}

	@Override
	public void hit(int damage) {

	}
}
