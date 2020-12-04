package game.textures;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

public class ImageFilters {
	public static void renderImageWithRotation(Graphics g, BufferedImage img, int x, int y,
											   int width, int height,
											   int anchX, int anchY, int rot_deg) {
		Graphics2D g2d = (Graphics2D) g;
		double rotationRequired = Math.toRadians(rot_deg);
		AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, anchX, anchY);
		AffineTransformOp op = new AffineTransformOp(tx, null);
		g2d.drawImage(op.filter(img, null), x, y, width, height, null);
	}

	public static void renderImageWithRotationFromCenter(Graphics g, BufferedImage img, int x, int y,
											   int width, int height, int rot_deg) {
		Graphics2D g2d = (Graphics2D) g;
		double rotationRequired = Math.toRadians(rot_deg);
		AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, img.getWidth() / 2, img.getHeight() / 2);
		AffineTransformOp op = new AffineTransformOp(tx, null);
		g2d.drawImage(op.filter(img, null), x, y, width, height, null);
	}
}
