package game.objects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import game.main.GameObject;
import game.main.ID;

public class Tile extends GameObject{

	private BufferedImage tex;
	
	public Tile(int x, int y, ID id, BufferedImage tex) {
		super(x, y, id);
		this.tex = tex;
	}
	
	public void tick() {
		
	}

	public void render(Graphics g) {
		g.setColor(Color.red);
		g.drawImage(tex, x, y, null);
	}

}
