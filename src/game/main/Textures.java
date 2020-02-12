package game.main;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Textures {

	static BufferedImage tileSet, playerSheet;
	
	public static List<BufferedImage>tileSetBlocks = new ArrayList<BufferedImage>();
	public static List<BufferedImage>playerImg = new ArrayList<BufferedImage>();
	
	public Textures() {
		BufferedImageLoader loader = new BufferedImageLoader();
		
		tileSet = loader.loadImage("assets/dirt-tiles.png");
		playerSheet = loader.loadImage("assets/entities/player/adventurer-Sheet.png");
		
		SpriteSheet ss = new SpriteSheet(tileSet);
		for(int i = 0;i<tileSet.getHeight();i+=16) {
			for(int j = 0;j<tileSet.getWidth();j+=16) {
				tileSetBlocks.add(ss.grabImage(j, i, 16, 16));
			}
		}
		
		SpriteSheet ss2 = new SpriteSheet(playerSheet);
		for(int i = 0;i<playerSheet.getHeight();i+=37) {
			for(int j = 0;j<playerSheet.getWidth();j+=50) {
				playerImg.add(ss2.grabImage(j, i, 50, 37));
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
