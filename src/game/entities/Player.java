package game.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.Random;

import game.inventory.Inventory;
import game.items.ITEM_ID;
import game.items.Item;
import game.items.ItemRock;
import game.main.Game;
import game.main.GameObject;
import game.main.ID;
import game.main.KeyInput;
import game.textures.Animation;
import game.textures.Textures;
import game.world.BIOME;
import game.world.World;

public class Player extends GameObject {

	Random r = new Random();
	private KeyInput keyInput;

	private String direction;
	private int health, food, water;
	private static int needs_timer = 0;

	private Animation idle_down, idle_up, idle_left, idle_right;
	private Animation walk_down, walk_up, walk_left, walk_right;
	private Textures textures;

	public Inventory inventory;
	public Inventory hotbar;

	public Player(int x, int y, int z_index, ID id, KeyInput keyInput, Textures textures) {
		super(x, y, z_index, id);
		this.keyInput = keyInput;
		this.textures = textures;
		this.direction = "down";

		this.inventory = new Inventory(5, 5);
		this.inventory.addItem(new ItemRock(5, ITEM_ID.Rock));
		this.inventory.addItem(new ItemRock(300, ITEM_ID.Rock));

		this.hotbar = new Inventory(5, 1);
		int hotbar_x = Game.WIDTH / 2 - this.hotbar.getInventoryBounds().width / 2;
		int hotbar_y = Game.HEIGHT - this.hotbar.getInventoryBounds().height;
		this.hotbar.setXY(hotbar_x, hotbar_y);

		// default = 100
		this.health = 75;
		this.food = 75;
		this.water = 25;

		initAnimations();
	}

	private void initAnimations() {
		idle_down = new Animation(8, textures.playerImg.get(0), textures.playerImg.get(1), textures.playerImg.get(2),
				textures.playerImg.get(3));
		idle_up = new Animation(8, textures.playerImg.get(36), textures.playerImg.get(37), textures.playerImg.get(38),
				textures.playerImg.get(39));
		idle_left = new Animation(8, textures.playerImg.get(24), textures.playerImg.get(25), textures.playerImg.get(26),
				textures.playerImg.get(27));
		idle_right = new Animation(8, textures.playerImg.get(12), textures.playerImg.get(13),
				textures.playerImg.get(14), textures.playerImg.get(15));

		walk_down = new Animation(5, textures.playerImg.get(6), textures.playerImg.get(7), textures.playerImg.get(8),
				textures.playerImg.get(9), textures.playerImg.get(10), textures.playerImg.get(11));
		walk_up = new Animation(5, textures.playerImg.get(42), textures.playerImg.get(43), textures.playerImg.get(44),
				textures.playerImg.get(45), textures.playerImg.get(46), textures.playerImg.get(47));
		walk_left = new Animation(5, textures.playerImg.get(30), textures.playerImg.get(31), textures.playerImg.get(32),
				textures.playerImg.get(33), textures.playerImg.get(34), textures.playerImg.get(35));
		walk_right = new Animation(5, textures.playerImg.get(18), textures.playerImg.get(19),
				textures.playerImg.get(20), textures.playerImg.get(21), textures.playerImg.get(22),
				textures.playerImg.get(23));
	}

	public void tick() {

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

}
