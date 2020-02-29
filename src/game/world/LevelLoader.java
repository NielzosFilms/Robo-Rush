package game.world;

import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import game.main.Game;
import game.main.Handler;
import game.main.ID;
import game.objects.Tile;
import game.objects.Tree;
import game.textures.Textures;

//"assets/World_map.json"

public class LevelLoader {
	
	static JSONParser parser = new JSONParser();
	
	private Game game;
	private static Random r = new Random();
	private static OpenSimplexNoise noise;
	
	public static ArrayList<ArrayList<Long>> listdata;
	public static ArrayList<Rectangle> rectangle_bounds;
	public static ArrayList<Polygon> polygon_bounds;
	
	public LevelLoader() {
		
	}
	
	public static void loadLevelData(String file_path) {
		try {
	         Object obj = parser.parse(new FileReader(file_path));
	         JSONObject jsonObject = (JSONObject)obj;
	         JSONArray layers = (JSONArray)jsonObject.get("layers");
	         listdata = new ArrayList<ArrayList<Long>>();
	         
	         for(Object layer : layers) {
	        	 if(((JSONObject)layer).get("objects") != null) {
	        		 JSONArray objects = (JSONArray) ((JSONObject)layer).get("objects");
	        		 if(objects != null) {
	        			 rectangle_bounds = new ArrayList<Rectangle>();
	        			 polygon_bounds = new ArrayList<Polygon>();
	        			 for(int i = 0;i<objects.size();i++) {
	        				 JSONObject collisionLayer = (JSONObject)objects.get(i);
	        				 //if(collisionLayer.get(""))
	        				 
	    	        		 
	        				 if(collisionLayer.get("polygon") == null) {
		    	        		 Long xL = (Long.parseLong(collisionLayer.get("x").toString()));
		    	        		 int x = xL.intValue();
		    	        		 Long yL = (Long.parseLong(collisionLayer.get("y").toString()));
		    	        		 int y = yL.intValue();
		    	        		 Long widthL = (Long.parseLong(collisionLayer.get("width").toString()));
		    	        		 int width = widthL.intValue();
		    	        		 Long heightL = (Long.parseLong(collisionLayer.get("height").toString()));
		    	        		 int height = heightL.intValue();
		    	        		 
		    	        		 rectangle_bounds.add(new Rectangle(x, y, width, height));
	        				 }else {
	        					 
	        					 Long xL = (Long.parseLong(collisionLayer.get("x").toString()));
		    	        		 int x = xL.intValue();
		    	        		 Long yL = (Long.parseLong(collisionLayer.get("y").toString()));
		    	        		 int y = yL.intValue();
		    	        		 
	        					 JSONArray polygon_points = (JSONArray)collisionLayer.get("polygon");
	        					 System.out.println(polygon_points);
	        					 Polygon poly = new Polygon();
	        					 if(polygon_points != null) {
	        						 for(int j = 0;j<polygon_points.size();j++) {
	        							 JSONObject point = (JSONObject)polygon_points.get(j);
	        							 Long tempXL = (Long.parseLong(point.get("x").toString()));
	    		    	        		 int tempX = tempXL.intValue();
	    		    	        		 Long tempYL = (Long.parseLong(point.get("y").toString()));
	    		    	        		 int tempY = tempYL.intValue();
	    		    	        		 
	    		    	        		 poly.addPoint(x+tempX, y+tempY);
	        						 }
	        					 }
	        					 polygon_bounds.add(poly);
	        				 }
	        			 }
	        		 }
	        		 
	        	 }else {
		        	 JSONArray data = (JSONArray)((JSONObject)layer).get("data");
		        	 //System.out.println(data);
		        	 
		        	 ArrayList<Long> layerdata = new ArrayList<Long>();
		        	 
		        	 if (data != null) { 
		        	    for (int i=0;i<data.size();i++){
		        	    	layerdata.add(Long.parseLong(data.get(i).toString()));
		        	    	//System.out.println(Long.parseLong(data.get(i).toString()));
		        	    	//game.blocks.add(Long.parseLong(data.get(i).toString()));
		        	    } 
		        	 } 
		        	 listdata.add(layerdata);
		        	 /*for(Object num : data) {
		        		 JSONObject numObject = (JSONObject)num;
		        		 System.out.println(numObject.toString());
		        		 //game.blocks.add(((Long)num).intValue());
		        	 }*/
	        	 }
	         }
	         
	    } catch(Exception e) {
	    	e.printStackTrace();
	    }
	}
	
	public static ArrayList<ArrayList<Long>> getLevelData(){
		try {
	         Object obj = parser.parse(new FileReader("assets/level 2.json"));
	         JSONObject jsonObject = (JSONObject)obj;
	         JSONArray layers = (JSONArray)jsonObject.get("layers");
	         ArrayList<ArrayList<Long>> listdata = new ArrayList<ArrayList<Long>>();
	         
	         for(Object layer : layers) {
	        	 if(((JSONObject)layer).get("objects") != null) {
	        		 System.out.println("test");
	        		 JSONArray objects = (JSONArray) ((JSONObject)layer).get("objects");
	        		 JSONObject collisionLayer = (JSONObject)objects.get(0);
	        		 ArrayList<Rectangle> rectangle_bounds = new ArrayList<Rectangle>();
	        		 
	        		 Long xL = (Long.parseLong(collisionLayer.get("x").toString()));
	        		 int x = xL.intValue();
	        		 Long yL = (Long.parseLong(collisionLayer.get("y").toString()));
	        		 int y = yL.intValue();
	        		 Long widthL = (Long.parseLong(collisionLayer.get("width").toString()));
	        		 int width = widthL.intValue();
	        		 Long heightL = (Long.parseLong(collisionLayer.get("height").toString()));
	        		 int height = heightL.intValue();
	        		 
	        		 rectangle_bounds.add(new Rectangle(x, y, width, height));
	        	 }else {
		        	 JSONArray data = (JSONArray)((JSONObject)layer).get("data");
		        	 //System.out.println(data);
		        	 
		        	 ArrayList<Long> layerdata = new ArrayList<Long>();
		        	 
		        	 if (data != null) { 
		        	    for (int i=0;i<data.size();i++){
		        	    	layerdata.add(Long.parseLong(data.get(i).toString()));
		        	    	//System.out.println(Long.parseLong(data.get(i).toString()));
		        	    	//game.blocks.add(Long.parseLong(data.get(i).toString()));
		        	    } 
		        	 } 
		        	 listdata.add(layerdata);
		        	 /*for(Object num : data) {
		        		 JSONObject numObject = (JSONObject)num;
		        		 System.out.println(numObject.toString());
		        		 //game.blocks.add(((Long)num).intValue());
		        	 }*/
	        	 }
	         }
	         return listdata;
	         
	    } catch(Exception e) {
	    	e.printStackTrace();
	    	return null;
	    }
	}
	
	public static void LoadLevelHeightMap(Handler handler) {
		/*float[][] osn = generateOctavedSimplexNoise(0, 0, 100, 100, 3, 0.4f, 0.05f);
		for(int y = 0;y<osn.length;y++) {
			for(int x = 0;x<osn[y].length;x++) {
				float val = osn[x][y];
				if(val < -0.2) {
					handler.addObjectNoTick(new Tile(x*16, y*16, ID.Tile, Textures.tileSetBlocks.get(27)));
				}else if(val < 0 && val > -0.2){
					handler.addObjectNoTick(new Tile(x*16, y*16, ID.Tile, Textures.tileSetBlocks.get(7)));
				}else if(val > 0.5 && val < 0.9){
					handler.addObjectNoTick(new Tile(x*16, y*16, ID.Tile, Textures.tileSetBlocks.get(33)));
				}else if(val > 0.9) {
					handler.addObjectNoTick(new Tile(x*16, y*16, ID.Tile, Textures.tileSetBlocks.get(34)));
				} else {
					handler.addObjectNoTick(new Tile(x*16, y*16, ID.Tile, Textures.tileSetBlocks.get(18)));
				}
				
			}
		}*/
		
		
	}
	
	public static float[][] generateOctavedSimplexNoise(int xx, int yy, int width, int height, int octaves, float roughness, float scale, Long seed){
		float[][] totalNoise = new float[width][height];
	    float layerFrequency = scale;
	    float layerWeight = 1;
	    float weightSum = 0;
	    //Long seed = r.nextLong();
	    //Long seed = 3695317381661324390L;
	    System.out.println(seed);
	    noise = new OpenSimplexNoise(seed);

	    for (int octave = 0; octave < octaves; octave++) {
	          //Calculate single layer/octave of simplex noise, then add it to total noise
	       for(int x = xx; x < width; x++){
	          for(int y = yy; y < height; y++){
	             totalNoise[x][y] += (float) noise.eval(x * layerFrequency,y * layerFrequency) * layerWeight;
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
