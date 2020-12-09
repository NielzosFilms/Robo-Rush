package game.assets.tiles.floor.cave;

import game.assets.items.Item;
import game.assets.tiles.Tile;
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

	public Rectangle getBounds() {
		return null;
	}

	public Rectangle getSelectBounds() {
		return null;
	}

	public void findAndSetEdgeTexture() {

	}

	public void update() {

	}

	public Item getItem() {
		return null;
	}
}
