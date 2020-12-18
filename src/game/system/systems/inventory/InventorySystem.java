package game.system.systems.inventory;

import game.assets.entities.Player;
import game.assets.items.item.Item;
import game.assets.items.Item_Ground;
import game.assets.items.item.Placeable;
import game.assets.tiles.tile.Tile;
import game.system.helpers.Helpers;
import game.system.helpers.Timer;
import game.system.main.*;
import game.system.inputs.MouseInput;
import game.system.systems.gameObject.GameObject;
import game.system.systems.gameObject.HasItem;
import game.system.systems.particles.Particle_String;
import game.system.world.Chunk;
import game.system.world.World;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.io.Serializable;
import java.util.ArrayList;

public class InventorySystem implements Serializable {
	public static final int slot_w = 18, slot_h = 18;
	public static final int item_w = 16, item_h = 16;
	public static final int stackSize = 99;
	public static final Color slot_bg = new Color(0, 0, 0, 127);
	public static final Color slot_outline = new Color(20, 20, 20, 255);
	public static final Color slot_hover = new Color(255, 255, 255, 50);

	private transient Handler handler;
	private transient MouseInput mouseInput;
	private transient World world;
	private transient Inventory player_inv;
	private transient Camera cam;
	public transient Inventory player_hotbar;

	public int hotbar_selected = 0;
	private ArrayList<Inventory> open_inventories = new ArrayList<>();

	//public static boolean player_inventory_open = true;
	private Item holding = null;

	private Timer placeTimer = new Timer(10);

	public InventorySystem() {}

	public void setRequirements(Handler handler, MouseInput mouseInput, World world, Player player, Camera cam) {
		this.handler = handler;
		this.mouseInput = mouseInput;
		this.world = world;
		this.player_hotbar = player.hotbar;
		this.open_inventories.clear();
		this.open_inventories.add(this.player_hotbar);
		this.player_inv = player.inventory;
		this.cam = cam;
	}

	public void tick() {
		placeTimer.tick();
		for(int i=0; i<open_inventories.size(); i++) {
			Inventory inv = open_inventories.get(i);
			inv.tick();
		}

		if(!mouseOverInventory()) {
			mouseOutside();
		} else {
			Inventory inv = getHoveredInventory();
			if(inv.isMoveable() && mouseInput.leftMouseDown()) {
				if(mouseInput.mouseOverLocalRect(inv.getInventoryMoveBounds())) {
					inv.setXY(mouseInput.mouse_x - inv.getInventoryMoveBounds().width / 2, mouseInput.mouse_y + 12 / 2);
				}
			}
		}
	}

	public void renderCam(Graphics g) {
		if(isHolding()) {
			if(holding instanceof Placeable) {
				if(!mouseOverInventory()) {
					Graphics2D g2d = (Graphics2D) g;
					Point world_coords = Helpers.getWorldCoords(mouseInput.mouse_x, mouseInput.mouse_y, cam);
					Point tile_coords = Helpers.getTileCoords(world_coords, item_w, item_h);
					Rectangle bnds = new Rectangle(tile_coords.x, tile_coords.y, item_w, item_h);
					if(Helpers.getDistanceBetweenBounds(Game.world.getPlayer().getBounds(), bnds) < Game.world.getPlayer().REACH) {
						g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.8f));
					} else {
						g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f));
					}
					holding.render(g, tile_coords.x, tile_coords.y);
					g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
				}
			}
		}
	}

	public void render(Graphics g) {
		// NOTICE using this for loop instead of "Inventory inv : open_inventories"
		// will fix the removing inventory inside bug
		for(int i=0; i<open_inventories.size(); i++) {
			Inventory inv = open_inventories.get(i);
			inv.render(g);
			if(inv == player_hotbar) {
				Rectangle bnds = inv.getSlots().get(hotbar_selected).getBounds();
				g.setColor(new Color(255, 255, 255, 127));
				g.drawRect(bnds.x, bnds.y, bnds.width, bnds.height);
			}
		}
		if(isHolding()) {
			if(!(holding instanceof Placeable)) {
				holding.render(g, mouseInput.mouse_x - item_w / 2, mouseInput.mouse_y - item_h / 2);
			} else {
				if(mouseOverInventory()) {
					holding.render(g, mouseInput.mouse_x - item_w / 2, mouseInput.mouse_y - item_h / 2);
				}
			}
		}
	}

	public void mouseClicked(MouseEvent e) {
		if(mouseOverInventory()) {
			getHoveredInventory().mouseClick(e, mouseInput, this);
		}
	}

	public void mouseOutside() {
		if(mouseInput.leftMouseDown()) {
			if(isHolding()) {
				if(holding instanceof Placeable && placeTimer.timerOver()) {
					placeTimer.resetTimer();
					Point world_coords = Helpers.getWorldCoords(mouseInput.mouse_x, mouseInput.mouse_y, cam);
					Point tile_coords = Helpers.getTileCoords(world_coords, item_w, item_h);
					Rectangle bnds = new Rectangle(tile_coords.x, tile_coords.y, item_w, item_h);
					if(Helpers.getDistanceBetweenBounds(Game.world.getPlayer().getBounds(), bnds) < Game.world.getPlayer().REACH) {
						if (((Placeable) holding).place(tile_coords.x, tile_coords.y)) {
							holding.setAmount(holding.getAmount() - 1);
							if (holding.getAmount() <= 0) clearHolding();
						}
					} else {
						world.getPs().addParticle(new Particle_String(world.getPlayer().getX(), world.getPlayer().getY(), 0f, -0.5f, 30, "Cannot Reach!"));
					}
				}
			} else {
				if(Game.world.getPlayer().canAttack()) {
					Game.world.getPlayer().attack();
				}
			}
		} else if(mouseInput.rightMouseDown()) {
			if(placeTimer.timerOver()) {
				placeTimer.resetTimer();
				Point world_coords = Helpers.getWorldCoords(mouseInput.mouse_x, mouseInput.mouse_y, cam);
				Point tile_coords = Helpers.getTileCoords(world_coords, item_w, item_h);
				Chunk chunk = world.getChunkWithCoordsPoint(world.getChunkPointWithCoords(world_coords.x, world_coords.y));
				tile_coords.x = tile_coords.x / 16 - chunk.x;
				tile_coords.y = tile_coords.y / 16 - chunk.y;
				if (chunk.tileExistsCoords(3, tile_coords)) {
					Tile tile_found = chunk.getTileMap(3).get(tile_coords);
					Rectangle tile_bnds = new Rectangle(tile_found.getX(), tile_found.getY(), item_w, item_h);
					if (Helpers.getDistanceBetweenBounds(world.getPlayer().getBounds(), tile_bnds) < world.getPlayer().REACH) {
						if(tile_found instanceof HasItem) {
							Item tile_item = ((HasItem) tile_found).getItem();
							if (isHolding() && holding.getClass() == tile_item.getClass() && holding.getAmount() + tile_item.getAmount() <= stackSize) {
								holding.setAmount(holding.getAmount() + tile_item.getAmount());
							} else if (player_inv.canAcceptItem(tile_item)) {
								player_inv.addItem(tile_item);
							} else {
								dropItemAtPlayer(tile_item);
							}
							chunk.removeTile(tile_found);
							chunk.updateSameTiles(tile_found);
						}
					} else {
						world.getPs().addParticle(new Particle_String(world.getPlayer().getX(), world.getPlayer().getY(), 0f, -0.5f, 30, "Cannot Reach!"));
					}
				}
			}
		}
	}

	public void mouseWheelMoved(MouseWheelEvent e) {
		int new_index = hotbar_selected + e.getWheelRotation();
		if (new_index > player_hotbar.getSizeX() - 1) {
			new_index = 0;
		} else if (new_index < 0) {
			new_index = player_hotbar.getSizeX() - 1;
		}
		setHotbarSelected(new_index);
	}

	public void keyPressed(KeyEvent e) {
		if(holding != null) {
			switch (e.getKeyCode()) {
				case KeyEvent.VK_Q -> {
					Item_Ground item_gnd = holding.getItemGround();
					Point world_coords = Helpers.getWorldCoords(mouseInput.mouse_x - item_w / 2, mouseInput.mouse_y - item_h / 2, cam);
					item_gnd.setX(world_coords.x);
					item_gnd.setY(world_coords.y);
					dropItem(item_gnd);
				}
			}
		}
		switch (e.getKeyCode()) {
			case KeyEvent.VK_1 -> hotbarKeyPressed(1);
			case KeyEvent.VK_2 -> hotbarKeyPressed(2);
			case KeyEvent.VK_3 -> hotbarKeyPressed(3);
			case KeyEvent.VK_4 -> hotbarKeyPressed(4);
			case KeyEvent.VK_5 -> hotbarKeyPressed(5);
			case KeyEvent.VK_6 -> hotbarKeyPressed(6);
			case KeyEvent.VK_7 -> hotbarKeyPressed(7);
			case KeyEvent.VK_8 -> hotbarKeyPressed(8);
			case KeyEvent.VK_9 -> hotbarKeyPressed(9);
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
		if(obj instanceof HasItem) {
			Item item = ((HasItem) obj).getItem();
			if (item != null) {
				if (player_inv.canAcceptItem(item)) {
					player_inv.addItem(item);
					handler.findAndRemoveObject(obj);
				}
			}
		}
	}

	public void dropItem(Item_Ground item) {
		handler.addObject(item);
		holding = null;
	}

	public void dropItemAtPlayer(Item item) {
		Item_Ground item_g = item.getItemGround();
		item_g.setX(Game.world.getPlayer().getX());
		item_g.setY(Game.world.getPlayer().getY());
		handler.addObject(item_g);
	}

	public boolean inventoryIsOpen() {
		return this.open_inventories.size() > 1;
	}

	public boolean openInventoriesContains(Inventory inv) {
		return this.open_inventories.contains(inv);
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

	private boolean mouseOverInventory() {
		for(int i=0; i<open_inventories.size(); i++) {
			if(mouseInput.mouseOverLocalRect(open_inventories.get(i).getInventoryBounds())) {
				return true;
			}
		}
		return false;
	}

	public void setHotbarSelected(int index) {
		this.hotbar_selected = Helpers.clampInt(index, 0, this.player_hotbar.getSizeX() - 1);
	}

	private void hotbarKeyPressed(int index) {
		index -= 1;
		setHotbarSelected(index);
		if(isHolding()) {
			Item hotbar_item = getHotbarSelectedItem();
			if(hotbar_item == null) {
				setHotbarSelectedItem(holding);
				clearHolding();
			}
		} else if(mouseOverInventory()) {
			Inventory inv = getHoveredInventory();
			for(int i=0; i<inv.getSlots().size(); i++) {
				InventorySlot invSlot = inv.getSlots().get(i);
				if(mouseInput.mouseOverLocalRect(invSlot.getBounds())) {
					Item hotbar_item = getHotbarSelectedItem();
					setHotbarSelectedItem(invSlot.getItem());
					invSlot.setItem(hotbar_item);
				}
			}
		}
	}

	public Inventory getHoveredInventory() {
		for(int i=0; i<open_inventories.size(); i++) {
			Inventory inv = open_inventories.get(i);
			if(mouseInput.mouseOverLocalRect(inv.getInventoryBounds())) {
				return inv;
			}
		}
		return null;
	}

	public void addHotbarSelected(int amount) {
		int new_index = this.hotbar_selected + amount;
		if(new_index > this.player_hotbar.getSizeX() - 1) {
			this.setHotbarSelected(0);
		} else {
			this.setHotbarSelected(new_index);
		}
	}

	public void subHotbarSelected(int amount) {
		int new_index = this.hotbar_selected - amount;
		if(new_index < 0) {
			this.setHotbarSelected(this.player_hotbar.getSizeX()-1);
		} else {
			this.setHotbarSelected(new_index);
		}
	}

	public Item getHotbarSelectedItem() {
		return this.player_hotbar.getSlots().get(hotbar_selected).getItem();
	}

	public void setHotbarSelectedItem(Item item) {
		this.player_hotbar.getSlots().get(hotbar_selected).setItem(item);
	}

}
