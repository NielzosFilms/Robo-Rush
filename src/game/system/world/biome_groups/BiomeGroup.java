package game.system.world.biome_groups;

import game.enums.BIOME;

import java.io.Serializable;

public abstract class BiomeGroup implements Serializable {

	public abstract BIOME getBiome(float height, float temp, float moist);
}
