package game.entities;

import java.awt.Color;
import java.awt.Graphics;

import game.main.GameObject;
import game.main.ID;

public class Enemy extends GameObject{

	public Enemy(int x, int y, ID id) {
		super(x, y, id);
	}

	public void tick() {
		
	}

	public void render(Graphics g) {
		g.setColor(Color.red);
		g.drawRect(x, y, 16, 16);
	}

}
