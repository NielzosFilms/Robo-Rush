package game.objects;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import game.main.GameObject;
import game.main.ID;

public class Tree extends GameObject{
	
	private BufferedImage tex;

	public Tree(int x, int y, ID id, BufferedImage tex) {
		super(x, y, id);
		this.tex = tex;
	}

	public void tick() {
		
	}

	public void render(Graphics g) {
		g.drawImage(tex, x, y, null);
	}

}
