package game.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

import game.main.GameObject;
import game.main.ID;

public class Player extends GameObject{
	
	Random r = new Random();

	public Player(int x, int y, ID id) {
		super(x, y, id);
		
		velX = r.nextInt(5)+1;
		velY = r.nextInt(5)+1;
	}

	public void tick() {
		x += velX;
		y += velY;
	}

	public void render(Graphics g) {
		g.setColor(Color.red);
		g.fillRect(x, y, 32, 32);
	}

}
