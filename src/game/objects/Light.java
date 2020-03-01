package game.objects;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import game.main.GameObject;
import game.main.ID;

public class Light extends GameObject {

	public BufferedImage light_img;
	
	public Light(int x, int y, ID id, BufferedImage light_img) {
		super(x, y, id);
		this.light_img = light_img;
	}

	public void tick() {
		
	}

	public void render(Graphics g) {
		
	}

}
