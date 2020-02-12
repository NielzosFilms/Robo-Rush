package game.main;

import java.awt.Rectangle;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

//"assets/World_map.json"

public class LevelLoader {
	
	static JSONParser parser = new JSONParser();
	
	private Game game;
	
	public static ArrayList<ArrayList<Long>> listdata;
	public static ArrayList<Rectangle> rectangle_bounds;
	
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
	        		 System.out.println("test");
	        		 JSONArray objects = (JSONArray) ((JSONObject)layer).get("objects");
	        		 if(objects != null) {
	        			 rectangle_bounds = new ArrayList<Rectangle>();
	        			 for(int i = 0;i<objects.size();i++) {
	        				 JSONObject collisionLayer = (JSONObject)objects.get(i);
	    	        		 
	    	        		 Long xL = (Long.parseLong(collisionLayer.get("x").toString()));
	    	        		 int x = xL.intValue();
	    	        		 Long yL = (Long.parseLong(collisionLayer.get("y").toString()));
	    	        		 int y = yL.intValue();
	    	        		 Long widthL = (Long.parseLong(collisionLayer.get("width").toString()));
	    	        		 int width = widthL.intValue();
	    	        		 Long heightL = (Long.parseLong(collisionLayer.get("height").toString()));
	    	        		 int height = heightL.intValue();
	    	        		 
	    	        		 rectangle_bounds.add(new Rectangle(x, y, width, height));
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
}
