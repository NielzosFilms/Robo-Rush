package game.assets.tiles.floor.cave;

import game.assets.items.item.Item;
import game.assets.tiles.tile.Tile;
import game.enums.BIOME;
import game.system.world.Chunk;
import game.textures.TEXTURE_LIST;
import game.textures.Texture;

import java.awt.*;

public class Tile_Floor_Cave extends Tile {
	public Tile_Floor_Cave(int x, int y, int chunk_x, int chunk_y, int z_index, BIOME biome, Chunk chunk) {
		super(x, y, chunk_x, chunk_y, z_index, biome, chunk);
		this.texture = new Texture(TEXTURE_LIST.forest_list, 1, 19);
	}

	public void tick() {

	}

	public void render(Graphics g) {
		g.drawImage(texture.getTexure(), x, y, 16, 16, null);
	}

	public void update() {

	}
}
