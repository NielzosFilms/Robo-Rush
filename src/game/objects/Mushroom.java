package game.objects;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import game.main.GameObject;
import game.main.ID;
import game.textures.Textures;

public class Mushroom extends GameObject{
	
	private BufferedImage tex;
	private Textures textures;

	public Mushroom(int x, int y, int z_index, ID id, Textures textures) {
		super(x, y, z_index, id);
		this.textures = textures;
		this.tex = textures.mushroom;
	}

	public void tick() {
		
	}

	public void render(Graphics g) {
		g.drawImage(this.tex, x, y, null);
	}

	public Rectangle getBounds() {
		return null;
	}
	
	public Rectangle getSelectBounds() {
		return new Rectangle(x+4, y, 12, 16);
	}

}
