package game.assets.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import game.assets.items.Item;
import game.system.main.GameObject;
import game.enums.ID;

public class Enemy extends GameObject {

	public Enemy(int x, int y, int z_index, ID id) {
		super(x, y, z_index, id);
	}

	public void tick() {

	}

	public void render(Graphics g) {
		g.setColor(Color.red);
		g.drawRect(x, y, 16, 16);
	}

	public Rectangle getBounds() {
		return new Rectangle(x, y, 16, 16);
	}

	public Rectangle getSelectBounds() {
		return new Rectangle(x, y, 16, 16);
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
