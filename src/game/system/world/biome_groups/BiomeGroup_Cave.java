package game.system.world.biome_groups;

import game.assets.tiles.tile.Tile;
import game.enums.BIOME;
import game.system.world.Chunk;

public class BiomeGroup_Cave extends BiomeGroup {
	public BIOME getBiome(float height, float temp, float moist) {
		if(height < 0 && temp < -0.5) return BIOME.Cave_floor;
		return BIOME.Cave_wall;
	}

	@Override
	public Tile getTile(int x, int y, int tile_chunk_x, int tile_chunk_y, Chunk chunk, BIOME biome) {
		return null;
	}
}
