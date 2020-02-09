package game.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.Random;

import game.main.Game;
import game.main.GameObject;
import game.main.ID;
import game.main.KeyInput;

public class Player extends GameObject{
	
	Random r = new Random();
	private KeyInput keyInput;
	public boolean onGround;

	public Player(int x, int y, ID id, KeyInput keyInput) {
		super(x, y, id);
		this.onGround = false;
		this.keyInput = keyInput;
	}

	public void tick() {
		if(onGround) {
			if(keyInput.keysDown.get(KeyEvent.VK_A) == true) velX -= 0.5;
			if(keyInput.keysDown.get(KeyEvent.VK_D) == true) velX += 0.5;
			if(velX > 0)
				if((keyInput.keysDown.get(KeyEvent.VK_A) == true && keyInput.keysDown.get(KeyEvent.VK_D) == true) || (keyInput.keysDown.get(KeyEvent.VK_A) == false && keyInput.keysDown.get(KeyEvent.VK_D) == false)) velX -= 0.3;
			if(velX < 0)
				if((keyInput.keysDown.get(KeyEvent.VK_A) == true && keyInput.keysDown.get(KeyEvent.VK_D) == true) || (keyInput.keysDown.get(KeyEvent.VK_A) == false && keyInput.keysDown.get(KeyEvent.VK_D) == false)) velX += 0.3;
		}else {
			if(keyInput.keysDown.get(KeyEvent.VK_A) == true) velX -=0.3;
			if(keyInput.keysDown.get(KeyEvent.VK_D) == true) velX += 0.3;
		}
		if(keyInput.keysDown.get(KeyEvent.VK_W) == true && onGround) {
			onGround = false;
			velY = -7;
		}
		
		//misschien powerup voor reverse gravity
		velY = Game.clampFloat(velY, -9.8, 9.8);
		if(onGround) {
			velX = Game.clampFloat(velX, -7, 7);
			velY = 0;
		}else {
			velY = velY + 0.2;
			if(velX > 0) {
				velX = velX - 0.05;
			}else if(velX < 0) {
				velX = velX + 0.05;
			}
			velX = Game.clampFloat(velX, -7, 7);
			
		}
		x += velX;
		y += velY;
		
		y = Game.clamp(y, 0, Game.HEIGHT-61);
		if(y == Game.HEIGHT-61) {
			onGround = true;
		}
	}

	public void render(Graphics g) {
		g.setColor(Color.red);
		g.fillRect(x, y, 32, 32);
	}

}
