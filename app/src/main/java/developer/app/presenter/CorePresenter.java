package developer.app.presenter;

import android.content.Context;

import org.json.JSONObject;

import java.util.ArrayList;

import cnergee.sbbroadband.R;
import cnergee.sbbroadband.utils.MyUtils;
import developer.app.interface_.IError;
import developer.app.interface_.IPresenter;
import developer.app.interface_.IReturn;
import developer.app.obj.CommonWebResponse;
import developer.app.parser.JSONParser;
import developer.app.webservices.CommonThread;


/**
 * Created by Jyoti on 7/30/2016.
 */

public class CorePresenter implements IPresenter {

    private static final String TAG = "CorePresenter";

    Context context;
    CommonThread commonThread;
    IError iError;
    IReturn iReturn;

    JSONParser jsonParser;

    public CorePresenter(Context context, IError iError){
        this.context=context;
        this.iError = iError;
        iReturn=(IReturn)context;
    }

    public void ws_execute(JSONObject jsonObject, String url,String method) {
        commonThread=new CommonThread(context, MyUtils.POST,url,method);
        commonThread.set_request_data(jsonObject);
        commonThread.start();
    }

    @Override
    public void web_response(ArrayList<String> response, Context context, String method_name) {
        if (method_name.equalsIgnoreCase(context.getString(R.string.GET_LOGIN_DETAILS))) {

            MyUtils.l("Login Response",":"+response);
            if (handle_response(response, iError, true)) {
                jsonParser = new JSONParser(context);
                CommonWebResponse commonWebResponse = jsonParser.parse_login_details(response.get(2), method_name);
                iReturn.return_object(commonWebResponse, context, method_name);

                MyUtils.l("Login Response",":"+response);
            }

        }

        if(method_name.equalsIgnoreCase(context.getString(R.string.GET_PROFILE_DETAILS))){

            MyUtils.l(TAG,"response : "+response);
            if(handle_response(response, iError, true)){
                jsonParser = new JSONParser(context);
                CommonWebResponse commonWebResponse = jsonParser.parse_profile_details(response.get(2), method_name);
                iReturn.return_object(commonWebResponse, context, method_name);
            }
        }
    }

    public boolean handle_response(ArrayList<String> al_response, IError iError, boolean is_show_error_page){
        if(al_response!=null){
            if(al_response.size()>0){
                if(al_response.get(0).equalsIgnoreCase("OK")){

                    if(al_response.get(1).equalsIgnoreCase("200")){
                        return true;
                    }
                    else{
                        if(is_show_error_page)
                            iError.displayErrorPage(al_response.get(0), al_response.get(2));
                        return false;
                    }
                }
                else{
                    if(is_show_error_page)
                        iError.displayErrorPage(al_response.get(0), al_response.get(2));
                    return false;
                }
            }
            else{
                if(is_show_error_page)
                    iError.displayErrorPage(al_response.get(0), al_response.get(2));
                return false;
            }
        }
        else{
            if(is_show_error_page)
                iError.displayErrorPage(al_response.get(0), al_response.get(2));
            return false;
        }
    }

}
