package game.items;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Random;

import game.items.Item;
import game.main.Game;
import game.main.GameObject;
import game.main.ID;
import game.textures.Textures;

public class ItemGround extends GameObject {

	private Random r = new Random();

	private Item inventoryItem;

	private int x_diff, y_diff, timer;
	private int lifeTimer = (60 * 60) * 5; // 5 mins till destroyed
	private boolean direction = false;

	private double buffer_x, buffer_y;

	public ItemGround(int x, int y, int z_index, ID id, Item inventoryItem) {
		super(x, y, z_index, id);
		this.inventoryItem = inventoryItem;
		velY = (r.nextDouble() * 3) - 2;
		velX = (r.nextDouble() * 3) - 2;
		buffer_x = x;
		buffer_y = y;
	}

	public void tick() {
		timer++;

		buffer_x += velX;
		buffer_y += velY;
		x = (int) Math.round(buffer_x);
		y = (int) Math.round(buffer_y);
		double xTarg = 0;
		velX += (xTarg - velX) * (0.1f);
		double yTarg = 0;
		velY += (yTarg - velY) * (0.1f);

		if (timer >= 10) {
			timer = 0;
			if (direction) {
				y_diff++;
				if (y_diff >= 0) {
					direction = !direction;
				}
			} else {
				y_diff--;
				if (y_diff <= -5) {
					direction = !direction;
				}
			}
		}
		lifeTimer--;
		if (lifeTimer <= 0) {
			Game.handler.findAndRemoveObject(this, Game.world);
			// Game.handler.removeObject(z_index, this);
		}
	}

	public void render(Graphics g) {
		g.drawImage(Textures.entity_shadow, x, y + 5, null);
		g.drawImage(inventoryItem.getTexture(), x + x_diff, y + y_diff, 16, 16, null);
	}

	public Rectangle getBounds() {
		return null;
	}

	public Rectangle getSelectBounds() {
		return new Rectangle(x, y, 16, 16);
	}

	public Item getInventoryItem() {
		return this.inventoryItem;
	}

	public Item getItem() {
		return null;
	}

	public void interact() {
		Game.inventory.pickupItem(this);
	}

}
