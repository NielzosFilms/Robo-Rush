package game.assets.entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Random;

import game.assets.items.Item_Crate;
import game.assets.items.tools.Tool_WoodenAxe;
import game.assets.items.tools.Tool_WoodenPickaxe;
import game.assets.items.tools.Tool_WoodenSword;
import game.system.inventory.Inventory;
import game.enums.ITEM_ID;
import game.assets.items.Item;
import game.assets.items.Item_Rock;
import game.assets.items.Item_Stick;
import game.system.inventory.InventorySystem;
import game.system.main.Game;
import game.system.main.GameObject;
import game.enums.ID;
import game.system.inputs.KeyInput;
import game.enums.BIOME;
import game.system.main.Logger;
import game.system.world.World;
import game.textures.Animation;
import game.textures.Textures;

public class Player extends GameObject {
	private static final int ATTACK_DELAY = 15;
	private static final int DEFAULT_ATTACK_DAMAGE = 1;
	public final int REACH = 50;
	Random r = new Random();
	private KeyInput keyInput;

	private String direction;
	private int health, food, water;
	private static int needs_timer = 0;
	private int attack_timer = 0;

	private Animation idle_down, idle_up, idle_left, idle_right;
	private Animation walk_down, walk_up, walk_left, walk_right;

	public Inventory inventory;
	public Inventory hotbar;

	public Player(int x, int y, int z_index, ID id, KeyInput keyInput) {
		super(x, y, z_index, id);
		this.keyInput = keyInput;
		this.direction = "down";

		this.inventory = new Inventory(5, 5);
		this.inventory.addItem(new Item_Rock(InventorySystem.stackSize, ITEM_ID.Rock));
		this.inventory.addItem(new Item_Stick(InventorySystem.stackSize, ITEM_ID.Stick));
		this.inventory.addItem(new Item_Crate(InventorySystem.stackSize, ITEM_ID.Crate));

		this.hotbar = new Inventory(5, 1);
		int hotbar_x = Game.WIDTH / 2 - this.hotbar.getInventoryBounds().width / 2;
		int hotbar_y = Game.HEIGHT - this.hotbar.getInventoryBounds().height;
		this.hotbar.setXY(hotbar_x, hotbar_y);
		this.hotbar.addItem(new Tool_WoodenAxe(ITEM_ID.Wooden_Axe));
		this.hotbar.addItem(new Tool_WoodenPickaxe(ITEM_ID.Wooden_Pickaxe));
		this.hotbar.addItem(new Tool_WoodenSword(ITEM_ID.Wooden_Sword));

		// default = 100
		this.health = 75;
		this.food = 75;
		this.water = 25;

		initAnimations();
	}

	private void initAnimations() {
		idle_down = new Animation(8, Textures.playerImg.get(0), Textures.playerImg.get(1), Textures.playerImg.get(2),
				Textures.playerImg.get(3));
		idle_up = new Animation(8, Textures.playerImg.get(36), Textures.playerImg.get(37), Textures.playerImg.get(38),
				Textures.playerImg.get(39));
		idle_left = new Animation(8, Textures.playerImg.get(24), Textures.playerImg.get(25), Textures.playerImg.get(26),
				Textures.playerImg.get(27));
		idle_right = new Animation(8, Textures.playerImg.get(12), Textures.playerImg.get(13),
				Textures.playerImg.get(14), Textures.playerImg.get(15));

		walk_down = new Animation(5, Textures.playerImg.get(6), Textures.playerImg.get(7), Textures.playerImg.get(8),
				Textures.playerImg.get(9), Textures.playerImg.get(10), Textures.playerImg.get(11));
		walk_up = new Animation(5, Textures.playerImg.get(42), Textures.playerImg.get(43), Textures.playerImg.get(44),
				Textures.playerImg.get(45), Textures.playerImg.get(46), Textures.playerImg.get(47));
		walk_left = new Animation(5, Textures.playerImg.get(30), Textures.playerImg.get(31), Textures.playerImg.get(32),
				Textures.playerImg.get(33), Textures.playerImg.get(34), Textures.playerImg.get(35));
		walk_right = new Animation(5, Textures.playerImg.get(18), Textures.playerImg.get(19),
				Textures.playerImg.get(20), Textures.playerImg.get(21), Textures.playerImg.get(22),
				Textures.playerImg.get(23));
	}

	public void tick() {
		if(attack_timer > 0) attack_timer--;

		updateAnimations();

		int walk_speed = 2;
		if (keyInput.keysDown[5]) {
			walk_speed = 10;
		}

		if (keyInput.keysDown[2] && !keyInput.keysDown[3]) {
			velX = -walk_speed;
			direction = "left";
		} else if (keyInput.keysDown[3] && !keyInput.keysDown[2]) {
			velX = walk_speed;
			direction = "right";
		} else {
			velX = 0;
		}

		if (keyInput.keysDown[0] && !keyInput.keysDown[1]) {
			velY = -walk_speed;
			direction = "up";
		} else if (keyInput.keysDown[1] && !keyInput.keysDown[0]) {
			velY = walk_speed;
			direction = "down";
		} else {
			velY = 0;
		}
		// misschien powerup voor reverse gravity

		// velY = Game.clampDouble(velY, -9.8, 9.8);

		velX = velX;
		velY = velY;
		/*
		 * if(!space && velY < 0)velY = velY + accY*3; else velY = velY + accY;
		 */

		if (velX < 0) {
			x = (int) Math.floor(x + velX);
		} else if (velX > 0) {
			x = (int) Math.ceil(x + velX);
		} else {
			x = (int) Math.round(x + velX);
		}

		if (velY < 0) {
			y = (int) Math.floor(y + velY);
		} else if (velY > 0) {
			y = (int) Math.ceil(y + velY);
		} else {
			y = (int) Math.round(y + velY);
		}

		// x = Game.clamp(x, -13, 800-50);

		// y = Game.clamp(y, 0, Game.HEIGHT);
		needs_timer++;
		if (needs_timer >= 60) {
			needs_timer = 0;
			this.health = r.nextInt(100) + 1;
			this.food = r.nextInt(100) + 1;
			this.water = r.nextInt(100) + 1;
		}
	}

	private void updateVelocity() {

	}

	private void updateAnimations() {
		if (direction == "down") {
			if (velY == 0) {
				idle_down.runAnimation();
			} else {
				walk_down.runAnimation();
			}
		} else if (direction == "up") {
			if (velY == 0) {
				idle_up.runAnimation();
			} else {
				walk_up.runAnimation();
			}
		} else if (direction == "left") {
			if (velX == 0) {
				idle_left.runAnimation();
			} else {
				walk_left.runAnimation();
			}
		} else if (direction == "right") {
			if (velX == 0) {
				idle_right.runAnimation();
			} else {
				walk_right.runAnimation();
			}
		} else {
			idle_down.runAnimation();
		}

	}

	public void render(Graphics g) {
		if (direction == "down") {
			if (velY == 0) {
				idle_down.drawAnimation(g, x, y);
			} else {
				walk_down.drawAnimation(g, x, y);
			}
		} else if (direction == "up") {
			if (velY == 0) {
				idle_up.drawAnimation(g, x, y);
			} else {
				walk_up.drawAnimation(g, x, y);
			}
		} else if (direction == "left") {
			if (velX == 0) {
				idle_left.drawAnimation(g, x, y);
			} else {
				walk_left.drawAnimation(g, x, y);
			}
		} else if (direction == "right") {
			if (velX == 0) {
				idle_right.drawAnimation(g, x, y);
			} else {
				walk_right.drawAnimation(g, x, y);
			}
		} else {
			idle_down.drawAnimation(g, x, y);
		}

		// g.setColor(Color.pink);
		// g.drawRect(getBounds().x, getBounds().y, getBounds().width,
		// getBounds().height);

		// if(Game.showHitboxes) g.drawRect(x+19, y+6, 13, 30);
	}

	public Rectangle getBounds() {
		return new Rectangle(x + 2, y + 12, 12, 12);
	}

	public Rectangle getSelectBounds() {
		return null;
	}

	public int getHealth() {
		return this.health;
	}

	public int getFood() {
		return this.food;
	}

	public int getWater() {
		return this.water;
	}

	public BIOME getCurrentBiome() {
		return World.getBiomeWithCoords(x, y);
	}

	public Item getItem() {
		return null;
	}

	public void interact() {
		Game.inventorySystem.addOpenInventory(inventory);
	}

	public void destroyed() {

	}

	@Override
	public void hit(int damage) {

	}

	public boolean canHit() {
		return attack_timer == 0;
	}

	public int getExpectedDamage() {
		// Get hotbar selected item
		// Calculate damage output
		int damage_output = DEFAULT_ATTACK_DAMAGE;
		int attack_delay = ATTACK_DELAY;
//		if(Game.inventorySystem.isHolding()) {
//			Item holding = Game.inventorySystem.getHolding();
//			damage_output += holding.getDamage();
//		}
		Item holding = Game.inventorySystem.getHotbarSelectedItem();
		if(holding != null) {
			damage_output += holding.getDamage();
			attack_delay += holding.getAttack_speed();
		}

		attack_timer = attack_delay;
		Logger.print("new attack_delay: " + attack_delay);
		return damage_output;
	}

}
