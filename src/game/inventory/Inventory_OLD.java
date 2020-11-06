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
import game.items.ItemWood;
import game.entities.particles.Particle;
import game.entities.particles.ParticleSystem;
import game.items.ITEM_ID;
import game.items.Item;
import game.main.Camera;
import game.main.Game;
import game.main.GameObject;
import game.main.Handler;
import game.main.ID;
import game.main.MouseInput;
import game.textures.Textures;
import game.world.World;

public class Inventory_OLD {

	public static final int MAX_STACK_SIZE = 99;

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
	private World world;
	private int hotbar_selected_item;

	public Inventory_OLD(int size_x, int size_y, MouseInput mouseInput, ParticleSystem particleSystem, Handler handler,
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

		inventoryItems.put(new Point(2, 0), new ItemRock(98, ITEM_ID.Rock));

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

			if (mouse_holding != null) {
				int hotbar_x = (Game.WIDTH / 2) - (size_x * 20) / 2;
				int hotbar_y = Game.HEIGHT - 23;
				for (int i = 0; i < size_x; i++) {
					int item_x = hotbar_x + i * 20;
					if (mouseInput.mouseOverLocalVar(item_x, hotbar_y, 20, 20)) {
						if (hotbarItems.get(i) == null) {
							hotbarItems.put(i, mouse_holding);
							mouse_holding = null;
							return;
						} else {
							Item itemInventory = hotbarItems.get(i);
							Item holding = mouse_holding;
							if (itemInventory.getItemType() == mouse_holding.getItemType()) {
								// int stackDiff = MAX_STACK_SIZE - itemInventory.getAmount();
								itemInventory.setAmount(itemInventory.getAmount() + holding.getAmount());
								holding.setAmount(0);
								if (itemInventory.getAmount() > MAX_STACK_SIZE) {
									int diff = itemInventory.getAmount() - MAX_STACK_SIZE;
									itemInventory.setAmount(MAX_STACK_SIZE);
									holding.setAmount(diff);
								}
								hotbarItems.put(i, itemInventory);
								if (holding.getAmount() <= 0) {
									mouse_holding = null;
								} else {
									mouse_holding = holding;
								}
							} else {
								mouse_holding = hotbarItems.get(i);
								hotbarItems.put(i, holding);
								return;
							}
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
							Item item = hotbarItems.get(i);
							mouse_holding = item;
							hotbarItems.remove(i);
							return;
						}
					}
				}
			}

			boolean in_inventory = false;
			if (inventory_open) {
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
									return;
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
									return;
								} else {
									Item itemInventory = inventoryItems.get(new Point(x, y));
									Item holding = mouse_holding;
									if (itemInventory.getItemType() == mouse_holding.getItemType()) {
										// int stackDiff = MAX_STACK_SIZE - itemInventory.getAmount();
										itemInventory.setAmount(itemInventory.getAmount() + holding.getAmount());
										holding.setAmount(0);
										if (itemInventory.getAmount() > MAX_STACK_SIZE) {
											int diff = itemInventory.getAmount() - MAX_STACK_SIZE;
											itemInventory.setAmount(MAX_STACK_SIZE);
											holding.setAmount(diff);
										}
										inventoryItems.put(new Point(x, y), itemInventory);
										if (holding.getAmount() <= 0) {
											mouse_holding = null;
										} else {
											mouse_holding = holding;
										}
									} else {
										mouse_holding = inventoryItems.get(new Point(x, y));
										inventoryItems.put(new Point(x, y), holding);
										return;
									}
								}
							}
						}
					}
				}
			}
			if (!in_inventory && mouse_holding != null) {
				handler.addObject(4, new ItemGround(mouseInput.getMouseWorldCoords().x - 8,
						mouseInput.getMouseWorldCoords().y - 8, 1, ID.Item, mouse_holding));
				mouse_holding = null;
				return;
			}
		} else if (e.getButton() == MouseEvent.BUTTON3) {
			if (mouse_holding != null) {
				int hotbar_x = (Game.WIDTH / 2) - (size_x * 20) / 2;
				int hotbar_y = Game.HEIGHT - 23;
				for (int i = 0; i < size_x; i++) {
					int item_x = hotbar_x + i * 20;
					if (mouseInput.mouseOverLocalVar(item_x, hotbar_y, 20, 20)) {
						if (!hotbarItems.containsKey(i)) {
							Item itemHolding = mouse_holding;
							try {
								Item inventoryItem = (Item) itemHolding.clone();
								itemHolding.setAmount(itemHolding.getAmount() - 1);
								inventoryItem.setAmount(1);
								if (itemHolding.getAmount() <= 0) {
									mouse_holding = null;
								}
								hotbarItems.put(i, inventoryItem);
								return;
							} catch (CloneNotSupportedException e1) {
								e1.printStackTrace();
							}
						} else {
							Item itemInventory = hotbarItems.get(i);
							if (itemInventory.getAmount() + 1 <= MAX_STACK_SIZE) {
								mouse_holding.setAmount(mouse_holding.getAmount() - 1);
								itemInventory.setAmount(itemInventory.getAmount() + 1);
								if (mouse_holding.getAmount() <= 0) {
									mouse_holding = null;
								}
								return;
							}
						}
					}
				}
			} else {
				int hotbar_x = (Game.WIDTH / 2) - (size_x * 20) / 2;
				int hotbar_y = Game.HEIGHT - 23;
				for (int i = 0; i < size_x; i++) {
					int item_x = hotbar_x + i * 20;
					if (mouseInput.mouseOverLocalVar(item_x, hotbar_y, 20, 20)) {
						Item itemInventory = hotbarItems.get(i);
						try {
							Item newHolding = (Item) itemInventory.clone();
							newHolding.setAmount(Math.round(itemInventory.getAmount() / 2));
							itemInventory.setAmount(itemInventory.getAmount() - newHolding.getAmount());
							if (newHolding.getAmount() <= 0 || itemInventory.getAmount() <= 0) {
								return;
							}
							mouse_holding = newHolding;
							hotbarItems.put(i, itemInventory);
							return;
						} catch (CloneNotSupportedException e1) {
							e1.printStackTrace();
						}
					}
				}
			}

			boolean in_inventory = false;
			if (inventory_open) {
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
									Item itemInventory = inventoryItems.get(new Point(x, y));
									try {
										Item newHolding = (Item) itemInventory.clone();
										newHolding.setAmount(Math.round(itemInventory.getAmount() / 2));
										itemInventory.setAmount(itemInventory.getAmount() - newHolding.getAmount());
										if (newHolding.getAmount() <= 0 || itemInventory.getAmount() <= 0) {
											return;
										}
										mouse_holding = newHolding;
										inventoryItems.put(new Point(x, y), itemInventory);
										return;
									} catch (CloneNotSupportedException e1) {
										e1.printStackTrace();
									}
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
									Item itemHolding = mouse_holding;
									try {
										Item inventoryItem = (Item) itemHolding.clone();
										itemHolding.setAmount(itemHolding.getAmount() - 1);
										inventoryItem.setAmount(1);
										if (itemHolding.getAmount() <= 0) {
											mouse_holding = null;
										}
										inventoryItems.put(new Point(x, y), inventoryItem);
										return;
									} catch (CloneNotSupportedException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
								} else {
									Item itemInventory = inventoryItems.get(new Point(x, y));
									if (itemInventory.getAmount() + 1 <= MAX_STACK_SIZE) {
										mouse_holding.setAmount(mouse_holding.getAmount() - 1);
										itemInventory.setAmount(itemInventory.getAmount() + 1);
										if (mouse_holding.getAmount() <= 0) {
											mouse_holding = null;
										}
										return;
									}
								}
							}
						}
					}
				}
			}
			if (!in_inventory && mouse_holding != null) {
				try {
					Item itemGround = (Item) mouse_holding.clone();
					itemGround.setAmount(1);
					mouse_holding.setAmount(mouse_holding.getAmount() - 1);
					if (mouse_holding.getAmount() <= 0) {
						mouse_holding = null;
					}
					handler.addObject(4, new ItemGround(mouseInput.getMouseWorldCoords().x - 8,
							mouseInput.getMouseWorldCoords().y - 8, 1, ID.Item, itemGround));
					return;
				} catch (CloneNotSupportedException e1) {
					e1.printStackTrace();
				}

			}
		}
	}

	public void pickupItem(GameObject obj) {

		ITEM_ID itemType = ITEM_ID.NULL;
		int add_amount = 1;
		if (obj.getId() == ID.Item) {
			ItemGround itemGround = (ItemGround) obj;
			itemType = itemGround.getInventoryItem().getItemType();
			add_amount = itemGround.getInventoryItem().getAmount();
		} else {
			if (obj.getItem() == null)
				return;
			itemType = obj.getItem().getItemType();
			add_amount = obj.getItem().getAmount();
		}
		if (checkInventoryItem(itemType)) {
			for (int y = 0; y < size_y; y++) {
				for (int x = 0; x < size_x; x++) {
					if (inventoryItems.containsKey(new Point(x, y))) {
						Item item = inventoryItems.get(new Point(x, y));
						if (item.getItemType() == itemType) {
							int item_amount = item.getAmount();
							int sum_amount = item_amount + add_amount;
							// needs work
							if (item_amount < MAX_STACK_SIZE && sum_amount <= MAX_STACK_SIZE) {
								item.setAmount(sum_amount);
								handler.findAndRemoveObject(obj, world);
								return;
							} else {
								if (checkItemExistsBelowStackSize(item.getItemType(), add_amount)) {
									continue;
								} else {
									addItemToInventory(handler, world, obj);
									return;
								}
							}
						}
					}
				}
			}
		} else {
			addItemToInventory(handler, world, obj);
		}

	}

	private void addItemToInventory(Handler handler, World world, GameObject obj) {
		if (checkInventoryFreeSlots()) {
			for (int y = 0; y < size_y; y++) {
				for (int x = 0; x < size_x; x++) {
					if (!inventoryItems.containsKey(new Point(x, y))) {
						if (obj.getId() == ID.Item) {
							ItemGround item_ground = (ItemGround) obj;
							inventoryItems.put(new Point(x, y), item_ground.getInventoryItem());
							handler.findAndRemoveObject(obj, world);
							return;
						} else {
							Item item = createNewItem(obj);
							if (item == null)
								return;
							inventoryItems.put(new Point(x, y), item);

							handler.findAndRemoveObject(obj, world);
							return;
						}
					}
				}
			}
		}
	}

	private Item createNewItem(GameObject obj) {
		ID id = obj.getId();
		if (id == ID.Item) {
			ItemGround itemGround = (ItemGround) obj;
			return itemGround.getInventoryItem();
		}
		return obj.getItem();
	}

	private boolean checkItemExistsBelowStackSize(ITEM_ID itemType, int add_amount) {
		boolean ret = false;
		for (int y = 0; y < size_y; y++) {
			for (int x = 0; x < size_x; x++) {
				if (inventoryItems.containsKey(new Point(x, y))) {
					Item item = inventoryItems.get(new Point(x, y));
					if (item.getItemType() == itemType) {
						if (item.getAmount() < MAX_STACK_SIZE && item.getAmount() + add_amount <= MAX_STACK_SIZE) {
							ret = true;
						}
					}
				}
			}
		}
		return ret;
	}

	private boolean checkInventoryItem(ITEM_ID itemType) {
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

	public void setWorld(World world) {
		this.world = world;
	}

	/*
	 * public void addItem(Point point, Item item) { this.items.put(point, item); }
	 * 
	 * public Item getItem(Point point) { return this.items.get(point); }
	 */

}
