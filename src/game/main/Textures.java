package game.main;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Textures {

	static BufferedImage tileSet;
	
	public static List<BufferedImage>tileSetBlocks = new ArrayList<BufferedImage>();
	
	public Textures() {
		BufferedImageLoader loader = new BufferedImageLoader();
		
		tileSet = loader.loadImage("assets/dirt-tiles.png");
		
		SpriteSheet ss = new SpriteSheet(tileSet);
		
		for(int i = 0;i<tileSet.getHeight();i+=16) {
			for(int j = 0;j<tileSet.getWidth();j+=16) {
				tileSetBlocks.add(ss.grabImage(j, i, 16, 16));
			}
		}
		
		//block = ss.grabImage(0, 0, 16, 16);
	}
	
	//static getter from image list
	
	public BufferedImage getBlock(int index) {
		return tileSetBlocks.get(index);
	}
	
	public static List<BufferedImage> getList() {
	    return tileSetBlocks;
	}
	
}
