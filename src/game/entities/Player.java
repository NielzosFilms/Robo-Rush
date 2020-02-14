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
	
	private Animation idle, running, jumping_ani, crouch_ani, slide;

	public Player(int x, int y, ID id, KeyInput keyInput) {
		super(x, y, id);
		this.onGround = false;
		this.keyInput = keyInput;
		this.accX = 0;
		this.accY = 0;
		
		initAnimations();
	}
	
	private void initAnimations() {
		idle = new Animation(6, Textures.playerImg.get(0), Textures.playerImg.get(1), Textures.playerImg.get(2), Textures.playerImg.get(3));
		running = new Animation(6, Textures.playerImg.get(8), Textures.playerImg.get(9), Textures.playerImg.get(10), Textures.playerImg.get(11), Textures.playerImg.get(12), Textures.playerImg.get(13));
		jumping_ani = new Animation(5, Textures.playerImg.get(16), Textures.playerImg.get(17),
				Textures.playerImg.get(18), Textures.playerImg.get(19), Textures.playerImg.get(20), Textures.playerImg.get(21), Textures.playerImg.get(22), Textures.playerImg.get(23));
		crouch_ani = new Animation(6, Textures.playerImg.get(4), Textures.playerImg.get(5), Textures.playerImg.get(6), Textures.playerImg.get(7));
		slide = new Animation(6, Textures.playerImg.get(24), Textures.playerImg.get(25), Textures.playerImg.get(26), Textures.playerImg.get(27), Textures.playerImg.get(28));
	}

	public void tick() {
		updateAnimations();
		
		/*if(y == Game.HEIGHT) {
			onGround = true;
		}*/
		
		updateVelocity();
		
		/*if(crouch && velX != 0 && sliding_timer_wait >= 10) {
			sliding = true;
		}*/
		
		
		//sliding
		/*if(sliding) {
			sliding_timer++;
			if(!direction)velX = 3;
			else velX = -3;
			if(sliding_timer > 42) {
				sliding = false;
				if(keyInput.keysDown[1] && onGround)crouch = true;
				sliding_timer = 0;
				sliding_timer_wait = 0;
			}
			//velX = Game.clampDouble(velX, -3, 3);
		}else if(!sliding && crouch){
			//velX = Game.clampDouble(velX, -1, 1);
		}else {
			sliding_timer_wait++;
			if(sliding_timer_wait > 10) sliding_timer_wait = 10;
			//velX = Game.clampDouble(velX, -2, 2);
		}*/
		//end_sliding
		
		
		//misschien powerup voor reverse gravity
		
		
		
		if(velX < 0) { //sets direction, moving booleans
			direction = true;
			moving = true;
		}else if(velX > 0) {
			direction = false;
			moving = true;
		}else {
			moving = false;
		}
		
		
		if(onGround) { //sets booleans
			velY = 0;
			jumping = false;
			falling = false;
		}else {
			if(velY > 4 && jumping)
				falling = true;
			else if(velY > 0 && !jumping)
				falling = true;
			else
				falling = false;
		}
		
		if(y > 320) { //respawns player if out of bounds
			x = 0;
			y = 0;
		}
		
		velY = Game.clampDouble(velY, -9.8, 9.8);
		
		velX = velX + accX;
		if(!space && velY < 0)velY = velY + accY*3;
		else velY = velY + accY;
		
		x = (int) Math.round(x + velX);
		y = (int) Math.round(y + velY);
		
		x = Game.clamp(x, -13, 800-50);
		
		//y = Game.clamp(y, 0, Game.HEIGHT);
		
		
	}
	
	private void updateVelocity() {
		/*if(onGround) {
			if(keyInput.keysDown[1] == true && !sliding && (velX > 2 || velX < -1)) {
				if(!moving)
					crouch = true;
				else if(!crouch && sliding_timer_wait >= 10)
					sliding = true;
			}else crouch = false;
			if(keyInput.keysDown[2] == true) velX = velX + (-2 - velX) * (0.07f);
			if(keyInput.keysDown[3] == true) velX = velX + (3 - velX) * (0.07f);
			if(velX > 0)
				if((keyInput.keysDown[2] == true && keyInput.keysDown[3] == true) || (keyInput.keysDown[2] == false && keyInput.keysDown[3] == false)) {
					velX = velX + (-2 - velX) * (0.07f);
					if(velX > -0.2 && velX < 0.2)
						velX = 0;
				}
			if(velX < 0)
				if((keyInput.keysDown[2] == true && keyInput.keysDown[3] == true) || (keyInput.keysDown[2] == false && keyInput.keysDown[3] == false)) {
					velX = velX + (2 - velX) * (0.07f);
					if(velX > -0.2 && velX < 0.2)
						velX = 0;
				}
			if(keyInput.keysDown[4] == true && !jumping) {
				onGround = false;
				jumping = true;
				velY = -5;
			}
		}else if(!onGround && !sliding){
			if(keyInput.keysDown[2] == true) velX = velX + (-2 - velX) * (0.05f);
			if(keyInput.keysDown[3] == true) velX = velX + (2 - velX) * (0.05f);
			if(keyInput.keysDown[1] == true) velY += (9.8 - velY) * (0.05f);
		}*/
		
		
		
		if(onGround) {
			if(keyInput.keysDown[2] && !keyInput.keysDown[3]) {
				if(velX > -3) {
					accX = WALK_F * -1;
				}else {
					accX = 0;
				}
			}else if(keyInput.keysDown[3] && !keyInput.keysDown[2]) {
				if(velX < 3) {
					accX = WALK_F * 1;
				}else {
					accX = 0;
				}
			}else {
				if(velX < -0.1) {
					accX = BRAKE_F * 1;
				}else if(velX > 0.1) {
					accX = BRAKE_F * -1;
				}else {
					accX = 0;
					if(velX > -0.3f && velX < 0.3f) {//sill a bug here <<
						velX = 0;
					}
				}
				
			}
			
			if(keyInput.keysDown[4] == true && !jumping && !space) {
				//keyInput.keysDown[4] = false; //bug blijft nog steeds springen
				space = true;
				onGround = false;
				jumping = true;
				velY = -5;
			}
		}else if(!onGround) {
			
			if(keyInput.keysDown[1]) {
				if(velY < 12) {
					accY = GRAVITY * 2;
				}else {
					accY = 0;
				}
			}else {
				if(velY < 10) {
					accY = GRAVITY * 1;
				}else {
					accY = 0;
				}
			}
			
			if(keyInput.keysDown[2] && !keyInput.keysDown[3]) { // Keys: A - D
				if(velX > -3) {
					accX = AIR_F * -1;
				}else {
					accX = 0;
				}
			}else if(keyInput.keysDown[3] && !keyInput.keysDown[2]) {
				if(velX < 3) {
					accX = AIR_F * 1;
				}else {
					accX = 0;
				}
			}else {
				if(velX < 0) {
					accX = AIR_RES_F * 1;
				}else if(velX > 0) {
					accX = AIR_RES_F * -1;
				}
			}
			
			
		}
		
		if(keyInput.keysDown[4] == false) {
			space = false;
		}
		
	}
	
	private void updateAnimations() {
		if(velX == 0 && onGround && !crouch) {
			idle.runAnimation();
			
			running.resetAnimation();
			jumping_ani.resetAnimation();
			crouch_ani.resetAnimation();
			slide.resetAnimation();
		}else if((velX > 0 || velX < 0) && onGround && !crouch && !sliding) {
			running.runAnimation();
			
			idle.resetAnimation();
			jumping_ani.resetAnimation();
			crouch_ani.resetAnimation();
			slide.resetAnimation();
		}else if(!onGround && !falling) {
			jumping_ani.runAnimation();
			
			idle.resetAnimation();
			running.resetAnimation();
			crouch_ani.resetAnimation();
			slide.resetAnimation();
		}else if(onGround && crouch && !sliding) {
			crouch_ani.runAnimation();
			
			idle.resetAnimation();
			running.resetAnimation();
			jumping_ani.resetAnimation();
			slide.resetAnimation();
		}else if(sliding) {
			slide.runAnimation();
			
			idle.resetAnimation();
			running.resetAnimation();
			jumping_ani.resetAnimation();
			crouch_ani.resetAnimation();
		}
		
		if(direction) {
			idle.mirrorAnimationW(true);
			running.mirrorAnimationW(true);
			jumping_ani.mirrorAnimationW(true);
			crouch_ani.mirrorAnimationW(true);
			slide.mirrorAnimationW(true);
		}else {
			idle.mirrorAnimationW(false);
			running.mirrorAnimationW(false);
			jumping_ani.mirrorAnimationW(false);
			crouch_ani.mirrorAnimationW(false);
			slide.mirrorAnimationW(false);
		}
	}
	
	public void render(Graphics g) {
		g.setColor(Color.red);
		//g.fillRect(x, y, 32, 32);
		
		if(velX == 0 && onGround && !crouch) {
			idle.drawAnimation(g, x, y, 50, 37);
		}else if(onGround && !crouch && !sliding){
			running.drawAnimation(g, x, y);
		}else if(!onGround && !falling && jumping){
			jumping_ani.drawAnimation(g, x, y);
		}else if(!onGround && falling) {
			if(direction) {
				AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
				tx.translate(-Textures.playerImg.get(23).getWidth(null), 0);
				AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
				BufferedImage temp = op.filter(Textures.playerImg.get(23), null);
				g.drawImage(temp, x, y, null);
				
			}else
				g.drawImage(Textures.playerImg.get(23), x, y, null);
		}else if(onGround && crouch && !sliding) {
			crouch_ani.drawAnimation(g, x, y);
		}else if(sliding) {
			slide.drawAnimation(g, x, y);
		}else {
			if(direction) {
				AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
				tx.translate(-Textures.playerImg.get(23).getWidth(null), 0);
				AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
				BufferedImage temp = op.filter(Textures.playerImg.get(23), null);
				g.drawImage(temp, x, y, null);
				
			}else
				g.drawImage(Textures.playerImg.get(23), x, y, null);
		}
		g.setColor(Color.pink);
		
		if(Game.showHitboxes) g.drawRect(x+19, y+6, 13, 30);
	}
	
	public Rectangle getBoundsRect() {
		return new Rectangle(x+19, y+6, 13, 30);
	}

}
