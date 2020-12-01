package game.textures;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Textures {
	public static Animation water;
	public static Animation water_d_br, water_d_b_r, water_d_b, water_d_bl, water_d_b_l, water_d_l, water_d_tl,
			water_d_t_l, water_d_t, water_d_tr, water_d_t_r, water_d_r;
	public static Animation water_r_br, water_r_b_r, water_r_b, water_r_bl, water_r_b_l, water_r_l, water_r_tl,
			water_r_t_l, water_r_t, water_r_tr, water_r_t_r, water_r_r;

	// tilesets / sheets
	static BufferedImage tileSetForest, playerSheet, mushroom, tileSetDesert, tileSetWater, tileSetNatureObjects,
			tileSetHouse;
	public static BufferedImage entity_shadow;

	private static BufferedImage stick_png;
	public static HashMap<Point, BufferedImage> stick = new HashMap<>();

	private static BufferedImage stone_png;
	public static HashMap<Point, BufferedImage> stone = new HashMap<>();

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
	public static HashMap<Point, BufferedImage> tools = new HashMap<>();

	public static HashMap<Point, BufferedImage> forest_list = new HashMap<>();
	public static HashMap<Point, BufferedImage> desert_list = new HashMap<>();
	public static HashMap<Point, BufferedImage> nature_list = new HashMap<>();
	public static HashMap<Point, BufferedImage> house_list = new HashMap<>();
	public static HashMap<Point, BufferedImage> player_list = new HashMap<>();

	public static HashMap<Point, BufferedImage> healthbar_list = new HashMap<>();

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


		fillListWithSpriteSheet(tileSetForest, 32, 32, forest_list);
		initWaterAnimations();
		fillListWithSpriteSheet(playerSheet, 16, 24, player_list);
		fillListWithSpriteSheet(tileSetDesert, 16, 16, desert_list);
		fillListWithSpriteSheet(tileSetNatureObjects, 32, 32, nature_list);
		fillListWithSpriteSheet(tileSetHouse, 32, 32, house_list);
		fillListWithSpriteSheet(healthbar_content_img, 1, 2, healthbar_list);
		fillListWithSpriteSheet(stick_png, 16, 16, stick);
		fillListWithSpriteSheet(stone_png, 16, 16, stone);
		fillListWithSpriteSheet(tools_png, 16, 16, tools);

		// water = new Animation(25, tileSetWaterBlocks.get(0),
		// tileSetWaterBlocks.get(1));
	}

	private static void fillListWithSpriteSheet(BufferedImage sheet, int width, int height, HashMap<Point, BufferedImage> list) {
		SpriteSheet ss = new SpriteSheet(sheet);
		for (int y = 0; y < sheet.getHeight() / height; y++) {
			for (int x = 0; x < sheet.getWidth() / width; x++) {
				list.put(new Point(x, y), ss.grabImage(x * width, y * height, width, height));
			}
		}
	}

	private void initWaterAnimations() {
		int speed = 25;

		water = new Animation(speed,
				new Texture(TEXTURE_LIST.forest_list, 11, 4),
				new Texture(TEXTURE_LIST.forest_list, 1, 4),
				new Texture(TEXTURE_LIST.forest_list, 6, 4),
				new Texture(TEXTURE_LIST.forest_list, 1, 4));

		water_r_br = new Animation(speed,
				new Texture(TEXTURE_LIST.forest_list, 11, 4),
				new Texture(TEXTURE_LIST.forest_list, 1, 4),
				new Texture(TEXTURE_LIST.forest_list, 6, 4),
				new Texture(TEXTURE_LIST.forest_list, 1, 4));
		water_r_b_r = new Animation(speed,
				new Texture(TEXTURE_LIST.forest_list, 11, 4),
				new Texture(TEXTURE_LIST.forest_list, 1, 4),
				new Texture(TEXTURE_LIST.forest_list, 6, 4),
				new Texture(TEXTURE_LIST.forest_list, 1, 4));
		water_r_b = new Animation(speed,
				new Texture(TEXTURE_LIST.forest_list, 11, 4),
				new Texture(TEXTURE_LIST.forest_list, 1, 4),
				new Texture(TEXTURE_LIST.forest_list, 6, 4),
				new Texture(TEXTURE_LIST.forest_list, 1, 4));
		water_r_bl = new Animation(speed,
				new Texture(TEXTURE_LIST.forest_list, 11, 4),
				new Texture(TEXTURE_LIST.forest_list, 1, 4),
				new Texture(TEXTURE_LIST.forest_list, 6, 4),
				new Texture(TEXTURE_LIST.forest_list, 1, 4));
		water_r_b_l = new Animation(speed,
				new Texture(TEXTURE_LIST.forest_list, 11, 4),
				new Texture(TEXTURE_LIST.forest_list, 1, 4),
				new Texture(TEXTURE_LIST.forest_list, 6, 4),
				new Texture(TEXTURE_LIST.forest_list, 1, 4));
		water_r_l = new Animation(speed,
				new Texture(TEXTURE_LIST.forest_list, 11, 4),
				new Texture(TEXTURE_LIST.forest_list, 1, 4),
				new Texture(TEXTURE_LIST.forest_list, 6, 4),
				new Texture(TEXTURE_LIST.forest_list, 1, 4));
		water_r_tl =new Animation(speed,
				new Texture(TEXTURE_LIST.forest_list, 11, 4),
				new Texture(TEXTURE_LIST.forest_list, 1, 4),
				new Texture(TEXTURE_LIST.forest_list, 6, 4),
				new Texture(TEXTURE_LIST.forest_list, 1, 4));
		water_r_t_l = new Animation(speed,
				new Texture(TEXTURE_LIST.forest_list, 11, 4),
				new Texture(TEXTURE_LIST.forest_list, 1, 4),
				new Texture(TEXTURE_LIST.forest_list, 6, 4),
				new Texture(TEXTURE_LIST.forest_list, 1, 4));
		water_r_t =new Animation(speed,
				new Texture(TEXTURE_LIST.forest_list, 11, 4),
				new Texture(TEXTURE_LIST.forest_list, 1, 4),
				new Texture(TEXTURE_LIST.forest_list, 6, 4),
				new Texture(TEXTURE_LIST.forest_list, 1, 4));
		water_r_tr =new Animation(speed,
				new Texture(TEXTURE_LIST.forest_list, 11, 4),
				new Texture(TEXTURE_LIST.forest_list, 1, 4),
				new Texture(TEXTURE_LIST.forest_list, 6, 4),
				new Texture(TEXTURE_LIST.forest_list, 1, 4));
		water_r_t_r =new Animation(speed,
				new Texture(TEXTURE_LIST.forest_list, 11, 4),
				new Texture(TEXTURE_LIST.forest_list, 1, 4),
				new Texture(TEXTURE_LIST.forest_list, 6, 4),
				new Texture(TEXTURE_LIST.forest_list, 1, 4));
		water_r_r =new Animation(speed,
				new Texture(TEXTURE_LIST.forest_list, 11, 4),
				new Texture(TEXTURE_LIST.forest_list, 1, 4),
				new Texture(TEXTURE_LIST.forest_list, 6, 4),
				new Texture(TEXTURE_LIST.forest_list, 1, 4));
	}

	// static getter from image list

	public static BufferedImage getTexture(Texture texture) {
		return getTextureList(texture.getTexture_list()).get(new Point(texture.getX(), texture.getY()));
	}

	private static HashMap<Point, BufferedImage> getTextureList(TEXTURE_LIST list) {
		HashMap<Point, BufferedImage> ret = null;
		switch (list) {
			case stick -> ret = stick;
			case stone -> ret = stone;
			case tools -> ret = tools;
			case forest_list -> ret = forest_list;
			case desert_list -> ret = desert_list;
			case nature_list -> ret = nature_list;
			case house_list -> ret = house_list;
			case player_list -> ret = player_list;
			case healthbar_list -> ret = healthbar_list;
		}
		return ret;
	}

}
