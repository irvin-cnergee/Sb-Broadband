package developer.app.parser;

import android.content.Context;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cnergee.sbbroadband.obj.ProfileData;
import cnergee.sbbroadband.utils.MyUtils;
import developer.app.obj.CommonWebResponse;
import developer.app.utils.JSONUtils;


/**
 * Created by Jyoti on 8/1/2016.
 */
public class JSONParser {

    private static final String TAG = "JSONParser";

    Context context;

    public JSONParser(Context context){
        this.context=context;
    }


    public CommonWebResponse parse_login_details(String response, String method_name) {

        MyUtils.l(TAG,"response :"+response);
        CommonWebResponse commonWebResponse = new CommonWebResponse();
        ArrayList<HashMap<String, String>> login_response = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(response);
            HashMap<String,String> map = new HashMap<>();
            map.put("error_msg", JSONUtils.parse_json_string(jsonObject,"ErrorMessage",""));
            map.put("error_status", JSONUtils.parse_json_string(jsonObject,"ErrorStatus",""));
            map.put("msg", JSONUtils.parse_json_string(jsonObject,"Message",""));
            map.put("status", JSONUtils.parse_json_string(jsonObject,"status",""));

            login_response.add(map);

            if(jsonObject.length()>0){
                commonWebResponse.setProceed_status("1");
                commonWebResponse.setMessage("");
                commonWebResponse.setProceed_object(login_response);
            }else {
                commonWebResponse.setProceed_status("0");
                commonWebResponse.setMessage("wrong url");
                commonWebResponse.setProceed_object(null);
            }
        }catch (Exception e){
            e.printStackTrace();
            commonWebResponse.setProceed_status("0");
            commonWebResponse.setMessage("App facing some problem");
            commonWebResponse.setProceed_object(null);
        }

        return commonWebResponse;
    }

    public CommonWebResponse parse_profile_details(String response, String method_name) {

        CommonWebResponse commonWebResponse = new CommonWebResponse();
        ArrayList<ProfileData> list = new ArrayList<>();

        try {
            JSONObject object = new JSONObject(response);
            int status = object.optInt("status",0);
            String msg = object.optString("msg","");

            if(status == 0){

                commonWebResponse.setProceed_status("0");
                commonWebResponse.setMessage(msg);
                commonWebResponse.setProceed_object(null);

            }else {
                ProfileData data = new ProfileData();
                data.setAddress(JSONUtils.parse_json_string(object,"address",""));
                data.setCity(JSONUtils.parse_json_string(object,"city",""));
                data.setContact_no(JSONUtils.parse_json_string(object,"contact_no",""));
                data.setCountry(JSONUtils.parse_json_string(object,"country",""));
                data.setDob(JSONUtils.parse_json_string(object,"dob",""));
                data.setEmail(JSONUtils.parse_json_string(object,"email",""));
                data.setFirst_name(JSONUtils.parse_json_string(object,"first_name",""));
                data.setMiddle_name(JSONUtils.parse_json_string(object,"middle_name",""));
                data.setLast_name(JSONUtils.parse_json_string(object,"last_name",""));
                data.setGender(JSONUtils.parse_json_string(object,"gender",""));
                data.setNationality(JSONUtils.parse_json_string(object,"nationality",""));
                data.setSubscriber_username(JSONUtils.parse_json_string(object,"subscriber_username",""));

                list.add(data);

                commonWebResponse.setProceed_status("1");
                commonWebResponse.setMessage("");
                commonWebResponse.setProceed_object(list);
            }

        }catch (Exception e){
            e.printStackTrace();

            commonWebResponse.setProceed_status("2");
            commonWebResponse.setMessage("App facing some problem");
            commonWebResponse.setProceed_object(null);
        }

        MyUtils.l(TAG,commonWebResponse.getProceed_status());
        return commonWebResponse;
    }
}
