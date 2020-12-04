package game.assets.entities;

import java.awt.*;
import java.util.Random;

import game.assets.objects.crate.Item_Crate;
import game.assets.items.tools.wood.Tool_WoodenAxe;
import game.assets.items.tools.wood.Tool_WoodenPickaxe;
import game.assets.items.tools.wood.Tool_WoodenSword;
import game.assets.tiles.floor.wood.Item_FloorWood;
import game.system.audioEngine.AudioFiles;
import game.system.audioEngine.AudioPlayer;
import game.enums.DIRECTIONS;
import game.system.helpers.Helpers;
import game.system.helpers.Logger;
import game.system.systems.GameObject;
import game.system.systems.hitbox.Hitbox;
import game.system.systems.hitbox.HitboxContainer;
import game.system.systems.inventory.Inventory;
import game.enums.ITEM_ID;
import game.assets.items.Item;
import game.assets.objects.rock.Item_Rock;
import game.assets.objects.stick.Item_Stick;
import game.system.systems.inventory.InventorySystem;
import game.system.main.*;
import game.enums.ID;
import game.system.inputs.KeyInput;
import game.enums.BIOME;
import game.textures.Animation;
import game.enums.TEXTURE_LIST;
import game.textures.ImageFilters;
import game.textures.Texture;

public class Player extends GameObject {
	private static final int ATTACK_DELAY = 15;
	private static final int DEFAULT_ATTACK_DAMAGE = 1;
	public final int REACH = 50;
	Random r = new Random();
	private transient KeyInput keyInput;

	private DIRECTIONS direction;
	private int health, food, water;
	private int needs_timer = 0;
	private int attack_timer = 0;

	private boolean attacking = false;
	private int attacking_item_rot = 0;

	private Animation idle_down, idle_up, idle_left, idle_right;
	private Animation walk_down, walk_up, walk_left, walk_right;

	public Inventory inventory;
	public Inventory hotbar;

	public Player(int x, int y, int z_index, ID id, KeyInput keyInput) {
		super(x, y, z_index, id);
		this.keyInput = keyInput;
		this.direction = DIRECTIONS.down;

		this.inventory = new Inventory(5, 5);
		this.inventory.addItem(new Item_Rock(InventorySystem.stackSize));
		this.inventory.addItem(new Item_Stick(InventorySystem.stackSize));
		this.inventory.addItem(new Item_Crate(InventorySystem.stackSize));
		this.inventory.addItem(new Item_FloorWood(InventorySystem.stackSize));

		this.hotbar = new Inventory(5, 1);
		hotbar.setMoveable(false);
		int hotbar_x = Game.WIDTH / 2 - this.hotbar.getInventoryBounds().width / 2;
		int hotbar_y = Game.HEIGHT - this.hotbar.getInventoryBounds().height;
		this.hotbar.setXY(hotbar_x, hotbar_y);
		this.hotbar.addItem(new Tool_WoodenAxe());
		this.hotbar.addItem(new Tool_WoodenPickaxe());
		this.hotbar.addItem(new Tool_WoodenSword());

		// default = 100
		this.health = 75;
		this.food = 75;
		this.water = 25;

		initAnimations();
	}

	private void initAnimations() {
		idle_down = new Animation(8,
				new Texture(TEXTURE_LIST.player_list, 0, 0),
				new Texture(TEXTURE_LIST.player_list, 1, 0),
				new Texture(TEXTURE_LIST.player_list, 2, 0),
				new Texture(TEXTURE_LIST.player_list, 3, 0));
		idle_up = new Animation(8,
				new Texture(TEXTURE_LIST.player_list, 0, 6),
				new Texture(TEXTURE_LIST.player_list, 1, 6),
				new Texture(TEXTURE_LIST.player_list, 2, 6),
				new Texture(TEXTURE_LIST.player_list, 3, 6));
		idle_left = new Animation(8,
				new Texture(TEXTURE_LIST.player_list, 0, 4),
				new Texture(TEXTURE_LIST.player_list, 1, 4),
				new Texture(TEXTURE_LIST.player_list, 2, 4),
				new Texture(TEXTURE_LIST.player_list, 3, 4));
		idle_right = new Animation(8,
				new Texture(TEXTURE_LIST.player_list, 0, 2),
				new Texture(TEXTURE_LIST.player_list, 1, 2),
				new Texture(TEXTURE_LIST.player_list, 2, 2),
				new Texture(TEXTURE_LIST.player_list, 3, 2));

		walk_down = new Animation(5,
				new Texture(TEXTURE_LIST.player_list, 0, 1),
				new Texture(TEXTURE_LIST.player_list, 1, 1),
				new Texture(TEXTURE_LIST.player_list, 2, 1),
				new Texture(TEXTURE_LIST.player_list, 3, 1),
				new Texture(TEXTURE_LIST.player_list, 4, 1),
				new Texture(TEXTURE_LIST.player_list, 5, 1));
		walk_up = new Animation(5,
				new Texture(TEXTURE_LIST.player_list, 0, 7),
				new Texture(TEXTURE_LIST.player_list, 1, 7),
				new Texture(TEXTURE_LIST.player_list, 2, 7),
				new Texture(TEXTURE_LIST.player_list, 3, 7),
				new Texture(TEXTURE_LIST.player_list, 4, 7),
				new Texture(TEXTURE_LIST.player_list, 5, 7));
		walk_left = new Animation(5,
				new Texture(TEXTURE_LIST.player_list, 0, 5),
				new Texture(TEXTURE_LIST.player_list, 1, 5),
				new Texture(TEXTURE_LIST.player_list, 2, 5),
				new Texture(TEXTURE_LIST.player_list, 3, 5),
				new Texture(TEXTURE_LIST.player_list, 4, 5),
				new Texture(TEXTURE_LIST.player_list, 5, 5));
		walk_right = new Animation(5,
				new Texture(TEXTURE_LIST.player_list, 0, 3),
				new Texture(TEXTURE_LIST.player_list, 1, 3),
				new Texture(TEXTURE_LIST.player_list, 2, 3),
				new Texture(TEXTURE_LIST.player_list, 3, 3),
				new Texture(TEXTURE_LIST.player_list, 4, 3),
				new Texture(TEXTURE_LIST.player_list, 5, 3));
	}

	public void tick() {
		if(attack_timer > 0) attack_timer--;
		if(attacking) {
			attacking_item_rot += 5;
			if(attacking_item_rot > 90) {
				attacking = false;
				attacking_item_rot = 0;
			}
		}

		updateAnimations();

		int walk_speed = 2;
		if (keyInput.keysDown[5]) {
			walk_speed = 10;
		}

		if (keyInput.keysDown[2] && !keyInput.keysDown[3]) {
			velX = -walk_speed;
			direction = DIRECTIONS.left;
		} else if (keyInput.keysDown[3] && !keyInput.keysDown[2]) {
			velX = walk_speed;
			direction = DIRECTIONS.right;
		} else {
			velX = 0;
		}

		if (keyInput.keysDown[0] && !keyInput.keysDown[1]) {
			velY = -walk_speed;
			direction = DIRECTIONS.up;
		} else if (keyInput.keysDown[1] && !keyInput.keysDown[0]) {
			velY = walk_speed;
			direction = DIRECTIONS.down;
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
		if (direction == DIRECTIONS.down) {
			if (velY == 0) {
				idle_down.runAnimation();
			} else {
				walk_down.runAnimation();
			}
		} else if (direction == DIRECTIONS.up) {
			if (velY == 0) {
				idle_up.runAnimation();
			} else {
				walk_up.runAnimation();
			}
		} else if (direction == DIRECTIONS.left) {
			if (velX == 0) {
				idle_left.runAnimation();
			} else {
				walk_left.runAnimation();
			}
		} else if (direction == DIRECTIONS.right) {
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

		if (direction == DIRECTIONS.down) {
			if (velY == 0) {
				idle_down.drawAnimation(g, x, y);
			} else {
				walk_down.drawAnimation(g, x, y);
			}
		} else if (direction == DIRECTIONS.up) {
			if (velY == 0) {
				idle_up.drawAnimation(g, x, y);
			} else {
				walk_up.drawAnimation(g, x, y);
			}
		} else if (direction == DIRECTIONS.left) {
			if (velX == 0) {
				idle_left.drawAnimation(g, x, y);
			} else {
				walk_left.drawAnimation(g, x, y);
			}
		} else if (direction == DIRECTIONS.right) {
			if (velX == 0) {
				idle_right.drawAnimation(g, x, y);
			} else {
				walk_right.drawAnimation(g, x, y);
			}
		} else {
			idle_down.drawAnimation(g, x, y);
		}

		if(attacking) {
			Item holding = Game.world.getInventorySystem().getHotbarSelectedItem();
			if(holding != null) {
				ImageFilters.renderImageWithRotationFromCenter(g, holding.getTexture().getTexure(), x + 16, y, 16, 16,
						attacking_item_rot);
			}
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
		return Game.world.getBiomeWithCoords(x, y);
	}

	public Item getItem() {
		return null;
	}

	public void interact() {
		inventory.open();
	}

	public void destroyed() {

	}

	public void hit(int damage) {

	}

	public void attack() {
		attacking = true;
		//Item holding = Game.inventorySystem.getHotbarSelectedItem();
		int dmg = getExpectedDamage();
		AudioPlayer.playSound(AudioFiles.swing, 0.5f, false, 0);
		// TODO make direction function 8 way instead of 4
		Point screenCoords = Helpers.getScreenCoords((int)getBounds().getCenterX(), (int)getBounds().getCenterY(), Game.world.getCam());
		DIRECTIONS direction = Helpers.getDirection(screenCoords, new Point(Game.mouseInput.mouse_x, Game.mouseInput.mouse_y));
		//Logger.print(direction.name());
		//this.direction = direction;

		switch (direction) {
			case up -> {
				Game.world.getHitboxSystem().addHitboxContainer(new HitboxContainer(new Hitbox[]{
						new Hitbox(x-8, y-16, 32, 16, 0, 5, dmg),
				}, this));
			}
			case down -> {
				Game.world.getHitboxSystem().addHitboxContainer(new HitboxContainer(new Hitbox[]{
						new Hitbox(x-8, y+getBounds().height + 16, 32, 16, 0, 5, dmg),
				}, this));
			}
			case left -> {
				Game.world.getHitboxSystem().addHitboxContainer(new HitboxContainer(new Hitbox[]{
						new Hitbox(x-24, y, 16, 32, 0, 5, dmg),
				}, this));
			}
			case right -> {
				Game.world.getHitboxSystem().addHitboxContainer(new HitboxContainer(new Hitbox[]{
						new Hitbox(x+getBounds().width + 16, y, 16, 32, 0, 5, dmg),
				}, this));
			}
		}
	}

	public boolean canAttack() {
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
		Item holding = Game.world.getInventorySystem().getHotbarSelectedItem();
		if(holding != null) {
			damage_output += holding.getDamage();
			attack_delay += holding.getAttack_speed();
		}

		attack_timer = attack_delay;
		//Logger.print("new attack_delay: " + attack_delay);
		return damage_output;
	}

	public void setKeyInput(KeyInput keyInput) {
		this.keyInput = keyInput;
	}

}
