package game.textures;

import java.awt.image.BufferedImage;

public class SpriteSheet {

private BufferedImage sprite;
	
	public SpriteSheet(BufferedImage ss) {
		this.sprite = ss;
	}
	
	public BufferedImage grabImage(int x, int y, int width, int height) {
		BufferedImage img = sprite.getSubimage(x, y, width, height);
		return img;
	}
	
}

