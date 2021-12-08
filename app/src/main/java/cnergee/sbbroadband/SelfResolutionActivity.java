package cnergee.sbbroadband;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

import cnergee.sbbroadband.retroObj.CommonResolutionData;
import cnergee.sbbroadband.retroObj.NullResolutionDatum;
import cnergee.sbbroadband.retroObj.ResolutionResponse;
import cnergee.sbbroadband.utils.MyUtils;
import developer.app.interface_.AllAPIs;
import developer.app.webservices.APIClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelfResolutionActivity extends BaseActivity {

    private static final String TAG = "SelfResolutionActivity";

    Button btn_action;
    TextView tv_msg,mtv_hdr;
    int subscriberid,entityId;
    String subscriberUsername = "";
    String clientAccessId = "";
    AllAPIs allAPIs;
    EditText et_passwrd,et_cnf_passwrd;
    String desc = "";
    LinearLayout ll_status;
    SweetAlertDialog sweetAlert;
    ImageView img_action;
    TextInputLayout til_pass, til_cnf_pass;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self_resolution);

        initControls();

        allAPIs = APIClient.getClient(SelfResolutionActivity.this).create(AllAPIs.class);

        SharedPreferences sharedPreferences = getSharedPreferences(MyUtils.MY_PREF,MODE_PRIVATE);
        subscriberid        = sharedPreferences.getInt(MyUtils.SUBSCRIBER_ID,0);
        subscriberUsername  = sharedPreferences.getString(MyUtils.USER_NAME,"");
        entityId            = sharedPreferences.getInt(MyUtils.ENTITY_ID,0);
        clientAccessId      = sharedPreferences.getString(MyUtils.CLIENT_ACCESS_ID,"");

        if(MyUtils.isOnline(SelfResolutionActivity.this)){
            ws_SelfResolution(subscriberid, subscriberUsername,clientAccessId);
//            ws_SelfResolution(669, "testmac5");
        }else {
            Toast.makeText(this, getString(R.string.internet_error_msg), Toast.LENGTH_SHORT).show();
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initControls() {
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow);
        upArrow.setColorFilter(getResources().getColor(android.R.color.black), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

       // toolbar.setTitleTextColor(getColor(android.R.color.colorAccent));
        tv_msg = (TextView)findViewById(R.id.tv_msg);
        mtv_hdr = (TextView)findViewById(R.id.mtv_hdr);
        btn_action = (Button)findViewById(R.id.btn_action);
        img_action = (ImageView) findViewById(R.id.img_action);

        ll_status = (LinearLayout) findViewById(R.id.ll_status);

        et_passwrd = (EditText) findViewById(R.id.et_passwrd);
        et_cnf_passwrd = (EditText) findViewById(R.id.et_cnf_passwrd);
        til_pass = (TextInputLayout) findViewById(R.id.til_pass);
        til_cnf_pass = (TextInputLayout) findViewById(R.id.til_cnf_pass);

        btn_action.setOnClickListener(listener);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void ws_SelfResolution(int subscriberid, String subscriberUsername, String clientAccessId) {

        sweetAlert = new SweetAlertDialog(SelfResolutionActivity.this,SweetAlertDialog.PROGRESS_TYPE);
        sweetAlert.setTitleText(getString(R.string.please_wait));
        sweetAlert.setCancelable(false);
        sweetAlert.setContentText("");
        sweetAlert.show();

        JsonObject postParam2 = null;
        try {
            JsonObject postParam = new JsonObject();
            postParam.addProperty("subscriberId",subscriberid) ;
            postParam.addProperty("clientAccessId", clientAccessId) ;
            postParam2=new JsonObject();
            postParam2.add("requestData",postParam);
        }catch (Exception e){
            e.printStackTrace();
        }

        Call<List<ResolutionResponse>> resApi = allAPIs.getSelfResolution(postParam2);
        resApi.enqueue(new Callback<List<ResolutionResponse>>() {
            @Override
            public void onResponse(Call<List<ResolutionResponse>> call, Response<List<ResolutionResponse>> response) {

                if(response.isSuccessful()) {
                    // your code to get data from the list
                    List<ResolutionResponse> list_response=response.body();
                   MyUtils.l("list size",":"+list_response.size());
                    if(list_response.size() > 0) {
                        String reply = list_response.get(0).reply;
                        String authdate = list_response.get(0).authdate;
                        MyUtils.l("reply", ":" + reply);
                        MyUtils.l("authdate", ":" + authdate);
                        desc = reply;

                        if (reply.equals("Wrong Password")) {
                            sweetAlert.dismiss();
                            tv_msg.setVisibility(View.VISIBLE);
                            ll_status.setVisibility(View.VISIBLE);
                            tv_msg.setText("You can change password by click on Reset Password button");
                            btn_action.setVisibility(View.VISIBLE);
                            til_pass.setVisibility(View.VISIBLE);
                            til_cnf_pass.setVisibility(View.VISIBLE);
                            btn_action.setText("Reset Password");
                            img_action.setImageDrawable(getResources().getDrawable(R.drawable.key));
                            mtv_hdr.setText("Reset Password");
                        }

                        if (reply.equals("Wrong MAC")) {
                            sweetAlert.dismiss();
                            tv_msg.setVisibility(View.VISIBLE);
                            ll_status.setVisibility(View.VISIBLE);
                            tv_msg.setText("You can reset MAC by click on Release MAC button");
                            btn_action.setVisibility(View.VISIBLE);
                            btn_action.setText("Release MAC");
                            mtv_hdr.setText("Release MAC");
                            img_action.setImageDrawable(getResources().getDrawable(R.drawable.mac));
                        }

                        if(reply.equals("session-exceed")){
                            sweetAlert.dismiss();
                            tv_msg.setVisibility(View.VISIBLE);
                            ll_status.setVisibility(View.VISIBLE);
                            tv_msg.setText("Your session exceeds");
                        }

                        if (reply.equals("{null}")) {
                            getNullResolution(reply);
                        }

                    }else{
                        /*sweetAlert.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                        sweetAlert.setContentText(getString(R.string.no_data));
                        sweetAlert.setConfirmText("Ok");
                        sweetAlert.setTitleText(getString(R.string.oops));
                        sweetAlert.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                                finish();
                            }
                        });*/
                        sweetAlert.dismiss();
                        getNullResolution("{null}");

                    }
                }else{

                }
            }

            @Override
            public void onFailure(Call<List<ResolutionResponse>> call, Throwable t) {
                sweetAlert.dismiss();
            }
        });
    }

    private void getNullResolution(String reply){
        JsonObject param = null;
        try {
            JsonObject param1 = new JsonObject();
            param1.addProperty("entity_id",entityId);
            param1.addProperty("subscriber_id",subscriberid);
            param1.addProperty("subscriberName", subscriberUsername);
            param1.addProperty("description",reply);
            param = new JsonObject();
            param.add("requestData",param1);
        }catch (Exception e){
            e.printStackTrace();
        }

        Call<List<NullResolutionDatum>> resApi = allAPIs.getNullResolution(param);
        resApi.enqueue(new Callback<List<NullResolutionDatum>>() {
            @Override
            public void onResponse(Call<List<NullResolutionDatum>> call, Response<List<NullResolutionDatum>> response) {

                List<NullResolutionDatum> list_response=response.body();
                if(list_response.size() > 0){
                    int status = list_response.get(0).activeStatus;

                    MyUtils.l(TAG,"status : "+status);

                    ll_status.setVisibility(View.VISIBLE);

                    sweetAlert.dismiss();

                    if(status == 0 ){
                        tv_msg.setVisibility(View.VISIBLE);
                        tv_msg.setText("You are not login at this moment");
                        img_action.setImageDrawable(getResources().getDrawable(R.drawable.log_out));
                        btn_action.setVisibility(View.GONE);
                    }

                    if(status == 1 ){
                        tv_msg.setVisibility(View.VISIBLE);
                        tv_msg.setText("You are logged in at this moment. You can logged of on click on below button");
                        btn_action.setVisibility(View.VISIBLE);
                        btn_action.setText("Log off");
                        mtv_hdr.setText("Log off");
                        img_action.setImageDrawable(getResources().getDrawable(R.drawable.log_out));
                        desc = "Log off";
                    }
                }
            }

            @Override
            public void onFailure(Call<List<NullResolutionDatum>> call, Throwable t) {

            }
        });


    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            String action = btn_action.getText().toString().toLowerCase();

            if(MyUtils.isOnline(SelfResolutionActivity.this)) {

                if (action.contains("password")) {

                    String password = et_passwrd.getText().toString();
                    String cnf_password = et_cnf_passwrd.getText().toString();

                    if (TextUtils.isEmpty(password)) {
                        et_passwrd.setError("Please enter password");
                    } else if(!password.equals(cnf_password)){
                        et_passwrd.setError("Both passwords doesn't match");
                    }else {
                        resetPassword(password);
                    }

                } else if (action.contains("mac")) {

                    releaseMAC();

                } else if (action.contains("log")) {

                    logOff();
                }
            }else {
                Toast.makeText(SelfResolutionActivity.this, getText(R.string.internet_error_msg), Toast.LENGTH_SHORT).show();
            }
        }
    };

    private void logOff() {

        final SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(SelfResolutionActivity.this,SweetAlertDialog.PROGRESS_TYPE);
        sweetAlertDialog.setContentText(getString(R.string.please_wait));
        sweetAlertDialog.setCancelable(false);
        sweetAlertDialog.setContentText("");
        sweetAlertDialog.show();

        JsonObject param = null;
        try {
            JsonObject object = new JsonObject();
            object.addProperty("entity_id",entityId);
            object.addProperty("subscriber_id",subscriberid);
            object.addProperty("subscriberName",subscriberUsername);
            object.addProperty("description",desc);

            param = new JsonObject();
            param.add("requestData",object);
        }catch (Exception e){
            e.printStackTrace();
        }

        Call<CommonResolutionData> api = allAPIs.getCommonResolution(param);

        api.enqueue(new Callback<CommonResolutionData>() {
            @Override
            public void onResponse(Call<CommonResolutionData> call, Response<CommonResolutionData> response) {

                    CommonResolutionData list_response=response.body();
//                    if(list_response.size() > 0){

                        int affected_rows = list_response.affectedRows;

                        if(affected_rows > 0){
                            sweetAlertDialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                            sweetAlertDialog.setTitleText("Success");
                            sweetAlertDialog.setContentText("User log off successfully");
                            sweetAlertDialog.setConfirmText("Ok");

                        }else {
                            sweetAlertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                            sweetAlertDialog.setTitleText(getString(R.string.oops));
                            sweetAlertDialog.setContentText("Something went wrong");
                            sweetAlertDialog.setConfirmText("Ok");
                        }

                        sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
//                                refresh();
                                finish();
                            }
                        });



            }

            @Override
            public void onFailure(Call<CommonResolutionData> call, Throwable t) {
                sweetAlertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                sweetAlertDialog.setTitleText("Error");
                sweetAlertDialog.setContentText("Something went wrong");
                sweetAlertDialog.setConfirmText("Ok");
                sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
//                        refresh();
                        finish();
                    }
                });
            }
        });
    }

    private void releaseMAC() {

        final SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(SelfResolutionActivity.this,SweetAlertDialog.PROGRESS_TYPE);
        sweetAlertDialog.setContentText(getString(R.string.please_wait));
        sweetAlertDialog.setCancelable(false);
        sweetAlertDialog.setTitleText("");
        sweetAlertDialog.show();

        JsonObject param = null;
        try {
            JsonObject object = new JsonObject();
            object.addProperty("entityId",entityId);
            object.addProperty("id",subscriberid);
            object.addProperty("subscriber_username",subscriberUsername);
            object.addProperty("clientAccessId",clientAccessId);
            object.addProperty("description",desc);

            param = new JsonObject();
            param.add("requestData",object);
        }catch (Exception e){
            e.printStackTrace();
        }

        Call<CommonResolutionData> api = allAPIs.getCommonResolution(param);
        api.enqueue(new Callback<CommonResolutionData>() {
            @Override
            public void onResponse(Call<CommonResolutionData> call, Response<CommonResolutionData> response) {
                if(response.isSuccessful()) {
                    CommonResolutionData list_response = response.body();


                    int affected_rows = list_response.affectedRows;

                    if (affected_rows > 0) {
                        sweetAlertDialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                        sweetAlertDialog.setTitleText("Success");
                        sweetAlertDialog.setContentText("Release MAC successfully");
                        sweetAlertDialog.setConfirmText("Ok");

                    } else {
                        sweetAlertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                        sweetAlertDialog.setTitleText(getString(R.string.oops));
                        sweetAlertDialog.setContentText("Something went wrong");
                        sweetAlertDialog.setConfirmText("Ok");
                    }

                    sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
//                            refresh();
                            finish();
                        }
                    });


                }
            }

            @Override
            public void onFailure(Call<CommonResolutionData> call, Throwable t) {
                sweetAlertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                sweetAlertDialog.setTitleText("Error");
                sweetAlertDialog.setContentText("Something went wrong");
                sweetAlertDialog.setConfirmText("Ok");
                sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
//                        refresh();
                        finish();
                    }
                });
            }
        });

    }

    private void resetPassword(String et_passwrd) {

        final SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(SelfResolutionActivity.this,SweetAlertDialog.PROGRESS_TYPE);
        sweetAlertDialog.setContentText(getString(R.string.please_wait));
        sweetAlertDialog.setCancelable(false);
        sweetAlertDialog.setContentText("");
        sweetAlertDialog.show();

        JsonObject param = null;
        try {
            JsonObject object = new JsonObject();
            object.addProperty("entity_id",entityId);
            object.addProperty("subscriber_id",subscriberid);
            object.addProperty("id",subscriberid);
            object.addProperty("newPass",et_passwrd);
            object.addProperty("description",desc);

            param = new JsonObject();
            param.add("requestData",object);
        }catch (Exception e){
            e.printStackTrace();
        }

        Call<CommonResolutionData> api = allAPIs.getCommonResolution(param);
        api.enqueue(new Callback<CommonResolutionData>() {
            @Override
            public void onResponse(Call<CommonResolutionData> call, Response<CommonResolutionData> response) {
                if(response.isSuccessful()){
                    CommonResolutionData list_response=response.body();

                        int affected_rows = list_response.affectedRows;

                        if(affected_rows > 0){
                            sweetAlertDialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                            sweetAlertDialog.setTitleText("Success");
                            sweetAlertDialog.setContentText("Password updated successfully");
                            sweetAlertDialog.setConfirmText("Ok");

                        }else {
                            sweetAlertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                            sweetAlertDialog.setTitleText(getString(R.string.oops));
                            sweetAlertDialog.setContentText("Something went wrong");
                            sweetAlertDialog.setConfirmText("Ok");
                        }

                        sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
//                                refresh();
                                finish();
                            }
                        });

                    }

            }

            @Override
            public void onFailure(Call<CommonResolutionData> call, Throwable t) {
                sweetAlertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                sweetAlertDialog.setTitleText("Error");
                sweetAlertDialog.setContentText("Something went wrong");
                sweetAlertDialog.setConfirmText("Ok");
                sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
//                        refresh();
                        finish();
                    }
                });

            }
        });
    }

    public void refresh(){
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }
}
