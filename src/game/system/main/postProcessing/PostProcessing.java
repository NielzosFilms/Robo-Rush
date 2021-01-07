package game.system.main.postProcessing;

import game.system.main.Game;
import game.system.main.postProcessing.filters.CompositeFilter;
import game.system.main.postProcessing.filters.GaussianFilter;
import game.system.main.postProcessing.filters.GlowFilter;
import game.system.main.postProcessing.filters.SoftLightComposite;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.Buffer;

public class PostProcessing {
	BufferedImage image;
	GlowFilter glow = new GlowFilter();
	GaussianFilter blur = new GaussianFilter();

	SoftLightComposite softLight = new SoftLightComposite(0.5f);
	public PostProcessing() {
		image = new BufferedImage(Game.WIDTH, Game.HEIGHT, BufferedImage.TYPE_INT_ARGB);
		glow.setAmount(0.2f);
		//glow.setRadius(6f);
	}

	public void render(Graphics g, BufferedImage game_image) {
		Graphics2D g2d = (Graphics2D) g;
		image = resizeImage(game_image, Game.WIDTH/2, Game.HEIGHT/2);

		blur.setRadius(1f);
		BufferedImage blur_1 = blur.filter(highPass(image, 0.46f), null);
		blur.setRadius(2f);
		BufferedImage blur_2 = blur.filter(blur_1, null);
		blur.setRadius(3f);
		BufferedImage blur_3 = blur.filter(blur_2, null);

		g.drawImage(glow.filter(blur_3, null), 0, 0, Game.NEW_WIDTH, Game.NEW_HEIGHT, null);

		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.8f));
		g.drawImage(game_image, 0, 0, Game.NEW_WIDTH, Game.NEW_HEIGHT, null);



		//g.drawImage(, 0, 0, Game.NEW_WIDTH, Game.NEW_HEIGHT, null);
	}

	private BufferedImage highPass(BufferedImage original, float threshold) {
		BufferedImage ret = new BufferedImage(original.getWidth(), original.getHeight(), BufferedImage.TYPE_INT_ARGB);
		for(int y=0; y< original.getHeight(); y++) {
			for(int x=0; x< original.getWidth(); x++) {
				int color = image.getRGB(x, y);

				// extract each color component
				int red   = (color >>> 16) & 0xFF;
				int green = (color >>>  8) & 0xFF;
				int blue  = (color >>>  0) & 0xFF;

				// calc luminance in range 0.0 to 1.0; using SRGB luminance constants
				float luminance = (red * 0.2126f + green * 0.7152f + blue * 0.0722f) / 255;

				// choose brightness threshold as appropriate:
				if (luminance >= threshold) {
					ret.setRGB(x, y, color);
					// bright color
				} else {
					// dark color
					int alpha = 255;
					int r   = 0;
					int g = 0;
					int b  = 0;

					int argb = alpha << 24 + r << 16 + g << 8 + b;
					ret.setRGB(x, y, argb);
				}
			}
		}
		return ret;
	}

	private BufferedImage resizeImage(BufferedImage original, int targW, int targH) {
		BufferedImage ret = new BufferedImage(targW, targH, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = ret.createGraphics();
		g2d.drawImage(original, 0, 0, targW, targH, null);
		g2d.dispose();
		return ret;
	}
}
