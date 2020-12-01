package game.system.world;

import java.awt.Graphics;
import java.awt.Point;
import java.io.*;
import java.lang.reflect.Field;
import java.util.*;

import game.assets.entities.Player;
import game.enums.BIOME;
import game.enums.ID;
import game.system.inputs.KeyInput;
import game.system.lighting.Light;
import game.system.main.Game;
import game.system.main.GameObject;
import game.system.main.Logger;
import game.textures.Textures;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import static game.system.main.Game.keyInput;

public class World implements Serializable {

	public Long seed, temp_seed, moist_seed;
	public HashMap<Point, Chunk> chunks = new HashMap<Point, Chunk>();
	public static boolean loaded = false;
	private Player player;
	private transient Textures textures;
	private Random r;
	private transient KeyInput keyInput;

	private static OpenSimplexNoise noise;

	public World() {
		this.player = new Player(0, 0, 2, ID.Player, null);
	}

	public void setRequirements(Textures textures, KeyInput keyInput) {
		this.textures = textures;
		this.keyInput = keyInput;
		this.player.setKeyInput(this.keyInput);
	}

	public void tick() {
		if(!loaded) return;
		int camX = (Math.round(-Game.cam.getX() / 16));
		int camY = (Math.round(-Game.cam.getY() / 16));
		int camW = (Math.round(Game.WIDTH / 16));
		int camH = (Math.round(Game.HEIGHT / 16));

		runWaterAnimations();

		for (int y = camY - 32; y < camY + camH + 16; y++) {
			for (int x = camX - 32; x < camX + camW + 16; x++) {
				if (chunks.containsKey(new Point(x, y))) {
					chunks.get(new Point(x, y)).tick();
					if (!chunks.containsKey(new Point(x - 16, y))) {
						chunks.put(new Point(x - 16, y),
								new Chunk(x - 16, y, seed, temp_seed, moist_seed, this, player, textures));
					} else if (!chunks.containsKey(new Point(x + 16, y))) {
						chunks.put(new Point(x + 16, y),
								new Chunk(x + 16, y, seed, temp_seed, moist_seed, this, player, textures));
					}
					if (!chunks.containsKey(new Point(x, y - 16))) {
						chunks.put(new Point(x, y - 16),
								new Chunk(x, y - 16, seed, temp_seed, moist_seed, this, player, textures));
					} else if (!chunks.containsKey(new Point(x, y + 16))) {
						chunks.put(new Point(x, y + 16),
								new Chunk(x, y + 16, seed, temp_seed, moist_seed, this, player, textures));
					}
				}
			}
		}
	}

	private void runWaterAnimations() {
		textures.water.runAnimation();

		textures.water_r_br.runAnimation();
		textures.water_r_b_r.runAnimation();
		textures.water_r_b.runAnimation();
		textures.water_r_bl.runAnimation();
		textures.water_r_b_l.runAnimation();
		textures.water_r_l.runAnimation();
		textures.water_r_tl.runAnimation();
		textures.water_r_t_l.runAnimation();
		textures.water_r_t.runAnimation();
		textures.water_r_tr.runAnimation();
		textures.water_r_t_r.runAnimation();
		textures.water_r_r.runAnimation();
	}

	public LinkedList<Chunk> getChunksOnScreen() {
		LinkedList<Chunk> tmp_chunks = new LinkedList<Chunk>();

		int camX = (Math.round(-Game.cam.getX() / 16));
		int camY = (Math.round(-Game.cam.getY() / 16));
		int camW = (Math.round(Game.WIDTH / 16));
		int camH = (Math.round(Game.HEIGHT / 16));

		for (int y = camY - 32; y < camY + camH + 16; y++) {
			for (int x = camX - 32; x < camX + camW + 16; x++) {
				if (chunks.containsKey(new Point(x, y))) {
					Chunk chunk = chunks.get(new Point(x, y));
					tmp_chunks.add(chunk);
				}
			}
		}
		return tmp_chunks;
	}

	public void render(Graphics g) {

		int camX = (Math.round(-Game.cam.getX() / 16));
		int camY = (Math.round(-Game.cam.getY() / 16));
		int camW = (Math.round(Game.WIDTH / 16));
		int camH = (Math.round(Game.HEIGHT / 16));

		for (int y = camY - 32; y < camY + camH + 16; y++) {
			for (int x = camX - 32; x < camX + camW + 16; x++) {
				if (chunks.containsKey(new Point(x, y))) {
					Chunk chunk = chunks.get(new Point(x, y));
					chunk.renderTiles(g);
				}
			}
		}

		for (int y = camY - 32; y < camY + camH + 16; y++) {
			for (int x = camX - 32; x < camX + camW + 16; x++) {
				if (chunks.containsKey(new Point(x, y))) {
					Chunk chunk = chunks.get(new Point(x, y));
					chunk.renderEntities(g);
				}
			}
		}
	}

	public boolean addEntityToChunk(GameObject entity) {
		int x = entity.getX();
		int y = entity.getY();
		Chunk crsp_chunk = getChunkWithCoordsPoint(getChunkPointWithCoords(x, y));
		if (crsp_chunk != null) { // adds enemy to a chunk to be unloaded
			//Logger.print(String.valueOf(entity.getId()));
			crsp_chunk.addEntity(entity);
			return true;
		}
		return false;
	}

	public boolean addLightToChunk(Light light) {
		int x = light.getX();
		int y = light.getY();
		Chunk crsp_chunk = getChunkWithCoordsPoint(getChunkPointWithCoords(x, y));
		if (crsp_chunk != null) { // adds enemy to a chunk to be unloaded
			crsp_chunk.addLight(light);

			return true;
		}
		return false;
	}

	// functions to get specific tiles/tiletypes

	private boolean OnScreen(int x, int y, int w, int h) {
		return (x + (16 * 16) > -Game.cam.getX() && x < -Game.cam.getX() + Game.WIDTH)
				&& (y + (16 * 16) > -Game.cam.getY() && y < -Game.cam.getY() + Game.HEIGHT);
	}

	public Chunk getChunkWithCoords(int x, int y) {
		Point chunk_point = new Point(x, y);
		return chunks.get(chunk_point);
	}

	public Chunk getChunkWithCoordsPoint(Point coords) {
		return chunks.get(coords);
	}

	public Point getChunkPointWithCoords(int x, int y) {
		x = x / 16;
		y = y / 16;

		x -= Math.floorMod(x, 16);
		y -= Math.floorMod(y, 16);

		return new Point(x, y);
	}

	public static BIOME getBiome(float val, float temp_val, float moist_val) {
		// biome generation needs refinement
		if ((temp_val > -0.5 && temp_val < 0.5) && (moist_val > 0.5)) { // forest
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
		return BIOME.Ocean;
	}

	public BIOME getBiomeWithCoords(int x, int y) {
		x /= 16;
		y /= 16;
		float[] arr = getHeightMapValuePoint(x, y);
		return getBiome(arr[0], arr[1], arr[2]);
	}

	public float[] getHeightMapValuePoint(int x, int y) {
		// x = x/16/16;
		// y = y/16/16;
		float[][] osn = generateOctavedSimplexNoise(x, y, 1, 1, 3, 0.4f, 0.05f, seed);
		float[][] temp_osn = generateOctavedSimplexNoise(x, y, 1, 1, 3, 0.4f, 0.02f, temp_seed); // scale 0.01f ?
		float[][] moist_osn = generateOctavedSimplexNoise(x, y, 1, 1, 3, 0.4f, 0.02f, moist_seed);
		float[] arr = { osn[0][0], temp_osn[0][0], moist_osn[0][0] };
		return arr;
	}

	public float[][] generateOctavedSimplexNoise(int xx, int yy, int width, int height, int octaves,
			float roughness, float scale, Long seed) {
		float[][] totalNoise = new float[width][height];
		float layerFrequency = scale;
		float layerWeight = 1;
		float weightSum = 0;
		// Long seed = r.nextLong();
		// Long seed = 3695317381661324390L;
		noise = new OpenSimplexNoise(seed);

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

	public float[][] getOsn(int x, int y, int w, int h) {
		return generateOctavedSimplexNoise(x, y, w, h, 3, 0.4f, 0.05f, seed);
	}

	public float[][] getTemperatureOsn(int x, int y, int w, int h) {
		return generateOctavedSimplexNoise(x, y, 16, 16, 3, 0.4f, 0.02f, temp_seed);
	}

	public float[][] getMoistureOsn(int x, int y, int w, int h) {
		return generateOctavedSimplexNoise(x, y, 16, 16, 3, 0.4f, 0.02f, moist_seed);
	}

	public void generate(Long seed) {
		this.r = new Random(seed);
		this.seed = seed;
		this.temp_seed = r.nextLong();
		this.moist_seed = r.nextLong();
		loaded = false;
		chunks.clear();

		Logger.print("[seed]: " + this.seed);

		Point chunk_point = getChunkPointWithCoords(player.getX(), player.getY());
		chunks.put(chunk_point, new Chunk(chunk_point.x, chunk_point.y, seed, temp_seed, moist_seed, this, player, textures));
		loaded = true;
	}

	public Player getPlayer() {
		return this.player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public Long getSeed() {
		return this.seed;
	}

	public Long getTemp_seed() {
		return temp_seed;
	}

	public Long getMoist_seed() {
		return moist_seed;
	}

	public void setSeeds(Long seed, Long temp_seed, Long moist_seed) {
		this.seed = seed;
		this.temp_seed = temp_seed;
		this.moist_seed = moist_seed;
	}
}
