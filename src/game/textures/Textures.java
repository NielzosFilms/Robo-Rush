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
	public static Animation water;

	// tilesets / sheets
	static BufferedImage tileSetForest, playerSheet, mushroom, tileSetDesert, tileSetWater;
	public BufferedImage entity_shadow;

	// nature
	public BufferedImage tree;
	public static List<BufferedImage> mushrooms = new ArrayList<BufferedImage>();

	// inventory
	public BufferedImage wood, inventory_bg;

	// lights
	public BufferedImage light;

	public Font font;

	public static List<BufferedImage> tileSetForestBlocks = new ArrayList<BufferedImage>();
	public static List<BufferedImage> tileSetDesertBlocks = new ArrayList<BufferedImage>();
	public static List<BufferedImage> tileSetWaterBlocks = new ArrayList<BufferedImage>();
	public static List<BufferedImage> playerImg = new ArrayList<BufferedImage>();

	public Textures() {
		BufferedImageLoader loader = new BufferedImageLoader();

		tileSetForest = loader.loadImage("assets/main/tile_sheets/grass_tiles.png");
		tileSetDesert = loader.loadImage("assets/main/tile_sheets/desert_tile.png");
		tileSetWater = loader.loadImage("assets/main/tile_sheets/water_tiles.png");

		playerSheet = loader.loadImage("assets/entities/player/player_sheet.png");
		entity_shadow = loader.loadImage("assets/entities/shadow.png");

		tree = loader.loadImage("assets/world/nature/tree.png");
		mushroom = loader.loadImage("assets/world/nature/paddenstoel.png");

		wood = loader.loadImage("assets/items/wood.png");
		inventory_bg = loader.loadImage("assets/main/hud/inventory_bg.png");

		light = loader.loadImage("assets/main/lights/light_orange.png");

		// height_map = loader.loadImage("assets/main/height_map.png");

		SpriteSheet ss = new SpriteSheet(tileSetForest);
		for (int i = 0; i < tileSetForest.getHeight(); i += 16) {
			for (int j = 0; j < tileSetForest.getWidth(); j += 16) {
				tileSetForestBlocks.add(ss.grabImage(j, i, 16, 16));
			}
		}

		SpriteSheet ss2 = new SpriteSheet(playerSheet);
		for (int i = 0; i < playerSheet.getHeight(); i += 24) {
			for (int j = 0; j < playerSheet.getWidth(); j += 16) {
				playerImg.add(ss2.grabImage(j, i, 16, 24));
			}
		}

		SpriteSheet ss3 = new SpriteSheet(mushroom);
		for (int i = 0; i < mushroom.getHeight(); i += 16) {
			for (int j = 0; j < mushroom.getWidth(); j += 16) {
				mushrooms.add(ss3.grabImage(j, i, 16, 16));
			}
		}

		SpriteSheet ss4 = new SpriteSheet(tileSetDesert);
		for (int i = 0; i < tileSetDesert.getHeight(); i += 16) {
			for (int j = 0; j < tileSetDesert.getWidth(); j += 16) {
				tileSetDesertBlocks.add(ss4.grabImage(j, i, 16, 16));
			}
		}

		SpriteSheet ss5 = new SpriteSheet(tileSetWater);
		for (int i = 0; i < tileSetWater.getHeight(); i += 16) {
			for (int j = 0; j < tileSetWater.getWidth(); j += 16) {
				tileSetWaterBlocks.add(ss5.grabImage(j, i, 16, 16));
			}
		}
		water = new Animation(25, tileSetWaterBlocks.get(0), tileSetWaterBlocks.get(1));
	}

	// static getter from image list

	public BufferedImage getBlock(int index) {
		return tileSetForestBlocks.get(index);
	}

	public static List<BufferedImage> getList() {
		return tileSetForestBlocks;
	}

}
