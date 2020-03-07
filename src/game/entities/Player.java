package game.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.Random;

import game.main.Game;
import game.main.GameObject;
import game.main.ID;
import game.main.KeyInput;
import game.textures.Animation;
import game.textures.Textures;
import game.world.World;

public class Player extends GameObject{
	
	Random r = new Random();
	private KeyInput keyInput;
	public float accX, accY; //acceleration
	private static final float WALK_F = 0.5f, BRAKE_F = 0.5f, AIR_F = 0.2f, AIR_RES_F = 0.02f; //force OLD_GRAVITY 0.16f
	private static float GRAVITY = 0.17f; // 0.16f
	public boolean onGround, direction, falling, crouch, moving, sliding, jumping, space;
	private int sliding_timer, sliding_timer_wait = 0;
	private int stop_walking_timer = 0;
	
	private Animation idle;
	private Textures textures;

	public Player(int x, int y, int z_index, ID id, KeyInput keyInput, Textures textures) {
		super(x, y, z_index, id);
		this.onGround = false;
		this.keyInput = keyInput;
		this.accX = 0;
		this.accY = 0;
		this.textures = textures;
		
		initAnimations();
	}
	
	private void initAnimations() {
		idle = new Animation(8, textures.playerImg.get(0), textures.playerImg.get(1), textures.playerImg.get(2), textures.playerImg.get(3));
	}

	public void tick() {
		
		updateAnimations();
		
		int walk_speed = 3;
		
		if(keyInput.keysDown[2] && !keyInput.keysDown[3]) {
			velX = -walk_speed;
		}else if(keyInput.keysDown[3] && !keyInput.keysDown[2]) {
			velX = walk_speed;
		}else {
			velX = 0;
		}
		
		if(keyInput.keysDown[0] && !keyInput.keysDown[1]) {
			velY = -walk_speed;
		}else if(keyInput.keysDown[1] && !keyInput.keysDown[0]) {
			velY = walk_speed;
		}else {
			velY = 0;
		}
		//misschien powerup voor reverse gravity
		
		//velY = Game.clampDouble(velY, -9.8, 9.8);
		
		velX = velX + accX;
		velY = velY + accY;
		/*if(!space && velY < 0)velY = velY + accY*3;
		else velY = velY + accY;*/
		
		if(velX < 0) {
			x = (int) Math.floor(x + velX);
		}else if(velX > 0) {
			x = (int) Math.ceil(x + velX);
		}else {
			x = (int) Math.round(x + velX);
		}
		
		if(velY < 0) {
			y = (int) Math.floor(y + velY);
		}else if(velY > 0) {
			y = (int) Math.ceil(y + velY);
		}else {
			y = (int) Math.round(y + velY);
		}
		
		
		//x = Game.clamp(x, -13, 800-50);
		
		//y = Game.clamp(y, 0, Game.HEIGHT);
		
		
	}
	
	private void updateVelocity() {
		
		
		
	}
	
	private void updateAnimations() {
		idle.runAnimation();
		
	}
	
	public void render(Graphics g) {
		idle.drawAnimation(g, x, y);
		
		//g.setColor(Color.pink);
		//g.drawRect(getBounds().x, getBounds().y, getBounds().width, getBounds().height);
		
		//if(Game.showHitboxes) g.drawRect(x+19, y+6, 13, 30);
	}

	public Rectangle getBounds() {
		return new Rectangle(x+2, y+12, 12, 12);
	}
	
	public Rectangle getSelectBounds() {
		return new Rectangle(x, y, 16, 16);
	}

}
