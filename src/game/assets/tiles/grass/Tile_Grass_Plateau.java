package game.assets.tiles.grass;
import game.assets.tiles.tile.Tile;
import game.assets.tiles.tile.Transition;
import game.enums.BIOME;
import game.enums.TILE_TYPE;
import game.system.helpers.TileHelperFunctions;
import game.system.helpers.TransitionHelpers;
import game.system.world.Chunk;
import game.textures.TEXTURE_LIST;
import game.textures.Texture;

import java.awt.*;
import java.util.HashMap;

public class Tile_Grass_Plateau extends Tile implements Transition {
	private HashMap<TILE_TYPE, Texture> textures = new HashMap<>();
	public Tile_Grass_Plateau(int x, int y, int chunk_x, int chunk_y, int z_index, BIOME biome, Chunk chunk) {
		super(x, y, chunk_x, chunk_y, z_index, biome, chunk);
		textures.put(TILE_TYPE.center, new Texture(TEXTURE_LIST.grass_plateau, 1, 2));
		textures.put(TILE_TYPE.top, new Texture(TEXTURE_LIST.grass_plateau, 1, 2));
		textures.put(TILE_TYPE.bottom, new Texture(TEXTURE_LIST.grass_plateau, 1, 3));
		textures.put(TILE_TYPE.left, new Texture(TEXTURE_LIST.grass_plateau, 0, 2));
		textures.put(TILE_TYPE.right, new Texture(TEXTURE_LIST.grass_plateau, 1, 2));

		textures.put(TILE_TYPE.top_right, new Texture(TEXTURE_LIST.grass_plateau, 13, 0));
		textures.put(TILE_TYPE.bottom_right, new Texture(TEXTURE_LIST.grass_plateau, 2, 3));
		textures.put(TILE_TYPE.bottom_left, new Texture(TEXTURE_LIST.grass_plateau, 0, 3));
		textures.put(TILE_TYPE.top_left, new Texture(TEXTURE_LIST.grass_plateau, 11, 0));

		texture = textures.get(TILE_TYPE.center);
	}

	@Override
	public void tick() {

	}

	@Override
	public void render(Graphics g) {
		g.drawImage(texture.getTexure(), x, y, tileSize, tileSize, null);
	}

	@Override
	public void createTransitionTiles() {
		TransitionHelpers.createTransitionTiles(this, chunk, Tile_Grass_Plateau_Transition.class);
	}
}
