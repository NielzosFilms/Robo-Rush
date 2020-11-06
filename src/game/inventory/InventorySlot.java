package game.inventory;

import game.items.Item;

import java.awt.*;

public class InventorySlot {
	private Item item;
	private int inv_x, inv_y;
	private int x, y;
	private int w = InventorySystem.slot_w, h = InventorySystem.slot_h;

	public InventorySlot(int inv_x, int inv_y, int x, int y) {
		this.inv_x = inv_x;
		this.inv_y = inv_y;
		this.x = x;
		this.y = y;
	}

	public void render(Graphics g) {
		g.setColor(InventorySystem.slot_bg);
		g.fillRect(inv_x + x, inv_y + y, w, h);
		if(this.item != null) item.render(g, inv_x + x, inv_y + y);
	}

	public void setItem(Item item) {
		this.item = item;
	}
	public Item getItem() {
		return this.item;
	}
	public void clearItem() {
		this.item = null;
	}

	public void setInvXY(int inv_x, int inv_y) {
		this.inv_x = inv_x;
		this.inv_y = inv_y;
	}

	public Rectangle getBounds() {
		return new Rectangle(inv_x + x, inv_y + y, w, h);
	}
}
