package game.textures;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class Textures {
	
	//tilesets / sheets
	static BufferedImage tileSet, playerSheet, mushroom;
	
	//nature
	public BufferedImage tree;
	public static List<BufferedImage>mushrooms  = new ArrayList<BufferedImage>();
	
	//inventory
	public BufferedImage wood, inventory_bg;
	
	//lights
	public BufferedImage light;
	
	public Font font;
	
	public static List<BufferedImage>tileSetBlocks = new ArrayList<BufferedImage>();
	public static List<BufferedImage>playerImg = new ArrayList<BufferedImage>();
	
	public Textures() {
		BufferedImageLoader loader = new BufferedImageLoader();
		
		tileSet = loader.loadImage("assets/main/tile_sheets/grass_tiles2.png");
		playerSheet = loader.loadImage("assets/entities/player/player_sheet.png");
		
		tree = loader.loadImage("assets/world/nature/tree.png");
		mushroom = loader.loadImage("assets/world/nature/paddenstoel.png");
		
		wood = loader.loadImage("assets/items/wood.png");
		inventory_bg = loader.loadImage("assets/main/hud/inventory_bg.png");
		
		light = loader.loadImage("assets/main/lights/light_orange.png");
		
		//height_map = loader.loadImage("assets/main/height_map.png");
		
		SpriteSheet ss = new SpriteSheet(tileSet);
		for(int i = 0;i<tileSet.getHeight();i+=16) {
			for(int j = 0;j<tileSet.getWidth();j+=16) {
				tileSetBlocks.add(ss.grabImage(j, i, 16, 16));
			}
		}
		
		SpriteSheet ss2 = new SpriteSheet(playerSheet);
		for(int i = 0;i<playerSheet.getHeight();i+=24) {
			for(int j = 0;j<playerSheet.getWidth();j+=16) {
				playerImg.add(ss2.grabImage(j, i, 16, 24));
			}
		}
		
		SpriteSheet ss3 = new SpriteSheet(mushroom);
		for(int i = 0;i<mushroom.getHeight();i+=16) {
			for(int j = 0;j<mushroom.getWidth();j+=16) {
				mushrooms.add(ss3.grabImage(j, i, 16, 16));
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
