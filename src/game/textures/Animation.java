package game.textures;

import java.awt.Graphics;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.Serializable;

public class Animation implements Serializable {

	private int speed;
	private int frames;
	private int index = 0;
	private int count = 0;
	
	private boolean mirrorW;
	
	private transient BufferedImage[] images;
	private transient BufferedImage currentImg;
	
	public Animation(int speed, BufferedImage... args) {
		this.speed = speed;
		this.mirrorW = false;
		images = new BufferedImage[args.length];
		for(int i = 0; i < args.length; i++) {
			images[i] = args[i];
		}
		frames = args.length;
		currentImg = images[0];
	}
	
	public void runAnimation() {
		index++;
		if(index > speed) {
			index = 0;
			nextFrame();
		}
	}
	
	public void resetAnimation() {
		index = 0;
		count = 0;
		currentImg = images[0];
	}
	
	public void mirrorAnimationW(boolean temp) {
		this.mirrorW = temp;
	}
	
	private void nextFrame() {
		for(int i = 0; i < frames; i++) {
			if(count == i)
				currentImg = images[i];
		}
		count++;
		if(count >= frames)
			count = 0;
	}
	
	public void drawAnimation(Graphics g, int x, int y) {
		if(mirrorW) {
			AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
			tx.translate(-currentImg.getWidth(null), 0);
			AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
			BufferedImage temp = op.filter(currentImg, null);
			g.drawImage(temp, x, y, null);
			
		}else
			g.drawImage(currentImg, x, y, null);
	}
	
	public void drawAnimation(Graphics g, int x, int y, int scaleX, int scaleY) {
		if(mirrorW) {
			AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
			tx.translate(-currentImg.getWidth(null), 0);
			AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
			BufferedImage temp = op.filter(currentImg, null);
			g.drawImage(temp, x, y, scaleX, scaleY, null);
			
		}else
			g.drawImage(currentImg, x, y, scaleX, scaleY, null);
	}
	
}
