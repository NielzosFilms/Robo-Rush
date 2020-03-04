package game.objects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import game.main.GameObject;
import game.main.ID;

public class Tile extends GameObject{

	private BufferedImage tex;
	public int tex_id;
	
	public Tile(int x, int y, ID id, BufferedImage tex, int tex_id) {
		super(x, y, id);
		this.tex = tex;
		this.tex_id = tex_id;
	}
	
	public void tick() {
		
	}

	public void render(Graphics g) {
		g.setColor(Color.red);
		g.drawImage(tex, x, y, null);
	}

}
