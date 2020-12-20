package game.system.world.biome_groups;

import game.assets.tiles.dirt.Tile_Dirt;
import game.assets.tiles.snow.Tile_Snow;
import game.assets.tiles.Tile_Water;
import game.assets.tiles.grass.Tile_Grass;
import game.assets.tiles.grass.Tile_Grass_Plateau;
import game.assets.tiles.sand.Tile_Sand;
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
		Tile ret;
		switch(biome) {
			case Forest -> ret = new Tile_Grass(x, y, tile_chunk_x, tile_chunk_y, 1, biome, chunk);
			case Forest_Plateau -> ret = new Tile_Grass_Plateau(x, y, tile_chunk_x, tile_chunk_y, 3, biome, chunk);
			case Desert -> ret = new Tile_Sand(x, y, tile_chunk_x, tile_chunk_y, 2, biome, chunk);
			case Tundra -> ret = new Tile_Dirt(x, y, tile_chunk_x, tile_chunk_y, 4, biome, chunk);
			case Polar -> ret = new Tile_Snow(x, y, tile_chunk_x, tile_chunk_y, 5, biome, chunk);
			case Ocean -> ret = new Tile_Water(x, y, tile_chunk_x, tile_chunk_y, 0, biome, chunk);
			default -> ret = new Tile_Water(x, y, tile_chunk_x, tile_chunk_y, 0, biome, chunk);
		}
		return ret;
	}
}
