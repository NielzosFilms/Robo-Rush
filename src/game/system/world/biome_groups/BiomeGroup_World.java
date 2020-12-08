package game.system.world.biome_groups;

import game.enums.BIOME;

public class BiomeGroup_World extends BiomeGroup {
	public BiomeGroup_World() {}

	public BIOME getBiome(float height, float temp, float moist) {
		// biome generation needs refinement
		/*if ((temp_val > -0.5 && temp_val < 0.5) && (moist_val > 0.5)) { // forest
			if (val < -0.3) {
				return BIOME.Beach;
			} else {
				return BIOME.Forest;
			}
		} else if (temp_val < 0 && moist_val < 0) { // desert
			return BIOME.Desert;
		} else if (temp_val > 0 && moist_val < 0) { // dirt
			return BIOME.Dirt;
		}
		return BIOME.Ocean;*/
		if (height > -0.5) {
			return BIOME.Forest;
		}
		return BIOME.Ocean;
	}


}
