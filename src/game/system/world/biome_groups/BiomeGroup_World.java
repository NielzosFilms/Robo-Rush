package game.system.world.biome_groups;

import game.enums.BIOME;

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
}
