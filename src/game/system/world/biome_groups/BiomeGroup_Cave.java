package game.system.world.biome_groups;

import game.enums.BIOME;

public class BiomeGroup_Cave extends BiomeGroup {
	public BIOME getBiome(float height, float temp, float moist) {
		if(height < 0 && temp < 0) return BIOME.Cave_floor;
		return BIOME.Cave_wall;
	}
}
