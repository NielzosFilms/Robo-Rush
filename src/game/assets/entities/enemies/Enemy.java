package game.assets.entities.enemies;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

import game.assets.HealthBar;
import game.audio.SoundEffect;
import game.enums.DIRECTIONS;
import game.system.helpers.Helpers;
import game.system.helpers.Logger;
import game.system.helpers.Timer;
import game.system.main.Game;
import game.system.systems.gameObject.*;
import game.enums.ID;
import game.system.systems.hitbox.Hitbox;
import game.system.systems.hitbox.HitboxContainer;
import game.system.world.Chunk;
import game.textures.*;

enum Decision {
	wander,
	goto_target,
	avoid_target,
	circle_target,
	attack_target,
}

public class Enemy extends GameObject implements Bounds, Hitable, Health, Destroyable {
	float max_vel = 1f; //0.8
	float wander_vel = 0.5f;
	float acceleration = 0.05f;
	float deceleration = 0.1f;
	private HashMap<Integer, Float> angles = new HashMap<>();
	private HashMap<Integer, Float> prev_angles = new HashMap<>();
	private Point spawnPoint;

	private Timer decide = new Timer(120);
	private Timer decideAction = new Timer(120);
	private boolean move = false;
	private Decision action = Decision.wander;
	private GameObject target = Game.world.getPlayer();

	private int wonderAreaSize = 75, circle_offset = 60, folow_time = 0;

	private Random r = new Random();
	private int circle_radius = 60 - r.nextInt(10);

	private Animation idle = new Animation(150, new Texture(TEXTURE_LIST.lil_skeleton_list, 0, 0), new Texture(TEXTURE_LIST.lil_skeleton_list, 1, 0), new Texture(TEXTURE_LIST.lil_skeleton_list, 2, 0), new Texture(TEXTURE_LIST.lil_skeleton_list, 3, 0));
	private HealthBar health = new HealthBar(0, 0, 0, 8, 1);
	private boolean destroyed = false;

	public Enemy(int x, int y, int z_index, ID id) {
		super(x, y, z_index, id);
		for(int i=0; i<360; i+=30) {
			angles.put(i, 0.5f);
		}
		prev_angles = new HashMap<>(angles);
		spawnPoint = new Point(x, y);
	}

	public void tick() {
		buffer_x += velX;
		buffer_y += velY;
		x = Math.round(buffer_x);
		y = Math.round(buffer_y);

		float targ_velX = 0f;
		float targ_velY = 0f;
		for(int angle : getPositiveAngles()) {
			if(action == Decision.wander) {
				targ_velX += (float) (wander_vel*Math.cos(Math.toRadians(getClosestAngle(angle))));
				targ_velY += (float) (wander_vel*Math.sin(Math.toRadians(getClosestAngle(angle))));
			} else {
				targ_velX += (float) (max_vel*Math.cos(Math.toRadians(getClosestAngle(angle))));
				targ_velY += (float) (max_vel*Math.sin(Math.toRadians(getClosestAngle(angle))));
			}
		}

		for(int angle : getNegativeAngles()) {
			if(action == Decision.wander) {
				targ_velX -= (float) (wander_vel*Math.cos(Math.toRadians(getClosestAngle(angle))));
				targ_velY -= (float) (wander_vel*Math.sin(Math.toRadians(getClosestAngle(angle))));
			} else {
				targ_velX -= (float) (max_vel*Math.cos(Math.toRadians(getClosestAngle(angle))));
				targ_velY -= (float) (max_vel*Math.sin(Math.toRadians(getClosestAngle(angle))));
			}
		}
		//float distance = (float)Helpers.getDistance(new Point(x, y), mouse);

		if(move) {
			velX += (targ_velX - velX) * acceleration;
			velY += (targ_velY - velY) * acceleration;
		} else {
			velX -= (velX) * deceleration;
			velY -= (velY) * deceleration;
		}

		/*velX = Helpers.clampFloat(velX, -max_vel, max_vel);
		velY = Helpers.clampFloat(velY, -max_vel, max_vel);*/
		if(Math.abs(velX) > max_vel) {
			velX -= (velX) * deceleration;
		}
		if(Math.abs(velY) > max_vel) {
			velY -= (velY) * deceleration;
		}

		for(int angle : angles.keySet()) {
			angles.put(angle, 0f);
		}

		int target_x = target.getX();
		int target_y = target.getY();
		if(target instanceof Bounds) {
			if(((Bounds) target).getBounds() != null) {
				target_x = (int) ((Bounds) target).getBounds().getCenterX();
				target_y = (int) ((Bounds) target).getBounds().getCenterY();
			}
		}
		int target_angle = getClosestAngle((int) Helpers.getAngle(new Point(x, y), new Point(target_x, target_y)));
		int target_dist = (int) Helpers.getDistance(new Point(x, y), new Point(target_x, target_y));

		if(action != Decision.wander) {
			switch(action) {
				case goto_target:
					if(target_dist < 25) {
						attackTarget(target_x, target_y);
						action = Decision.avoid_target;
					}
					if(target_dist > 200 && folow_time > r.nextInt(15)*8) {
						if(r.nextInt(2) == 0) {
							action = Decision.wander;
						}
						folow_time = 0;
					} else if(folow_time > 300 + r.nextInt(60)) {
						if(r.nextInt(3) == 0) {
							action = Decision.wander;
						} else {
							action = Decision.circle_target;
						}
						folow_time = 0;
					}
					folow_time++;
					break;
				case avoid_target:
					if(target_dist > 35) {
						action = Decision.circle_target;
					}
					if(decideAction.timerOver()) {
						decideAction.setDelay(r.nextInt(30)*4);
						action = Decision.goto_target;
					}
					break;
				case circle_target:
					if(decideAction.timerOver()) {
						decideAction.setDelay(r.nextInt(30)*4);
						if(r.nextInt(4) == 0) {
							action = Decision.goto_target;
						}
					}
					break;
			}
		}
		if(decideAction.timerOver()) decideAction.resetTimer();

		switch (action) {
			case wander:
				if(decide.timerOver()) {
					decide.resetTimer();
					decide.setDelay(r.nextInt(120)*2);
					if(r.nextInt(2) == 0) {
						move = false;
					} else {
						move = true;
						int random_angle = getClosestAngle(r.nextInt(360));
						if(angles.containsKey(random_angle)) angles.put(random_angle, 1f);
					}
					int spawn_angle = getClosestAngle((int) Helpers.getAngle(new Point(x, y), spawnPoint));
					int spawn_distance = (int) Helpers.getDistance(new Point(x, y), spawnPoint);
					if(spawn_distance > wonderAreaSize) {
						angles.put(spawn_angle, 1f);
					}
					prev_angles = new HashMap<>(angles);
				} else {
					angles = new HashMap<>(prev_angles);
					decide.tick();
				}
				break;
			case goto_target:
				move = true;
				angles.put(target_angle, 1f);
				break;
			case avoid_target:
				move = true;
				angles.put(target_angle, -0.3f);
				break;
			case circle_target:
				move = true;
				if(decide.timerOver()) {
					decide.resetTimer();
					decide.setDelay(r.nextInt(120)*4);
					if(r.nextInt(2) == 0) {
						circle_offset = -circle_offset;
					}
				} else {
					decide.tick();
				}
				if(target_dist < circle_radius) {
					angles.put(target_angle, -0.3f);

				} else {
					angles.put(target_angle, 1f);
				}
				angles.put(getOffsetAngle(target_angle, circle_offset), 1f);
				break;
		}

		/*int spawn_angle = getClosestAngle((int) Helpers.getAngle(new Point(x, y), spawnPoint));
		int spawn_distance = (int) Helpers.getDistance(new Point(x, y), spawnPoint);
		if(spawn_distance > wonderAreaSize) {
			// decide if it wants to stop chasing and return to wandering
			if(decide.timerOver()) {
				decide.resetTimer();
				if(r.nextInt(2) == 0) {
					prev_angles = new HashMap<>(angles);
					action = Decision.wander;
				}
			} else {
				decide.tick();
			}
			//angles.put(spawn_angle, 1f);
		}*/

		LinkedList<GameObject> objects = Game.world.getHandler().getObjectsWithIds(ID.Enemy);
		objects.addAll(Game.world.getHandler().getBoundsObjects());

		LinkedList<Rectangle> all_bounds = new LinkedList<>();
		for(Chunk chunk : Game.world.getChunksOnScreen()) {
			all_bounds.addAll(chunk.getAllTileBounds());
		}
		for(GameObject object : objects) {
			if(object == target) continue;
			if(object instanceof Bounds) {
				if(((Bounds) object).getBounds() != null) {
					all_bounds.add(((Bounds) object).getBounds());
				}
			}
		}
		for(Rectangle bounds : all_bounds) {
			if(bounds == getBounds() || Helpers.sameRectangle(bounds, getBounds())) continue;
			int objX = (int) bounds.getCenterX();
			int objY = (int) bounds.getCenterY();
			int dist = (int) Helpers.getDistance(new Point(x, y), new Point(objX, objY));

			if(Helpers.getDistance(new Point(x, y), new Point(bounds.x, objY)) < dist) {
				objX = bounds.x;
			}
			if(Helpers.getDistance(new Point(x, y), new Point(bounds.x + bounds.width, objY)) < dist) {
				objX = bounds.x + bounds.width;
			}

			if(Helpers.getDistance(new Point(x, y), new Point(objX, bounds.y)) < dist) {
				objY = bounds.y;
			}
			if(Helpers.getDistance(new Point(x, y), new Point(objX, bounds.y + bounds.height)) < dist) {
				objY = bounds.y + bounds.height;
			}

			int angle = getClosestAngle((int) Helpers.getAngle(new Point(x, y), new Point(objX, objY)));
			if(dist < 20) angles.put(angle, -0.3f);
		}

		decideAction.tick();
		idle.runAnimation();
		health.setXY(x-8, y - 16);
	}

	private void attackTarget(int targ_x, int targ_y) {
		DIRECTIONS directions = Helpers.getDirection(new Point(x, y), new Point(targ_x, targ_y));
		int dmg = 2;
		float knockback = 1f;

		int angle = getClosestAngle((int) Helpers.getAngle(new Point(x, y), new Point(targ_x, targ_y)));
		double[] pt = {x + 16, y - 8};

		AffineTransform.getRotateInstance(Math.toRadians(angle), x, y).transform(pt, 0, pt, 0, 1);

		int newX = (int) Math.round(pt[0]);
		int newY = (int) Math.round(pt[1]);

		Game.world.getHitboxSystem().addHitboxContainer(new HitboxContainer(new Hitbox[]{
				new Hitbox(newX, newY, 8, 16, 10, 4, dmg, knockback),
		}, this));

		/*switch (directions) {
			case up:
				Game.world.getHitboxSystem().addHitboxContainer(new HitboxContainer(new Hitbox[]{
						new Hitbox(x-8, y-16, 16, 8, 10, 4, dmg, knockback),
				}, this));
				break;
			case down:
				Game.world.getHitboxSystem().addHitboxContainer(new HitboxContainer(new Hitbox[]{
						new Hitbox(x-8, y+16, 16, 8, 10, 4, dmg, knockback),
				}, this));
				break;
			case left:
				Game.world.getHitboxSystem().addHitboxContainer(new HitboxContainer(new Hitbox[]{
						new Hitbox(x-16, y-8, 8, 16, 10, 4, dmg, knockback),
				}, this));
				break;
			case right:
				Game.world.getHitboxSystem().addHitboxContainer(new HitboxContainer(new Hitbox[]{
						new Hitbox(x+16, y-8, 8, 16, 10, 4, dmg, knockback),
				}, this));
				break;
		}*/
	}

	private void setAngle(int angle, float value) {
		if(value < 0.5f) {
			int highest = getHighestAngle();
			int diff = highest - angle;
			if(Math.abs(diff) < 90) {
				if(diff < 0) {
					angles.put(getOffsetAngle(highest, -60), angles.get(highest));
				} else if(diff > 0) {
					angles.put(getOffsetAngle(highest, 60), angles.get(highest));
				} else {
					angles.put(getOffsetAngle(highest, 60), angles.get(highest));
					angles.put(getOffsetAngle(highest, -60), angles.get(highest));
				}
				angles.put(highest, angles.get(highest)/2);
			}
		}
		angles.put(angle, value);
	}

	public void render(Graphics g) {
		g.setColor(Color.red);
		g.drawRect(getBounds().x, getBounds().y, getBounds().width, getBounds().height);

		if(velX < 0) {
			idle.drawAnimationMirroredH(g, x-8, y-16);
		} else {
			idle.drawAnimation(g, x-8, y-16);
		}

		if(Game.DEBUG_MODE) {
			g.setFont(Fonts.default_fonts.get(5));

			renderAI(g);
		}
	}

	private void renderAI(Graphics g) {
		g.setColor(new Color(199, 130, 77));
		g.drawRect(spawnPoint.x, spawnPoint.y, 1, 1);
		g.drawArc(spawnPoint.x-wonderAreaSize, spawnPoint.y-wonderAreaSize, wonderAreaSize*2, wonderAreaSize*2,0, 360);

		for(int angle : angles.keySet()) {
			if(angle == getHighestAngle()) {
				g.setColor(new Color(99, 199, 77));
			} else if(angle == getClosestAngle((int) Helpers.getAngle(new Point(x, y), spawnPoint))) {
				g.setColor(new Color(199, 130, 77));
			} else {
				g.setColor(new Color(255, 0, 68));
			}
			int length = (int) ((int) 10 + (angles.get(angle)*20));
			int startX = (int) Math.round(x + 10 * Math.cos(Math.toRadians(angle)));
			int startY = (int) Math.round(y + 10 * Math.sin(Math.toRadians(angle)));

			int endX = (int) Math.round(x + length * Math.cos(Math.toRadians(angle)));
			int endY = (int) Math.round(y + length * Math.sin(Math.toRadians(angle)));
			g.drawLine(startX, startY, endX, endY);
			//g.drawString(String.valueOf(angle), endX, endY);
		}

		g.setColor(new Color(139, 155, 180));
		g.drawArc(x-10, y-10, 20, 20, 0, 360);
		g.setColor(new Color(99, 199, 77));
		g.drawString(action.name(), x, y);
	}

	@Override
	public Rectangle getBounds() {
		return new Rectangle(x-5, y-6, 10, 14);
	}

	@Override
	public Rectangle getTopBounds() {
		return new Rectangle(x-3, y-6, 6, 7);
	}

	@Override
	public Rectangle getBottomBounds() {
		return new Rectangle(x-3, y-6+7, 6, 7);
	}

	@Override
	public Rectangle getLeftBounds() {
		return new Rectangle(x-5, y-5, 2, 12);
	}

	@Override
	public Rectangle getRightBounds() {
		return new Rectangle(x+5-2, y-5, 2, 12);
	}

	@Override
	public void hit(int damage, int knockback_angle, float knockback, GameObject hit_by) {
		SoundEffect.hurt_1.play();
		if(action == Decision.wander) {
			target = hit_by;
			action = r.nextInt(2) == 0 ? Decision.goto_target : Decision.circle_target;
		}
		for(GameObject mate : Game.world.getHandler().getObjectsWithIds(this.id)) {
			int mate_dist = (int) Helpers.getDistance(new Point(x, y), new Point(mate.getX(), mate.getY()));
			try {
				Enemy mate_e = (Enemy) mate;
				if (mate_dist < wonderAreaSize * 2) {
					if (mate_e.getAction() == Decision.wander) {
						mate_e.setTarget(hit_by);
						mate_e.setDecision(r.nextInt(2) == 0 ? Decision.goto_target : Decision.circle_target);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		int hit_x = hit_by.getX();
		int hit_y = hit_by.getY();
		if(hit_by instanceof Bounds) {
			if(((Bounds) hit_by).getBounds() != null) {
				hit_x = (int) ((Bounds) hit_by).getBounds().getCenterX();
				hit_y = (int) ((Bounds) hit_by).getBounds().getCenterY();
			}
		}
		health.subtractHealth(damage);
		velX = (float) -(knockback*Math.cos(Math.toRadians(getClosestAngle(knockback_angle))));
		velY = (float) -(knockback*Math.sin(Math.toRadians(getClosestAngle(knockback_angle))));
	}

	private LinkedList<Integer> getPositiveAngles() {
		LinkedList<Integer> ret = new LinkedList<>();
		for(int angle : angles.keySet()) {
			if(angles.get(angle) > 0f) {
				ret.add(angle);
			}
		}
		return ret;
	}
	private LinkedList<Integer> getNegativeAngles() {
		LinkedList<Integer> ret = new LinkedList<>();
		for(int angle : angles.keySet()) {
			if(angles.get(angle) < 0f) {
				ret.add(angle);
			}
		}
		return ret;
	}
	private int getAverageAngle(LinkedList<Integer> angles) {
		float total = 0;
		for(int angle : angles) {
			total += angle;
		}
		return Math.round(total / angles.size());
	}

	private int getHighestAngle() {
		int highest = 0;
		float lastval = 0f;
		for(int angle : angles.keySet()) {
			if(angles.get(angle) > lastval) {
				lastval = angles.get(angle);
				highest = angle;
			}
		}
		return highest;
	}

	private int getLowestAngle() {
		int lowest = 0;
		float lastval = 0f;
		for(int angle : angles.keySet()) {
			if(angles.get(angle) < lastval) {
				lastval = angles.get(angle);
				lowest = angle;
			}
		}
		return lowest;
	}

	private int getClosestAngle(int input_angle) {
		int closest = 0;
		int distance = input_angle;
		for(int angle : angles.keySet()) {
			if(angle == 0) continue;
			int tmp_distance = Math.abs(angle - input_angle);
			if(tmp_distance < distance) {
				distance = tmp_distance;
				closest = angle;
			}
		}
		return closest;
	}

	private int getOffsetAngle(int angle, int offset) {
		int ret = angle + offset;
		if(ret < 0) ret += 360;
		if(ret >= 360) ret -= 360;
		//if(ret == 360) ret = 0;
		return ret;
	}

	public void setTarget(GameObject target) {
		this.target = target;
	}

	@Override
	public int getHealth() {
		return health.getHealth();
	}

	@Override
	public HealthBar getHealthBar() {
		return health;
	}

	@Override
	public boolean dead() {
		return health.dead();
	}

	@Override
	public void destroyed() {
		SoundEffect.explosion.play();
		destroyed = true;
	}

	@Override
	public boolean destroyedCalled() {
		return destroyed;
	}

	@Override
	public boolean canRemove() {
		return destroyed;
	}

	public void setDecision(Decision decision) {
		this.action = decision;
	}

	public Decision getAction() {
		return action;
	}
}
