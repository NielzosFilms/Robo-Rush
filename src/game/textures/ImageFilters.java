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
		AffineTransform backup = g2d.getTransform();
		AffineTransform rotation_transform = new AffineTransform(backup);
		rotation_transform.rotate(Math.toRadians(rot_deg), anchX, anchY);
		g2d.setTransform(rotation_transform);
		g2d.drawImage(img, x, y, width, height, null);

		g2d.setTransform(backup);
	}

	public static void renderImageWithRotationFromCenter(Graphics g, BufferedImage img, int x, int y,
											   int width, int height, int rot_deg) {
		Graphics2D g2d = (Graphics2D) g;
		AffineTransform backup = g2d.getTransform();
		AffineTransform rotation_transform = new AffineTransform(backup);
		rotation_transform.rotate(Math.toRadians(rot_deg), x + width/2, y + height/2);
		g2d.setTransform(rotation_transform);
		g2d.drawImage(img, x, y, width, height, null);

		g2d.setTransform(backup);
	}

	public static BufferedImage resize(BufferedImage img, int newW, int newH) {
		//Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
		BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

		Graphics2D g2d = dimg.createGraphics();
		g2d.drawImage(img, 0, 0, newW, newH, null);
		g2d.dispose();

		return dimg;
	}
}
