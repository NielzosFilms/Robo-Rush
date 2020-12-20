package game.system.world.biome_groups;

import game.assets.tiles.tile.Tile;
import game.enums.BIOME;
import game.system.world.Chunk;

import java.io.Serializable;

public abstract class BiomeGroup implements Serializable {

	public abstract BIOME getBiome(float height, float temp, float moist);

	public abstract Tile getTile(int x, int y, int tile_chunk_x, int tile_chunk_y, Chunk chunk, BIOME biome);
}
