package developer.app.webservices;

import android.app.Activity;
import android.content.Context;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONObject;

import java.util.ArrayList;

import cnergee.sbbroadband.utils.MyUtils;
import developer.app.interface_.IError;
import developer.app.interface_.IPresenter;
import developer.app.presenter.CorePresenter;


/**
 * Created by Sumit on 25/06/2016.
 */

public class CommonThread extends Thread {

    Context context;
    String url;
    int method;
    String method_name;

    JSONObject request_data;
    ArrayList<String> al_responses;
    private IPresenter iPresenter;
    IError iError;

    public CommonThread(Context context,int method,String url,String method_name){
        this.context=context;
        this.method=method;
        this.url=url;
        iError=(IError)context;
        this.method_name=method_name;
        //this.iPresenter =(IPresenter) context;

        this.iPresenter = new CorePresenter(context,iError);
    }

    @Override
    public void run() {

        WebServiceCall call=new WebServiceCall();
        ArrayList<String> al_responses = null;

        if(method==0){
            MyUtils.l("Method", "Type: GET");
            MyUtils.l("Url", "is: "+url);
            try {
                al_responses=call.get_data(url);
            } catch (ClientProtocolException e) {
                MyUtils.l("Error", ":"+e.toString());
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                MyUtils.l("Error", ":"+e.toString());
            }
        }

        if(method==1){
            JSONObject jsonObject=get_request_data();
            try {
                MyUtils.l("Method", "Type: POST");
                MyUtils.l("Url", "is: "+url);
                al_responses=call.postDataWithParam(url, jsonObject);
            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        setAl_responses(al_responses);

        ((Activity)context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(iPresenter!=null)
                iPresenter.web_response(getAl_responses(),context,method_name);
               // MyUtils.l("Thread Response",":"+getAl_responses().get(2));
            }
        });
    }


    public JSONObject get_request_data(){
        return  request_data;
    }

    public void set_request_data(JSONObject data){
        this.request_data=data;
    }

    public ArrayList<String> getAl_responses() {
        return al_responses;
    }

    public void setAl_responses(ArrayList<String> al_responses) {
        this.al_responses = al_responses;
    }
}
