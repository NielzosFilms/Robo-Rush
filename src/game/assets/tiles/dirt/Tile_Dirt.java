package game.assets.tiles.dirt;

import game.assets.tiles.tile.Tile;
import game.assets.tiles.tile.Transition;
import game.enums.BIOME;
import game.system.helpers.TransitionHelpers;
import game.system.world.Chunk;
import game.textures.TEXTURE_LIST;
import game.textures.Texture;

import java.awt.*;

public class Tile_Dirt extends Tile implements Transition {
	public Tile_Dirt(int x, int y, int chunk_x, int chunk_y, int z_index, BIOME biome, Chunk chunk) {
		super(x, y, chunk_x, chunk_y, z_index, biome, chunk);
		texture = new Texture(TEXTURE_LIST.forest_list, 6, 10);
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
		TransitionHelpers.createTransitionTiles(this, chunk, Tile_Dirt_Transition.class);
	}
}
