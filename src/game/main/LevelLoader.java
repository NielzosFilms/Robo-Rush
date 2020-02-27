package game.main;

import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import game.objects.Tile;

//"assets/World_map.json"

public class LevelLoader {
	
	static JSONParser parser = new JSONParser();
	
	private Game game;
	private static Random r = new Random();
	
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
		BufferedImage map = Textures.height_map;
		int w = map.getWidth();
		int h = map.getHeight();

		for(int yy = 0; yy < h; yy++) {
			for(int xx = 0; xx < w; xx++) {
				int pixel = map.getRGB(xx, yy);
				int red = (pixel >> 16) & 0xff;
				int green = (pixel >> 8) & 0xff;
				int blue = (pixel) & 0xff;
				if(red > 120) {
					int rand = r.nextInt(20);
					switch(rand) {
						case 1:
							handler.addObjectNoTick(new Tile(xx*16, yy*16, ID.Tile, Textures.tileSetBlocks.get(6)));
							break;
						case 2:
							handler.addObjectNoTick(new Tile(xx*16, yy*16, ID.Tile, Textures.tileSetBlocks.get(30)));
							break;
						case 3:
							handler.addObjectNoTick(new Tile(xx*16, yy*16, ID.Tile, Textures.tileSetBlocks.get(54)));
							break;
						case 4:
							handler.addObjectNoTick(new Tile(xx*16, yy*16, ID.Tile, Textures.tileSetBlocks.get(78)));
							break;
						case 5:
							handler.addObjectNoTick(new Tile(xx*16, yy*16, ID.Tile, Textures.tileSetBlocks.get(102)));
							break;
						case 6:
							handler.addObjectNoTick(new Tile(xx*16, yy*16, ID.Tile, Textures.tileSetBlocks.get(126)));
							break;
						default:
							handler.addObjectNoTick(new Tile(xx*16, yy*16, ID.Tile, Textures.tileSetBlocks.get(150)));
							break;
					}
					
				}else {
					handler.addObjectNoTick(new Tile(xx*16, yy*16, ID.Tile, Textures.tileSetBlocks.get(171)));
				}
			}
		}
	}
}
