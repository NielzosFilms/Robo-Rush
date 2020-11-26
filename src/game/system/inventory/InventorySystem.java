package game.system.inventory;

import game.assets.entities.Player;
import game.assets.items.Item;
import game.assets.items.ItemGround;
import game.system.main.*;
import game.system.inputs.MouseInput;
import game.system.world.World;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class InventorySystem {
	public static final int slot_w = 18, slot_h = 18;
	public static final int item_w = 16, item_h = 16;
	public static final int stackSize = 99;
	public static final Color slot_bg = new Color(0, 0, 0, 127);
	public static final Color slot_outline = new Color(20, 20, 20, 255);
	public static final Color slot_hover = new Color(255, 255, 255, 50);

	private Handler handler;
	private MouseInput mouseInput;
	private World world;
	private Inventory player_inv;
	private Camera cam;
	public Inventory player_hotbar;

	public int hotbar_selected;
	private ArrayList<Inventory> open_inventories = new ArrayList<>();

	//public static boolean player_inventory_open = true;
	private Item holding = null;

	public InventorySystem() {}

	public void setRequirements(Handler handler, MouseInput mouseInput, World world, Player player, Camera cam) {
		this.handler = handler;
		this.mouseInput = mouseInput;
		this.world = world;
		this.player_hotbar = player.hotbar;
		this.open_inventories.add(this.player_hotbar);
		this.player_inv = player.inventory;
		this.cam = cam;
	}

	public void tick() {
		for(int i=0; i<open_inventories.size(); i++) {
			Inventory inv = open_inventories.get(i);
			inv.tick();
		}
	}

	public void render(Graphics g) {
		// NOTICE using this for loop instead of "Inventory inv : open_inventories"
		// will fix the removing inventory inside bug
		for(int i=0; i<open_inventories.size(); i++) {
			Inventory inv = open_inventories.get(i);
			inv.render(g);
		}
		if(isHolding()) holding.render(g, mouseInput.mouse_x, mouseInput.mouse_y);
	}

	public void mouseClicked(MouseEvent e) {
		Inventory inv_clicked = null;
		for(int i=0; i<open_inventories.size(); i++) {
			Inventory inv = open_inventories.get(i);
			if(mouseInput.mouseOverLocalRect(inv.getInventoryBounds())) {
				inv_clicked = inv;
				break;
			}
		}
		if(inv_clicked != null) {
			inv_clicked.mouseClick(e, mouseInput, this);
		} else {
			mouseClickedOutside(e);
		}
	}

	public void mouseClickedOutside(MouseEvent e) {
		if(holding != null) {
			ItemGround item_gnd = holding.getItemGround();
			Point world_coords = Helpers.getWorldCoords(mouseInput.mouse_x, mouseInput.mouse_y, cam);
			item_gnd.setCoords(world_coords);
			dropItem(item_gnd);
		}
	}

	public void addOpenInventory(Inventory inv) {
		if(!this.open_inventories.contains(inv)) {
			//if(this.open_inventories.size() > 2) closeAll();
			// TODO change inventory positions
			for(Inventory inventory : open_inventories) {
				if(inv.getInventoryBounds().intersects(inventory.getInventoryBounds())) {
					removeOpenInventory(inventory);
					break;
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

	public void dropItem(ItemGround item) {
		handler.addObject(item);
		holding = null;
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
