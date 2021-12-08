package cnergee.sbbroadband;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import cnergee.sbbroadband.utils.MyUtils;
import developer.app.interface_.IError;
import developer.app.interface_.IReturn;
import developer.app.obj.CommonWebResponse;

/**
 * Created by Siddhesh on 8/1/2017.
 */

public class BasicFragment extends Fragment implements IError,IReturn {

    public static IError iError;
    int i= 0;

    @Override
    public void displayErrorPage(String error, final String actual_error) {
        MyUtils.l("Error", "view");
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.activity_error_page, null);

        i = 0;
        getActivity().setContentView(view);
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
                getActivity().finish();
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
