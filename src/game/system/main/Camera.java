package game.system.main;

import game.system.helpers.Timer;
import game.system.systems.gameObject.GameObject;

import java.io.Serializable;
import java.util.Random;

public class Camera implements Serializable {
	private Random r = new Random();

	public float x, y;
	private float velX = 0f, velY = 0f;
	private float shake_x = 0f, shake_y = 0f;
	private float screenShake = 0f;
	private Timer shakeTimer = new Timer(4);
	
	public Camera(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public void tick(GameObject Player) {
		float xTarg = -Player.getX() + Game.WIDTH/2-16;
		x += (xTarg - x) * (0.1f);

		float yTarg = -Player.getY() + Game.HEIGHT/2-16;
		y += (yTarg - y) * (0.1f);

		if(screenShake <= 0) {
			screenShake = 0;
		} else {
			screenShake -= 0.1f * screenShake;
		}
		if(shakeTimer.timerOver()) {
			shakeTimer.resetTimer();
			shake_x = (r.nextFloat() * screenShake * 2) - screenShake;
			shake_y = (r.nextFloat() * screenShake * 2) - screenShake;
		}
		shakeTimer.tick();

		x += shake_x;
		y += shake_y;
	}
	
	public void setX(float x) { 
		this.x = x;
	}
	public void setY(float x) {
		this.y = y;
	}
	public float getX() {
		return x;
	}
	public float getY() {
		return y;
	}

	public void setCoordsWithPlayerCoords(int x, int y) {
		this.x = -x + Game.WIDTH/2-16;
		this.y = -y + Game.HEIGHT/2-16;
	}

	public void screenShake(float amount, int speed) {
		this.screenShake = amount;
		shakeTimer.setDelay(speed);
		shakeTimer.resetTimer();
	}
	
}
