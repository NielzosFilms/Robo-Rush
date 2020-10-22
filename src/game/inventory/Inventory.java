package game.inventory;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.io.Console;
import java.util.HashMap;
import java.util.LinkedList;

import game.items.ItemGround;
import game.items.ItemNull;
import game.items.ItemRock;
import game.entities.particles.Particle;
import game.entities.particles.ParticleSystem;
import game.items.Item;
import game.main.Camera;
import game.main.Game;
import game.main.GameObject;
import game.main.Handler;
import game.main.ID;
import game.main.MouseInput;
import game.textures.Textures;
import game.world.World;

public class Inventory {

	public HashMap<Point, Item> inventoryItems;
	public HashMap<Integer, Item> hotbarItems;
	private int size_x, size_y;
	private int x, y;
	private Textures textures;
	private MouseInput mouseInput;
	private Item mouse_holding;
	private Boolean inventory_open;
	private ParticleSystem particleSystem;
	private Handler handler;
	private Camera cam;
	private int hotbar_selected_item;

	public Inventory(int size_x, int size_y, MouseInput mouseInput, ParticleSystem particleSystem, Handler handler,
			Camera cam) {
		this.size_x = size_x;
		this.size_y = size_y;
		this.x = Game.WIDTH / 5;
		this.y = Game.HEIGHT / 2 - (textures.inventory_bg.getHeight() / 2);
		this.textures = textures;
		this.mouseInput = mouseInput;
		this.mouse_holding = null;
		this.inventory_open = false;
		this.hotbar_selected_item = 0;
		this.particleSystem = particleSystem;
		this.cam = cam;
		this.handler = handler;

		this.inventoryItems = new HashMap<Point, Item>();
		this.hotbarItems = new HashMap<Integer, Item>();

		/*
		 * for(int y = 0;y<2;y++) { for(int x = 0;x<size_x;x++) { items.put(new Point(x,
		 * y), new Item("wood", 1, textures)); } }
		 */
	}

	public void tick() {

	}

	public void render(Graphics g) {
		int hotbar_x = (Game.WIDTH / 2) - (size_x * 20) / 2;
		int hotbar_y = Game.HEIGHT - 23;
		for (int i = 0; i < size_x; i++) {
			g.setColor(new Color(0, 0, 0, 127));
			g.fillRect(hotbar_x + i * 20, hotbar_y, 20, 20);

			if (hotbarItems.get(i) != null) {
				hotbarItems.get(i).render(g, hotbar_x + i * 20 + 2, hotbar_y + 2);
			}

			if (mouseInput.mouseOverLocalVar(hotbar_x + (i * 20), hotbar_y, 20, 20)) {
				g.setColor(new Color(200, 200, 200, 80));
				g.fillRect(hotbar_x + (i * 20), hotbar_y, 20, 20);
			}

			g.setColor(new Color(20, 20, 20, 255));
			g.drawRect(hotbar_x + i * 20, hotbar_y, 20, 20);

			if (i == hotbar_selected_item) {
				g.setColor(new Color(120, 120, 120, 255));
				g.drawRect(hotbar_x + i * 20 + 1, hotbar_y + 1, 18, 18);
			}
		}

		if (inventory_open) {
			for (int y = 0; y < size_y; y++) {
				for (int x = 0; x < size_x; x++) {
					g.setColor(new Color(0, 0, 0, 127));
					g.fillRect(this.x + (x * 20), this.y + (y * 20), 20, 20);
					if (inventoryItems.containsKey(new Point(x, y))) {
						Item item = inventoryItems.get(new Point(x, y));
						item.render(g, this.x + (x * 20) + 2, this.y + (y * 20) + 2);
					}
					if (mouseInput.mouseOverLocalVar(this.x + (x * 20), this.y + (y * 20), 20, 20)) {
						g.setColor(new Color(200, 200, 200, 80));
						g.fillRect(this.x + (x * 20), this.y + (y * 20), 20, 20);
					}
					g.setColor(new Color(0, 0, 0, 200));
					g.drawRect(this.x + (x * 20), this.y + (y * 20), 20, 20);
				}
			}
		}
		if (mouse_holding != null) {
			mouse_holding.render(g, mouseInput.mouse_x - 8, mouseInput.mouse_y - 8);
		}
	}

	public void mouseClicked(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) {
			if (inventory_open) {
				boolean in_inventory = false;
				for (int y = 0; y < size_y; y++) {
					for (int x = 0; x < size_x; x++) {
						if (mouse_holding == null) {
							if (inventoryItems.containsKey(new Point(x, y))) {
								int item_x = this.x + (x * 20);
								int item_y = this.y + (y * 20);
								int item_w = 20;
								int item_h = 20;
								if (mouseInput.mouseOverLocalVar(item_x, item_y, item_w, item_h)) {
									in_inventory = true;
									Item item = inventoryItems.get(new Point(x, y));
									mouse_holding = item;
									inventoryItems.remove(new Point(x, y), item);
								}
							}
						} else {
							int item_x = this.x + (x * 20);
							int item_y = this.y + (y * 20);
							int item_w = 20;
							int item_h = 20;
							if (mouseInput.mouseOverLocalVar(item_x, item_y, item_w, item_h)) {
								in_inventory = true;
								if (!inventoryItems.containsKey(new Point(x, y))) {
									inventoryItems.put(new Point(x, y), mouse_holding);
									mouse_holding = null;
								}
							}
						}
					}
				}
				if (!in_inventory && mouse_holding != null) {
					handler.addObject(4, new ItemGround(mouseInput.getMouseWorldCoords().x - 8,
							mouseInput.getMouseWorldCoords().y - 8, 1, ID.Item, mouse_holding));
					mouse_holding = null;
				}
			}

			if (mouse_holding != null) {
				int hotbar_x = (Game.WIDTH / 2) - (size_x * 20) / 2;
				int hotbar_y = Game.HEIGHT - 23;
				for (int i = 0; i < size_x; i++) {
					int item_x = hotbar_x + i * 20;
					if (mouseInput.mouseOverLocalVar(item_x, hotbar_y, 20, 20)) {
						if (hotbarItems.get(i) == null) {
							hotbarItems.put(i, mouse_holding);
							mouse_holding = null;
						}
					}
				}
			} else {
				int hotbar_x = (Game.WIDTH / 2) - (size_x * 20) / 2;
				int hotbar_y = Game.HEIGHT - 23;
				for (int i = 0; i < size_x; i++) {
					int item_x = hotbar_x + i * 20;
					if (mouseInput.mouseOverLocalVar(item_x, hotbar_y, 20, 20)) {
						if (hotbarItems.get(i) != null) {
							mouse_holding = hotbarItems.get(i);
							hotbarItems.remove(i, hotbarItems.get(i));
						}
					}
				}
			}
		}
	}

	public void pickupItem(Handler handler, World world) {
		LinkedList<GameObject> objs = handler.getSelectableObjects(world);
		for (GameObject obj : objs) {
			if (obj.getSelectBounds() != null) {
				if (mouseInput.mouseOverWorldVar(obj.getSelectBounds().x, obj.getSelectBounds().y,
						obj.getSelectBounds().width, obj.getSelectBounds().height)) {
					if (checkInventoryItem(obj.getId())) {
						for (int y = 0; y < size_y; y++) {
							for (int x = 0; x < size_x; x++) {
								if (inventoryItems.containsKey(new Point(x, y))) {
									if (inventoryItems.get(new Point(x, y)).getItemType() == obj.getId()) {
										inventoryItems.get(new Point(x, y))
												.setAmount(inventoryItems.get(new Point(x, y)).getAmount() + 1);
										handler.findAndRemoveObject(obj, world);
										return;
									}
								}
							}
						}
					} else if (checkInventoryFreeSlots()) {
						for (int y = 0; y < size_y; y++) {
							for (int x = 0; x < size_x; x++) {
								if (!inventoryItems.containsKey(new Point(x, y))) {
									if (obj.getId() == ID.Item) {
										ItemGround item_ground = (ItemGround) obj;
										inventoryItems.put(new Point(x, y), item_ground.getInventoryItem());
										handler.findAndRemoveObject(obj, world);
										return;
									} else {
										Item item = createNewItem(obj.getId());
										inventoryItems.put(new Point(x, y), item);

										handler.findAndRemoveObject(obj, world);
										return;
									}
								}
							}
						}
					}
				}
			}
		}
	}

	private Item createNewItem(ID itemType) {
		if (itemType == ID.Pebble) {
			return new ItemRock(1, itemType, Textures.tileSetNatureBlocks.get(49));
		}
		return new ItemNull(0, ID.Null, Textures.placeholder);
	}

	private boolean checkInventoryItem(ID itemType) {
		if (inventoryItems.size() == 0) {
			return false;
		}
		for (int y = 0; y < size_y; y++) {
			for (int x = 0; x < size_x; x++) {
				if (inventoryItems.containsKey(new Point(x, y))) {
					if (inventoryItems.get(new Point(x, y)).getItemType() == itemType) {
						return true;
					}
				}
			}
		}
		return false;
	}

	private boolean checkInventoryFreeSlots() {
		return !(inventoryItems.size() == size_x * size_y);
	}

	public void setInventoryOpen(boolean var) {
		this.inventory_open = var;
	}

	public boolean getInventoryOpen() {
		return this.inventory_open;
	}

	public void switchInventoryState() {
		if (this.inventory_open)
			this.inventory_open = false;
		else if (!this.inventory_open)
			this.inventory_open = true;
	}

	public int getHotbarSelected() {
		return this.hotbar_selected_item;
	}

	public void setHotbarSelected(int index) {
		if (index >= 0 && index < size_x) {
			this.hotbar_selected_item = index;
		}
	}

	public Item getHotbarSelectedItem() {
		return this.hotbarItems.get(hotbar_selected_item);
	}

	public int getSizeX() {
		return this.size_x;
	}

	public int getSizeY() {
		return this.size_y;
	}

	public int getHotbarX() {
		return (Game.WIDTH / 2) - (size_x * 20) / 2;
	}

	public int getHotbarY() {
		return Game.HEIGHT - 23;
	}

	/*
	 * public void addItem(Point point, Item item) { this.items.put(point, item); }
	 * 
	 * public Item getItem(Point point) { return this.items.get(point); }
	 */

}
