package game.world;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;

import game.main.GameObject;
import game.main.ID;
import game.objects.Tile;
import game.objects.Tree;
import game.textures.Textures;

public class Chunk {
	
	private static OpenSimplexNoise noise;
	private static Random r = new Random();
	
	public LinkedList<GameObject> entities = new LinkedList<GameObject>();
	
	public LinkedList<HashMap<Point, GameObject>> tiles = new LinkedList<HashMap<Point, GameObject>>();
	public static int tile_width = 16, tile_height = 16;
	public int x, y;
	private static Long seed;
	private static Long temp_seed;
	private static Long moist_seed;

	public Chunk(int x, int y, Long seed, Long temp_seed, Long moist_seed, World world) {
		this.x = x;
		this.y = y;
		this.seed = seed;
		this.temp_seed = temp_seed;
		this.moist_seed = moist_seed;
		//entities.add(new Enemy((x+8)*16, (y+8)*16, ID.Enemy));
		//generate chunk tiles 16x16 then add to world
		tiles.add(new HashMap<Point, GameObject>());
		tiles.add(new HashMap<Point, GameObject>());
		GenerateTiles(world);
	}
	
	public void tick() {
		for(GameObject entity : entities) {
			entity.tick();
		}
	}
	
	public void render(Graphics g) {
		/*for(LinkedList<GameObject> tmp : tiles) {
			for(GameObject tile : tmp) {
				tile.render(g);
			}
		}*/
		for(GameObject entity : entities) {
			entity.render(g);
		}
		g.setColor(Color.pink);
		g.drawRect(x*16, y*16, 16*16, 16*16);
	}
	
	public void renderForeGround(Graphics g) {
		
	}
	
	private void GenerateTiles(World world) {
		float[][] osn = generateOctavedSimplexNoise(x, y, tile_width, tile_height, 3, 0.4f, 0.05f, seed);
		float[][] temp_osn = generateOctavedSimplexNoise(x, y, tile_width, tile_height, 3, 0.4f, 0.02f, temp_seed); //scale 0.01f ?
		float[][] moist_osn = generateOctavedSimplexNoise(x, y, tile_width, tile_height, 3, 0.4f, 0.02f, moist_seed);
		
		//create simple tiles
		for(int yy = 0;yy<osn.length;yy++) {
			for(int xx = 0;xx<osn[yy].length;xx++) {
				float val = osn[xx][yy];
				
				float temp_val = temp_osn[xx][yy];
				float moist_val = moist_osn[xx][yy];
				if((temp_val > -0.5 && temp_val < 0.5) && (moist_val > 0.5)) { //forest
					if(val < -0.2) {
						tiles.get(0).put(new Point(xx*16+x, yy*16+y), new Tile(xx*16+x*16, yy*16+y*16, ID.Tile, 14));
					} else {
						tiles.get(0).put(new Point(xx*16+x, yy*16+y), new Tile(xx*16+x*16, yy*16+y*16, ID.Tile, 0));
						int num = r.nextInt(25);
						if(num == 0) {
							//tiles.get(1).put(new Point(xx*16+x, yy*16+y), new Tree(xx*16+x*16, yy*16+y*16, ID.Tree, "forest"));
						}
					}
				}else if(temp_val < 0 && moist_val < 0) { //desert
					tiles.get(0).put(new Point(xx*16+x, yy*16+y), new Tile(xx*16+x*16, yy*16+y*16, ID.Tile, 14));
				}else {
					tiles.get(0).put(new Point(xx*16+x, yy*16+y), new Tile(xx*16+x*16, yy*16+y*16, ID.Tile, 14));
				}
				
			}
		}
		
		
		
		//change to advanced tiles
		Iterator it = tiles.get(0).entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        Tile tile = (Tile)pair.getValue();
	        
	        int tex_id = tile.getTextureId(this.tiles.get(0), (Point)pair.getKey(), tile.tex_id, world, this);
	        tile.setTexture(tex_id);
	    }
	}
	
	public void updateTilesAdvanced() {
		
	}
	
	public static float[] getHeightMapValuePoint(int x, int y) {
		//x = x/16/16;
		//y = y/16/16;
		float[][] osn = generateOctavedSimplexNoise(x, y, 1, 1, 3, 0.4f, 0.05f, seed);
		float[][] temp_osn = generateOctavedSimplexNoise(x, y, 1, 1, 3, 0.4f, 0.02f, temp_seed); //scale 0.01f ?
		float[][] moist_osn = generateOctavedSimplexNoise(x, y, 1, 1, 3, 0.4f, 0.02f, moist_seed);
		float[] arr = { osn[0][0], temp_osn[0][0], moist_osn[0][0] };
		return arr;
	}
	
	public static float[][] generateOctavedSimplexNoise(int xx, int yy, int width, int height, int octaves, float roughness, float scale, Long seed){
		float[][] totalNoise = new float[width][height];
	    float layerFrequency = scale;
	    float layerWeight = 1;
	    float weightSum = 0;
	    //Long seed = r.nextLong();
	    //Long seed = 3695317381661324390L;
	    noise = new OpenSimplexNoise(seed);

	    for (int octave = 0; octave < octaves; octave++) {
	          //Calculate single layer/octave of simplex noise, then add it to total noise
	       for(int x = 0; x < width; x++){
	          for(int y = 0; y < height; y++){
	             totalNoise[x][y] += (float) noise.eval((x+xx) * layerFrequency,(y+yy) * layerFrequency) * layerWeight;
	          }
	       }
	          
	          //Increase variables with each incrementing octave
	        layerFrequency *= 2;
	        weightSum += layerWeight;
	        layerWeight *= roughness;
	           
	    }
	    return totalNoise;
	}
	
}
