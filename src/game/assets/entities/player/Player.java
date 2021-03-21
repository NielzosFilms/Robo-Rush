package game.assets.entities.player;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

import game.assets.entities.Effect_Texture;
import game.assets.entities.bullets.Bullet;
import game.audio.SoundEffect;
import game.enums.DIRECTIONS;
import game.enums.ITEM_ID;
import game.system.helpers.Helpers;
import game.system.helpers.Timer;
import game.system.systems.gameObject.Bounds;
import game.system.systems.gameObject.GameObject;
import game.system.systems.gameObject.Hitable;
import game.assets.items.item.Item;
import game.system.main.*;
import game.enums.ID;
import game.system.inputs.KeyInput;
import game.textures.*;

public abstract class Player extends GameObject implements Bounds, Hitable {
	protected LinkedList<Item> items = new LinkedList<>();
	protected HashMap<PLAYER_STAT, Float> player_stats = new HashMap<PLAYER_STAT, Float>(){{
		put(PLAYER_STAT.move_speed, 1f);
		put(PLAYER_STAT.damage, 1f);
		put(PLAYER_STAT.health, 100f);
		put(PLAYER_STAT.rate_of_fire, 30f);
		put(PLAYER_STAT.dash_speed, 8f);
		put(PLAYER_STAT.dash_duration, 20f);
		put(PLAYER_STAT.dash_cooldown, 60f);
	}}, original_stats;

	protected float acceleration = 0.2f, deceleration = 0.3f;
	protected DIRECTIONS direction;

	protected KeyInput keyInput;
	protected Random rand = new Random();

	protected Timer attack_timer = new Timer(Math.round(player_stats.get(PLAYER_STAT.rate_of_fire)));
	protected boolean attacking = false;
	protected float time;

	protected Texture hand, dash_idle;
	protected Animation idle, run, blink, hurt, dash_start, dash_end;
	protected Timer idle_timer = new Timer(300);
	protected boolean hurt_animation = false;
	protected Timer invincible_timer = new Timer(50);

	protected final float dash_speed = player_stats.get(PLAYER_STAT.dash_speed);
	protected boolean dashing = false;
	protected DIRECTIONS dash_direction = DIRECTIONS.up;
	protected Timer dash_cooldown = new Timer(Math.round(player_stats.get(PLAYER_STAT.dash_cooldown)));
	protected Timer dash = new Timer(Math.round(player_stats.get(PLAYER_STAT.dash_duration)));
	protected Timer particle_timer = new Timer(5);

	public Player(int x, int y, int z_index, KeyInput keyInput) {
		super(x, y, z_index, ID.Player);
		this.keyInput = keyInput;
		this.direction = DIRECTIONS.down;
		this.original_stats = new HashMap<>(this.player_stats);

		initAnimations();

		items.add(new Item(new Texture(TEXTURE_LIST.items, 2, 0), ITEM_ID.power_up, new HashMap<>()));
		items.add(new Item(new Texture(TEXTURE_LIST.items, 2, 0), ITEM_ID.power_up, new HashMap<>()));
		items.add(new Item(new Texture(TEXTURE_LIST.items, 2, 0), ITEM_ID.power_up, new HashMap<>()));
		items.add(new Item(new Texture(TEXTURE_LIST.items, 2, 0), ITEM_ID.power_up, new HashMap<>()));
		items.add(new Item(new Texture(TEXTURE_LIST.items, 2, 0), ITEM_ID.power_up, new HashMap<>()));
		items.add(new Item(new Texture(TEXTURE_LIST.items, 2, 0), ITEM_ID.power_up, new HashMap<>()));
		items.add(new Item(new Texture(TEXTURE_LIST.items, 2, 0), ITEM_ID.power_up, new HashMap<>()));

	}

	protected abstract void initAnimations();

	public void tick() {
		buffer_x += velX;
		buffer_y += velY;
		x = Math.round(buffer_x);
		y = Math.round(buffer_y);

		updateAnimations();

		tickWalking();

		tickDash();

		tickAttack();

		updatePlayerStats();

		invincible_timer.tick();
	}

	protected void tickWalking() {
		if(dash.timerOver()) {
			float walk_speed = player_stats.get(PLAYER_STAT.move_speed);
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
	}

	protected void tickDash() {
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
				invincible_timer.setDelay(20);
				invincible_timer.resetTimer();
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

	protected void updateAnimations() {
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

	protected void tickAttack() {
		attack_timer.tick();
		if(Game.mouseInput.leftMouseDown() && canAttack()) {
			attack();
		}
	}

	protected abstract void tickAbstract();

	public void render(Graphics g) {
		g.drawImage(Textures.entity_shadow, getBounds().x-3, getBounds().y+2, 16, 16, null);

		Point mouse = Game.mouseInput.getMouseWorldCoords();
		renderHolding(g, mouse);

		boolean mirror = mouse.x < this.x;
		drawPlayerAnimations(g, mirror);

		if(Game.DEBUG_MODE) {
			g.setColor(new Color(255, 108, 252, 92));
			Helpers.drawBounds(g, this);
		}
		renderAbstract(g);
	}

	protected void renderHolding(Graphics g, Point mouse) {
		Graphics2D g2d = (Graphics2D) g;

		int cenX = (int)getBounds().getCenterX();
		int cenY = (int)getBounds().getCenterY();
		int angle = Math.round(Helpers.getAngle(new Point(cenX, cenY), mouse));

		AffineTransform backup = g2d.getTransform();
		AffineTransform rotation_transform = new AffineTransform(backup);
		rotation_transform.rotate(Math.toRadians(angle), cenX, cenY);
		g2d.setTransform(rotation_transform);

		g.drawImage(hand.getTexure(), cenX+8, cenY-2, 16, 24, null);
		g2d.setTransform(backup);
	}

	protected abstract void renderAbstract(Graphics g);

	protected void drawPlayerAnimations(Graphics g, boolean mirror) {
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

	public void attack() {
		attacking = true;
		SoundEffect.swing.play();
		Point screenCoords = Helpers.getScreenCoords((int) getBounds().getCenterX(), (int) getBounds().getCenterY(), Game.gameController.getCam());

		int cenX = (int) getBounds().getCenterX();
		int cenY = (int) getBounds().getCenterY();
		int angle = (int) Helpers.getAngle(screenCoords, new Point(Game.mouseInput.mouse_x, Game.mouseInput.mouse_y));

		int spread_angle = new Random().nextInt(12) - 6;

		Bullet bullet = new Bullet(cenX, cenY, z_index, angle + spread_angle, this);
		Game.gameController.getHandler().addObject(bullet);
		attack_timer.setDelay(Math.round(player_stats.get(PLAYER_STAT.rate_of_fire)));
		attack_timer.resetTimer();
	}

	public boolean canAttack() {
		return attack_timer.timerOver();
	}


	@Override
	public void hit(int damage, int knockback_angle, float knockback, GameObject hit_by) {
		invincible_timer.resetTimer();
		invincible_timer.setDelay(50);
		Game.gameController.getCam().screenShake(2f, 6);
		SoundEffect.hurt_2.play();
		velX = (float) -(knockback*Math.cos(Math.toRadians(knockback_angle)));
		velY = (float) -(knockback*Math.sin(Math.toRadians(knockback_angle)));
		//health -= damage;
		hurt_animation = true;
	}

	public boolean canBeHit() {
		return invincible_timer.timerOver();
	}

	public void setKeyInput(KeyInput keyInput) {
		this.keyInput = keyInput;
	}

	public boolean hasItem(ITEM_ID item_id) {
		for(Item item : items) {
			if(item.getItem_id() == item_id) {
				return true;
			}
		}
		return false;
	}

	public boolean hasKey(int need_key_id) {
		if(need_key_id == 0) {
			for(Item item : items) {
				if(item.getItem_id() == ITEM_ID.key) {
					return true;
				}
			}
		}
		// test key ids
		return false;
	}

	public void removeItemKey(int need_key_id) {
		if(need_key_id == 0) {
			for(Item item : items) {
				if(item.getItem_id() == ITEM_ID.key) {
					items.remove(item);
					return;
				}
			}
		}
	}

	public void addItem(Item item) {
		this.items.add(item);
	}

	public LinkedList<Item> getItems() {
		return this.items;
	}

	protected void updatePlayerStats() {
		player_stats = new HashMap<>(original_stats);
		for(Item item : items) {
			for(PLAYER_STAT stat : player_stats.keySet()) {
				if(item.item_stats.containsKey(stat)) {
					float old_val = player_stats.get(stat);
					float new_val = old_val + item.item_stats.get(stat);
					player_stats.put(stat, new_val);
				}
			}
		}
	}

	public HashMap<PLAYER_STAT, Float> getPlayerStats() {
		return this.player_stats;
	}

	public HashMap<PLAYER_STAT, Float> getBasePlayerStats() {
		return this.original_stats;
	}
}
