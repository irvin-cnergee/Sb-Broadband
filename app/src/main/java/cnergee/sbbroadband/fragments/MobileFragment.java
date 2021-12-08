package cnergee.sbbroadband.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.hbb20.CountryCodePicker;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import cnergee.sbbroadband.DashboardActivity;
import cnergee.sbbroadband.R;
import cnergee.sbbroadband.receiver.SMSReceiver;
import cnergee.sbbroadband.retroObj.MobileLoginData;
import cnergee.sbbroadband.retroObj.ResponseData;
import cnergee.sbbroadband.retroObj.Subscriber;
import cnergee.sbbroadband.utils.MyUtils;
import developer.app.interface_.AllAPIs;
import developer.app.interface_.SmsListener;
import developer.app.webservices.APIClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Siddhesh on 4/28/2017.
 */

public class MobileFragment extends Fragment {

    private static final String TAG = "MobileFragment";

    CountryCodePicker cpp;
    EditText et_otp,et_mobile;
    Button btn_submit,btn_submit_otp;
    String country_code = "";
    TextView tv_resend;
    AllAPIs allAPIs;
    SweetAlertDialog sweetDialog;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String deviceId = "";

    AlertDialog builder;
    String clientAccessId = "",userName = "",email = "";
    int entityId, subscriberStatus, areaId, subscriberId;
    int entity_Id;
    View v1 = null;
    TextView tv_count,tv_msg;
    Button confirm_button;

    public MobileFragment(){};
    SMSReceiver smsReceiver;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.mobile_login_layout, container, false);

        allAPIs = APIClient.getClient(getActivity()).create(AllAPIs.class);

        MyUtils.getPhoneInfo(getActivity());

        builder = new AlertDialog.Builder(getActivity()).create();

        sharedPreferences = getActivity().getSharedPreferences(MyUtils.MY_PREF, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        deviceId = sharedPreferences.getString(MyUtils.TOKEN,"");
        entity_Id = sharedPreferences.getInt(MyUtils.ENTITY_ID,0);

        MyUtils.l(TAG,"deviceid : "+deviceId);

        cpp = (CountryCodePicker)view.findViewById(R.id.ccp);
        et_otp = (EditText)view.findViewById(R.id.et_otp);
        btn_submit = (Button)view.findViewById(R.id.btn_submit);
        btn_submit_otp = (Button)view.findViewById(R.id.btn_submit_otp);
        et_mobile = (EditText)view.findViewById(R.id.et_mobile);
        tv_resend = (TextView) view.findViewById(R.id.tv_resend);

        country_code = cpp.getSelectedCountryCode();

        cpp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                Log.e("cpp",":"+cpp.getSelectedCountryCode());
                country_code = cpp.getSelectedCountryCode();
                et_otp.setVisibility(View.GONE);
                btn_submit_otp.setVisibility(View.GONE);
                btn_submit.setVisibility(View.VISIBLE);
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateUsing_libphonenumber(country_code,et_mobile.getText().toString());
            }
        });

        btn_submit_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String otp = et_otp.getText().toString().trim();
                View focusView = null;
                boolean isCancel = false;

                if(TextUtils.isEmpty(otp)){
                    et_otp.setError(getString(R.string.err_otp));
                    isCancel = true;
                    focusView = et_otp;
                }

                if(isCancel){
                    focusView.requestFocus();
                }else{
                    ws_verifyOTP(otp);
                }

            }
        });

        tv_resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ws_insertMobileData(entity_Id, subscriberId);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");

        smsReceiver = new SMSReceiver();
        getActivity().registerReceiver(smsReceiver,intentFilter);

        smsReceiver.bindListener(new SmsListener() {
            @Override
            public void messageReceived(String messageText) {

                MyUtils.l(TAG,"messageText : "+messageText);
                builder.dismiss();
                et_otp.setText(messageText);
                ws_verifyOTP(messageText);
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        getActivity().unregisterReceiver(smsReceiver);
    }

    private void ws_verifyOTP(String otp) {
        sweetDialog.dismiss();
        sweetDialog = new SweetAlertDialog(getActivity(),SweetAlertDialog.PROGRESS_TYPE);
        sweetDialog.setTitleText("Please wait....");
        sweetDialog.setCancelable(false);
        sweetDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.bg_end));
        sweetDialog.show();

        Call<ResponseData> verifyAPi = allAPIs.verifyOTP(subscriberId,entityId,otp);
        verifyAPi.enqueue(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {

                ResponseData data = response.body();
                int status = data.status;
                String msg = data.message;

                if(status == 1){
                    sweetDialog.dismiss();

                    editor.putInt(MyUtils.APP_REG,1);
                    editor.commit();

                    Intent intent = new Intent(getActivity(),DashboardActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    getActivity().finish();

                }else {
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
            public void onFailure(Call<ResponseData> call, Throwable t) {
                sweetDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                sweetDialog.setContentText("Webservice error");
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


    private boolean validateUsing_libphonenumber(String country_code,String mobile_no){

        if(TextUtils.isEmpty(mobile_no)){
            et_mobile.requestFocus();
            et_mobile.setError(getString(R.string.err_mobile));
            return false;
        }else {
            PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
            String isoCode = phoneNumberUtil.getRegionCodeForCountryCode(Integer.parseInt(country_code));

            Phonenumber.PhoneNumber phoneNumber = null;
            boolean isValid = false;
            try {
                //phoneNumber = phoneNumberUtil.parse(phNumber, "IN");  //if you want to pass region code
                phoneNumber = phoneNumberUtil.parse(mobile_no, isoCode);
            } catch (NumberParseException e) {
                System.err.println(e);
            }

                isValid = phoneNumberUtil.isValidNumber(phoneNumber);

            if (isValid) {
                String internationalFormat = phoneNumberUtil.format(phoneNumber, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL);
//            Toast.makeText(getActivity(), "Phone Number is Valid " + internationalFormat, Toast.LENGTH_LONG).show();
                et_mobile.setError(null);
                if(MyUtils.isOnline(getActivity())){
//                    ws_geMobile(mobile_no, entity_Id, getString(R.string.for_whome));
                    ws_geMobile(mobile_no, getString(R.string.client_access_id), getString(R.string.for_whome));
                }else {
                    Toast.makeText(getActivity(), getString(R.string.internet_error_msg), Toast.LENGTH_SHORT).show();
                }

                return true;
            } else {
//            Toast.makeText(getActivity(), "Phone Number is Invalid " + phoneNumber, Toast.LENGTH_LONG).show();
                et_mobile.setError(getString(R.string.err_mobile));
                tv_resend.setVisibility(View.GONE);
                et_otp.setVisibility(View.GONE);
                return false;
            }
        }
    }

    RadioGroup rg;
    List<Subscriber> subscriber;

    private void ws_geMobile(String mb_num, String clientAccessID, String subscrber) {

        sweetDialog = new SweetAlertDialog(getActivity(),SweetAlertDialog.PROGRESS_TYPE);
        sweetDialog.setTitleText("Please wait....");
        sweetDialog.setCancelable(false);
        sweetDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.bg_end));
        sweetDialog.show();

        Call<MobileLoginData> mobApi = allAPIs.getLoginResponse(mb_num,clientAccessID,subscrber);
        mobApi.enqueue(new Callback<MobileLoginData>() {
            @Override
            public void onResponse(Call<MobileLoginData> call, Response<MobileLoginData> response) {

                MobileLoginData loginData = response.body();
                String status = loginData.status;
                String msg = loginData.message;
                subscriber = loginData.subscriber;

                if("1".equalsIgnoreCase(status)){

                    if(subscriber.size() > 0){

                        if(subscriber.size() == 1) {

                            for (Subscriber subscriber1 : subscriber) {

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
                        }else if(subscriber.size() > 1){

//                            sweetDialog.dismiss();
                            List<String> list = new ArrayList<String>();

                            View view = LayoutInflater.from(getActivity()).inflate(R.layout.multiple_user,null);

                            sweetDialog.setContentView(view);
                            sweetDialog.setCancelable(true);
                            Window window = sweetDialog.getWindow();
                            window.setLayout(dpToPx(290), LinearLayout.LayoutParams.WRAP_CONTENT);
                            sweetDialog.show();

                            Button confirm_button = (Button)view.findViewById(R.id.confirm_button);

                            for(int row = 0;row<1;row++) {

                                rg = new RadioGroup(getActivity());
                                RadioGroup.LayoutParams rprms = null;
                                Typeface type = Typeface.createFromAsset(getActivity().getAssets(),"fonts/rockwell.ttf");

                                for (int i = 0; i < subscriber.size(); i++) {
                                    email = subscriber.get(i).subscriberEmail;
                                    RadioButton rdbtn = new RadioButton(getActivity());
                                    rdbtn.setTypeface(type);
                                    rdbtn.setId((row * 2) + i);
                                    rdbtn.setText(email);
                                    rg.addView(rdbtn);
//                                    rprms= new RadioGroup.LayoutParams(RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.MATCH_PARENT);
                                    list.add(email);
                                }

                                ((ViewGroup)view.findViewById(R.id.rg)).addView(rg);
                            }

                            confirm_button.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if(rg.getCheckedRadioButtonId() == -1){
//                                        Toast.makeText(getActivity(), "Please select account", Toast.LENGTH_SHORT).show();
                                    }else {
//                                        Toast.makeText(getActivity(), ""+rg.getCheckedRadioButtonId(), Toast.LENGTH_SHORT).show();
                                        clientAccessId = subscriber.get(rg.getCheckedRadioButtonId()).clientAccessId;
                                        entityId = subscriber.get(rg.getCheckedRadioButtonId()).entityId;
                                        subscriberStatus = subscriber.get(rg.getCheckedRadioButtonId()).subscriberStatus;
                                        areaId = subscriber.get(rg.getCheckedRadioButtonId()).subscriberAreaId;
                                        userName = subscriber.get(rg.getCheckedRadioButtonId()).subscriberUsername;
                                        email = subscriber.get(rg.getCheckedRadioButtonId()).subscriberEmail;
                                        subscriberId = subscriber.get(rg.getCheckedRadioButtonId()).subscriberId;

                                        editor.putString(MyUtils.USER_NAME, userName);
                                        editor.putString(MyUtils.CLIENT_ACCESS_ID, clientAccessId);
//                                        editor.putInt(MyUtils.ENTITY_ID, entityId);
                                        editor.putInt(MyUtils.SUBSCRIBER_ID, subscriberId);
                                        editor.commit();

                                        ws_insertMobileData(entityId, subscriberId);
                                    }
                                }
                            });

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
                sweetDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                sweetDialog.setContentText("Webservice error");
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

    private void ws_insertMobileData(int entityId, int subscriberId) {

       /* MyUtils.l(TAG,"imei : "+MyUtils.imei);
        MyUtils.l(TAG,"app_version : "+MyUtils.app_version);
        MyUtils.l(TAG,"androidOS : "+MyUtils.androidOS);
        MyUtils.l(TAG,"manufacturer : "+MyUtils.manufacturer);
        MyUtils.l(TAG,"phone_unique_id : "+MyUtils.phone_unique_id);
        MyUtils.l(TAG,"mobile_name : "+MyUtils.mobile_name);
        MyUtils.l(TAG,"platform : "+MyUtils.platform);*/
        Call<ResponseData> insertApi = allAPIs.insertPhoneDetails(  subscriberId,
                                                                    entityId,
                                                                    "mobile",
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
                       sweetDialog = new SweetAlertDialog(getActivity(),SweetAlertDialog.PROGRESS_TYPE);
                       sweetDialog.changeAlertType(SweetAlertDialog.PROGRESS_TYPE);
                       sweetDialog.setContentText("");
                       sweetDialog.setTitleText("Fetching OTP");
                       sweetDialog.show();
                       CountDownTimer countDownTimer = new CountDownTimer(15000,1000) {
                           @Override
                           public void onTick(long l) {
                               Log.d(TAG,"milliseconds : "+l);
//                           Toast.makeText(getActivity(), "seconds : "+l/1000, Toast.LENGTH_SHORT).show();
                           }

                           @Override
                           public void onFinish() {
//                           sweetDialog.dismiss();
                               tv_resend.setVisibility(View.VISIBLE);
                               et_otp.setVisibility(View.VISIBLE);
                               btn_submit_otp.setVisibility(View.VISIBLE);
                               btn_submit.setVisibility(View.GONE);
                          /* new SweetAlertDialog(getActivity())
                                   .setContentText("Please check your messages for OTP.")
                                   .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                       @Override
                                       public void onClick(SweetAlertDialog sweetAlertDialog) {
                                           sweetAlertDialog.dismiss();
                                       }
                                   })
                                   .show();*/
                               sweetDialog.changeAlertType(SweetAlertDialog.NORMAL_TYPE);
                               sweetDialog.setTitleText("");
                               sweetDialog.setContentText("Please check your messages for OTP.");
                               sweetDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                   @Override
                                   public void onClick(SweetAlertDialog sweetAlertDialog) {
                                       sweetAlertDialog.dismiss();
                                   }
                               });
                           }
                       }.start();
//                    sweetDialog.show();

                   }else{
                       sweetDialog.dismiss();
                       sweetDialog = new SweetAlertDialog(getActivity(),SweetAlertDialog.ERROR_TYPE);
                       sweetDialog.setContentText(msg);
                       sweetDialog.setTitleText("Oops..");
                       sweetDialog.setConfirmText("Ok");
                       sweetDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                           @Override
                           public void onClick(SweetAlertDialog sweetAlertDialog) {
                               sweetAlertDialog.dismiss();
                           }
                       });
                       sweetDialog.show();
                   }

               }
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                sweetDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                sweetDialog.setContentText("Webservice error");
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

    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

}
