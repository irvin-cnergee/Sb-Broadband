package cnergee.sbbroadband;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import cnergee.sbbroadband.retroObj.CurrentPckgRateData;
import cnergee.sbbroadband.retroObj.Due;
import cnergee.sbbroadband.retroObj.DuesData;
import cnergee.sbbroadband.retroObj.HelpData;
import cnergee.sbbroadband.retroObj.OnlineRenewResponse;
import cnergee.sbbroadband.retroObj.ResponseData;
import cnergee.sbbroadband.utils.MyUtils;
import developer.app.interface_.AllAPIs;
import developer.app.webservices.APIClient;
import picker.ugurtekbas.com.Picker.Picker;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MakePaymentActivity extends AppCompatActivity {

    private static final String TAG = "MakePaymentActivity";

    CardView cv_pckgupdate;
    String from = "";
    String packg_name="";
    int packg_rate ;
    String currency="";
    int due_id;
    int packg_id,renewal_mode = 1;
    TextView tv_rupees,tv_pckg_name,tv_terms;
    RadioButton radioRenew,radioImm;
    AllAPIs apIs;
    SharedPreferences sharedPreferences;
    int subscriberId, entityId, areaId;
    LinearLayout ll_pickup;
    View pay_view;

    EditText met_comment;
    Button btn_sumit;
    Picker amPicker;
    MaterialCalendarView calendarView;
    Dialog dialog;
    CheckBox chk_terms;

    SweetAlertDialog sweetAlertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_payment);

        initControls();

        apIs = APIClient.getClient(MakePaymentActivity.this).create(AllAPIs.class);

        sharedPreferences = getSharedPreferences(MyUtils.MY_PREF,MODE_PRIVATE);
        subscriberId = sharedPreferences.getInt(MyUtils.SUBSCRIBER_ID,0);
        entityId = sharedPreferences.getInt(MyUtils.ENTITY_ID,0);
        areaId = sharedPreferences.getInt(MyUtils.AREA_ID,0);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        if(MyUtils.isOnline(MakePaymentActivity.this)){
            getCurrentPckgRate(packg_id,subscriberId,areaId,entityId);
        }else {
            Toast.makeText(this, getString(R.string.internet_error_msg), Toast.LENGTH_SHORT).show();
        }

        SpannableString ss = new SpannableString(getString(R.string.terms_condition));
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
//                startActivity(new Intent(MyActivity.this, NextActivity.class));
//                Toast.makeText(MakePaymentActivity.this, "terms", Toast.LENGTH_SHORT).show();
                if(MyUtils.isOnline(MakePaymentActivity.this)) {
                    getTerms(entityId);
                }else {
                    Toast.makeText(MakePaymentActivity.this, getString(R.string.internet_error_msg), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };
        ss.setSpan(clickableSpan, 28, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_terms.setText(ss);
        tv_terms.setMovementMethod(LinkMovementMethod.getInstance());

    }

    private void getCurrentPckgRate(int packg_id, int subscriberId, int areaId, int entityId) {

        sweetAlertDialog = new SweetAlertDialog(MakePaymentActivity.this,SweetAlertDialog.PROGRESS_TYPE);
        sweetAlertDialog.setTitleText(getString(R.string.please_wait));
        sweetAlertDialog.setContentText("");
        sweetAlertDialog.setCancelable(false);
        sweetAlertDialog.show();

        Call<CurrentPckgRateData> api = apIs.getCurrentPckgRate(packg_id,subscriberId,areaId,entityId);
        api.enqueue(new Callback<CurrentPckgRateData>() {
            @Override
            public void onResponse(Call<CurrentPckgRateData> call, Response<CurrentPckgRateData> response) {

                if(response.isSuccessful()){

                    sweetAlertDialog.dismiss();

                    CurrentPckgRateData rateData = response.body();
                    Double amount = rateData.amount;
                    String currency = rateData.currency;
                    int packageStatus = rateData.packageStatus;
                    //int areaStatus = rateData.areaStatus;


                    int areaStatus;
                    if(rateData.areaStatus!=null){
                        areaStatus = rateData.areaStatus;
                    }else{
                        areaStatus=1;
                    }

                    MyUtils.l(TAG,"packageStatus : "+packageStatus);
                    MyUtils.l(TAG,"areaStatus : "+areaStatus);

                    tv_rupees.setText(amount+" "+currency);

                    if(packageStatus == 0 || areaStatus == 0){
                        sweetAlertDialog = new SweetAlertDialog(MakePaymentActivity.this,SweetAlertDialog.WARNING_TYPE);
                        sweetAlertDialog.setCancelable(false);
                        sweetAlertDialog.setTitleText(getString(R.string.oops));
                        sweetAlertDialog.setContentText("You can not renew discontinued package.");
                        sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                                finish();
                            }
                        }).show();
                    }

                }else {
                    sweetAlertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                    sweetAlertDialog.setTitleText("Error");
                    sweetAlertDialog.setContentText(getString(R.string.went_wrong));
                    sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                        }
                    });
                }

            }

            @Override
            public void onFailure(Call<CurrentPckgRateData> call, Throwable t) {
                sweetAlertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                sweetAlertDialog.setTitleText("Error");
                sweetAlertDialog.setContentText("webservice error");
                sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                    }
                });
            }
        });

    }

    private void getTerms(int entityId) {

        sweetAlertDialog = new SweetAlertDialog(MakePaymentActivity.this,SweetAlertDialog.PROGRESS_TYPE);
        sweetAlertDialog.setTitleText(getString(R.string.please_wait));
        sweetAlertDialog.setContentText("");
        sweetAlertDialog.show();

        Call<HelpData> termsApi = apIs.getTermsAndConditions(entityId);
        termsApi.enqueue(new Callback<HelpData>() {
            @Override
            public void onResponse(Call<HelpData> call, Response<HelpData> response) {

                HelpData helpData = response.body();
                String content    = helpData.content;
                int status        = helpData.status;
                String message    = helpData.message;
                if(status == 1) {
                    sweetAlertDialog.dismiss();
                    Spanned htmlText = Html.fromHtml(content);
                    MyUtils.l(TAG, "htmlText : " + htmlText);

                    View view = LayoutInflater.from(MakePaymentActivity.this).inflate(R.layout.terms_content, null);
                    AlertDialog.Builder builder = new AlertDialog.Builder(MakePaymentActivity.this);

                    builder.setView(view);

//                SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(MakePaymentActivity.this);

//                sweetAlertDialog.setContentView(view);
                    TextView tv_tnc = (TextView) view.findViewById(R.id.tv_tnc);
                    Button btn_ok = (Button) view.findViewById(R.id.btn_ok);
                    tv_tnc.setText(htmlText.toString());

                    final AlertDialog alertDialog = builder.create();
                    alertDialog.show();

                    btn_ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.dismiss();
                        }
                    });
                }else {
                    sweetAlertDialog.changeAlertType(SweetAlertDialog.WARNING_TYPE);
                    sweetAlertDialog.setTitleText(getString(R.string.oops));
                    sweetAlertDialog.setContentText(message);
                    sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                        }
                    });
                }

            }

            @Override
            public void onFailure(Call<HelpData> call, Throwable t) {
                sweetAlertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                sweetAlertDialog.setTitleText("Error");
                sweetAlertDialog.setContentText(getString(R.string.went_wrong));
                sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                    }
                });

            }
        });
    }

    private void initControls() {

        from = getIntent().getStringExtra("from");
        packg_name = getIntent().getStringExtra("packg_name");
        packg_rate = getIntent().getIntExtra("packg_rate",0);
        packg_id = getIntent().getIntExtra("packg_id",0);
        currency = getIntent().getStringExtra("currency");

        cv_pckgupdate = (CardView)findViewById(R.id.cv_internet);
        tv_pckg_name = (TextView) findViewById(R.id.tv_pckg_name);
        tv_rupees = (TextView) findViewById(R.id.tv_rupees);
        tv_terms = (TextView) findViewById(R.id.tv_terms);
        pay_view = (View) findViewById(R.id.pay_view);
        ll_pickup = (LinearLayout) findViewById(R.id.ll_pickup);
        chk_terms = (CheckBox) findViewById(R.id.chk_terms);

        radioImm = (RadioButton) findViewById(R.id.radioImm);
        radioRenew = (RadioButton) findViewById(R.id.radioRenew);

        if(packg_name != null)
            tv_pckg_name.setText(packg_name);
        else
            tv_pckg_name.setText("");

//        if(packg_rate != null)
//            tv_rupees.setText(packg_rate+" "+currency);
//        else
//            tv_rupees.setText("");

        if("renew".equalsIgnoreCase(from)){
            cv_pckgupdate.setVisibility(View.GONE);
        }else if("change".equalsIgnoreCase(from)){
            cv_pckgupdate.setVisibility(View.VISIBLE);
        }

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbarLayout.setTitle(getString(R.string.title_activity_make_payment));
                    toolbar.setBackground(getDrawable(R.drawable.bg));
                    isShow = true;
                } else if(isShow) {
                    collapsingToolbarLayout.setTitle(" ");
//                    collapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(android.R.color.transparent));
                    toolbar.setTitle(" ");
                    toolbar.setBackground(getDrawable(android.R.color.transparent));//carefull there should a space between double quote otherwise it wont work
                    isShow = false;
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }


    private void checkforDues(int entityId, int subscriberId) {

        sweetAlertDialog = new SweetAlertDialog(MakePaymentActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        sweetAlertDialog.setTitleText("");
        sweetAlertDialog.setContentText(getString(R.string.please_wait));
        sweetAlertDialog.setCancelable(false);
        sweetAlertDialog.show();

        Call<DuesData> duesAPI = apIs.getDuesData(entityId,subscriberId);
//        Call<DuesData> duesAPI = apIs.getDuesData(entityId,subscriberId);
        duesAPI.enqueue(new Callback<DuesData>() {
            @Override
            public void onResponse(Call<DuesData> call, Response<DuesData> response) {

                DuesData duesResponse = response.body();
                int status = duesResponse.status;
                String msg = duesResponse.msg;
                List<Due> dueList = duesResponse.dues;
                sweetAlertDialog.dismiss();
                if(dueList.size() > 0){
                    pay_view.setVisibility(View.VISIBLE);
                    ll_pickup.setVisibility(View.VISIBLE);

                    due_id = dueList.get(0).dueId;

                }else{
                    pay_view.setVisibility(View.GONE);
                    ll_pickup.setVisibility(View.GONE);
                }

            }

            @Override
            public void onFailure(Call<DuesData> call, Throwable t) {
                MyUtils.l(TAG,"error : "+t.toString());
                 sweetAlertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                    sweetAlertDialog.setTitleText(getString(R.string.oops));
                    sweetAlertDialog.setContentText("Webservice error");
                    sweetAlertDialog.setConfirmText("Ok");
                    sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                        }
                    });
            }
        });
    }

    String selected_date = "";
    boolean isSelected;

    public void selectPaymentMethod(View view){

        switch (view.getId()){
            case R.id.img_online:

                MyUtils.l(TAG,"renewal_mode : "+renewal_mode);
                if(radioRenew.isChecked()){
                    renewal_mode = 1;
                }

                if(radioImm.isChecked()){
                    renewal_mode = 2;
                }

                boolean isTermsChecked = chk_terms.isChecked();
                boolean isCancel = false;

                if(from.equalsIgnoreCase("change")) {

                    if(!isTermsChecked){
                        Toast.makeText(this, "Please accept Terms and Conditions.", Toast.LENGTH_SHORT).show();
                        isCancel = true;
                    }

                    if (renewal_mode == 0) {
                        Toast.makeText(this, "Please select update from option.", Toast.LENGTH_SHORT).show();
                        isCancel = true;
                    }

                    if(isCancel){

                    }else {
                        if (MyUtils.isOnline(MakePaymentActivity.this)) {
                            ws_OnlineRenew();
                        } else {
                            Toast.makeText(this, getString(R.string.internet_error_msg), Toast.LENGTH_SHORT).show();
                        }
                    }
                }else if(from.equalsIgnoreCase("renew")){
                    if(!isTermsChecked){
                        Toast.makeText(this, "Please accept Terms and Conditions.", Toast.LENGTH_SHORT).show();

                    }else {
                        if (MyUtils.isOnline(MakePaymentActivity.this)) {
                            ws_OnlineRenew();
                        } else {
                            Toast.makeText(this, getString(R.string.internet_error_msg), Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                break;

            case R.id.img_pickup:
                dialog = new Dialog(MakePaymentActivity.this);
                View view1 = LayoutInflater.from(MakePaymentActivity.this).inflate(R.layout.pickup_layout,null);
                dialog.setContentView(view1);
                btn_sumit = (Button)view1.findViewById(R.id.btn_submit);
                met_comment = (EditText)view1.findViewById(R.id.met_comment);
                amPicker = (Picker)view1.findViewById(R.id.amPicker);
                calendarView = (MaterialCalendarView)view1.findViewById(R.id.calendarView);

                calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
                    @Override
                    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {

                        isSelected = selected;

                        if(selected){
                            int day = date.getDay();
                            int month = date.getMonth()+1;
                            int year = date.getYear();

                            selected_date  = day+"/"+month+"/"+year;
                            Toast.makeText(MakePaymentActivity.this,selected_date , Toast.LENGTH_SHORT).show();
                        }else {
//                            Toast.makeText(MakePaymentActivity.this, "please select date", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                Window window = dialog.getWindow();
                window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                btn_sumit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        validatePickup();
                    }
                });

                dialog.show();
                break;

            default:
                break;
        }
    }

    private void validatePickup() {
        int hrs;
        String minute = Integer.toString(amPicker.getCurrentMin());
        if (amPicker.getCurrentMin() < 10) {
            minute = "0" + minute;
        }
        if(amPicker.getAmPM().equals("PM")){
            hrs = amPicker.getCurrentHour() - 12;
        }else {
            hrs = amPicker.getCurrentHour();
        }

        String selected_time = hrs + ":" + minute+" "+amPicker.getAmPM();
        String dt_time = selected_date+" "+selected_time;

        String comment = met_comment.getText().toString();
        MyUtils.l(TAG,"dueid : "+due_id);

        boolean isCancel = false;
        String date = "";

        if(!isSelected){
            Toast.makeText(this, "Please select date", Toast.LENGTH_SHORT).show();
            isCancel = true;
        }else{

            if(!isOutDated(selected_date)) {

                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
                Date newDate = null;
                try {
                    newDate = format.parse(dt_time);
                    Date currentTime = Calendar.getInstance().getTime();
                    MyUtils.l(TAG, "currentTime : " + currentTime);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
                date = format.format(newDate);

                MyUtils.l(TAG, "date : " + date);

            }else {
                isCancel = true;
                Toast.makeText(this, "Please select future date", Toast.LENGTH_SHORT).show();
            }
        }

        if(TextUtils.isEmpty(comment)){
            Toast.makeText(this, "Please write comment", Toast.LENGTH_SHORT).show();
            isCancel = true;
        }

        if(!isCancel){
            ws_launchRequest(date,comment);
        }

    }

    private boolean isOutDated(String selected_date){
        boolean retVal = false;

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date strDate = sdf.parse(selected_date);
            if (new Date().after(strDate)) {
                retVal = true;
            } else {
                retVal = false;
            }
        }catch (Exception e){
            retVal = false;
            e.printStackTrace();
        }

        return retVal;
    }

    private void ws_launchRequest(String date, String comment) {

        sweetAlertDialog = new SweetAlertDialog(MakePaymentActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        sweetAlertDialog.setCancelable(false);
        sweetAlertDialog.setTitleText("");
        sweetAlertDialog.setContentText(getString(R.string.please_wait));
        sweetAlertDialog.show();

        Call<ResponseData> launchAPI = apIs.launchPickupRequest(due_id,areaId,comment,entityId,date,subscriberId,0);
        launchAPI.enqueue(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {

                ResponseData responseData = response.body();
                int status = responseData.status;
                String msg = responseData.message;

                if(status == 1){
                    sweetAlertDialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                    sweetAlertDialog.setCancelable(false);
                    sweetAlertDialog.setTitleText("Thank You");
                    sweetAlertDialog.setContentText(msg);
                    sweetAlertDialog.setConfirmText("Ok");
                    sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                            dialog.dismiss();
                        }
                    });

                }else {
                    sweetAlertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                    sweetAlertDialog.setCancelable(false);
                    sweetAlertDialog.setTitleText("Error");
                    sweetAlertDialog.setContentText(msg);
                    sweetAlertDialog.setConfirmText("Ok");
                    sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                            dialog.dismiss();
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {

            }
        });
    }

    private void ws_OnlineRenew() {

       /* RequestData requestData = new RequestData();
        requestData.setAmount(packg_rate);
        requestData.setCompanyId(entityId);
        requestData.setCreatedBy(subscriberId);
        requestData.setCreatedOn("");
        requestData.setUserId(subscriberId);
        requestData.setPackageId(packg_id);
        requestData.setCurrency(currency);
        requestData.setRenewalMode(renewal_mode);*/

        Call<OnlineRenewResponse> olAPI = apIs.onlineRenew(subscriberId, entityId, packg_id, renewal_mode, packg_rate, currency, " ",subscriberId);
        olAPI.enqueue(new Callback<OnlineRenewResponse>() {
            @Override
            public void onResponse(Call<OnlineRenewResponse> call, Response<OnlineRenewResponse> response) {
                MyUtils.l(TAG,"response : "+response);
                OnlineRenewResponse renewResponse = response.body();
                int status = renewResponse.status;
                String pay_id = renewResponse.payId;

                MyUtils.l(TAG,"pay_id : "+pay_id);

                Intent intent = new Intent(MakePaymentActivity.this,WebOnlinePaymentActivity.class);
                intent.putExtra("payId",pay_id);
                startActivity(intent);

            }

            @Override
            public void onFailure(Call<OnlineRenewResponse> call, Throwable t) {

            }
        });
    }
}
