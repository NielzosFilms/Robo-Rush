package game.assets.entities.enemies;

import java.awt.*;
import java.util.HashMap;
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

public class Enemy extends GameObject implements Collision, Hitable {
	float max_vel = 0.5f;
	float acceleration = 0.05f;
	float deceleration = 0.1f;
	private HashMap<Integer, Float> angles = new HashMap<>();
	private Point spawnPoint;

	private Timer decide = new Timer(120);
	private boolean move = false;

	private Random r = new Random();

	public Enemy(int x, int y, int z_index, ID id) {
		super(x, y, z_index, id);
		for(int i=0; i<360; i+=30) {
			angles.put(i, ((float)Math.round(r.nextFloat()*10))/10);
		}
		spawnPoint = new Point(x, y);
		System.out.println(angles);
	}

	public void tick() {
		buffer_x += velX;
		buffer_y += velY;
		x = Math.round(buffer_x);
		y = Math.round(buffer_y);

		Point mouse = Helpers.getWorldCoords(Game.mouseInput.mouse_x, Game.mouseInput.mouse_y, Game.world.getCam());

		int player_x = Game.world.getPlayer().getX();
		int player_y = Game.world.getPlayer().getY();

		/*velX += (mouse.x - x) * 0.01f;
		velY += (mouse.y - y) * 0.01f;
		velX -= (velX) * 0.03f;
		velY -= (velY) * 0.03f;*/

		float targ_velX = (float) (max_vel*Math.cos(Math.toRadians(getHighestAngle())));
		float targ_velY = (float) (max_vel*Math.sin(Math.toRadians(getHighestAngle())));

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

		if(decide.timerOver()) {
			decide.setDelay(r.nextInt(240));
			decide.resetTimer();
			if(r.nextInt(2) == 0) {
				move = false;
			} else {
				move = true;
				for(int angle : angles.keySet()) {
					angles.put(angle, ((float)Math.round(r.nextFloat()*10))/10);
				}
				int spawn_angle = getClosestAngle((int) Helpers.getAngle(new Point(x, y), spawnPoint));
				int spawn_distance = (int) Helpers.getDistance(new Point(x, y), spawnPoint);
				float new_val = 1f / 50 * spawn_distance;
				if(angles.get(spawn_angle) < new_val) {
					angles.put(spawn_angle, new_val);
				}
				int player_angle = getClosestAngle((int) Helpers.getAngle(new Point(x, y), new Point(player_x, player_y)));
				if(angles.get(player_angle) < 1f) {
					angles.put(player_angle, 1f);
				}
			}
		}
		decide.tick();
		if(Game.mouseInput.leftMouseDown()) {
			setX(mouse.x);
			setY(mouse.y);
		}
	}

	public void render(Graphics g) {
		g.setColor(Color.red);
		g.drawRect(x-8, y-8, 16, 16);

		if(Game.DEBUG_MODE) {

			g.setColor(new Color(255, 123, 47));
			Point mouse = Helpers.getWorldCoords(Game.mouseInput.mouse_x, Game.mouseInput.mouse_y, Game.world.getCam());
			g.drawLine(x, y, mouse.x, mouse.y);

			g.setColor(new Color(255, 180, 139));
			g.setFont(Fonts.default_fonts.get(5));
			g.drawString(String.valueOf(Helpers.getAngle(new Point(x, y), mouse)), mouse.x, mouse.y);

			renderAI(g);
		}
	}

	private void renderAI(Graphics g) {
		g.setColor(new Color(199, 130, 77));
		g.drawRect(spawnPoint.x, spawnPoint.y, 1, 1);
		g.drawArc(spawnPoint.x-50, spawnPoint.y-50, 100, 100,0, 360);

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
			g.drawString(String.valueOf(angle), endX, endY);
		}

		g.setColor(new Color(139, 155, 180));
		g.drawArc(x-10, y-10, 20, 20, 0, 360);
	}

	@Override
	public Rectangle getBounds() {
		//return new Rectangle(x-8, y-8, 16, 16);
		return null;
	}

	@Override
	public void hit(int damage) {

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
}
