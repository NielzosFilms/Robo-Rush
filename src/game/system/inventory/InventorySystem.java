package game.system.inventory;

import game.assets.entities.Player;
import game.assets.items.Item;
import game.assets.items.ItemGround;
import game.system.main.Game;
import game.system.main.GameObject;
import game.system.main.Handler;
import game.system.inputs.MouseInput;
import game.system.world.World;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class InventorySystem {
	public static final int slot_w = 20, slot_h = 20;
	public static final int stackSize = 99;
	public static final Color slot_bg = new Color(0, 0, 0, 127);
	public static final Color slot_outline = new Color(20, 20, 20, 255);
	public static final Color slot_hover = new Color(255, 255, 255, 50);

	private Handler handler;
	private MouseInput mouseInput;
	private World world;
	private Inventory player_inv;
	public Inventory player_hotbar;

	public int hotbar_selected;
	private ArrayList<Inventory> open_inventories = new ArrayList<>();

	//public static boolean player_inventory_open = true;
	private Item holding = null;

	public InventorySystem() {}

	public void setRequirements(Handler handler, MouseInput mouseInput, World world, Player player) {
		this.handler = handler;
		this.mouseInput = mouseInput;
		this.world = world;
		this.player_hotbar = player.hotbar;
		this.open_inventories.add(this.player_hotbar);
		this.player_inv = player.inventory;
	}

	public void tick() {
		for(Inventory inv : open_inventories) {
			inv.tick();
		}
	}

	public void render(Graphics g) {
		for(Inventory inv : open_inventories) {
			inv.render(g);
		}
		if(isHolding()) holding.render(g, mouseInput.mouse_x, mouseInput.mouse_y);
	}

	public void mouseClicked(MouseEvent e) {
		for(Inventory inv : open_inventories) {
			if(mouseInput.mouseOverLocalRect(inv.getInventoryBounds())) {
				inv.mouseClick(e, mouseInput, this);
			} else {
				mouseClickedOutside(e);
			}
		}
	}

	public void mouseClickedOutside(MouseEvent e) {

	}

	public void addOpenInventory(Inventory inv) {
		if(!this.open_inventories.contains(inv)) {
			if(this.open_inventories.size() > 2) closeAll();
			// TODO change inventory positions
			for(Inventory inventory : open_inventories) {
				if(inv.getInventoryBounds().intersects(inventory.getInventoryBounds())) {
					inv.setXY(inv.getX(), inv.getY() + inventory.getInventoryBounds().height);
				}
			}
			this.open_inventories.add(inv);
		} else {
			removeOpenInventory(inv);
		}
	}
	public void removeOpenInventory(Inventory inv) {
		this.open_inventories.remove(inv);
	}

	public void pickupItemToPlayerInv(GameObject obj) {
		Item item = obj.getItem();
		if (item != null) {
			if (player_inv.canAcceptItem(item)) {
				player_inv.addItem(item);
				handler.findAndRemoveObject(obj, world);
			}
		}
	}

	public void dropItem(Item item) {
		handler.addObject(item.getItemGround());
	}

	public boolean inventoryIsOpen() {
		return this.open_inventories.size() > 1;
	}

	public boolean isHolding() {
		return this.holding != null;
	}

	public void clearHolding() {
		this.holding = null;
	}

	public void setHolding(Item item) {
		this.holding = item;
	}

	public Item getHolding() {
		return this.holding;
	}

	public void closeAll() {
		this.open_inventories.clear();
		this.open_inventories.add(player_hotbar);
	}
}
