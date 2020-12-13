package game.system.helpers;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class LevelLoaderHelpers {

    public static String getFullClassname(JSONObject object) {
        String ret = "";
        for(Object o : (JSONArray)object.get("properties")) {
            JSONObject prop = (JSONObject) o;
            if(prop.get("name").toString().equals("package")) {
                ret += prop.get("value").toString();
                break;
            }
        }
        return ret + "." + object.get("type").toString();
    }

    public static String getCustomProp(JSONObject object, String propname) {
        for(Object o : (JSONArray)object.get("properties")) {
            JSONObject prop = (JSONObject) o;
            if(prop.get("name").toString().equals(propname)) {
                return prop.get("value").toString();
            }
        }
        return null;
    }

    public static int getIntProp(JSONObject object, String propname) {
        return Integer.parseInt(object.get(propname).toString());
    }
}
