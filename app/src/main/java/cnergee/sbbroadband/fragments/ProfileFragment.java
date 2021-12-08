package cnergee.sbbroadband.fragments;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;
import cnergee.sbbroadband.BasicFragment;
import cnergee.sbbroadband.DashboardActivity;
import cnergee.sbbroadband.R;
import cnergee.sbbroadband.retroObj.ProfileData;
import cnergee.sbbroadband.retroObj.ResponseData;
import cnergee.sbbroadband.utils.MyUtils;
import developer.app.interface_.AllAPIs;
import developer.app.loadingview.ProgressWheel;
import developer.app.presenter.CorePresenter;
import developer.app.webservices.APIClient;
import developer.app.webservices.CommonAsyncTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Siddhesh on 5/25/2017.
 */

public class ProfileFragment extends BasicFragment implements View.OnClickListener{

    EditText et_name,et_mail,et_dob,et_userid,et_contact,et_addrs,et_country,et_city;
    RadioButton rb_male,rb_female;
    Button btn_submit;
    ProgressWheel progressBar;
    LinearLayout ll_profile;
    String nationality = "";

    CommonAsyncTask get_deyails;
    CommonAsyncTask update_details;
    private DatePickerDialog dobDialog;
    private SimpleDateFormat dateFormatter;

    private static final String TAG = "ProfileFragment";
    CorePresenter corePresenter;

    AllAPIs api;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    int entityId = 0;
    int subscriberId = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.content_profile,container,false);

        dateFormatter = new SimpleDateFormat("dd MMM yyyy", Locale.US);

        ((DashboardActivity)getActivity()).updateTitle(getString(R.string.profile_title));

        sharedPreferences = (SharedPreferences) getActivity().getSharedPreferences(MyUtils.MY_PREF,MODE_PRIVATE);
        entityId = sharedPreferences.getInt(MyUtils.ENTITY_ID,0);
        subscriberId = sharedPreferences.getInt(MyUtils.SUBSCRIBER_ID,0);
        editor = sharedPreferences.edit();

        api = APIClient.getClient(getActivity()).create(AllAPIs.class);

        et_dob = (EditText)view.findViewById(R.id.et_dob);
        et_name = (EditText)view.findViewById(R.id.et_name);
        et_mail = (EditText)view.findViewById(R.id.et_mail);
        et_userid = (EditText)view.findViewById(R.id.et_userid);
        et_contact = (EditText)view.findViewById(R.id.et_contact);
        et_addrs = (EditText)view.findViewById(R.id.et_addrs);
        et_city = (EditText)view.findViewById(R.id.et_city);
        et_country = (EditText)view.findViewById(R.id.et_country);
        rb_male = (RadioButton)view.findViewById(R.id.rb_male);
        rb_female = (RadioButton)view.findViewById(R.id.rb_female);
        btn_submit = (Button)view.findViewById(R.id.btn_submit);
        progressBar = (ProgressWheel) view.findViewById(R.id.progressBar);
        ll_profile = (LinearLayout) view.findViewById(R.id.ll_profile);

        et_dob.setInputType(InputType.TYPE_NULL);

        setHasOptionsMenu(true);

        corePresenter = new CorePresenter(getActivity(),iError);

        if(MyUtils.isOnline(getActivity())){
            ws_getProfileDetails();
        }else{
            setOfflineProfile();
            Toast.makeText(getActivity(), getString(R.string.internet_error_msg), Toast.LENGTH_SHORT).show();
        }

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(MyUtils.isOnline(getActivity())){

                    validateData();

                }else{
                    Toast.makeText(getActivity(), getString(R.string.internet_error_msg), Toast.LENGTH_SHORT).show();
                }
            }
        });

        et_dob.setOnClickListener(this);

        return view;
    }

    public void validateData(){
        String email = et_mail.getText().toString().trim();
        String mobile = et_contact.getText().toString().trim();

        boolean isCancel = false;
        View focusView = null;

        if(!isValidEmail(email)){
            et_mail.setError("Please enter valid email id");
            focusView = et_mail;
            isCancel = true;
        }

        if(!isValidMobile(mobile)){
            et_contact.setError("Please enter valid mobile number");
            focusView = et_contact;
            isCancel = true;
        }

        if(isCancel){
            focusView.requestFocus();
        }else {
            new SweetAlertDialog(getActivity(),SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Confirmation")
                    .setContentText("Are you sure want to update details?")
                    .setCancelText("Cancel")
                    .setConfirmText("Confirm")
                    .showCancelButton(true)
                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                        }
                    })
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
//                                    sweetAlertDialog.dismiss();
                            ws_updateProfileDetails(sweetAlertDialog);
                            sweetAlertDialog.setTitleText(getString(R.string.please_wait))
                                    .setContentText("")
                                    .showCancelButton(false)
                                    .setCancelClickListener(null)
                                    .setConfirmClickListener(null)
                                    .changeAlertType(SweetAlertDialog.PROGRESS_TYPE);
                        }
                    }).show();
        }
    }

    private boolean isValidMobile(String phone) {
        boolean isValid = false;
        PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
        try {
            Phonenumber.PhoneNumber number = phoneNumberUtil.parse(phone,"IN");
            isValid = phoneNumberUtil.isValidNumber(number);
            String usNumber = phoneNumberUtil.format(number, PhoneNumberUtil.PhoneNumberFormat.E164);

            MyUtils.l(TAG," number : "+usNumber);
        }catch (Exception e){
            e.printStackTrace();
        }

        return isValid;
    }

    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }


    private void ws_updateProfileDetails(final SweetAlertDialog sweetAlertDialog) {

        String f_name = "";
        String l_name = "";
        String subscriber_username = et_userid.getText().toString();
        String name = et_name.getText().toString();
        if(name.contains(" ")){
            String[] arr = name.split(" ");
            if(arr.length > 0){
                f_name = arr[0];
                l_name = arr[arr.length-1];
            }
        }

        String dob = et_dob.getText().toString();
        String contact = et_contact.getText().toString();
        String mail = et_mail.getText().toString();
        String addrs = et_addrs.getText().toString();
/*        String country = et_country.getText().toString();
        String city = et_city.getText().toString();*/
        String gender = "";
        if(rb_male.isChecked()){
            gender = "male";
        }else if(rb_female.isChecked()){
            gender = "female";
        }else {
            gender = "";
        }
        String date = "";
        String strCurrentDate = et_dob.getText().toString();
        SimpleDateFormat format = new SimpleDateFormat("dd MMM yyyy");
        Date newDate = null;
        try {
            newDate = format.parse(strCurrentDate);
            format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
            date = format.format(newDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Call<ResponseData> responseApi = api.updateProfileDetails(subscriberId,entityId,
                                                                  subscriber_username,f_name,
                                                                  l_name,date,
                                                                  gender,contact,
                                                                  mail,addrs);

        responseApi.enqueue(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {

                if(response.isSuccessful()){
                    ResponseData responseData = response.body();
                    int status = responseData.status;
                    String msg = responseData.message;

                    if(status == 1){
                        sweetAlertDialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                        sweetAlertDialog .setConfirmText("Ok");
                        sweetAlertDialog.setTitleText("Success");
                        sweetAlertDialog .setContentText(msg);
                    }else{
                        sweetAlertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                        sweetAlertDialog .setConfirmText("Ok");
                        sweetAlertDialog.setTitleText("Failed");
                        sweetAlertDialog .setContentText(getString(R.string.went_wrong));
                    }

                    sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            et_dob.setEnabled(false);
                            et_mail.setEnabled(false);
                            et_name.setEnabled(false);
                            et_userid.setEnabled(false);
                            et_contact.setEnabled(false);
                            et_city.setEnabled(false);
                            et_country.setEnabled(false);
                            et_addrs.setEnabled(false);
                            rb_male.setEnabled(false);
                            rb_female.setEnabled(false);
                            btn_submit.setVisibility(View.GONE);

                            sweetAlertDialog.dismiss();
                        }
                    }).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {

            }
        });

    }

    private void parse_udpate(String s, SweetAlertDialog sweetAlertDialog) {

        MyUtils.l(TAG,"update : "+s);
        int status = 0;
        String message = "";

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(s);
            status = jsonObject.optInt("status",0);
            message = jsonObject.optString("message","Failed");
        }catch (Exception e){
            e.printStackTrace();
        }

        sweetAlertDialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);

        if(status == 1){
            sweetAlertDialog.setContentText(message);
            sweetAlertDialog.setTitleText("Success");
            sweetAlertDialog.setConfirmText("Ok");
        }else{
            sweetAlertDialog.setContentText(message);
            sweetAlertDialog.setTitleText("Failed");
            sweetAlertDialog.setConfirmText("Ok");
        }

        sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {

                et_dob.setEnabled(false);
                et_mail.setEnabled(false);
                et_name.setEnabled(false);
                et_userid.setEnabled(false);
                et_contact.setEnabled(false);
                et_city.setEnabled(false);
                et_country.setEnabled(false);
                et_addrs.setEnabled(false);
                rb_male.setEnabled(false);
                rb_female.setEnabled(false);
                btn_submit.setVisibility(View.GONE);

                sweetAlertDialog.dismiss();
            }
        }).show();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.profile_edit, menu);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void ws_getProfileDetails() {

        progressBar.setVisibility(View.VISIBLE);
        ll_profile.setVisibility(View.GONE);

        Call<ProfileData> profileApi = api.getProfileDetails(subscriberId,entityId);

        profileApi.enqueue(new Callback<ProfileData>() {

            @Override
            public void onResponse(Call<ProfileData> call, Response<ProfileData> response) {

                if(response.isSuccessful()){
                    progressBar.setVisibility(View.GONE);
                    ll_profile.setVisibility(View.VISIBLE);
                    ProfileData object = response.body();

                    et_name.setText(object.firstName+" "+object.lastName);
                    et_userid.setText(object.subscriberUsername);
                    et_contact.setText(object.contactNo);
                    et_mail.setText(object.email);
                    et_addrs.setText(object.address_line_1+" "+object.address_line_2);
                /*et_city.setText(object.city);
                et_country.setText(object.country);
                nationality = object.nationality;*/

                if(object.gender != null) {
                    String gender = object.gender;
                    if (gender.equalsIgnoreCase("male")) {
                        rb_male.setChecked(true);
                        rb_female.setChecked(false);
                    } else if (gender.equalsIgnoreCase("female")) {
                        rb_male.setChecked(false);
                        rb_female.setChecked(true);
                    }
                }

                    String strCurrentDate = object.dob;
//                String strCurrentDate = "2017-07-11T05:19:06.000Z";
                    if(strCurrentDate != "" && strCurrentDate != null) {
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                        Date newDate = null;
                        try {
                            newDate = format.parse(strCurrentDate);
                            format = new SimpleDateFormat("dd MMM yyyy");
                            String date = format.format(newDate);
                            et_dob.setText(date);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }


                    }

                    Gson gson = new Gson();
                    String profileData = gson.toJson(object);
                    editor.putString("profileData",profileData);
                    editor.commit();
                }
            }

            @Override
            public void onFailure(Call<ProfileData> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                ll_profile.setVisibility(View.VISIBLE);
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_edit) {

            et_dob.setEnabled(false);
            et_mail.setEnabled(true);
            et_name.setEnabled(false);
//            et_userid.setEnabled(true);
            et_contact.setEnabled(true);
            et_city.setEnabled(false);
            et_country.setEnabled(false);
            et_addrs.setEnabled(false);
            rb_male.setEnabled(false);
            rb_female.setEnabled(false);
            btn_submit.setVisibility(View.VISIBLE);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        Calendar newCalendar = Calendar.getInstance();
        dobDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                et_dob.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        dobDialog.show();
    }

    public void setOfflineProfile(){
        try {
            Gson gson = new Gson();
            String json = sharedPreferences.getString("profileData", "");

            progressBar.setVisibility(View.GONE);
            ll_profile.setVisibility(View.VISIBLE);
            ProfileData object = gson.fromJson(json, ProfileData.class);

            et_name.setText(object.firstName+" "+object.lastName);
            et_userid.setText(object.subscriberUsername);
            et_contact.setText(object.contactNo);
            et_mail.setText(object.email);
            et_addrs.setText(object.address_line_1+" "+object.address_line_2);
                /*et_city.setText(object.city);
                et_country.setText(object.country);
                nationality = object.nationality;*/

            String gender = object.gender;
            if(gender.equalsIgnoreCase("male")){
                rb_male.setChecked(true);
                rb_female.setChecked(false);
            }else if(gender.equalsIgnoreCase("female")){
                rb_male.setChecked(false);
                rb_female.setChecked(true);
            }

            String strCurrentDate = object.dob;
//                String strCurrentDate = "2017-07-11T05:19:06.000Z";
            if(strCurrentDate != "" && strCurrentDate != null) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
                Date newDate = null;
                try {
                    newDate = format.parse(strCurrentDate);
                    format = new SimpleDateFormat("dd MMM yyyy");
                    String date = format.format(newDate);
                    et_dob.setText(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }


            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
