package cnergee.sbbroadband.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.carlosmuvi.segmentedprogressbar.SegmentedProgressBar;
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.pedant.SweetAlert.SweetAlertDialog;
import cnergee.sbbroadband.BasicFragment;
import cnergee.sbbroadband.DashboardActivity;
import cnergee.sbbroadband.MakePaymentActivity;
import cnergee.sbbroadband.R;
import cnergee.sbbroadband.adapters.ISPPackgListAdapter;
import cnergee.sbbroadband.adapters.RenewHistoryAdapter;
import cnergee.sbbroadband.adapters.SlidingImage_Adapter;
import cnergee.sbbroadband.adapters.TransactionHistoryAdapter;
import cnergee.sbbroadband.obj.PckgList;
import cnergee.sbbroadband.obj.RenewHostoryObj;
import cnergee.sbbroadband.obj.UsageData;
import cnergee.sbbroadband.retroObj.PackageList;
import cnergee.sbbroadband.retroObj.SubscriberPckDatum;
import cnergee.sbbroadband.retroObj.TransHistory;
import cnergee.sbbroadband.retroObj.TransactionData;
import cnergee.sbbroadband.utils.MyUtils;
import cnergee.sbbroadband.widgets.InternalStorage;
import developer.app.interface_.AllAPIs;
import developer.app.loadingview.ProgressWheel;
import developer.app.webservices.APIClient;
import developer.app.webservices.CommonAsyncTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Siddhesh on 5/25/20_cable7.
 */

public class HomeFragment extends BasicFragment implements View.OnClickListener{

    private static final String TAG = "HomeFragment";

    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private static final Integer[] IMAGES= {R.drawable.banner, R.drawable.ad1, R.drawable.ad2};
    private ArrayList<Integer> ImagesArray = new ArrayList<Integer>();
    ProgressBar progress;
    private SegmentedProgressBar segmentedProgressBar;
    int totalDays = 0 , remainingDays = 0;
    int totalSegment = 50;
    TextView tv_remaining;
    RecyclerView rv_renew_history;
    ArrayList<RenewHostoryObj> history_list;
    ArrayList<PckgList> pckg_list;
    CirclePageIndicator indicator;
    RenewHistoryAdapter historyAdapter = null;
    TransactionHistoryAdapter transAdapter = null;
    ISPPackgListAdapter adapter = null;
    TextView btn_int_cng_pckg,btn_int_renew,btn_cbl_chng_pck,btn_cbl_renew;
    TextView tv_isp_pckg_name,tv_isp_rate,tv_isp_validity,tv_isp_exp_date;
    TextView tv_cbl_pck_name,tv_cbl_pck_rate,tv_cbl_validity,tv_cbl_exp_date;
    CommonAsyncTask getPackageTask;
    CommonAsyncTask getUsageTask;
    TextView tv_total_data,tv_used_data,tv_history;
    AllAPIs api;
    ProgressWheel progressBar;

    RelativeLayout rl_progress,rl_transaction;
    CardView cv_internet,cv_cable,cv_progress;

    int entityId = 0;
    int subscriberId = 0;
    int areaId = 0,cable_conType = 0;
    int isp_pId = 0;
    int cable_pId = 0;
    String isp_conType = "";
    int packagId,total,cb_packagId,cb_total;
    String packageName = "", currency="", cb_packageName="", cb_currency= "";

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String logo = "";
    int renewal;
    LinearLayout ll_isp_renew;
    LinearLayout ll_isp_msg;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.content_home,container,false);
//        setBanner();
        ((DashboardActivity)getActivity()).updateTitle("");

        sharedPreferences = (SharedPreferences) getActivity().getSharedPreferences(MyUtils.MY_PREF,MODE_PRIVATE);
        editor = sharedPreferences.edit();
        entityId = sharedPreferences.getInt(MyUtils.ENTITY_ID,0);
        subscriberId = sharedPreferences.getInt(MyUtils.SUBSCRIBER_ID,0);

        api = APIClient.getClient(getActivity()).create(AllAPIs.class);
        mPager = (ViewPager)view.findViewById(R.id.pager);
        progress = (ProgressBar) view.findViewById(R.id.circle_progress_bar);

        tv_remaining = (TextView) view.findViewById(R.id.tv_remaining);
        ScrollView home_scroll = (ScrollView) view.findViewById(R.id.home_scroll);
//        home_scroll.fullScroll(View.FOCUS_UP);
        home_scroll.scrollTo(0, 100);

        btn_int_cng_pckg = (TextView) view.findViewById(R.id.btn_int_cng_pckg);
        btn_int_renew = (TextView) view.findViewById(R.id.btn_int_renew);
        btn_cbl_chng_pck = (TextView) view.findViewById(R.id.btn_cbl_chng_pck);
        btn_cbl_renew = (TextView) view.findViewById(R.id.btn_cbl_renew);
        tv_used_data = (TextView) view.findViewById(R.id.tv_used_data);
        tv_total_data = (TextView) view.findViewById(R.id.tv_total_data);
        tv_cbl_pck_name = (TextView) view.findViewById(R.id.tv_cbl_pck_name);
        tv_cbl_pck_rate = (TextView) view.findViewById(R.id.tv_cbl_pck_rate);
        tv_cbl_validity = (TextView) view.findViewById(R.id.tv_cbl_validity);
        tv_cbl_exp_date = (TextView) view.findViewById(R.id.tv_cbl_exp_date);
        tv_isp_exp_date = (TextView) view.findViewById(R.id.tv_isp_exp_date);
        tv_isp_validity = (TextView) view.findViewById(R.id.tv_isp_validity);
        tv_isp_rate = (TextView) view.findViewById(R.id.tv_isp_rate);
        tv_isp_pckg_name = (TextView) view.findViewById(R.id.tv_isp_pckg_name);
        tv_history = (TextView) view.findViewById(R.id.tv_history);
        rl_progress = (RelativeLayout) view.findViewById(R.id.rl_progress);
        rl_transaction = (RelativeLayout) view.findViewById(R.id.rv_transaction);
        ll_isp_renew = (LinearLayout) view.findViewById(R.id.ll_isp_renew);
        ll_isp_msg = (LinearLayout) view.findViewById(R.id.ll_isp_msg);
        cv_internet = (CardView) view.findViewById(R.id.cv_internet);
        cv_progress = (CardView) view.findViewById(R.id.cv_progress);
        cv_cable = (CardView) view.findViewById(R.id.cv_cable);
        progressBar = (ProgressWheel) view.findViewById(R.id.progressBar);

        indicator = (CirclePageIndicator) view.findViewById(R.id.indicator);

        segmentedProgressBar = (SegmentedProgressBar) view.findViewById(R.id.segmented_progressbar);

        rv_renew_history = (RecyclerView)view.findViewById(R.id.rv_renew);

        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        rv_renew_history.setLayoutManager(manager);
        rv_renew_history.setHasFixedSize(true);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rv_renew_history.getContext(),
                manager.getOrientation());
        rv_renew_history.addItemDecoration(dividerItemDecoration);

        progressBar.setVisibility(View.VISIBLE);

        if(MyUtils.isOnline(getActivity())){
            ws_getUsage(subscriberId);
            ws_getSubscriberPackages();
            ws_getTransactions();
        }else {
            getOfflineUsageDetails();
            getOfflinePackageDetails();
            getOfflineTransactions();
            Toast.makeText(getActivity(), getString(R.string.internet_error_msg), Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    private void ws_getSubscriberPackages() {

        Call<SubscriberPckDatum> pckgApi = api.getSubscriberPackages(subscriberId,entityId);
        pckgApi.enqueue(new Callback<SubscriberPckDatum>() {
            @Override
            public void onResponse(Call<SubscriberPckDatum> call, Response<SubscriberPckDatum> response) {

                cv_internet.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                if(response.isSuccessful()) {

                    int entityid = 0;
                    int subscriberid = 0;
                    int payment_gateway = 0;
                    int isCurrent = 0;
                    int isOnlineAllowed = 0;
                    int allow_change_package = 0;

                    try {

                        SubscriberPckDatum pckDatum = response.body();

                        MyUtils.l(TAG,"pckDatum : "+pckDatum.isPckgAvailable);

                        if(pckDatum.isPckgAvailable != null) {

                            ll_isp_renew.setVisibility(View.VISIBLE);
                            ll_isp_msg.setVisibility(View.GONE);

                            if (pckDatum.subscriberId != null) {
                                subscriberid = pckDatum.subscriberId;
                            }
                            String subscriberUsername = pckDatum.subscriberName;
                            String firstName = pckDatum.firstName;
                            String lastName = pckDatum.lastName;
                            logo = pckDatum.entityLogo;

                            if (pckDatum.areaId != null) {
                                areaId = pckDatum.areaId;
                            }

                            if (pckDatum.entityId != null) {
                                entityid = pckDatum.entityId;
                            }

                            String authType = pckDatum.authType;

                            if (pckDatum.isCurrent != null) {
                                isCurrent = pckDatum.isCurrent;
                            }

                            if (pckDatum.packageId != null) {
                                packagId = pckDatum.packageId;
                            }

                            if (pckDatum.renewal != null) {
                                renewal = pckDatum.renewal;
                            }

                            String fromDate = pckDatum.validFrom;
                            String toDate = pckDatum.validTo;
                            if (pckDatum.payment_gateway != null) {
                                payment_gateway = pckDatum.payment_gateway;
                            }

                            if (pckDatum.isOnlineRenew != null) {
                                isOnlineAllowed = pckDatum.isOnlineRenew;
                            }

                            if (pckDatum.allow_change_package != null) {
                                allow_change_package = pckDatum.allow_change_package;
                            }

                            if (isOnlineAllowed != 1) {
                                btn_int_cng_pckg.setVisibility(View.GONE);
                                btn_int_renew.setVisibility(View.GONE);
                            } else {

                                if (allow_change_package == 1) {
                                    btn_int_cng_pckg.setVisibility(View.VISIBLE);

                                } else if (allow_change_package == 0) {
                                    btn_int_cng_pckg.setVisibility(View.GONE);
                                }

                                btn_int_renew.setVisibility(View.VISIBLE);
                            }


                            if (pckDatum.total != null) {
                                total = pckDatum.total;
                            }

                            if (pckDatum.currency != null) {
                                currency = pckDatum.currency;
                            }
                            packageName = pckDatum.packageName;
                            isp_conType = pckDatum.connectionType;
                            ((DashboardActivity) getActivity()).updateLogo(logo);
//               ((DashboardActivity)getActivity()).updateUsername(firstName+" "+lastName);

                            tv_isp_exp_date.setText(toDate);
                            tv_isp_pckg_name.setText(packageName);
                            tv_isp_rate.setText(total + " " + currency);

                            if (toDate != "" && toDate != null) {
                                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
                                Date newDate = null;
                                try {
                                    newDate = format.parse(toDate);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                format = new SimpleDateFormat("dd MMM yyyy");
                                String date = format.format(newDate);
                                tv_isp_exp_date.setText(date);
                            }

                            editor.putString(MyUtils.SUBSCRIBER_FULL_NAME, firstName + " " + lastName);
                            editor.putInt(MyUtils.AREA_ID, areaId);
                            editor.putString(MyUtils.AUTH_TYPE, authType);
                            editor.putString(MyUtils.PACKAGE_NAME, packageName);
                            editor.putString(MyUtils.FROM_DATE, fromDate);
                            editor.putString(MyUtils.TO_DATE, toDate);
                            editor.putInt(MyUtils.PCKG_AMOUNT, total);
                            editor.putString(MyUtils.CURRENCY, currency);
                            editor.putInt(MyUtils.PACKAGE_ID, packagId);
                            editor.putInt(MyUtils.IS_CURRENT, isCurrent);
                            editor.putInt(MyUtils.PAYMENT_GATEWAY, payment_gateway);
                            editor.putString(MyUtils.CONNECTION_TYPE, isp_conType);
                            editor.putInt(MyUtils.IS_ONLINE_RENEW_ALLOWED, isOnlineAllowed);
                            editor.putString(MyUtils.ENTITY_LOGO, logo);
                            editor.putInt(MyUtils.ALLOW_CHANGE_PCKG, allow_change_package);
                            editor.putString(MyUtils.IS_PCKG_AVAILABLE, String.valueOf(pckDatum.isPckgAvailable));
                            editor.putInt(MyUtils.RENEWAL, renewal);
                            editor.commit();
                        }else {
                            ll_isp_renew.setVisibility(View.GONE);
                            ll_isp_msg.setVisibility(View.VISIBLE);

                        }
                    }catch (Exception e){
                        getOfflinePackageDetails();
                        e.printStackTrace();
                    }

                }

            }

            @Override
            public void onFailure(Call<SubscriberPckDatum> call, Throwable t) {
                getOfflinePackageDetails();
                Toast.makeText(getActivity(), "webservice error", Toast.LENGTH_SHORT).show();
            }
        });
    }

//    int remainingDays = 0;
    int isExpired = 0;
    private void ws_getUsage(int subscriberId) {

        final Call<UsageData> usageCall = api.getUsageData(subscriberId);
        usageCall.enqueue(new Callback<UsageData>() {
            @Override
            public void onResponse(Call<UsageData> call, Response<UsageData> response) {

                cv_progress.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
              if(response.isSuccessful()){
                  try {

                      UsageData usageData = response.body();
                      Log.d(TAG,"remainingDays : "+usageData.remainingDays);

                      if(usageData.remainingDays != null){
                          remainingDays = usageData.remainingDays;
                      }

                      long usedData = usageData.usedData;
                      long allotedData = usageData.allotedData;
                      long remainingData = usageData.remainingData;

                      if(usageData.isExpired != null) {
                         isExpired = usageData.isExpired;
                      }

                      if(usageData.totaldays != null) {
                          totalDays = usageData.totaldays;
                      }

//                if(remainingDays != null){
                    if(isExpired == 0) {

                        if (remainingDays > 0) {
                            tv_remaining.setText(usageData.remainingDays + " days till next renewal");
                        }else if(remainingDays == 0){
                            tv_remaining.setText("Your plan is going to expire today");

                        }

                    }else if(isExpired == 1){
                          tv_remaining.setText("Your plan is expired");
                      }

                      Log.d(TAG,"allotedData : "+usageData.allotedData);
                      Log.d(TAG,"remainingData : "+usageData.remainingData);
                      Log.d(TAG,"usedData : "+usedData);

                      String alloted = "";
                      String used = "";

                      DecimalFormat decimalFormat = new DecimalFormat("##.##");

                      if(allotedData >= 1024*1024*1024){
                          String val = decimalFormat.format((double) allotedData / (1024*1024*1024));
                          alloted = val +" GB";
                      }else if(allotedData >= 1024*1024){
                          String val = decimalFormat.format((double) allotedData / (1024*1024));
                          alloted = val + " MB";
                      }else if(allotedData >= 1024){
                          String val = decimalFormat.format((double) allotedData / (1024));
                          alloted = val + " KB";
                      }else{
                          alloted = allotedData+" B";
                      }

                      if(usedData > 1024*1024*1024){
                          String val = decimalFormat.format((double) usedData / (1024*1024*1024));
                          used = val+" GB";
                      }else if(usedData > 1024*1024){
                          String val = decimalFormat.format((double) usedData / (1024*1024));
                          used = val + " MB";
                      }else if(usedData > 1024){
                          String val = decimalFormat.format((double) usedData / (1024));
                          used = val +" KB";
                      }else{
                          used = usedData +" B";
                      }

                      MyUtils.l(TAG,"alloted : "+alloted);
                      MyUtils.l(TAG,"used : "+used);

                      tv_total_data.setText("of "+alloted);
                      tv_used_data.setText(used);

                      double progressVal = Math.round((double)(100*usedData)/allotedData);
                      progress.setProgress((int)progressVal);
                      segmentedProgressBar.setSegmentCount(totalSegment); // number of segments in your bar
                      segmentedProgressBar.setContainerColor(getResources().getColor(R.color.light_gray)); //empty segment color
                      segmentedProgressBar.setFillColor(getResources().getColor(R.color.bg_end)); //empty segment color

                      int filledCount = 0;
                      if(totalDays > 0){
                          filledCount = (int)((totalDays-remainingDays)*totalSegment)/totalDays;
                      }

                      if(filledCount > totalSegment) {
                          segmentedProgressBar.setCompletedSegments(totalSegment);
                      }else {
                          segmentedProgressBar.setCompletedSegments(filledCount);
                      }

                      editor.putLong(MyUtils.TOTAL_DATA,usageData.allotedData);
                      editor.putLong(MyUtils.AVAILABLE_DATA,usageData.remainingData);
                      editor.putLong(MyUtils.USED_DATA,usageData.usedData);
                      editor.putString(MyUtils.EXPIRATION_DATE,usageData.expiryDt);
                      editor.putInt(MyUtils.REMAINING_DAYS,usageData.remainingDays);
                      editor.putInt(MyUtils.TOTAL_DAYS,usageData.totaldays);
                      editor.putInt(MyUtils.IS_EXPIRED,usageData.isExpired);
                      editor.commit();
//                }
                  }catch (Exception e){
                      e.printStackTrace();
                      getOfflineUsageDetails();
                  }
              }

            }

            @Override
            public void onFailure(Call<UsageData> call, Throwable t) {
                getOfflineUsageDetails();
            }
        });


    }

    public void getOfflineUsageDetails(){

        try {

            cv_progress.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);

            remainingDays    = sharedPreferences.getInt(MyUtils.REMAINING_DAYS,0);
            totalDays        = sharedPreferences.getInt(MyUtils.TOTAL_DAYS,0);
            long allotedData     = sharedPreferences.getLong(MyUtils.TOTAL_DATA,0);
            long usedData        = sharedPreferences.getLong(MyUtils.USED_DATA,0);
            long remainingData   = sharedPreferences.getLong(MyUtils.AVAILABLE_DATA,0);
            String expirationDt  = sharedPreferences.getString(MyUtils.EXPIRATION_DATE,"");
            int isExpired =  sharedPreferences.getInt(MyUtils.IS_EXPIRED,0);

            Log.d(TAG,"remainingDays : "+remainingDays);

            if(isExpired == 0) {

                if (remainingDays > 0) {
                    tv_remaining.setText(remainingDays + " days till next renewal");
                }else if(remainingDays == 0){
                    tv_remaining.setText("Your plan is going to expire today");

                }

            }else if(isExpired == 1){
                tv_remaining.setText("Your plan has been expired");
            }

            Log.d(TAG,"allotedData : "+allotedData);
            Log.d(TAG,"remainingData : "+remainingData);
            Log.d(TAG,"usedData : "+usedData);

            String alloted = "";
            String used = "";

            DecimalFormat decimalFormat = new DecimalFormat("##.##");

            if(allotedData >= 1024*1024*1024){
                String val = decimalFormat.format((double) allotedData / (1000*1000*1000));
                alloted = val +" GB";
            }else if(allotedData >= 1024*1024){
                String val = decimalFormat.format((double) allotedData / (1000*1000));
                alloted = val + " MB";
            }else if(allotedData >= 1024){
                String val = decimalFormat.format((double) allotedData / (1000));
                alloted = val + " KB";
            }else{
                alloted = allotedData+" B";
            }

            if(usedData > 1024*1024*1024){
                String val = decimalFormat.format((double) usedData / (1000*1000*1000));
                used = val+" GB";
            }else if(usedData > 1024*1024){
                String val = decimalFormat.format((double) usedData / (1000*1000));
                used = val + " MB";
            }else if(usedData > 1024){
                String val = decimalFormat.format((double) usedData / (1000));
                used = val +" KB";
            }else{
                used = usedData +" B";
            }

            MyUtils.l(TAG,"alloted : "+alloted);
            MyUtils.l(TAG,"used : "+used);


            tv_total_data.setText("of "+alloted);
            tv_used_data.setText(used);

            double progressVal = Math.round((double)(100*usedData)/allotedData);

            progress.setProgress((int)progressVal );

            segmentedProgressBar.setSegmentCount(totalSegment); // number of segments in your bar
            segmentedProgressBar.setContainerColor(getResources().getColor(R.color.light_gray)); //empty segment color
            segmentedProgressBar.setFillColor(getResources().getColor(R.color.bg_end)); //empty segment color

            int filledCount = 0;
            if(totalDays > 0){
                filledCount = (int)((totalDays-remainingDays)*totalSegment)/totalDays;
            }

            if(filledCount > totalSegment) {
                segmentedProgressBar.setCompletedSegments(totalSegment);
            }else {
                segmentedProgressBar.setCompletedSegments(filledCount);
            }

//                }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void getOfflinePackageDetails(){

        try {

            cv_internet.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);

            int subscriberid = sharedPreferences.getInt(MyUtils.SUBSCRIBER_ID,0);
            String subscriberUsername = sharedPreferences.getString(MyUtils.USER_NAME,"");
            areaId = sharedPreferences.getInt(MyUtils.AREA_ID,0);;
            int entityId = sharedPreferences.getInt(MyUtils.ENTITY_ID,0);
            String authType = sharedPreferences.getString(MyUtils.AUTH_TYPE,"");
            int isCurrent = sharedPreferences.getInt(MyUtils.IS_CURRENT,0);
            packagId = sharedPreferences.getInt(MyUtils.PACKAGE_ID,0);
            String fromDate = sharedPreferences.getString(MyUtils.FROM_DATE,"");
            String toDate = sharedPreferences.getString(MyUtils.TO_DATE,"");
            int payment_gateway = sharedPreferences.getInt(MyUtils.PAYMENT_GATEWAY,0);
            total = sharedPreferences.getInt(MyUtils.PCKG_AMOUNT,0);
            isp_conType = sharedPreferences.getString(MyUtils.CONNECTION_TYPE,"");
            currency = sharedPreferences.getString(MyUtils.CURRENCY,"");
            packageName = sharedPreferences.getString(MyUtils.PACKAGE_NAME,"");
            int isOnlineAllowed = sharedPreferences.getInt(MyUtils.IS_ONLINE_RENEW_ALLOWED,0);
            int allow_change_package = sharedPreferences.getInt(MyUtils.ALLOW_CHANGE_PCKG,0);
            renewal = sharedPreferences.getInt(MyUtils.RENEWAL,1);
            logo = sharedPreferences.getString(MyUtils.ENTITY_LOGO,"");
            String isPckgAvailable= sharedPreferences.getString(MyUtils.IS_PCKG_AVAILABLE,"1");

            if(isPckgAvailable != null) {

                ll_isp_renew.setVisibility(View.VISIBLE);
                ll_isp_msg.setVisibility(View.GONE);
                if (isOnlineAllowed != 1) {
                    btn_int_cng_pckg.setVisibility(View.GONE);
                    btn_int_renew.setVisibility(View.GONE);
                } else {

                    if (allow_change_package == 1) {
                        btn_int_cng_pckg.setVisibility(View.VISIBLE);

                    } else if (allow_change_package == 0) {
                        btn_int_cng_pckg.setVisibility(View.GONE);
                    }

                    btn_int_renew.setVisibility(View.VISIBLE);
                }

                ((DashboardActivity) getActivity()).updateLogo(logo);
//               ((DashboardActivity)getActivity()).updateUsername(firstName+" "+lastName);

                tv_isp_exp_date.setText(toDate);
                tv_isp_pckg_name.setText(packageName);
                tv_isp_rate.setText(total + " " + currency);

                if (toDate != "" && toDate != null) {
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
                    Date newDate = null;
                    try {
                        newDate = format.parse(toDate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    format = new SimpleDateFormat("dd MMM yyyy");
                    String date = format.format(newDate);
                    tv_isp_exp_date.setText(date);
                }
            }else {
                ll_isp_renew.setVisibility(View.GONE);
                ll_isp_msg.setVisibility(View.VISIBLE);

            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void getOfflineTransactions(){

        TransactionData   history=null;
//        TransactionHistoryAdapter trans_adapter;
        try {
            rl_transaction.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
           history=(TransactionData)InternalStorage.readObject(getActivity(),"Transactions");
            Log.e("history.message",":"+history.message);
        }catch (Exception e){
          e.printStackTrace();
        }

        int status = history.status;
        String msg = history.message;
        List<TransHistory> transList = history.transHistory;

        if(status == 0){

            rl_transaction.setVisibility(View.VISIBLE);
            rv_renew_history.setVisibility(View.GONE);
            tv_history.setVisibility(View.VISIBLE);
            tv_history.setText(msg);
        }else {
            if(transList.size() > 0){
                rl_transaction.setVisibility(View.VISIBLE);
                rv_renew_history.setVisibility(View.VISIBLE);
                tv_history.setVisibility(View.GONE);
                try {
                    transAdapter = new TransactionHistoryAdapter(getActivity(),transList,"home");
                    rv_renew_history.setAdapter(transAdapter);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else{
                rl_transaction.setVisibility(View.VISIBLE);
                rv_renew_history.setVisibility(View.GONE);
                tv_history.setVisibility(View.VISIBLE);
                tv_history.setText(msg);
            }
        }

    }

   /* private void ws_getTransactions() {

       *//* rv_renew_history.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);*//*

        Call<TransactionHistory> transApi = api.getTransactioHistory(subscriberId,entityId);
        transApi.enqueue(new Callback<TransactionHistory>() {
            @Override
            public void onResponse(Call<TransactionHistory> call, Response<TransactionHistory> response) {

                *//*rv_renew_history.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);*//*

                TransactionHistory history = response.body();

                int status = history.status;
                String msg = history.msg;
                List<TransList> transList = history.transList;

                if(status == 0){
                    new SweetAlertDialog(getActivity(),SweetAlertDialog.ERROR_TYPE)
                            .setTitleText(getString(R.string.oops))
                            .setContentText(getString(R.string.went_wrong))
                            .setConfirmText("Ok")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                }
                            }).show();
                }else {
                    if(transList.size() > 0){
                        rv_renew_history.setVisibility(View.VISIBLE);
                        tv_history.setVisibility(View.GONE);
                        rv_transaction.setVisibility(View.VISIBLE);
                        RecyclerView.LayoutManager manager = new LinearLayoutManager(getActivity());
                        rv_renew_history.setLayoutManager(manager);
                        rv_renew_history.setItemAnimator(new DefaultItemAnimator());

                        try {
                            historyAdapter = new RenewHistoryAdapter(getActivity(),transList,"home");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        rv_renew_history.setAdapter(historyAdapter);

                    }else{
                       *//* new SweetAlertDialog(getActivity(),SweetAlertDialog.ERROR_TYPE)
                                .setTitleText(getString(R.string.oops))
                                .setContentText(getString(R.string.no_data))
                                .setConfirmText("Ok")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.dismiss();
                                    }
                                }).show();*//*

                        rv_renew_history.setVisibility(View.GONE);
                        tv_history.setVisibility(View.VISIBLE);
                        rv_transaction.setVisibility(View.VISIBLE);
                        tv_history.setText("No Renewal history");
                    }
                }
            }

            @Override
            public void onFailure(Call<TransactionHistory> call, Throwable t) {

            }
        });

    }*/

    private void ws_getTransactions() {

       /* rv_renew_history.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);*/

        Call<TransactionData> transApi = api.getTransactioHistory(subscriberId,entityId);
        transApi.enqueue(new Callback<TransactionData>() {
            @Override
            public void onResponse(Call<TransactionData> call, Response<TransactionData> response) {

               /* rv_renew_history.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);*/
                rl_transaction.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
               if(response.isSuccessful()){

                   TransactionData history = response.body();

                   int status = history.status;
                   String msg = history.message;
                   List<TransHistory> transList = history.transHistory;

                  /* Gson gson = new Gson();
                   String json = gson.toJson(history);
                   editor.putString("Transactions",json);
                   editor.commit();*/

                   try {
                       InternalStorage.writeObject(getActivity(),"Transactions",history);
                   } catch (IOException e) {
                       e.printStackTrace();
                   }

                   if(status == 0){
                      /* new SweetAlertDialog(getActivity(),SweetAlertDialog.NORMAL_TYPE)
                               .setTitleText(getString(R.string.oops))
                               .setContentText(getString(R.string.no_data))
                               .setConfirmText("Ok")
                               .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                   @Override
                                   public void onClick(SweetAlertDialog sweetAlertDialog) {
                                       sweetAlertDialog.dismiss();
                                   }
                               }).show();*/

                       rl_transaction.setVisibility(View.VISIBLE);
                       rv_renew_history.setVisibility(View.GONE);
                       tv_history.setVisibility(View.VISIBLE);
                       tv_history.setText(msg);
                   }else {
                       if(transList.size() > 0){

                           rl_transaction.setVisibility(View.VISIBLE);
                           rv_renew_history.setVisibility(View.VISIBLE);
                           tv_history.setVisibility(View.GONE);
//                        rv_renew_history.setItemAnimator(new DefaultItemAnimator());

                           try {
                               transAdapter = new TransactionHistoryAdapter(getActivity(),transList,"home");
                               rv_renew_history.setAdapter(transAdapter);
                           } catch (Exception e) {
                               e.printStackTrace();
                           }


                       }else{
                       /* new SweetAlertDialog(getActivity(),SweetAlertDialog.ERROR_TYPE)
                                .setTitleText(getString(R.string.oops))
                                .setContentText(getString(R.string.no_data))
                                .setConfirmText("Ok")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.dismiss();
                                    }
                                }).show();*/

                           rl_transaction.setVisibility(View.VISIBLE);
                           rv_renew_history.setVisibility(View.GONE);
                           tv_history.setVisibility(View.VISIBLE);
                           tv_history.setText(msg);
                       }
                   }
               }else {
                   getOfflineTransactions();
               }
            }

            @Override
            public void onFailure(Call<TransactionData> call, Throwable t) {

                getOfflineTransactions();

            }
        });

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setBanner();
//        progress.setProgress((int) 93);
//        tv_remaining.setText(remainingDays+" days till next renew");


       /* LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        rv_renew_history.setLayoutManager(manager);
        rv_renew_history.setHasFixedSize(true);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rv_renew_history.getContext(),
                manager.getOrientation());
        rv_renew_history.addItemDecoration(dividerItemDecoration);*/

        try {
//            historyAdapter = new RenewHistoryAdapter(getActivity(),getJson(),"dashboard");
        } catch (Exception e) {
            e.printStackTrace();
        }
        rv_renew_history.setAdapter(historyAdapter);

        btn_cbl_chng_pck.setOnClickListener(this);
        btn_cbl_renew.setOnClickListener(this);
        btn_int_cng_pckg.setOnClickListener(this);
        btn_int_renew.setOnClickListener(this);

    }

    public void setBanner() {

        for(int i=0;i<IMAGES.length;i++)
            ImagesArray.add(IMAGES[i]);

        mPager.setAdapter(new SlidingImage_Adapter(getActivity(),ImagesArray));

        indicator.setViewPager(mPager);

        final float density = getResources().getDisplayMetrics().density;

//        indicator.setRadius(5 * density);

        NUM_PAGES =IMAGES.length;

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 3000, 3000);

        // Pager listener over indicator
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;

            }

            @Override
            public void onPageScrolled(int pos, float arg_cable, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });

    }

    @Override
    public void onClick(View view) {

        int id = 0;
        Intent intent;

        switch (view.getId()){
            case R.id.btn_int_cng_pckg:


                    if (MyUtils.isOnline(getActivity())) {

//                    ws_getPackages(entityId,isp_conType,areaId,packagId);
                        ws_getPackages(subscriberId, packagId);
                    } else {
                        Toast.makeText(getActivity(), getString(R.string.internet_error_msg), Toast.LENGTH_SHORT).show();
                    }


                break;
            case R.id.btn_int_renew:
                if(renewal == 0){
                    SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(getActivity(),SweetAlertDialog.WARNING_TYPE);
                    sweetAlertDialog.setTitleText("Alert");
                    sweetAlertDialog.setContentText("This package is only for Creation.Please renew the plan from change package.");
                    sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                        }
                    }).show();
                }else {
                    intent = new Intent(getActivity(), MakePaymentActivity.class);
                    intent.putExtra("from", "renew");
                    intent.putExtra("packg_id", packagId);
                    intent.putExtra("packg_name", packageName);
                    intent.putExtra("packg_rate", total);
                    intent.putExtra("currency", currency);

                    startActivity(intent);
                }
                break;
            case R.id.btn_cbl_chng_pck:

//                ws_getPackages(entityId,cable_pId,cable_conType,areaId);

                break;
            case R.id.btn_cbl_renew:
                intent = new Intent(getActivity(),MakePaymentActivity.class);
                intent.putExtra("from","renew");
                intent.putExtra("packg_id",cb_packagId);
                intent.putExtra("packg_name",cb_packageName);
                intent.putExtra("packg_rate",cb_total);
                intent.putExtra("currency",cb_currency);

                startActivity(intent);

        }
    }

    private void ws_getPackages(int subscriberId, int packageId) {
        final SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(getActivity(),SweetAlertDialog.PROGRESS_TYPE);
        sweetAlertDialog.setTitleText(getString(R.string.please_wait));
        sweetAlertDialog.setContentText("");
        sweetAlertDialog.showCancelButton(false);
        sweetAlertDialog.setCancelClickListener(null);
        sweetAlertDialog.setConfirmClickListener(null);
        sweetAlertDialog.show();

        Call<List<PackageList>> pckgcall = api.getPackagesList(subscriberId,packageId);
        pckgcall.enqueue(new Callback<List<PackageList>>() {
            @Override
            public void onResponse(Call<List<PackageList>> call, Response<List<PackageList>> response) {

                sweetAlertDialog.dismiss();
               /* PckgListData listData = response.body();
                int status = listData.status;
                String msg = listData.msg;*/
                List<PackageList> pckList = response.body();

                /*if(status == 0 ){
                    sweetAlertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                    sweetAlertDialog.setTitleText(getString(R.string.oops));
                    sweetAlertDialog.setContentText(getString(R.string.went_wrong));
                    sweetAlertDialog.setConfirmText("Ok");
                    sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                        }
                    });
                    sweetAlertDialog.show();
                }else{*/

                    if(pckList.size() > 0) {

                        final Dialog dialog = new Dialog(getActivity());
                        dialog.setContentView(R.layout.pckg_list_layout);
                        RecyclerView rv_pckg_list = (RecyclerView) dialog.findViewById(R.id.rv_pckg_list);
                        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
                        rv_pckg_list.setLayoutManager(manager);
                        rv_pckg_list.setItemAnimator(new DefaultItemAnimator());

                        try {
                            adapter = new ISPPackgListAdapter(getActivity(), pckList);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        rv_pckg_list.setAdapter(adapter);

                        Window window = dialog.getWindow();
                        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                        dialog.show();
                    }else {
                        sweetAlertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                        sweetAlertDialog.setTitleText(getString(R.string.oops));
                        sweetAlertDialog.setContentText("No package available.");
                        sweetAlertDialog.setConfirmText("Ok");
                        sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                            }
                        });
                        sweetAlertDialog.show();

                    }

//                }

            }

            @Override
            public void onFailure(Call<List<PackageList>> call, Throwable t) {
                sweetAlertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                sweetAlertDialog.setTitleText(getString(R.string.oops));
                sweetAlertDialog.setContentText("Webservice error "+t.toString());
                sweetAlertDialog.setConfirmText("Ok");
                sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                    }
                });
                sweetAlertDialog.show();
            }
        });

    }

    private List<TransHistory> getTransData(){

        List<TransHistory> list = new ArrayList<>();

        String str = " [\n" +
                "        {\n" +
                "            \"transactionType\": \"CR\",\n" +
                "            \"amount\": 120,\n" +
                "            \"currency\": \"INR\",\n" +
                "            \"transactionDate\": \"2017-11-18T10:32:35.000Z\",\n" +
                "            \"paymentMode\": \"Cash\",\n" +
                "            \"transactionStatus\": \"success\"\n" +
                "        }\n" +
                "    ]";

        try {
            JSONArray jsonArray = new JSONArray(str);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                TransHistory history = new TransHistory();
                history.amount = jsonObject.optDouble("amount",0.0);
                history.transactionType = jsonObject.optString("transactionType","");
                history.currency = jsonObject.optString("currency","");
                history.transactionDate = jsonObject.optString("transactionDate","");
                history.paymentMode = jsonObject.optString("paymentMode","");
                history.transactionStatus = jsonObject.optString("transactionStatus","");

                list.add(history);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }



}
