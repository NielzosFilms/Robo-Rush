package game.system.inventory;

import game.assets.items.Item;
import game.system.main.Game;

import java.awt.*;
import java.awt.event.MouseEvent;

public class InventorySlot {
	private Item item;
	private Inventory inv;
	private int x, y;
	private int w = InventorySystem.slot_w, h = InventorySystem.slot_h;
	private boolean hover = false;

	public InventorySlot(Inventory inv, int x, int y) {
		this.inv = inv;
		this.x = x;
		this.y = y;
	}

	public void tick() {
		this.hover = Game.mouseInput.mouseOverLocalRect(this.getBounds());
	}

	public void render(Graphics g) {
		int inv_x = inv.getX();
		int inv_y = inv.getY();
		g.setColor(InventorySystem.slot_bg);
		g.fillRect(inv_x + x, inv_y + y, w, h);

		int margin = (int)Math.floor((InventorySystem.slot_w - InventorySystem.item_w) / 2);
		if(this.item != null) item.render(g, inv_x + x + margin, inv_y + y + margin);

		if(hover) {
			g.setColor(InventorySystem.slot_hover);
			g.fillRect(inv_x + x, inv_y + y, w, h);
		}
	}

	public void onClick(MouseEvent e, Inventory inv, InventorySystem invSys) {
		switch (e.getButton()) {
			case MouseEvent.BUTTON1 -> leftClick(inv, invSys);
			case MouseEvent.BUTTON2 -> middleClick(inv, invSys);
			case MouseEvent.BUTTON3 -> rightClick(inv, invSys);
		}
	}

	private void leftClick(Inventory inv, InventorySystem invSys) {
		if(invSys.isHolding()) {
			if(this.hasItem()) {
				if(invSys.getHolding().getItemType() == this.item.getItemType()) {
					Item rest = this.addItem(invSys.getHolding(), inv);
					invSys.setHolding(rest);
				} else {
					Item tmp = invSys.getHolding();
					invSys.setHolding(this.getItem());
					this.setItem(tmp);
				}
			} else {
				this.setItem(invSys.getHolding());
				invSys.clearHolding();
			}
		} else {
			invSys.setHolding(this.getItem());
			this.clearItem();
		}
	}
	private void middleClick(Inventory inv, InventorySystem invSys) {

	}
	private void rightClick(Inventory inv, InventorySystem invSys) {
		if(invSys.isHolding()) {
			if(this.hasItem()) {
				if(this.getItem().getItemType() == invSys.getHolding().getItemType()) {
					if (this.getItem().getAmount() < InventorySystem.stackSize) {
						this.getItem().setAmount(this.getItem().getAmount() + 1);
						invSys.getHolding().setAmount(invSys.getHolding().getAmount() - 1);
						if (invSys.getHolding().getAmount() <= 0) invSys.clearHolding();
					}
				}
			} else {
				try {
					Item tmp = (Item) invSys.getHolding().clone();
					tmp.setAmount(1);
					this.setItem(tmp);
					invSys.getHolding().setAmount(invSys.getHolding().getAmount() - 1);
					if(invSys.getHolding().getAmount() <= 0) invSys.clearHolding();
				} catch (CloneNotSupportedException e) {
					e.printStackTrace();
				}
			}
		} else {
			if(this.hasItem()) {
				try {
					Item tmp = (Item) this.getItem().clone();
					tmp.setAmount((int) Math.ceil(tmp.getAmount() / 2));
					this.getItem().setAmount(this.getItem().getAmount() - tmp.getAmount());
					if (this.getItem().getAmount() <= 0) this.clearItem();
					if (tmp.getAmount() > 0) invSys.setHolding(tmp);
				} catch (CloneNotSupportedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void setItem(Item item) {
		this.item = item;
	}
	public Item addItem(Item item, Inventory inv) {
		if(this.hasItem()) {
			int sum = this.item.getAmount() + item.getAmount();
			int stack_diff = sum - InventorySystem.stackSize;
			if(stack_diff > 0) {
				this.item.setAmount(InventorySystem.stackSize);
				item.setAmount(stack_diff);
				return item;
			} else {
				this.item.setAmount(sum);
			}
		} else {
			if(item.getAmount() > InventorySystem.stackSize) {
				try {
					Item tmp = (Item) item.clone();
					tmp.setAmount(InventorySystem.stackSize);
					item.setAmount(item.getAmount() - InventorySystem.stackSize);
					this.setItem(tmp);
					return item;
				} catch (CloneNotSupportedException e) {
					e.printStackTrace();
				}
			} else {
				this.setItem(item);
			}
		}
		return null;
	}

	public Item getItem() {
		return this.item;
	}
	public void clearItem() {
		this.item = null;
	}
	public boolean hasItem() { return this.item != null; }

	public Rectangle getBounds() {
		return new Rectangle(inv.getX() + x, inv.getY() + y, w, h);
	}
}
