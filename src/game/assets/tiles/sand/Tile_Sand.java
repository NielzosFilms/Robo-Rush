package game.assets.tiles.sand;

import game.assets.tiles.grass.Tile_Grass_Transition;
import game.assets.tiles.tile.Tile;
import game.assets.tiles.tile.Transition;
import game.assets.tiles.tile.UpdateAble;
import game.enums.BIOME;
import game.system.helpers.Logger;
import game.system.helpers.Offsets;
import game.system.helpers.TileHelperFunctions;
import game.system.helpers.TransitionHelpers;
import game.system.main.Game;
import game.system.world.Chunk;
import game.textures.TEXTURE_LIST;
import game.textures.Texture;

import java.awt.*;
import java.lang.reflect.InvocationTargetException;

public class Tile_Sand extends Tile implements Transition {
	public Tile_Sand(int x, int y, int chunk_x, int chunk_y, int z_index, BIOME biome, Chunk chunk) {
		super(x, y, chunk_x, chunk_y, z_index, biome, chunk);
		texture = new Texture(TEXTURE_LIST.forest_list, 6, 13);
	}

	@Override
	public void tick() {

	}

	@Override
	public void render(Graphics g) {
		g.drawImage(this.texture.getTexure(), x, y, this.tileSize, this.tileSize, null);
	}

	@Override
	public void createTransitionTiles() {
		TransitionHelpers.createTransitionTiles(this, chunk, Tile_Sand_Transition.class);
	}
}
