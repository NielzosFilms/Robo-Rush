package game.assets.entities;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.LinkedList;
import java.util.Random;

import game.assets.entities.bullets.Bullet;
import game.assets.items.item.CanAttack;
import game.assets.items.tools.iron.Tool_Iron_Axe;
import game.assets.objects.crate.Item_Crate;
import game.assets.items.tools.wood.Tool_WoodenAxe;
import game.assets.items.tools.wood.Tool_WoodenPickaxe;
import game.assets.items.tools.wood.Tool_WoodenSword;
import game.audio.SoundEffect;
import game.enums.DIRECTIONS;
import game.system.helpers.Helpers;
import game.system.helpers.Timer;
import game.system.systems.gameObject.Bounds;
import game.system.systems.gameObject.GameObject;
import game.system.systems.gameObject.Hitable;
import game.system.systems.gameObject.Interactable;
import game.system.systems.hitbox.Hitbox;
import game.system.systems.hitbox.HitboxContainer;
import game.system.systems.inventory.Inventory;
import game.assets.items.item.Item;
import game.assets.objects.rock.Item_Rock;
import game.assets.objects.stick.Item_Stick;
import game.system.systems.inventory.InventorySystem;
import game.system.main.*;
import game.enums.ID;
import game.system.inputs.KeyInput;
import game.enums.BIOME;
import game.system.systems.inventory.inventoryDef.InventoryDef;
import game.textures.*;

import static java.lang.Math.pow;

public class Player extends GameObject implements Bounds, Interactable, Hitable {
	private static final int ATTACK_DELAY = 30;
	private static final int DEFAULT_ATTACK_DAMAGE = 1;
	private float acceleration = 0.2f, deceleration = 0.3f;
	public final int REACH = 50;
	Random r = new Random();
	private transient KeyInput keyInput;

	private DIRECTIONS direction;
	private DIRECTIONS attack_dir;
	private int health, food, water;

	private Timer attack_timer = new Timer(ATTACK_DELAY);
	private Timer needs_timer = new Timer(60);

	private boolean attacking = false;
	private float attacking_item_rot = 0;
	private float attacking_item_rot_vel = 0;
	private float time;

	private Texture hand = new Texture(TEXTURE_LIST.player_list, 4, 0);
	private Animation idle, run, blink, hurt, dash_start, dash_end;
	private Texture dash_idle = new Texture(TEXTURE_LIST.player_list, 4, 4);
	private Timer idle_timer = new Timer(300);
	private boolean hurt_animation = false;

	private Timer dash_cooldown = new Timer(60);
	private Timer dash = new Timer(20);
	private final float dash_speed = 8f;
	private boolean dashing = false;
	private DIRECTIONS dash_direction = DIRECTIONS.up;
	private Timer particle_timer = new Timer(5);

	private Animation attack_slice;

	public Inventory inventory;
	public Inventory hotbar;

	private LinkedList<Object> items = new LinkedList<>();

	public Player(int x, int y, int z_index, ID id, KeyInput keyInput) {
		super(x, y, z_index, id);
		this.keyInput = keyInput;
		this.direction = DIRECTIONS.down;

		this.inventory = new Inventory(5, 5);
		this.inventory.addItem(new Item_Rock(InventorySystem.stackSize));
		this.inventory.addItem(new Item_Stick(InventorySystem.stackSize));
		this.inventory.addItem(new Item_Crate(InventorySystem.stackSize));

		this.hotbar = new Inventory(5, 1);
		this.hotbar.setHotbar(true);
		hotbar.setMoveable(false);
		int hotbar_x = Game.WIDTH / 2 - this.hotbar.getInventoryBounds().width / 2;
		int hotbar_y = Game.HEIGHT - this.hotbar.getInventoryBounds().height;
		this.hotbar.setXY(hotbar_x, hotbar_y);
		this.hotbar.addItem(new Tool_WoodenAxe());
		this.hotbar.addItem(new Tool_Iron_Axe());
		this.hotbar.addItem(new Tool_WoodenSword());

		// default = 100
		this.health = 75;
		this.food = 75;
		this.water = 25;

		initAnimations();
	}

	private void initAnimations() {
		idle = new Animation(100,
				new Texture(TEXTURE_LIST.player_list, 0, 0),
				new Texture(TEXTURE_LIST.player_list, 1, 0),
				new Texture(TEXTURE_LIST.player_list, 2, 0),
				new Texture(TEXTURE_LIST.player_list, 3, 0));

		blink = new Animation(200,
				new Texture(TEXTURE_LIST.player_list, 0, 0),
				new Texture(TEXTURE_LIST.player_list, 0, 1),
				new Texture(TEXTURE_LIST.player_list, 1, 1),
				new Texture(TEXTURE_LIST.player_list, 0, 1),
				new Texture(TEXTURE_LIST.player_list, 1, 1),
				new Texture(TEXTURE_LIST.player_list, 0, 0),
				new Texture(TEXTURE_LIST.player_list, 0, 0));

		run = new Animation(100,
				new Texture(TEXTURE_LIST.player_list, 0, 2),
				new Texture(TEXTURE_LIST.player_list, 1, 2),
				new Texture(TEXTURE_LIST.player_list, 2, 2),
				new Texture(TEXTURE_LIST.player_list, 3, 2),
				new Texture(TEXTURE_LIST.player_list, 4, 2),
				new Texture(TEXTURE_LIST.player_list, 5, 2));

		hurt = new Animation(100,
				//new Texture(TEXTURE_LIST.player_list, 0, 3),
				new Texture(TEXTURE_LIST.player_list, 1, 3),
				new Texture(TEXTURE_LIST.player_list, 2, 3),
				new Texture(TEXTURE_LIST.player_list, 3, 3),
				new Texture(TEXTURE_LIST.player_list, 2, 3),
				new Texture(TEXTURE_LIST.player_list, 3, 3),
				new Texture(TEXTURE_LIST.player_list, 0, 3));

		dash_start = new Animation(50,
				new Texture(TEXTURE_LIST.player_list, 1, 4),
				new Texture(TEXTURE_LIST.player_list, 2, 4),
				new Texture(TEXTURE_LIST.player_list, 3, 4));

		dash_end = new Animation(100,
				//new Texture(TEXTURE_LIST.player_list, 5, 4),
				new Texture(TEXTURE_LIST.player_list, 6, 4),
				new Texture(TEXTURE_LIST.player_list, 7, 4),
				new Texture(TEXTURE_LIST.player_list, 1, 4));

		attack_slice = new Animation(50,
				//new Texture(TEXTURE_LIST.attack_slice_list, 2, 0),
				new Texture(TEXTURE_LIST.attack_slice_list, 0, 1),
				new Texture(TEXTURE_LIST.attack_slice_list, 1, 1),
				new Texture(TEXTURE_LIST.attack_slice_list, 2, 1),
				new Texture(TEXTURE_LIST.attack_slice_list, 0, 2),
				new Texture(TEXTURE_LIST.attack_slice_list, 1, 2),
				new Texture(TEXTURE_LIST.attack_slice_list, 2, 2)
				);
	}

	public void tick() {
		buffer_x += velX;
		buffer_y += velY;
		x = Math.round(buffer_x);
		y = Math.round(buffer_y);

		attack_timer.tick();
		if(attacking) {
			//attack_slice.runAnimation();
			attacking_item_rot += attacking_item_rot_vel;
			if(attacking_item_rot >= 90) {
				attacking_item_rot_vel -= 0.3f * (attacking_item_rot_vel+1);
				if(attacking_item_rot_vel <= 0) {
					attacking = false;
					attacking_item_rot = 0;
					attacking_item_rot_vel = 0;
				}
			} else {
				attacking_item_rot_vel += 0.3f * (attacking_item_rot_vel+1);
			}
			/*if(attack_slice.animationEnded()) {
				attacking = false;
				attacking_item_rot = 0;
				attack_slice.resetAnimation();
			}*/
		}

		updateAnimations();

		int walk_speed = 1;

		if(dash.timerOver()) {
			if (keyInput.keysDown[0] && !keyInput.keysDown[1]) {
				velY += (-walk_speed - velY) * acceleration;
				direction = DIRECTIONS.up;
			} else if (keyInput.keysDown[1] && !keyInput.keysDown[0]) {
				velY += (walk_speed - velY) * acceleration;
				direction = DIRECTIONS.down;
			} else {
				velY -= (velY) * deceleration;
				if (velY < 0.01f && velY > -0.01f) velY = 0;
			}

			if (keyInput.keysDown[2] && !keyInput.keysDown[3]) {
				velX += (-walk_speed - velX) * acceleration;
				direction = DIRECTIONS.left;
			} else if (keyInput.keysDown[3] && !keyInput.keysDown[2]) {
				velX += (walk_speed - velX) * acceleration;
				direction = DIRECTIONS.right;
			} else {
				velX -= (velX) * deceleration;
				if (velX < 0.01f && velX > -0.01f) velX = 0;
			}
		} else {
			velX -= (velX) * deceleration;
			if (velX < 0.01f && velX > -0.01f) velX = 0;
			velY -= (velY) * deceleration;
			if (velY < 0.01f && velY > -0.01f) velY = 0;
		}

		if(dash.timerOver()) {
			if(keyInput.keysDown[0] && keyInput.keysDown[2]) {
				dash_direction = DIRECTIONS.up_left;
			} else if(keyInput.keysDown[0] && keyInput.keysDown[3]) {
				dash_direction = DIRECTIONS.up_right;
			} else if(keyInput.keysDown[1] && keyInput.keysDown[2]) {
				dash_direction = DIRECTIONS.down_left;
			} else if(keyInput.keysDown[1] && keyInput.keysDown[3]) {
				dash_direction = DIRECTIONS.down_right;
			} else if(keyInput.keysDown[0]) {
				dash_direction = DIRECTIONS.up;
			} else if(keyInput.keysDown[1]) {
				dash_direction = DIRECTIONS.down;
			} else if(keyInput.keysDown[2]) {
				dash_direction = DIRECTIONS.left;
			} else if(keyInput.keysDown[3]) {
				dash_direction = DIRECTIONS.right;
			}
		}

		if (keyInput.keysDown[5]) {
			if(dash_cooldown.timerOver()) {
				dashing = true;
				dash_cooldown.resetTimer();
				dash.resetTimer();
			}
		}
		if(!dash.timerOver()) {
			switch(dash_direction) {
				case up:
					velY += (-dash_speed - velY) * acceleration;
					break;
				case down:
					velY += (dash_speed - velY) * acceleration;
					break;
				case left:
					velX += (-dash_speed - velX) * acceleration;
					break;
				case right:
					velX += (dash_speed - velX) * acceleration;
					break;
				case up_left:
					velY += ((dash_speed*Math.sin(Math.toRadians(-135))) - velY) * acceleration;
					velX += ((dash_speed*Math.cos(Math.toRadians(-135))) - velX) * acceleration;
					break;
				case up_right:
					velY += ((dash_speed*Math.sin(Math.toRadians(-45))) - velY) * acceleration;
					velX += ((dash_speed*Math.cos(Math.toRadians(-45))) - velX) * acceleration;
					break;
				case down_left:
					velY += ((dash_speed*Math.sin(Math.toRadians(135))) - velY) * acceleration;
					velX += ((dash_speed*Math.cos(Math.toRadians(135))) - velX) * acceleration;
					break;
				case down_right:
					velY += ((dash_speed*Math.sin(Math.toRadians(45))) - velY) * acceleration;
					velX += ((dash_speed*Math.cos(Math.toRadians(45))) - velX) * acceleration;
					break;
			}
			if(particle_timer.timerOver()) {
				Game.gameController.getPs().addParticle(new Effect_Texture(x, y, new Texture(TEXTURE_LIST.player_list, 7, 3), 10, 0.5f));
				particle_timer.resetTimer();
			}
			particle_timer.tick();
		}
		dash.tick();
		dash_cooldown.tick();
	}

	private void updateVelocity() {

	}

	private void updateAnimations() {
		if(hurt_animation) {
			if(hurt.animationEnded()) {
				hurt_animation = false;
				hurt.resetAnimation();
			}
			hurt.runAnimation();
		} else {
			if(dashing) {
				if(!dash.timerOver()) {
					dash_start.runAnimation();
				} else {
					if(dash_end.animationEnded()) {
						dashing = false;
					}
					dash_end.runAnimation();
				}
			} else {
				dash_start.resetAnimation();
				dash_end.resetAnimation();
				if (velX == 0 && velY == 0) {
					if (idle_timer.timerOver()) {
						blink.runAnimation();
						if (blink.animationEnded()) {
							blink.resetAnimation();
							idle_timer.resetTimer();
						}
					} else {
						idle.runAnimation();
					}
					idle_timer.tick();
				} else {
					idle_timer.resetTimer();
					blink.resetAnimation();
					run.runAnimation();
				}
			}
		}
	}

	public void render(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g.drawImage(Textures.entity_shadow, getBounds().x-3, getBounds().y+2, 16, 16, null);

		Point mouse = Game.mouseInput.getMouseWorldCoords();
		boolean mirror = mouse.x < this.x;//getBounds().getCenterX();

		int cenX = (int)getBounds().getCenterX();
		int cenY = (int)getBounds().getCenterY();
		int angle = Math.round(Helpers.getAngle(new Point(cenX, cenY), mouse));
		Item holding = Game.gameController.getInventorySystem().getHotbarSelectedItem();

		AffineTransform backup = g2d.getTransform();
		AffineTransform rotation_transform = new AffineTransform(backup);
		rotation_transform.rotate(Math.toRadians(angle), cenX, cenY);
		g2d.setTransform(rotation_transform);
		if(holding != null) {
			g.drawImage(holding.getTexture().getTexure(), cenX + 6, cenY - 13, 16, 16, null);
		}
		g.drawImage(hand.getTexure(), cenX+8, cenY-2, 16, 24, null);
		g2d.setTransform(backup);

		drawPlayerAnimations(g, mirror);

		if(Game.DEBUG_MODE) {
			g.setColor(new Color(255, 108, 252, 92));
			//Helpers.drawBounds(g, this);
			g.drawRect(x, y, 1, 1);
		}
	}

	private void drawPlayerAnimations(Graphics g, boolean mirror) {
		if(hurt_animation) {
			if(mirror) {
				hurt.drawAnimationMirroredH(g, x, y);
			} else {
				hurt.drawAnimation(g, x, y);
			}
		} else {
			if(dashing) {
				if(!dash.timerOver()) {
					if(dash_start.animationEnded()) {
						if(mirror) {
							ImageFilters.renderImageMirroredH(g, dash_idle.getTexure(), x, y);
						} else {
							g.drawImage(dash_idle.getTexure(), x, y, null);
						}
					} else {
						if(mirror) {
							dash_start.drawAnimationMirroredH(g, x, y);
						} else {
							dash_start.drawAnimation(g, x, y);
						}
					}
				} else {
					if(mirror) {
						dash_end.drawAnimationMirroredH(g, x, y);
					} else {
						dash_end.drawAnimation(g, x, y);
					}
				}
			} else {
				if (velX == 0 && velY == 0) {
					if (mirror) {
						if (idle_timer.timerOver()) {
							blink.drawAnimationMirroredH(g, x, y);
						} else {
							idle.drawAnimationMirroredH(g, x, y);
						}
					} else {
						if (idle_timer.timerOver()) {
							blink.drawAnimation(g, x, y);
						} else {
							idle.drawAnimation(g, x, y);
						}
					}
				} else {
					if (mirror) {
						run.drawAnimationMirroredH(g, x, y);
					} else {
						run.drawAnimation(g, x, y);
					}
				}
			}
		}
	}

	private void drawAttack(Graphics g) {
		int cenX = (int) getBounds().getCenterX();
		int cenY = (int) getBounds().getCenterY();
		Item holding = Game.gameController.getInventorySystem().getHotbarSelectedItem();
		if(holding != null) {
			if(holding instanceof CanAttack) {
				int direction_rotation = 0;
				switch(attack_dir) {
					case up:
						direction_rotation = -90;
						break;
					case down:
						direction_rotation = 90;
						break;
					case left:
						direction_rotation = 180;
						break;
				}
				//attack_slice.drawAnimationRotated(g, cenX-24, cenY-40, 64, 64, cenX, cenY, direction_rotation);
				ImageFilters.renderImageWithRotation(g, holding.getTexture().getTexure(), cenX + 4, cenY - 20, 16, 16,
						cenX, cenY, (int) (direction_rotation+90 + 45 + -attacking_item_rot));
			}
		}
	}

	@Override
	public Rectangle getBounds() {
		return new Rectangle(x + 3, y + 14, 10, 10);
	}

	@Override
	public Rectangle getTopBounds() {
		return new Rectangle(x + 5, y + 14, 6, 5);
	}

	@Override
	public Rectangle getBottomBounds() {
		return new Rectangle(x + 5, y + 19, 6, 5);
	}

	@Override
	public Rectangle getLeftBounds() {
		return new Rectangle(x + 3, y + 15, 2, 8);
	}

	@Override
	public Rectangle getRightBounds() {
		return new Rectangle(x + 11, y + 15, 2, 8);
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

//	public BIOME getCurrentBiome() {
//		return Game.gameController.getGeneration().getBiomeWithCoords(x, y);
//	}

	public void interact() {
		inventory.open();
	}

	public void attack() {
		attacking = true;
		//Item holding = Game.inventorySystem.getHotbarSelectedItem();
		int dmg = getExpectedDamage();
		SoundEffect.swing.play();
		// TODO make direction function 8 way instead of 4
		Point screenCoords = Helpers.getScreenCoords((int) getBounds().getCenterX(), (int) getBounds().getCenterY(), Game.gameController.getCam());
		attack_dir = Helpers.getDirection(screenCoords, new Point(Game.mouseInput.mouse_x, Game.mouseInput.mouse_y));
		//Logger.print(direction.name());
		//this.direction = direction;

		/*float knockback = 1f;
		if (dmg > DEFAULT_ATTACK_DAMAGE) {
			knockback = 2f;
		}

		switch (attack_dir) {
			case up:
				Game.world.getHitboxSystem().addHitboxContainer(new HitboxContainer(new Hitbox[]{
						new Hitbox(x - 8, y - 16, 32, 16, 10, 4, dmg, knockback),
				}, this));
				break;
			case down:
				Game.world.getHitboxSystem().addHitboxContainer(new HitboxContainer(new Hitbox[]{
						new Hitbox(x - 8, y + getBounds().height + 16, 32, 16, 10, 4, dmg, knockback),
				}, this));
				break;
			case left:
				Game.world.getHitboxSystem().addHitboxContainer(new HitboxContainer(new Hitbox[]{
						new Hitbox(x - 24, y, 16, 32, 10, 4, dmg, knockback),
				}, this));
				break;
			case right:
				Game.world.getHitboxSystem().addHitboxContainer(new HitboxContainer(new Hitbox[]{
						new Hitbox(x + getBounds().width + 16, y, 16, 32, 10, 4, dmg, knockback),
				}, this));
				break;
		}*/

		int cenX = (int) getBounds().getCenterX();
		int cenY = (int) getBounds().getCenterY();
		int angle = (int) Helpers.getAngle(screenCoords, new Point(Game.mouseInput.mouse_x, Game.mouseInput.mouse_y));
		Bullet bullet = new Bullet(cenX, cenY, z_index, angle, this);
		Game.gameController.getHandler().addObject(bullet);
	}

	public boolean canAttack() {
		return attack_timer.timerOver();
	}

	public int getExpectedDamage() {
		int damage_output = DEFAULT_ATTACK_DAMAGE;
		int attack_delay = ATTACK_DELAY;
		Item holding = Game.gameController.getInventorySystem().getHotbarSelectedItem();
		if(holding != null) {
			if(holding instanceof CanAttack) {
				damage_output += ((CanAttack) holding).getDamage();
				attack_delay += ((CanAttack) holding).getAttack_speed();
			}
		}
		attack_timer.setDelay(attack_delay);
		attack_timer.resetTimer();
		return damage_output;
	}

	public InventoryDef getInventory() {
		return inventory;
	}

	public void setKeyInput(KeyInput keyInput) {
		this.keyInput = keyInput;
	}

	@Override
	public void hit(int damage, int knockback_angle, float knockback, GameObject hit_by) {
		if(!hurt_animation) {
			Game.gameController.getCam().screenShake(2f, 6);
			SoundEffect.hurt_2.play();
			velX = (float) -(knockback*Math.cos(Math.toRadians(knockback_angle)));
			velY = (float) -(knockback*Math.sin(Math.toRadians(knockback_angle)));
			health -= damage;
			hurt_animation = true;
		}
	}

	public boolean hasKey(int need_key_id) {
		return true;
	}
}
