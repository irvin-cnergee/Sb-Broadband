package developer.app.utils;

import org.json.JSONObject;

/**
 * Created by Sandip on 8/6/2016.
 */
public class JSONUtils {


    public static String parse_json_string(JSONObject jsonObject,String name,String defaultvalue){
        String Value="";

        if(jsonObject.has(name)){
            if(jsonObject.optString(name).length()>0){
                Value=jsonObject.optString(name);
            }
            else{
                Value=defaultvalue;
            }
        }
        else{
            Value=defaultvalue;
        }

        return Value;
    }

    public static int parse_json_int(JSONObject jsonObject,String name,int defaultvalue){
        int Value=0;

        if(jsonObject.has(name)){
            if(jsonObject.optInt(name)>0){
                Value=jsonObject.optInt(name);
            }
            else{
                Value=defaultvalue;
            }
        }
        else{
            Value=defaultvalue;
        }

        return Value;
    }
}
