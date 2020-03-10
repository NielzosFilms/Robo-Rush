package game.objects;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Random;

import game.main.GameObject;
import game.main.ID;
import game.textures.Textures;

public class Mushroom extends GameObject{
	
	private BufferedImage tex;
	private Textures textures;
	private Random r;

	public Mushroom(int x, int y, int z_index, ID id, Textures textures) {
		super(x, y, z_index, id);
		this.textures = textures;
		this.r = new Random();
		
		int num = r.nextInt(20);
		
		if(num == 0) {
			this.tex = textures.mushrooms.get(3);
		}else if(num >= 1 && num <= 5) {
			this.tex = textures.mushrooms.get(1);
		}else if(num >= 6 && num <= 10) {
			this.tex = textures.mushrooms.get(2);
		}else if(num >= 11 && num <= 15) {
			this.tex = textures.mushrooms.get(4);
		}else {
			this.tex = textures.mushrooms.get(0);
		}
		
		//this.tex = textures.mushroom;
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
