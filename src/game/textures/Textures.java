package game.textures;

import game.enums.TEXTURE_LIST;
import game.enums.TILE_TYPE;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;

public class Textures {
	public static HashMap<TEXTURE_LIST, HashMap<Point, BufferedImage>> texture_lists = new HashMap<>();

	public static HashMap<TILE_TYPE, Animation> water_gray = new HashMap<>();
	public static HashMap<TILE_TYPE, Animation> water_red = new HashMap<>();


	// tilesets / sheets
	private static BufferedImage
			tileSetForest,
			playerSheet,
			mushroom,
			tileSetDesert,
			tileSetWater,
			tileSetNatureObjects,
			tileSetHouse,
			tileSetCave,
			loading_png,
			floorTiles_png,
			stick_png,
			stone_png,
			healthbar_content_img,
			tools_png;

	// other standalone tiles
	public static BufferedImage
			entity_shadow,
			placeholder,
			light,
			default_btn,
			healthbar;

	public Textures() {
		for(TEXTURE_LIST list_name : TEXTURE_LIST.values()) {
			texture_lists.put(list_name, new HashMap<>());
		}
		initImages();
		fillLists();
		initWaterAnimations();
	}

	private static void initImages() {
		BufferedImageLoader loader = new BufferedImageLoader();
		tileSetForest = loader.loadImage("assets/main/tile_sheets/downloaded_tiles/pack_1/1.png");
		tileSetCave = loader.loadImage("assets/main/tile_sheets/downloaded_tiles/pack_1/8.png");
		tileSetDesert = loader.loadImage("assets/main/tile_sheets/desert_tile.png");
		tileSetWater = loader.loadImage("assets/main/tile_sheets/water_tiles.png");
		tileSetNatureObjects = loader.loadImage("assets/main/tile_sheets/downloaded_tiles/pack_1/3.png");
		tileSetHouse = loader.loadImage("assets/main/tile_sheets/downloaded_tiles/pack_1/2.png");
		floorTiles_png = loader.loadImage("assets/main/tile_sheets/downloaded_tiles/pack_1/6.png");

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

		loading_png = loader.loadImage("assets/main/loading_animation_shadow.png");
	}

	private static void fillLists() {
		fillListWithSpriteSheet(tileSetForest, 32, 32, texture_lists.get(TEXTURE_LIST.forest_list));
		fillListWithSpriteSheet(tileSetCave, 32, 32, texture_lists.get(TEXTURE_LIST.cave_list));
		fillListWithSpriteSheet(playerSheet, 16, 24, texture_lists.get(TEXTURE_LIST.player_list));
		fillListWithSpriteSheet(tileSetDesert, 16, 16, texture_lists.get(TEXTURE_LIST.desert_list));
		fillListWithSpriteSheet(tileSetNatureObjects, 32, 32, texture_lists.get(TEXTURE_LIST.nature_list));
		fillListWithSpriteSheet(tileSetHouse, 32, 32, texture_lists.get(TEXTURE_LIST.house_list));
		fillListWithSpriteSheet(healthbar_content_img, 1, 2, texture_lists.get(TEXTURE_LIST.healthbar_list));
		fillListWithSpriteSheet(stick_png, 16, 16, texture_lists.get(TEXTURE_LIST.stick));
		fillListWithSpriteSheet(stone_png, 16, 16, texture_lists.get(TEXTURE_LIST.stone));
		fillListWithSpriteSheet(tools_png, 16, 16, texture_lists.get(TEXTURE_LIST.tools));
		fillListWithSpriteSheet(loading_png, 16, 16, texture_lists.get(TEXTURE_LIST.loading_list));
		fillListWithSpriteSheet(floorTiles_png, 32, 32, texture_lists.get(TEXTURE_LIST.floorTiles_list));
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

		water_red.put(TILE_TYPE.center, new Animation(speed,
				new Texture(TEXTURE_LIST.forest_list, 11, 4),
				new Texture(TEXTURE_LIST.forest_list, 1, 4),
				new Texture(TEXTURE_LIST.forest_list, 6, 4),
				new Texture(TEXTURE_LIST.forest_list, 1, 4)));

		water_red.put(TILE_TYPE.bottom_right_inverse, new Animation(speed,
				new Texture(TEXTURE_LIST.forest_list, 10, 6),
				new Texture(TEXTURE_LIST.forest_list, 0, 6),
				new Texture(TEXTURE_LIST.forest_list, 5, 6),
				new Texture(TEXTURE_LIST.forest_list, 0, 6)));

		water_red.put(TILE_TYPE.bottom_right, new Animation(speed,
				new Texture(TEXTURE_LIST.forest_list, 13, 6),
				new Texture(TEXTURE_LIST.forest_list, 3, 6),
				new Texture(TEXTURE_LIST.forest_list, 8, 6),
				new Texture(TEXTURE_LIST.forest_list, 3, 6)));

		water_red.put(TILE_TYPE.bottom, new Animation(speed,
				new Texture(TEXTURE_LIST.forest_list, 11, 6),
				new Texture(TEXTURE_LIST.forest_list, 1, 6),
				new Texture(TEXTURE_LIST.forest_list, 6, 6),
				new Texture(TEXTURE_LIST.forest_list, 1, 6)));

		water_red.put(TILE_TYPE.bottom_left_inverse, new Animation(speed,
				new Texture(TEXTURE_LIST.forest_list, 12, 6),
				new Texture(TEXTURE_LIST.forest_list, 2, 6),
				new Texture(TEXTURE_LIST.forest_list, 7, 6),
				new Texture(TEXTURE_LIST.forest_list, 2, 6)));

		water_red.put(TILE_TYPE.bottom_left, new Animation(speed,
				new Texture(TEXTURE_LIST.forest_list, 14, 6),
				new Texture(TEXTURE_LIST.forest_list, 4, 6),
				new Texture(TEXTURE_LIST.forest_list, 9, 6),
				new Texture(TEXTURE_LIST.forest_list, 4, 6)));

		water_red.put(TILE_TYPE.left, new Animation(speed,
				new Texture(TEXTURE_LIST.forest_list, 12, 7),
				new Texture(TEXTURE_LIST.forest_list, 2, 7),
				new Texture(TEXTURE_LIST.forest_list, 7, 7),
				new Texture(TEXTURE_LIST.forest_list, 2, 7)));

		water_red.put(TILE_TYPE.top_left_inverse, new Animation(speed,
				new Texture(TEXTURE_LIST.forest_list, 12, 8),
				new Texture(TEXTURE_LIST.forest_list, 2, 8),
				new Texture(TEXTURE_LIST.forest_list, 7, 8),
				new Texture(TEXTURE_LIST.forest_list, 2, 8)));

		water_red.put(TILE_TYPE.top_left, new Animation(speed,
				new Texture(TEXTURE_LIST.forest_list, 14, 7),
				new Texture(TEXTURE_LIST.forest_list, 4, 7),
				new Texture(TEXTURE_LIST.forest_list, 9, 7),
				new Texture(TEXTURE_LIST.forest_list, 4, 7)));

		water_red.put(TILE_TYPE.top, new Animation(speed,
				new Texture(TEXTURE_LIST.forest_list, 11, 8),
				new Texture(TEXTURE_LIST.forest_list, 1, 8),
				new Texture(TEXTURE_LIST.forest_list, 6, 8),
				new Texture(TEXTURE_LIST.forest_list, 1, 8)));

		water_red.put(TILE_TYPE.top_right_inverse, new Animation(speed,
				new Texture(TEXTURE_LIST.forest_list, 10, 8),
				new Texture(TEXTURE_LIST.forest_list, 0, 8),
				new Texture(TEXTURE_LIST.forest_list, 5, 8),
				new Texture(TEXTURE_LIST.forest_list, 0, 8)));

		water_red.put(TILE_TYPE.top_right, new Animation(speed,
				new Texture(TEXTURE_LIST.forest_list, 13, 7),
				new Texture(TEXTURE_LIST.forest_list, 3, 7),
				new Texture(TEXTURE_LIST.forest_list, 8, 7),
				new Texture(TEXTURE_LIST.forest_list, 3, 7)));

		water_red.put(TILE_TYPE.right, new Animation(speed,
				new Texture(TEXTURE_LIST.forest_list, 10, 7),
				new Texture(TEXTURE_LIST.forest_list, 0, 7),
				new Texture(TEXTURE_LIST.forest_list, 5, 7),
				new Texture(TEXTURE_LIST.forest_list, 0, 7)));
	}

	// static getter from image list

	public static BufferedImage getTexture(Texture texture) {
		return texture_lists.get(texture.getTexture_list()).get(new Point(texture.getX(), texture.getY()));
	}

}
