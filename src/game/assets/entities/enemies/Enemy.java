package game.assets.entities.enemies;

import java.awt.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

import game.system.helpers.Helpers;
import game.system.helpers.Logger;
import game.system.helpers.Timer;
import game.system.main.Game;
import game.system.systems.gameObject.Collision;
import game.system.systems.gameObject.GameObject;
import game.enums.ID;
import game.system.systems.gameObject.Hitable;
import game.textures.Fonts;

enum Decision {
	wonder,
	goto_target,
	avoid_target,
	circle_target,
	attack_target,
}

public class Enemy extends GameObject implements Collision, Hitable {
	float max_vel = 0.3f;
	float acceleration = 0.05f;
	float deceleration = 0.1f;
	private HashMap<Integer, Float> angles = new HashMap<>();
	private HashMap<Integer, Float> prev_angles = new HashMap<>();
	private Point spawnPoint;

	private Timer decide = new Timer(120);
	private Timer decideAction = new Timer(120);
	private boolean move = false;
	private Decision action = Decision.goto_target;
	private GameObject target = Game.world.getPlayer();

	private int wonderAreaSize = 75;

	private Random r = new Random();

	public Enemy(int x, int y, int z_index, ID id) {
		super(x, y, z_index, id);
		for(int i=0; i<360; i+=30) {
			angles.put(i, 0.5f);
		}
		prev_angles = new HashMap<>(angles);
		spawnPoint = new Point(x, y);
		System.out.println(angles);
	}

	public void tick() {
		buffer_x += velX;
		buffer_y += velY;
		x = Math.round(buffer_x);
		y = Math.round(buffer_y);

		Point mouse = Helpers.getWorldCoords(Game.mouseInput.mouse_x, Game.mouseInput.mouse_y, Game.world.getCam());

		/*velX += (mouse.x - x) * 0.01f;
		velY += (mouse.y - y) * 0.01f;
		velX -= (velX) * 0.03f;
		velY -= (velY) * 0.03f;*/

		float targ_velX = 0f;
		float targ_velY = 0f;
		for(int angle : getPositiveAngles()) {
			targ_velX += (float) (max_vel*Math.cos(Math.toRadians(getClosestAngle(angle))));
			targ_velY += (float) (max_vel*Math.sin(Math.toRadians(getClosestAngle(angle))));
		}

		for(int angle : getNegativeAngles()) {
			targ_velX -= (float) (max_vel*Math.cos(Math.toRadians(getClosestAngle(angle))));
			targ_velY -= (float) (max_vel*Math.sin(Math.toRadians(getClosestAngle(angle))));
		}
		//float distance = (float)Helpers.getDistance(new Point(x, y), mouse);

		if(move) {
			velX += (targ_velX - velX) * acceleration;
			velY += (targ_velY - velY) * acceleration;
		} else {
			velX -= (velX) * deceleration;
			velY -= (velY) * deceleration;
		}

		velX = Helpers.clampFloat(velX, -max_vel, max_vel);
		velY = Helpers.clampFloat(velY, -max_vel, max_vel);

		if(decideAction.timerOver()) {
			decideAction.setDelay(r.nextInt(120)*2);
			decideAction.resetTimer();
			if(r.nextInt(2) == 0) {
				action = Decision.goto_target;
			} else {
				action = Decision.avoid_target;
			}
		}

		for(int angle : angles.keySet()) {
			angles.put(angle, 0f);
		}

		int target_x = target.getX();
		int target_y = target.getY();
		if(target instanceof Collision) {
			if(((Collision) target).getBounds() != null) {
				target_x = (int) ((Collision) target).getBounds().getCenterX();
				target_y = (int) ((Collision) target).getBounds().getCenterY();
			}
		}
		int target_angle = getClosestAngle((int) Helpers.getAngle(new Point(x, y), new Point(target_x, target_y)));
		int target_dist = (int) Helpers.getDistance(new Point(x, y), new Point(target_x, target_y));

		switch (action) {
			case wonder:
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
					prev_angles = new HashMap<>(angles);
				} else {
					angles = new HashMap<>(prev_angles);
				}
				break;
			case goto_target:
				if(target_dist < 50 || true) {
					move = true;
					angles.put(target_angle, 1f);
				} else {
					move = false;
				}
				break;
			case avoid_target:
				if(target_dist < 50 || true) {
					move = true;
					angles.put(target_angle, -0.3f);
				} else {
					move = false;
				}
				break;
		}

		int spawn_angle = getClosestAngle((int) Helpers.getAngle(new Point(x, y), spawnPoint));
		int spawn_distance = (int) Helpers.getDistance(new Point(x, y), spawnPoint);
		if(spawn_distance > wonderAreaSize) {
			// decide if it wants to stop chasing and return to wandering
			action = Decision.wonder;
			//angles.put(spawn_angle, 1f);
		}

		LinkedList<GameObject> objects = Game.world.getHandler().getObjectsWithIds(ID.Enemy);
		for(GameObject object : objects) {
			if(object == this) continue;
			int objX = object.getX();
			int objY = object.getY();
			if(object instanceof Collision) {
				if(((Collision) object).getBounds() != null) {
					objX = (int) ((Collision) object).getBounds().getCenterX();
					objY = (int) ((Collision) object).getBounds().getCenterY();
				}
			}
			int angle = getClosestAngle((int) Helpers.getAngle(new Point(x, y), new Point(objX, objY)));
			int dist = (int) Helpers.getDistance(new Point(x, y), new Point(objX, objY));
			if(dist < 30) angles.put(angle, -0.3f);
		}

		decide.tick();
		decideAction.tick();
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
		g.drawRect(x-8, y-8, 16, 16);

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
		//return new Rectangle(x-8, y-8, 16, 16);
		return null;
	}

	@Override
	public void hit(int damage) {

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
}
