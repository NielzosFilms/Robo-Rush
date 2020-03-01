package game.world;

import java.awt.Color;
import java.awt.Graphics;
import java.util.LinkedList;

import game.main.GameObject;
import game.main.ID;
import game.objects.Tile;
import game.textures.Textures;

public class Chunk {
	
	private static OpenSimplexNoise noise;
	
	public LinkedList<GameObject> tiles = new LinkedList<GameObject>();
	public static int tile_width = 16, tile_height = 16;
	public int x, y;
	private Long seed, temp_seed, moist_seed;

	public Chunk(int x, int y, Long seed, Long temp_seed, Long moist_seed) {
		this.x = x;
		this.y = y;
		this.seed = seed;
		this.temp_seed = temp_seed;
		this.moist_seed = moist_seed;
		//generate chunk tiles 16x16 then add to world
		GenerateTiles();
		
		
	}
	
	/*public void tick() {
		
	}*/
	
	public void render(Graphics g) {
		for(GameObject tile : tiles) {
			tile.render(g);
		}
		g.setColor(Color.pink);
		g.drawRect(x*16, y*16, 16*16, 16*16);
	}
	
	private void GenerateTiles() {
		float[][] osn = generateOctavedSimplexNoise(x, y, tile_width, tile_height, 3, 0.4f, 0.05f, seed);
		float[][] temp_osn = generateOctavedSimplexNoise(x, y, tile_width, tile_height, 3, 0.4f, 0.02f, temp_seed); //scale 0.01f ?
		float[][] moist_osn = generateOctavedSimplexNoise(x, y, tile_width, tile_height, 3, 0.4f, 0.02f, moist_seed);
		for(int yy = 0;yy<osn.length;yy++) {
			for(int xx = 0;xx<osn[yy].length;xx++) {
				float val = osn[xx][yy];
				float temp_val = temp_osn[xx][yy];
				float moist_val = moist_osn[xx][yy];
				if((temp_val > -0.5 && temp_val < 0.5) && (moist_val > 0.5)) { //forest
					if(val < -0.2) {
						tiles.add(new Tile(xx*16+x*16, yy*16+y*16, ID.Tile, Textures.tileSetBlocks.get(27)));
					}else if(val < 0 && val > -0.2){
						tiles.add(new Tile(xx*16+x*16, yy*16+y*16, ID.Tile, Textures.tileSetBlocks.get(7)));
					}else if(val > 0.5 && val < 0.9){
						tiles.add(new Tile(xx*16+x*16, yy*16+y*16, ID.Tile, Textures.tileSetBlocks.get(33)));
					}else if(val > 0.9) {
						tiles.add(new Tile(xx*16+x*16, yy*16+y*16, ID.Tile, Textures.tileSetBlocks.get(34)));
					} else {
						tiles.add(new Tile(xx*16+x*16, yy*16+y*16, ID.Tile, Textures.tileSetBlocks.get(18)));
					}
				}else if(temp_val < 0 && moist_val < 0) { //desert
					tiles.add(new Tile(xx*16+x*16, yy*16+y*16, ID.Tile, Textures.tileSetBlocks.get(28)));
				}
				
			}
		}
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
