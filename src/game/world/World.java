package game.world;

import java.awt.Graphics;
import java.awt.Point;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;

import game.entities.Player;
import game.main.Game;
import game.textures.Textures;

public class World {

	public static Long seed, temp_seed, moist_seed;
	public HashMap<Point, Chunk> chunks = new HashMap<Point, Chunk>();
	public static boolean loaded = false;
	private Player player;
	private Textures textures;
	private Random r;

	private static OpenSimplexNoise noise;

	public World(Long seed, Player player, Textures textures) {
		
		this.r = new Random(seed);
		this.seed = seed;
		this.temp_seed = r.nextLong();
		this.moist_seed = r.nextLong();
		this.player = player;
		this.textures = textures;
		
		System.out.println("seed: "+this.seed);
		System.out.println("Temperature seed: "+this.temp_seed);
		System.out.println("Moisture seed: "+this.moist_seed);

		chunks.put(new Point(0, 0), new Chunk(0, 0, seed, temp_seed, moist_seed, this, player, textures));
		
		loaded = true;
	}
	
	public void tick() {
		int camX = (Math.round(-Game.cam.getX()/16));
		int camY = (Math.round(-Game.cam.getY()/16));
		int camW = (Math.round(Game.WIDTH/16));
		int camH = (Math.round(Game.HEIGHT/16));

		runWaterAnimations();
		
		for(int y = camY-32;y < camY+camH+16;y++) {
			for(int x = camX-32;x < camX+camW+16;x++) {
				if(chunks.containsKey(new Point(x, y))) {
					chunks.get(new Point(x, y)).tick();
					if(!chunks.containsKey(new Point(x-16, y))) {
						chunks.put(new Point(x-16, y), new Chunk(x-16, y, seed, temp_seed, moist_seed, this, player, textures));
					}else if(!chunks.containsKey(new Point(x+16, y))) {
						chunks.put(new Point(x+16, y), new Chunk(x+16, y, seed, temp_seed, moist_seed, this, player, textures));
					}
					if(!chunks.containsKey(new Point(x, y-16))) {
						chunks.put(new Point(x, y-16), new Chunk(x, y-16, seed, temp_seed, moist_seed, this, player, textures));
					}else if(!chunks.containsKey(new Point(x, y+16))) {
						chunks.put(new Point(x, y+16), new Chunk(x, y+16, seed, temp_seed, moist_seed, this, player, textures));
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
	
	public LinkedList<Chunk> getChunksOnScreen(){
		LinkedList<Chunk> tmp_chunks = new LinkedList<Chunk>();
		
		int camX = (Math.round(-Game.cam.getX()/16));
		int camY = (Math.round(-Game.cam.getY()/16));
		int camW = (Math.round(Game.WIDTH/16));
		int camH = (Math.round(Game.HEIGHT/16));
		
		for(int y = camY-32;y < camY+camH+16;y++) {
			for(int x = camX-32;x < camX+camW+16;x++) {
				if(chunks.containsKey(new Point(x, y))) {
					Chunk chunk = chunks.get(new Point(x, y));
					tmp_chunks.add(chunk);
				}
			}
		}
		return tmp_chunks;
	}
	
	public void render(Graphics g) {
		
		int camX = (Math.round(-Game.cam.getX()/16));
		int camY = (Math.round(-Game.cam.getY()/16));
		int camW = (Math.round(Game.WIDTH/16));
		int camH = (Math.round(Game.HEIGHT/16));
		
		for(int y = camY-32;y < camY+camH+16;y++) {
			for(int x = camX-32;x < camX+camW+16;x++) {
				if(chunks.containsKey(new Point(x, y))) {
					Chunk chunk = chunks.get(new Point(x, y));
					chunk.renderTiles(g);
				}
			}
		}
		
		for(int y = camY-32;y < camY+camH+16;y++) {
			for(int x = camX-32;x < camX+camW+16;x++) {
				if(chunks.containsKey(new Point(x, y))) {
					Chunk chunk = chunks.get(new Point(x, y));
					chunk.renderEntities(g);
				}
			}
		}
		
		/*for(Chunk chunk : chunks) {
			if(OnScreen(chunk.x*16, chunk.y*16, 16, 16)) {
				chunk.render(g);
			}
		}*/
		
		
		
		/*for(int y = -32;y <= 32;y+=16) {
			for(int x = -32;x <= 32;x+=16) {
				if(chunks.containsKey(new Point(x, y))) {
					chunks.get(new Point(x, y)).render(g);
				}
			}
		}*/
		
		
		
		/*chunks.get(0).get(0).render(g);
		chunks.get(16).get(0).render(g);*/
	}
	
	//functions to get specific tiles/tiletypes
	
	private boolean OnScreen(int x, int y, int w, int h) {
		return (x+(16*16) > -Game.cam.getX() && x < -Game.cam.getX()+Game.WIDTH) && 
				(y+(16*16) > -Game.cam.getY() && y < -Game.cam.getY()+Game.HEIGHT);
	}
	
	public Chunk getChunkWithCoords(int x, int y) {
		Point chunk_point = new Point(x, y);
		return chunks.get(chunk_point);
	}
	
	public Point getChunkPointWithCoords(int x, int y) {
		x = x/16;
		y = y/16;
		
		x -= Math.floorMod(x, 16);
		y -= Math.floorMod(y, 16);
		
		return new Point(x, y);
	}

	public static String getBiome(float val, float temp_val, float moist_val) {
		//biome generation needs refinement
		if ((temp_val > -0.5 && temp_val < 0.5) && (moist_val > 0.5)) { // forest
			if (val < -0.3) {
				return "beach";
			} else {
				return "forest";
			}
		} else if (temp_val < 0 && moist_val < 0) { // desert
			return "desert";
		} else if (temp_val > 0 && moist_val < 0) { // dirt
			return "dirt";
		}
		return "ocean";
	}

	public static String getBiomeWithCoords(int x, int y) {
		x /= 16;
		y /= 16;
		float[] arr = getHeightMapValuePoint(x, y);
		return getBiome(arr[0], arr[1], arr[2]);
	}
	
	public static float[] getHeightMapValuePoint(int x, int y) {
		// x = x/16/16;
		// y = y/16/16;
		float[][] osn = generateOctavedSimplexNoise(x, y, 1, 1, 3, 0.4f, 0.05f, seed);
		float[][] temp_osn = generateOctavedSimplexNoise(x, y, 1, 1, 3, 0.4f, 0.02f, temp_seed); // scale 0.01f ?
		float[][] moist_osn = generateOctavedSimplexNoise(x, y, 1, 1, 3, 0.4f, 0.02f, moist_seed);
		float[] arr = { osn[0][0], temp_osn[0][0], moist_osn[0][0] };
		return arr;
	}

	public static float[][] generateOctavedSimplexNoise(int xx, int yy, int width, int height, int octaves,
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

	public static float[][] getOsn(int x, int y, int w, int h) {
		float[][] osn = generateOctavedSimplexNoise(x, y, w, h, 3, 0.4f, 0.05f, seed);
		return osn;
	}

	public static float[][] getTemperatureOsn(int x, int y, int w, int h) {
		float[][] temp_osn = generateOctavedSimplexNoise(x, y, 16, 16, 3, 0.4f, 0.02f, temp_seed);
		return temp_osn;
	}

	public static float[][] getMoistureOsn(int x, int y, int w, int h) {
		float[][] moist_osn = generateOctavedSimplexNoise(x, y, 16, 16, 3, 0.4f, 0.02f, moist_seed);
		return moist_osn;
	}

	public void saveChunks(String fileName) throws FileNotFoundException{

		// Gson library to save to json, makes it readable

		PrintWriter pw = new PrintWriter(new FileOutputStream(fileName));
		Iterator it = chunks.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry)it.next();
			pw.println(pair.getKey()+";" +pair.getValue());
			it.remove(); // avoids a ConcurrentModificationException
		}
		pw.close();
	}
	
}
