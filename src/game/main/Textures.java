package game.main;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Textures {
	
	static BufferedImage tileSet, playerSheet;
	static BufferedImage ground, ground_fg;
	static BufferedImage height_map, height_map2;
	
	public static List<BufferedImage>tileSetBlocks = new ArrayList<BufferedImage>();
	public static List<BufferedImage>playerImg = new ArrayList<BufferedImage>();
	
	public Textures() {
		BufferedImageLoader loader = new BufferedImageLoader();
		
		tileSet = loader.loadImage("assets/main/tile_sheets/grass_tiles.png");
		playerSheet = loader.loadImage("assets/entities/player/human_base.png");
		
		ground = loader.loadImage("assets/levels/top_down_map.png");
		ground_fg = loader.loadImage("assets/levels/top_down_map_fg.png");
		
		//height_map = loader.loadImage("assets/main/height_map.png");
		height_map = LevelLoader.getHeightMap(50, 50);
		
		SpriteSheet ss = new SpriteSheet(tileSet);
		for(int i = 0;i<tileSet.getHeight();i+=16) {
			for(int j = 0;j<tileSet.getWidth();j+=16) {
				tileSetBlocks.add(ss.grabImage(j, i, 16, 16));
			}
		}
		
		SpriteSheet ss2 = new SpriteSheet(playerSheet);
		for(int i = 0;i<playerSheet.getHeight();i+=18) {
			for(int j = 0;j<playerSheet.getWidth();j+=16) {
				playerImg.add(ss2.grabImage(j, i, 16, 18));
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
