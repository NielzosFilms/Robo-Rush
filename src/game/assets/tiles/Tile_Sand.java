package game.assets.tiles;

import game.assets.items.item.Item;
import game.assets.tiles.tile.EdgeTextures;
import game.assets.tiles.tile.Tile;
import game.enums.BIOME;
import game.system.systems.gameObject.Collision;
import game.system.world.Chunk;
import game.textures.TEXTURE_LIST;
import game.textures.Texture;

import java.awt.*;

public class Tile_Sand extends Tile implements EdgeTextures {
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
	public void update() {

	}

	@Override
	public void findAndSetEdgeTexture() {

	}
}
