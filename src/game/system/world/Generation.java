package game.system.world;

import game.assets.tiles.tile.Tile;
import game.enums.BIOME;
import game.system.world.biome_groups.BiomeGroup;

import java.io.Serializable;
import java.util.Random;

public class Generation implements Serializable {
    private BiomeGroup biomeGroup;
    private Long
            seed,
            temp_seed,
            moist_seed;

    private int
            height_octaves = 3,
            temp_octaves = 3,
            moist_octaves = 3;

    private float
            height_roughness = 0.3f,
            temp_roughness = 0.3f,
            moist_roughness = 0.3f;

    private float
            height_scale = 0.02f,
            temp_scale = 0.02f,
            mois_scale = 0.02f;


    public Generation(Long seed, BiomeGroup biomeGroup) {
        this.seed = seed;
        Random r = new Random(seed);
        temp_seed = r.nextLong();
        moist_seed = r.nextLong();
        this.biomeGroup = biomeGroup;
    }

    public Generation(Long seed, Long temp_seed, Long moist_seed, BiomeGroup biomeGroup) {
        this.seed = seed;
        this.temp_seed = temp_seed;
        this.moist_seed = moist_seed;
        this.biomeGroup = biomeGroup;
    }

    public float[] getHeightMapValuePoint(int x, int y) {
        float[][] osn = getHeightOsn(x, y, 1, 1);
        float[][] temp_osn = getTemperatureOsn(x, y, 1, 1);
        float[][] moist_osn = getMoistureOsn(x, y, 1, 1);
        return new float[]{ osn[0][0], temp_osn[0][0], moist_osn[0][0] };
    }

    public float[][] getHeightOsn(int x, int y, int w, int h) {
        return generateOctavedSimplexNoise(x, y, w, h, height_octaves, height_roughness, height_scale, seed);
    }

    public float[][] getTemperatureOsn(int x, int y, int w, int h) {
        return generateOctavedSimplexNoise(x, y, w, h, temp_octaves, temp_roughness, temp_scale, temp_seed);
    }

    public float[][] getMoistureOsn(int x, int y, int w, int h) {
        return generateOctavedSimplexNoise(x, y, w, h, moist_octaves, moist_roughness, mois_scale, moist_seed);
    }

    public float[][] generateOctavedSimplexNoise(int xx, int yy, int width, int height, int octaves,
                                                 float roughness, float scale, Long seed) {
        float[][] totalNoise = new float[width][height];
        float layerFrequency = scale;
        float layerWeight = 1;
        float weightSum = 0;
        // Long seed = r.nextLong();
        // Long seed = 3695317381661324390L;
        OpenSimplexNoise noise = new OpenSimplexNoise(seed);

        for (int octave = 0; octave < octaves; octave++) {
            // Calculate single layer/octave of simplex noise, then add it to total noise
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    totalNoise[x][y] += (float) noise.eval((x + xx) * layerFrequency, (y + yy) * layerFrequency)
                            * layerWeight;
                }
            }

            // Increase variables with each incrementing octave
            layerFrequency *= 2;
            weightSum += layerWeight;
            layerWeight *= roughness;

        }
        return totalNoise;
    }

    public BIOME getBiome(float height, float temp, float moist) {
        return biomeGroup.getBiome(height, temp, moist);
    }

    public BIOME getBiomeWithCoords(int x, int y) {
		x /= 16;
		y /= 16;
		float[] arr = getHeightMapValuePoint(x, y);
		return biomeGroup.getBiome(arr[0], arr[1], arr[2]);
	}

    public Long getSeed() {
        return seed;
    }

    public void setSeed(Long seed) {
        this.seed = seed;
    }

    public void setNewSeed(Long seed) {
        this.seed = seed;
        Random r = new Random(seed);
        temp_seed = r.nextLong();
        moist_seed = r.nextLong();
    }

    public Tile getTile(int x, int y, int tile_chunk_x, int tile_chunk_y, Chunk chunk, BIOME biome) {
        return biomeGroup.getTile(x, y, tile_chunk_x, tile_chunk_y, chunk, biome);
    }

    public Long getTemp_seed() {
        return temp_seed;
    }

    public void setTemp_seed(Long temp_seed) {
        this.temp_seed = temp_seed;
    }

    public Long getMoist_seed() {
        return moist_seed;
    }

    public void setMoist_seed(Long moist_seed) {
        this.moist_seed = moist_seed;
    }

    public int getHeight_octaves() {
        return height_octaves;
    }

    public void setHeight_octaves(int height_octaves) {
        this.height_octaves = height_octaves;
    }

    public int getTemp_octaves() {
        return temp_octaves;
    }

    public void setTemp_octaves(int temp_octaves) {
        this.temp_octaves = temp_octaves;
    }

    public int getMoist_octaves() {
        return moist_octaves;
    }

    public void setMoist_octaves(int moist_octaves) {
        this.moist_octaves = moist_octaves;
    }

    public float getHeight_roughness() {
        return height_roughness;
    }

    public void setHeight_roughness(float height_roughness) {
        this.height_roughness = height_roughness;
    }

    public float getTemp_roughness() {
        return temp_roughness;
    }

    public void setTemp_roughness(float temp_roughness) {
        this.temp_roughness = temp_roughness;
    }

    public float getMoist_roughness() {
        return moist_roughness;
    }

    public void setMoist_roughness(float moist_roughness) {
        this.moist_roughness = moist_roughness;
    }

    public float getHeight_scale() {
        return height_scale;
    }

    public void setHeight_scale(float height_scale) {
        this.height_scale = height_scale;
    }

    public float getTemp_scale() {
        return temp_scale;
    }

    public void setTemp_scale(float temp_scale) {
        this.temp_scale = temp_scale;
    }

    public float getMois_scale() {
        return mois_scale;
    }

    public void setMois_scale(float mois_scale) {
        this.mois_scale = mois_scale;
    }
}
