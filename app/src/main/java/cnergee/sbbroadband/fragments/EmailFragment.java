package cnergee.sbbroadband.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import cnergee.sbbroadband.DashboardActivity;
import cnergee.sbbroadband.R;
import cnergee.sbbroadband.retroObj.MobileLoginData;
import cnergee.sbbroadband.retroObj.ResponseData;
import cnergee.sbbroadband.retroObj.Subscriber;
import cnergee.sbbroadband.utils.MyUtils;
import developer.app.interface_.AllAPIs;
import developer.app.presenter.CorePresenter;
import developer.app.webservices.APIClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static cnergee.sbbroadband.BaseActivity.iError;


/**
 * Created by Siddhesh on 4/28/2017.
 */

public class EmailFragment extends Fragment {
    private static final String TAG = "EmailFragment";
    CorePresenter corePresenter;
    SweetAlertDialog sweetDialog;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String country_code = "";
    String deviceId = "";
    EditText et_email, et_pass;
    Button btn_sign_in;

    AllAPIs allAPIs;

    String clientAccessId = "",userName = "",email = "";
    int entityId, subscriberStatus, areaId, subscriberId;
    int entity_Id;

    public EmailFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.email_login_content,container,false);

        allAPIs = APIClient.getClient(getActivity()).create(AllAPIs.class);

        sharedPreferences = getActivity().getSharedPreferences(MyUtils.MY_PREF, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        deviceId = sharedPreferences.getString(MyUtils.TOKEN,"");
        entity_Id = sharedPreferences.getInt(MyUtils.ENTITY_ID,0);

        btn_sign_in = (Button)view.findViewById(R.id.btn_sign_in);
        et_email = (EditText)view.findViewById(R.id.et_email);
        et_pass = (EditText)view.findViewById(R.id.et_pass);

       /* et_email.setText("testshamal12");
        et_pass.setText("1234");
*/
        corePresenter = new CorePresenter(getActivity(),iError);

        btn_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(MyUtils.isOnline(getActivity())) {
                    validateEmail();
                }else{
                    Toast.makeText(getActivity(), getString(R.string.internet_error_msg), Toast.LENGTH_SHORT).show();
                }
//                ws_login("deven+1@inkswipe.com", "1f70e9d479e1a700cf458134b773304fa53b7d1cc8c413ecfbee065a92edff90c41a805984453d643ca4aa41de0c77134396464ce34aa89210d1574561f37b5b");
            }
        });
        return view;
    }

    private void validateEmail() {

        String email = et_email.getText().toString().trim();
        String password = et_pass.getText().toString().trim();

        View focusView = null;
        boolean isCancel = false;

        if(TextUtils.isEmpty(email)){
            et_email.setError(getString(R.string.err_email));
            focusView = et_email;
            isCancel = true;
        }

        if(TextUtils.isEmpty(password)){
            et_pass.setError(getString(R.string.err_password));
            focusView = et_pass;
            isCancel = true;
        }

        if(isCancel){
            focusView.requestFocus();
        }else {
            String passwrd = password+"S!@#";
            byte[] pass = passwrd.getBytes();
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < pass.length; i++) {
                builder.append(String.format("%x", pass[i]));
            }

            MyUtils.l(TAG, "password : " + builder.toString());
//            ws_geMobile(email, builder.toString(), entity_Id, getString(R.string.for_whome));
            ws_geMobile(email, builder.toString(), getString(R.string.client_access_id), getString(R.string.for_whome));
        }
    }


    private void ws_geMobile(String mail,String password, String clientAccessID, String subscriber) {

        sweetDialog = new SweetAlertDialog(getActivity(),SweetAlertDialog.PROGRESS_TYPE);
        sweetDialog.setTitleText("Please wait....");
        sweetDialog.setCancelable(false);
        sweetDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.bg_end));
        sweetDialog.show();

        Call<MobileLoginData> mobApi = allAPIs.getEmailLoginResponse(mail,password,clientAccessID,subscriber);
        mobApi.enqueue(new Callback<MobileLoginData>() {
            @Override
            public void onResponse(Call<MobileLoginData> call, Response<MobileLoginData> response) {

                MobileLoginData loginData = response.body();
                String status = loginData.status;
                String msg = loginData.message;
                List<Subscriber> subscriber = loginData.subscriber;

                if("1".equalsIgnoreCase(status)){

                    if(subscriber.size() > 0){

                        for(Subscriber subscriber1 : subscriber){

                            clientAccessId = subscriber1.clientAccessId;
                            entityId = subscriber1.entityId;
                            subscriberStatus = subscriber1.subscriberStatus;
                            areaId = subscriber1.subscriberAreaId;
                            userName = subscriber1.subscriberUsername;
                            email = subscriber1.subscriberEmail;
                            subscriberId = subscriber1.subscriberId;

                            editor.putString(MyUtils.USER_NAME, userName);
                            editor.putString(MyUtils.CLIENT_ACCESS_ID, clientAccessId);
                            editor.putInt(MyUtils.ENTITY_ID, entityId);
                            editor.putInt(MyUtils.SUBSCRIBER_ID, subscriberId);
                            editor.commit();

                            ws_insertMobileData(entityId, subscriberId);
                        }
                    }
                }else{
                    sweetDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                    sweetDialog.setContentText(msg);
                    sweetDialog.setTitleText("Oops..");
                    sweetDialog.setConfirmText("Ok");
                    sweetDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<MobileLoginData> call, Throwable t) {

            }
        });
    }

    private void ws_insertMobileData(int entityId, int subscriberId) {

       /* MyUtils.l(TAG,"imei : "+MyUtils.imei);
        MyUtils.l(TAG,"app_version : "+MyUtils.app_version);
        MyUtils.l(TAG,"androidOS : "+MyUtils.androidOS);
        MyUtils.l(TAG,"manufacturer : "+MyUtils.manufacturer);
        MyUtils.l(TAG,"phone_unique_id : "+MyUtils.phone_unique_id);
        MyUtils.l(TAG,"mobile_name : "+MyUtils.mobile_name);
        MyUtils.l(TAG,"platform : "+MyUtils.platform);*/
        final Call<ResponseData> insertApi = allAPIs.insertPhoneDetails( subscriberId,
                                                                         entityId,
                                                                         "username",
                                                                         country_code,
                                                                         MyUtils.mobile_name,
                                                                         MyUtils.androidOS,
                                                                         MyUtils.phone_unique_id,
                                                                         MyUtils.platform,
                                                                         MyUtils.app_version,
                                                                         MyUtils.manufacturer,
                                                                         MyUtils.imei,
                                                                         deviceId );

        insertApi.enqueue(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {

                if(response.isSuccessful()){
                    ResponseData data = response.body();
                    int status = data.status;
                    String msg = data.message;

                    MyUtils.l(TAG,"status : "+status+" msg : "+msg);
                    if(status == 1){
                        sweetDialog.dismiss();
                        editor.putInt(MyUtils.APP_REG,1);
                        editor.commit();
                        Intent intent = new Intent(getActivity(), DashboardActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        getActivity().startActivity(intent);
                        getActivity().finish();

                    }else{
                        sweetDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                        sweetDialog.setContentText(msg);
                        sweetDialog.setTitleText("Oops..");
                        sweetDialog.setConfirmText("Ok");
                        sweetDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                            }
                        });
                    }
                }else {
                    sweetDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                    sweetDialog.setContentText(getString(R.string.went_wrong));
                    sweetDialog.setTitleText("Oops..");
                    sweetDialog.setConfirmText("Ok");
                    sweetDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                        }
                    });
                }

            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                sweetDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                sweetDialog.setContentText(t.toString());
                sweetDialog.setTitleText("Oops..");
                sweetDialog.setConfirmText("Ok");
                sweetDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                    }
                });
            }
        });

    }


}
