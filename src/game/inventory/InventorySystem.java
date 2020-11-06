package game.inventory;

import game.items.Item;
import game.main.Game;
import game.main.MouseInput;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class InventorySystem {
	public static final int slot_w = 20, slot_h = 20;
	public static final Color slot_bg = new Color(0, 0, 0, 127);
	public static final Color slot_outline = new Color(20, 20, 20, 255);

	private Inventory player_hotbar;
	private Inventory player_inventory;
	private ArrayList<Inventory> open_inventories = new ArrayList<>();

	public static boolean player_inventory_open = true;
	public static Item holding = null;

	public InventorySystem() {
		this.player_hotbar = new Inventory(5, 1);
		this.player_hotbar.setXY(Game.WIDTH - 5*slot_w, 100);

		this.player_inventory = new Inventory(5, 5);
	}

	public void tick() {
		/*for(Inventory inv : open_inventories) {
			inv.tick();
		}*/
	}

	public void render(Graphics g) {
		player_hotbar.render(g);
		if(player_inventory_open) player_inventory.render(g);

		for(Inventory inv : open_inventories) {
			inv.render(g);
		}
	}

	public void mouseClicked(MouseEvent e) {
		for(Inventory inv : open_inventories) {
			if(Game.mouseInput.mouseOverLocalRect(inv.getInventoryBounds())) {
				switch (e.getButton()) {
					case MouseEvent.BUTTON1 -> inv.mouseLeftClick(e);
					case MouseEvent.BUTTON2 -> inv.mouseMiddleClick(e);
					case MouseEvent.BUTTON3 -> inv.mouseRightClick(e);
					default -> System.out.println("Mouse function not recognized");
				}
			} else {
				mouseClickedOutside(e);
			}
		}
	}

	public void mouseClickedOutside(MouseEvent e) {

	}

	public void addOpenInventory(Inventory inv) {
		this.open_inventories.add(inv);
	}
	public void removeOpenInventory(Inventory inv) {
		this.open_inventories.remove(inv);
	}
}
