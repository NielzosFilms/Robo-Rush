package game.system.main;

import game.system.helpers.Helpers;
import game.system.helpers.Timer;
import game.system.systems.gameObject.GameObject;

import java.awt.*;
import java.io.Serializable;
import java.util.Random;

public class Camera implements Serializable {
	private Random r = new Random();

	public float x, y;
	public float buffer_x, buffer_y;

	private float velX = 0f, velY = 0f;
	private float shake_x = 0f, shake_y = 0f;
	private float screenShake = 0f;
	private Timer shakeTimer = new Timer(4);
	
	public Camera(float x, float y) {
		this.x = x;
		this.y = y;
		this.buffer_x = x;
		this.buffer_y = y;
	}
	
	public void tick(GameObject player) {
		Point mouse = Game.mouseInput.getMouseWorldCoords();
		Point player_coords = new Point(player.getX(), player.getY());

		Point target = new Point(((mouse.x + player_coords.x*3) / 4), ((mouse.y + player_coords.y*3) / 4));


		float xTarg = -target.x + Game.getGameSize().x/2-16;
		buffer_x += (xTarg - buffer_x) * (0.1f);

		float yTarg = -target.y + Game.getGameSize().y/2-16;
		buffer_y += (yTarg - buffer_y) * (0.1f);

		x = buffer_x;
		y = buffer_y;

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
		this.buffer_x = x;
	}
	public void setY(float y) {
		this.y = y;
		this.buffer_y = y;
	}
	public float getX() {
		return x;
	}
	public float getY() {
		return y;
	}

	public void setCoordsWithPlayerCoords(int x, int y) {
		this.x = -x + Game.getGameSize().x/2-16;
		this.y = -y + Game.getGameSize().y/2-16;
		this.buffer_x = this.x;
		this.buffer_y = this.y;
	}

	public void screenShake(float amount, int speed) {
		this.screenShake = amount;
		shakeTimer.setDelay(speed);
		shakeTimer.resetTimer();
	}
	
}
