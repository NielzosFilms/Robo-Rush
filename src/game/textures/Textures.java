package game.textures;

import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Textures implements Serializable {
	public static Animation water;
	public static Animation water_d_br, water_d_b_r, water_d_b, water_d_bl, water_d_b_l, water_d_l, water_d_tl,
			water_d_t_l, water_d_t, water_d_tr, water_d_t_r, water_d_r;
	public static Animation water_r_br, water_r_b_r, water_r_b, water_r_bl, water_r_b_l, water_r_l, water_r_tl,
			water_r_t_l, water_r_t, water_r_tr, water_r_t_r, water_r_r;

	// tilesets / sheets
	static BufferedImage tileSetForest, playerSheet, mushroom, tileSetDesert, tileSetWater, tileSetNatureObjects,
			tileSetHouse;
	public static BufferedImage entity_shadow;

	// nature
	public static List<BufferedImage> mushrooms = new ArrayList<BufferedImage>();

	private static BufferedImage stick_png;
	public static List<BufferedImage> stick = new ArrayList<>();

	private static BufferedImage stone_png;
	public static List<BufferedImage> stone = new ArrayList<>();

	// inventory
	public static BufferedImage placeholder;

	// lights
	public BufferedImage light;

	// Menu
	public static BufferedImage default_btn;

	//HUD
	public static BufferedImage healthbar;
	private static BufferedImage healthbar_content_img;

	//ITEMS
	private static BufferedImage tools_png;
	public static List<BufferedImage> tools = new ArrayList<>();

	public static List<BufferedImage> tileSetForestBlocks = new ArrayList<BufferedImage>();
	public static List<BufferedImage> tileSetDesertBlocks = new ArrayList<BufferedImage>();
	public static List<BufferedImage> tileSetWaterBlocks = new ArrayList<BufferedImage>();
	public static List<BufferedImage> tileSetNatureBlocks = new ArrayList<BufferedImage>();
	public static List<BufferedImage> tileSetHouseBlocks = new ArrayList<BufferedImage>();
	public static List<BufferedImage> playerImg = new ArrayList<BufferedImage>();

	public static List<BufferedImage> healthbar_content = new ArrayList<>();

	public Textures() {
		BufferedImageLoader loader = new BufferedImageLoader();

		tileSetForest = loader.loadImage("assets/main/tile_sheets/downloaded_tiles/pack_1/1.png");
		tileSetDesert = loader.loadImage("assets/main/tile_sheets/desert_tile.png");
		tileSetWater = loader.loadImage("assets/main/tile_sheets/water_tiles.png");
		tileSetNatureObjects = loader.loadImage("assets/main/tile_sheets/downloaded_tiles/pack_1/3.png");
		tileSetHouse = loader.loadImage("assets/main/tile_sheets/downloaded_tiles/pack_1/2.png");

		playerSheet = loader.loadImage("assets/entities/player/player_sheet.png");
		entity_shadow = loader.loadImage("assets/entities/shadow.png");

		mushroom = loader.loadImage("assets/world/nature/paddenstoel.png");
		stick_png = loader.loadImage("assets/items/stick.png");
		stone_png = loader.loadImage("assets/items/stone.png");
		placeholder = loader.loadImage("assets/main/placeholder.png");

		light = loader.loadImage("assets/main/lights/light_orange.png");

		default_btn = loader.loadImage("assets/menu/buttons/default_btn.png");

		healthbar = loader.loadImage("assets/main/hud/healthbar.png");
		healthbar_content_img = loader.loadImage("assets/main/hud/healthbar_content.png");

		tools_png = loader.loadImage("assets/items/tools.png");

		// height_map = loader.loadImage("assets/main/height_map.png");

		SpriteSheet ss = new SpriteSheet(tileSetForest);
		for (int i = 0; i < tileSetForest.getHeight(); i += 32) {
			for (int j = 0; j < tileSetForest.getWidth(); j += 32) {
				tileSetForestBlocks.add(ss.grabImage(j, i, 32, 32));
			}
		}
		initWaterAnimations();

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

		SpriteSheet nature_ss = new SpriteSheet(tileSetNatureObjects);
		for (int i = 0; i < tileSetNatureObjects.getHeight(); i += 32) {
			for (int j = 0; j < tileSetNatureObjects.getWidth(); j += 32) {
				tileSetNatureBlocks.add(nature_ss.grabImage(j, i, 32, 32));
			}
		}

		SpriteSheet house_ss = new SpriteSheet(tileSetHouse);
		for (int i = 0; i < tileSetHouse.getHeight(); i += 32) {
			for (int j = 0; j < tileSetHouse.getWidth(); j += 32) {
				tileSetHouseBlocks.add(house_ss.grabImage(j, i, 32, 32));
			}
		}

		SpriteSheet healthbar_content_ss = new SpriteSheet(healthbar_content_img);
		for (int i = 0; i < healthbar_content_img.getWidth(); i++) {
			healthbar_content.add(healthbar_content_ss.grabImage(i, 0, 1, 2));
		}

		SpriteSheet stick_ss = new SpriteSheet(stick_png);
		for (int i = 0; i < stick_png.getHeight(); i += 16) {
			for (int j = 0; j < stick_png.getWidth(); j += 16) {
				stick.add(stick_ss.grabImage(j, i, 16, 16));
			}
		}

		SpriteSheet stone_ss = new SpriteSheet(stone_png);
		for (int i = 0; i < stone_png.getHeight(); i += 16) {
			for (int j = 0; j < stone_png.getWidth(); j += 16) {
				stone.add(stone_ss.grabImage(j, i, 16, 16));
			}
		}

		SpriteSheet tools_ss = new SpriteSheet(tools_png);
		for (int i = 0; i < tools_png.getHeight(); i += 16) {
			for (int j = 0; j < tools_png.getWidth(); j += 16) {
				tools.add(tools_ss.grabImage(j, i, 16, 16));
			}
		}

		// water = new Animation(25, tileSetWaterBlocks.get(0),
		// tileSetWaterBlocks.get(1));
	}

	private void initWaterAnimations() {
		int speed = 25;
		water = new Animation(speed, tileSetForestBlocks.get(71), tileSetForestBlocks.get(61),
				tileSetForestBlocks.get(66), tileSetForestBlocks.get(61));

		water_r_br = new Animation(speed, tileSetForestBlocks.get(55), tileSetForestBlocks.get(45),
				tileSetForestBlocks.get(50), tileSetForestBlocks.get(45));
		water_r_b_r = new Animation(speed, tileSetForestBlocks.get(58), tileSetForestBlocks.get(48),
				tileSetForestBlocks.get(53), tileSetForestBlocks.get(48));
		water_r_b = new Animation(speed, tileSetForestBlocks.get(56), tileSetForestBlocks.get(46),
				tileSetForestBlocks.get(51), tileSetForestBlocks.get(46));
		water_r_bl = new Animation(speed, tileSetForestBlocks.get(57), tileSetForestBlocks.get(47),
				tileSetForestBlocks.get(52), tileSetForestBlocks.get(47));
		water_r_b_l = new Animation(speed, tileSetForestBlocks.get(59), tileSetForestBlocks.get(49),
				tileSetForestBlocks.get(54), tileSetForestBlocks.get(49));
		water_r_l = new Animation(speed, tileSetForestBlocks.get(72), tileSetForestBlocks.get(62),
				tileSetForestBlocks.get(67), tileSetForestBlocks.get(62));
		water_r_tl = new Animation(speed, tileSetForestBlocks.get(87), tileSetForestBlocks.get(77),
				tileSetForestBlocks.get(82), tileSetForestBlocks.get(77));
		water_r_t_l = new Animation(speed, tileSetForestBlocks.get(74), tileSetForestBlocks.get(64),
				tileSetForestBlocks.get(69), tileSetForestBlocks.get(64));
		water_r_t = new Animation(speed, tileSetForestBlocks.get(86), tileSetForestBlocks.get(76),
				tileSetForestBlocks.get(81), tileSetForestBlocks.get(76));
		water_r_tr = new Animation(speed, tileSetForestBlocks.get(85), tileSetForestBlocks.get(75),
				tileSetForestBlocks.get(80), tileSetForestBlocks.get(75));
		water_r_t_r = new Animation(speed, tileSetForestBlocks.get(73), tileSetForestBlocks.get(63),
				tileSetForestBlocks.get(68), tileSetForestBlocks.get(63));
		water_r_r = new Animation(speed, tileSetForestBlocks.get(70), tileSetForestBlocks.get(60),
				tileSetForestBlocks.get(65), tileSetForestBlocks.get(60));
	}

	// static getter from image list

	public BufferedImage getBlock(int index) {
		return tileSetForestBlocks.get(index);
	}

	public static List<BufferedImage> getList() {
		return tileSetForestBlocks;
	}

}
