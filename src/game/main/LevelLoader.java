package game.main;

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
	
	public LevelLoader() {
		
	}
	
	public static ArrayList<Long> getLevelData(){
		try {
	         Object obj = parser.parse(new FileReader("assets/World_map.json"));
	         JSONObject jsonObject = (JSONObject)obj;
	         JSONArray layers = (JSONArray)jsonObject.get("layers");
	         ArrayList<Long> listdata = new ArrayList<Long>();
	         
	         for(Object layer : layers) {
	        	 JSONArray data = (JSONArray)((JSONObject)layer).get("data");
	        	 //System.out.println(data);
	        	 
	        	 if (data != null) { 
	        	    for (int i=0;i<data.size();i++){
	        	    	listdata.add(Long.parseLong(data.get(i).toString()));
	        	    	//System.out.println(Long.parseLong(data.get(i).toString()));
	        	    	//game.blocks.add(Long.parseLong(data.get(i).toString()));
	        	    } 
	        	 } 
	        	 /*for(Object num : data) {
	        		 JSONObject numObject = (JSONObject)num;
	        		 System.out.println(numObject.toString());
	        		 //game.blocks.add(((Long)num).intValue());
	        	 }*/
	         }
	         return listdata;
	         
	    } catch(Exception e) {
	    	e.printStackTrace();
	    	return null;
	    }
	}
}
