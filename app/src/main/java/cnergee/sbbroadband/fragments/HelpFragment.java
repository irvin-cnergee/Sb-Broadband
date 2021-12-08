package cnergee.sbbroadband.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import cn.pedant.SweetAlert.SweetAlertDialog;
import cnergee.sbbroadband.DashboardActivity;
import cnergee.sbbroadband.R;
import cnergee.sbbroadband.retroObj.HelpData;
import cnergee.sbbroadband.utils.MyUtils;
import developer.app.interface_.AllAPIs;
import developer.app.loadingview.ProgressWheel;
import developer.app.webservices.APIClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Siddhesh on 10/30/2017.
 */

public class HelpFragment extends Fragment {

    private static final String TAG = "HelpFragment";
    TextView mtv_content;
    SharedPreferences sharedPreferences;

    int entityId = 0;
    int subscriberId = 0;
    AllAPIs allAPIs;
    SweetAlertDialog sweetAlertDialog;
    ProgressWheel progressBar;
    ImageView img_error;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.content_help,container,false);

        ((DashboardActivity)getActivity()).updateTitle(getString(R.string.help_title));

        sharedPreferences = (SharedPreferences) getActivity().getSharedPreferences(MyUtils.MY_PREF,MODE_PRIVATE);
        entityId = sharedPreferences.getInt(MyUtils.ENTITY_ID,0);
        subscriberId = sharedPreferences.getInt(MyUtils.SUBSCRIBER_ID,0);

        allAPIs = APIClient.getClient(getActivity()).create(AllAPIs.class);

        mtv_content = (TextView)view.findViewById(R.id.mtv_content);
        progressBar = (ProgressWheel) view.findViewById(R.id.progressBar);
        img_error   = (ImageView) view.findViewById(R.id.img_error);

        if(MyUtils.isOnline(getActivity())){
            getHelpContent(entityId);
        }else {
//            Toast.makeText(getActivity(), getString(R.string.internet_error_msg), Toast.LENGTH_SHORT).show();

            mtv_content.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            img_error.setVisibility(View.VISIBLE);

            img_error.setImageDrawable(getResources().getDrawable(R.drawable.no_internet));
        }

        return view;
    }

    private void getHelpContent(int entityId) {

        /*sweetAlertDialog = new SweetAlertDialog(getActivity(),SweetAlertDialog.PROGRESS_TYPE);
        sweetAlertDialog.setTitleText(getString(R.string.please_wait));
        sweetAlertDialog.setContentText("");
        sweetAlertDialog.show();*/

        mtv_content.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        img_error.setVisibility(View.GONE);

        final Call<HelpData> helpApi = allAPIs.getHelpData(entityId);
        helpApi.enqueue(new Callback<HelpData>() {
            @Override
            public void onResponse(Call<HelpData> call, Response<HelpData> response) {

                if(response.isSuccessful()){

                    mtv_content.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    img_error.setVisibility(View.GONE);

                    HelpData helpData = response.body();
                    String content    = helpData.content;
                    int status        = helpData.status;
                    String message    = helpData.message;

                    MyUtils.l(TAG,"content : "+content);

                    if(status ==1) {
//                        sweetAlertDialog.dismiss();
                        if (content != "") {
                            Spanned htmlText = Html.fromHtml(content);
                            MyUtils.l(TAG, "htmlText : " + htmlText);
                            mtv_content.setText(htmlText.toString());
                        } else {
//                            mtv_content.setText("");
                            mtv_content.setVisibility(View.GONE);
                            progressBar.setVisibility(View.GONE);
                            img_error.setVisibility(View.VISIBLE);

                            img_error.setImageDrawable(getResources().getDrawable(R.drawable.no_data_found));
                        }
                    }else{
                        /*sweetAlertDialog.changeAlertType(SweetAlertDialog.WARNING_TYPE);
                        sweetAlertDialog.setTitleText(getString(R.string.oops));
                        sweetAlertDialog.setContentText(message);
                        sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                            }
                        });*/

                        mtv_content.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                        img_error.setVisibility(View.VISIBLE);

                        img_error.setImageDrawable(getResources().getDrawable(R.drawable.no_data_found));
                    }

//                mtv_content.setText(Html.toHtml(content));
                }
            }

            @Override
            public void onFailure(Call<HelpData> call, Throwable t) {
                /*sweetAlertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                sweetAlertDialog.setTitleText("Error");
                sweetAlertDialog.setContentText(getString(R.string.went_wrong));
                sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                    }
                });*/

                mtv_content.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                img_error.setVisibility(View.VISIBLE);

                img_error.setImageDrawable(getResources().getDrawable(R.drawable.something_wrong));
            }
        });

    }
}
