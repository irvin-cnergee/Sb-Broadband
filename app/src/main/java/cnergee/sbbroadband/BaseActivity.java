package cnergee.sbbroadband;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;

import java.util.ArrayList;

import cnergee.sbbroadband.utils.MyUtils;
import developer.app.interface_.IError;
import developer.app.interface_.IReturn;
import developer.app.obj.CommonWebResponse;


/**
 * Created by Sumit on 25/06/2016.
 */
public  class BaseActivity extends AppCompatActivity implements IError, IReturn {
    int i = 0;

    public static IError iError;
    public static NiftyDialogBuilder dialog_box;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public void displayErrorPage(String error, final String actual_error) {
        MyUtils.l("Error", "view");
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.activity_error_page, null);

        i = 0;
        setContentView(view);
        TextView tv_app_down = (TextView) view.findViewById(R.id.tv_app_down);
        ImageView iv_cloud_error = (ImageView) view.findViewById(R.id.iv_cloud_error);

        if (error.equalsIgnoreCase("slow")) {
            tv_app_down.setText(getString(R.string.soap_timeout));
        }

        if (error.equalsIgnoreCase("soap")) {
            tv_app_down.setText(getString(R.string.soap_fault));
        }

        if (error.equalsIgnoreCase("error")) {
            tv_app_down.setText(getString(R.string.error));
        }

        TextView tv_go_back = (TextView) view.findViewById(R.id.tv_go_back);
        final TextView tv_actual_error = (TextView) view.findViewById(R.id.tv_actual_error);

        tv_go_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                BaseActivity.this.finish();
            }
        });

        iv_cloud_error.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (i == 1) {
                    tv_actual_error.setVisibility(View.VISIBLE);
                    tv_actual_error.setText(actual_error);
                }
                i++;
            }
        });

    }


    /* @Override
    public void web_response(ArrayList<String> response, Context context) {
        MyUtils.l("BaseActivity","response:"+response.get(2).toString());

    }*/


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

    @Override
    public void return_object(CommonWebResponse commonWebResponse, Context context, String method_name) {

    }

    @Override
    public void return_string(String response, Context context, String method_name) {

    }

}
