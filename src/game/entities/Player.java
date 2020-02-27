package game.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.Random;

import game.main.Animation;
import game.main.Game;
import game.main.GameObject;
import game.main.ID;
import game.main.KeyInput;
import game.main.Textures;

public class Player extends GameObject{
	
	Random r = new Random();
	private KeyInput keyInput;
	public float accX, accY; //acceleration
	private static final float WALK_F = 0.5f, BRAKE_F = 0.5f, AIR_F = 0.2f, AIR_RES_F = 0.02f; //force OLD_GRAVITY 0.16f
	private static float GRAVITY = 0.17f; // 0.16f
	public boolean onGround, direction, falling, crouch, moving, sliding, jumping, space;
	private int sliding_timer, sliding_timer_wait = 0;
	private int stop_walking_timer = 0;
	
	private Animation walk_up, walk_down, walk_left, walk_right;

	public Player(int x, int y, ID id, KeyInput keyInput) {
		super(x, y, id);
		this.onGround = false;
		this.keyInput = keyInput;
		this.accX = 0;
		this.accY = 0;
		
		initAnimations();
	}
	
	private void initAnimations() {
		walk_up = new Animation(5, Textures.playerImg.get(0), Textures.playerImg.get(1), Textures.playerImg.get(2), Textures.playerImg.get(1));
		walk_down = new Animation(5, Textures.playerImg.get(18), Textures.playerImg.get(19), Textures.playerImg.get(20), Textures.playerImg.get(19));
		walk_left = new Animation(5, Textures.playerImg.get(27), Textures.playerImg.get(28), Textures.playerImg.get(29), Textures.playerImg.get(28));
		walk_right = new Animation(5, Textures.playerImg.get(9), Textures.playerImg.get(10), Textures.playerImg.get(11), Textures.playerImg.get(10));
	}

	public void tick() {
		updateAnimations();
		
		if(keyInput.keysDown[2] && !keyInput.keysDown[3]) {
			velX = -0.2;
		}else if(keyInput.keysDown[3] && !keyInput.keysDown[2]) {
			velX = 0.2;
		}else {
			velX = 0;
		}
		
		if(keyInput.keysDown[0] && !keyInput.keysDown[1]) {
			velY = -0.2;
		}else if(keyInput.keysDown[1] && !keyInput.keysDown[0]) {
			velY = 0.2;
		}else {
			velY = 0;
		}
		
		/*if(keyInput.keysDown[2] && !keyInput.keysDown[3]) {
			if(velX > -2) {
				accX = WALK_F * -1;
			}else {
				accX = 0;
			}
		}else if(keyInput.keysDown[3] && !keyInput.keysDown[2]) {
			if(velX < 2) {
				accX = WALK_F * 1;
			}else {
				accX = 0;
			}
		}else {
			if(velX < -0.5) {
				accX = BRAKE_F * 1;
			}else if(velX > 0.5) {
				accX = BRAKE_F * -1;
			}else {
				accX = 0;
				velX = 0;
			}
			
		}
		
		if(keyInput.keysDown[0] && !keyInput.keysDown[1]) {
			if(velY > -2) {
				accY = WALK_F * -1;
			}else {
				accY = 0;
			}
		}else if(keyInput.keysDown[1] && !keyInput.keysDown[0]) {
			if(velY < 2) {
				accY = WALK_F * 1;
			}else {
				accY = 0;
			}
		}else {
			if(velY < -0.5) {
				accY = BRAKE_F * 1;
			}else if(velY > 0.5) {
				accY = BRAKE_F * -1;
			}else {
				accY = 0;
				velY = 0;
			}
			
		}*/
		//misschien powerup voor reverse gravity
		
		//velY = Game.clampDouble(velY, -9.8, 9.8);
		
		velX = velX + accX;
		velY = velY + accY;
		/*if(!space && velY < 0)velY = velY + accY*3;
		else velY = velY + accY;*/
		if(velX < 0 || velY < 0) {
			x = (int) Math.floor(x + velX);
			y = (int) Math.floor(y + velY);
		}else if(velX > 0 || velY > 0) {
			x = (int) Math.ceil(x + velX);
			y = (int) Math.ceil(y + velY);
		}else {
			x = (int) Math.round(x + velX);
			y = (int) Math.round(y + velY);
		}
		
		
		//x = Game.clamp(x, -13, 800-50);
		
		//y = Game.clamp(y, 0, Game.HEIGHT);
		
		
	}
	
	private void updateVelocity() {
		
		
		
	}
	
	private void updateAnimations() {
		if(velY < 0) {
			walk_up.runAnimation();
		}else if(velY > 0) {
			walk_down.runAnimation();
		}
		if(velX > 0) {
			walk_right.runAnimation();
		}else if(velX < 0) {
			walk_left.runAnimation();
		}
		
	}
	
	public void render(Graphics g) {
		g.setColor(Color.red);
		//g.fillRect(x, y, 16, 16);
		
		/*if(velY < 0) {
			walk_up.drawAnimation(g, x, y);
		}else if(velY > 0) {
			walk_down.drawAnimation(g, x, y);
		}
		
		if(velX > 0) {
			walk_right.drawAnimation(g, x, y);
		}else if(velX < 0) {
			walk_left.drawAnimation(g, x, y);
		}*/
		g.drawImage(Textures.playerImg.get(0), x, y, null);
		
		g.setColor(Color.pink);
		
		//if(Game.showHitboxes) g.drawRect(x+19, y+6, 13, 30);
	}
	
	public Rectangle getBoundsRect() {
		return new Rectangle(x+19, y+6, 13, 30);
	}

}
