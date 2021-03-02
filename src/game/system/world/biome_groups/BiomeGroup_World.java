package game.system.world.biome_groups;

import game.assets.tiles.tile.Tile;
import game.enums.BIOME;
import game.system.world.Chunk;

public class BiomeGroup_World extends BiomeGroup {
	public BIOME getBiome(float height, float temp, float moist) {
		if (moist > 0.2 && temp > 0.2) {
			if(height > 0.8) return BIOME.Forest_Plateau;
			return BIOME.Forest;
		}
		if (moist < -0.35 && temp > 0.2) {
			return BIOME.Desert;
		}
		if(temp < -0.8) return BIOME.Polar;
		if(temp < -0.4) return BIOME.Tundra;
		if (height < -0.2) return BIOME.Ocean;
		return BIOME.NULL;
	}

	public Tile getTile(int x, int y, int tile_chunk_x, int tile_chunk_y, Chunk chunk, BIOME biome) {
		Tile ret = null;
//		switch(biome) {
//			case Forest:
//				ret = new Tile_Grass(x, y, tile_chunk_x, tile_chunk_y, 1, biome, chunk);
//				break;
//			case Forest_Plateau:
//				ret = new Tile_Grass_Plateau(x, y, tile_chunk_x, tile_chunk_y, 3, biome, chunk);
//				break;
//			case Desert:
//				ret = new Tile_Sand(x, y, tile_chunk_x, tile_chunk_y, 2, biome, chunk);
//				break;
//			case Tundra:
//				ret = new Tile_Dirt(x, y, tile_chunk_x, tile_chunk_y, 4, biome, chunk);
//				break;
//			case Polar:
//				ret = new Tile_Snow(x, y, tile_chunk_x, tile_chunk_y, 5, biome, chunk);
//				break;
//			case Ocean:
//				ret = new Tile_Water(x, y, tile_chunk_x, tile_chunk_y, 0, biome, chunk);
//				break;
//			default:
//				ret = new Tile_Water(x, y, tile_chunk_x, tile_chunk_y, 0, biome, chunk);
//				break;
//		}
		return ret;
	}
}
