package game.system.world.biome_groups;

import game.enums.BIOME;

public abstract class BiomeGroup {
	public BiomeGroup() {

	}

	public abstract BIOME getBiome(float height, float temp, float moist);
}
