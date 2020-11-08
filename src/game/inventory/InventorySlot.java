package game.inventory;

import game.items.Item;
import game.main.Game;
import game.main.MouseInput;

import java.awt.*;

public class InventorySlot {
	private Item item;
	private int inv_x, inv_y;
	private int x, y;
	private int w = InventorySystem.slot_w, h = InventorySystem.slot_h;
	private boolean hover = false;

	public InventorySlot(int inv_x, int inv_y, int x, int y) {
		this.inv_x = inv_x;
		this.inv_y = inv_y;
		this.x = x;
		this.y = y;
	}

	public void tick() {
		this.hover = Game.mouseInput.mouseOverLocalRect(this.getBounds());
	}

	public void render(Graphics g) {
		g.setColor(InventorySystem.slot_bg);
		g.fillRect(inv_x + x, inv_y + y, w, h);

		if(this.item != null) item.render(g, inv_x + x, inv_y + y);

		if(hover) {
			g.setColor(InventorySystem.slot_hover);
			g.fillRect(inv_x + x, inv_y + y, w, h);
		}
	}

	public void setItem(Item item, Inventory inv) {
		int stack_diff = item.getAmount() - InventorySystem.stackSize;
		if(stack_diff > 0) {
			item.setAmount(InventorySystem.stackSize);
			this.item = item;
			try {
				Item ret = (Item)item.clone();
				ret.setAmount(stack_diff);
				inv.addItem(ret);
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
		} else {
			this.item = item;
		}
	}
	public void addItem(Item item, Inventory inv) {
		int sum = this.item.getAmount() + item.getAmount();
		int stack_diff = sum - InventorySystem.stackSize;
		if(stack_diff > 0) {
			this.item.setAmount(InventorySystem.stackSize);
			item.setAmount(stack_diff);
			inv.addItem(item);
		} else {
			this.item.setAmount(sum);
		}
	}
	public Item getItem() {
		return this.item;
	}
	public void clearItem() {
		this.item = null;
	}
	public boolean hasItem() { return this.item != null; }

	public void setInvXY(int inv_x, int inv_y) {
		this.inv_x = inv_x;
		this.inv_y = inv_y;
	}

	public Rectangle getBounds() {
		return new Rectangle(inv_x + x, inv_y + y, w, h);
	}
}
