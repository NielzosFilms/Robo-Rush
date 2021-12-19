package game.textures;

import java.awt.Graphics;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.lang.reflect.Method;

public class Animation implements Serializable {

	private int speed;
	private int frames;
	private int count = 0;
	private int index = 0;
	
	private boolean mirrorW;
	private boolean ended = false, last_frame = false;
	
	private Texture[] images;
	private Texture currentImg;

	private AnimationCallback animationCallback;
	
	public Animation(int speed, Texture... args) {
		this.speed = speed;
		this.mirrorW = false;
		images = new Texture[args.length];
		for(int i = 0; i < args.length; i++) {
			images[i] = args[i];
		}
		frames = args.length;
		currentImg = images[0];
	}

	public void setCallback(AnimationCallback callback) {
		this.animationCallback = callback;
	}
	
	public void runAnimation() {
		index++;
		if(index >= speed) {
			index = 0;
			if(last_frame) {
				if(animationCallback != null) animationCallback.callback();
				ended = true;
			}
			nextFrame();
		}
	}
	
	public void resetAnimation() {
		index = 0;
		count = 0;
		currentImg = images[0];
		ended = false;
		last_frame = false;
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
		if(count >= frames){
			count = 0;
			last_frame = true;
		} else {
			last_frame = false;
		}
	}
	
	public void drawAnimation(Graphics g, int x, int y) {
		g.drawImage(currentImg.getTexure(), x, y, null);
	}
	
	public void drawAnimation(Graphics g, int x, int y, int scaleX, int scaleY) {
		g.drawImage(currentImg.getTexure(), x, y, scaleX, scaleY, null);
	}

	public void drawAnimationMirroredH(Graphics g, int x, int y) {
		AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
		tx.translate(-currentImg.getTexure().getWidth(null), 0);
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		BufferedImage temp = op.filter(currentImg.getTexure(), null);
		g.drawImage(temp, x, y, null);
	}

	public void drawAnimationMirroredH(Graphics g, int x, int y, int scaleX, int scaleY) {
		AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
		tx.translate(-currentImg.getTexure().getWidth(null), 0);
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		BufferedImage temp = op.filter(currentImg.getTexure(), null);
		g.drawImage(temp, x, y, scaleX, scaleY, null);
	}

	public void drawAnimationMirroredV(Graphics g, int x, int y) {
		AffineTransform tx = AffineTransform.getScaleInstance(1, -1);
		tx.translate(0, -currentImg.getTexure().getHeight(null));
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		BufferedImage temp = op.filter(currentImg.getTexure(), null);
		g.drawImage(temp, x, y, null);
	}

	public void drawAnimationMirroredV(Graphics g, int x, int y, int scaleX, int scaleY) {
		AffineTransform tx = AffineTransform.getScaleInstance(1, -1);
		tx.translate(0, -currentImg.getTexure().getHeight(null));
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		BufferedImage temp = op.filter(currentImg.getTexure(), null);
		g.drawImage(temp, x, y, scaleX, scaleY, null);
	}

	public void drawAnimationRotated(Graphics g, int x, int y, int scaleX, int scaleY, int anchX, int anchY, int rotation) {
		if(mirrorW) {
			AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
			tx.translate(-currentImg.getTexure().getWidth(null), 0);
			AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
			BufferedImage temp = op.filter(currentImg.getTexure(), null);
			//g.drawImage(temp, x, y, scaleX, scaleY, null);
			ImageFilters.renderImageWithRotation(g, temp, x, y, scaleX, scaleY, anchX, anchY, rotation);
		}else {
			//g.drawImage(currentImg.getTexure(), x, y, scaleX, scaleY, null);
			ImageFilters.renderImageWithRotation(g, currentImg.getTexure(), x, y, scaleX, scaleY, anchX, anchY, rotation);
		}
	}

	public boolean animationEnded() {
		return ended;
	}
	
}
