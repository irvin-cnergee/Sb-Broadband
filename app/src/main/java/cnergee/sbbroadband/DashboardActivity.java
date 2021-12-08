package cnergee.sbbroadband;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import cn.pedant.SweetAlert.SweetAlertDialog;
import cnergee.sbbroadband.fragments.HelpFragment;
import cnergee.sbbroadband.fragments.HomeFragment;
import cnergee.sbbroadband.fragments.MyTransactionFragment;
import cnergee.sbbroadband.fragments.PackageHistoryFragment;
import cnergee.sbbroadband.fragments.ProfileFragment;
import cnergee.sbbroadband.fragments.ServiceFragment;
import cnergee.sbbroadband.retroObj.ActiveFlagData;
import cnergee.sbbroadband.utils.MyUtils;
import cnergee.sbbroadband.widgets.CustomTypefaceSpan;
import developer.app.interface_.AllAPIs;
import developer.app.obj.CommonWebResponse;
import developer.app.webservices.APIClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    private static final String TAG = "DashboardActivity";
    String status = "";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    TextView tv_username,tv_title;
    Toolbar toolbar;
    String appVersion = "";
    int isMandatory;
    String entityName = "";
    ImageView iv_logo;
    String ip ="";

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        appVersion = getIntent().getStringExtra("appVersion");
        isMandatory = getIntent().getIntExtra("isMandatory",0);

        MyUtils.getPhoneInfo(DashboardActivity.this);

        MyUtils.l(TAG,"appVersion : "+appVersion);

        if(!TextUtils.isEmpty(appVersion) && !appVersion.equals(null)) {

            if (!appVersion.equalsIgnoreCase(MyUtils.app_version)) {
                updateDialog();
            } else {

            }
        }

        sharedPreferences = (SharedPreferences) getSharedPreferences(MyUtils.MY_PREF,MODE_PRIVATE);
        int entityId = sharedPreferences.getInt(MyUtils.ENTITY_ID,0);
        int subscriberId = sharedPreferences.getInt(MyUtils.SUBSCRIBER_ID,0);
        entityName = sharedPreferences.getString(MyUtils.ENTITY_NAME,"");
        ip = sharedPreferences.getString(MyUtils.IP_ADDRESS,"");

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tv_title = (TextView) toolbar.findViewById(R.id.tv_title);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(false);
        toggle.setDrawerIndicatorEnabled(false);
        toggle.setHomeAsUpIndicator(R.drawable.menu);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);

//        tv_username = (TextView)headerView.findViewById(R.id.tv_username);
        iv_logo = (ImageView) headerView.findViewById(R.id.iv_logo);

        Menu m = navigationView.getMenu();
        for (int i=0;i<m.size();i++) {
            MenuItem mi = m.getItem(i);

            //for aapplying a font to subMenu ...
            SubMenu subMenu = mi.getSubMenu();
            if (subMenu!=null && subMenu.size() >0 ) {
                for (int j=0; j <subMenu.size();j++) {
                    MenuItem subMenuItem = subMenu.getItem(j);
                    applyFontToMenuItem(subMenuItem);
                }
            }
            applyFontToMenuItem(mi);
        }

        Fragment fragment = new HomeFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

        if(MyUtils.isOnline(DashboardActivity.this)){
            checkActivityFlag(entityId,subscriberId, MyUtils.phone_unique_id);
        }else{
            Toast.makeText(this, getString(R.string.internet_error_msg), Toast.LENGTH_SHORT).show();
        }

        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });
    }

    private void checkActivityFlag(int entityId, int subscriberId, String phone_unique_id) {

        final SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(DashboardActivity.this);

        AllAPIs allAPIs = APIClient.getClient(DashboardActivity.this).create(AllAPIs.class);
        Call<ActiveFlagData> activeApi = allAPIs.getActiveFlagResponse(entityId,subscriberId,phone_unique_id);
        activeApi.enqueue(new Callback<ActiveFlagData>() {
            @Override
            public void onResponse(Call<ActiveFlagData> call, Response<ActiveFlagData> response) {
                ActiveFlagData data = response.body();
                int status = data.status;
                String msg = data.msg;
                int flag = data.activeFlag;

                if(status == 1){
                    if(flag == 0){
                        sweetAlertDialog.setTitleText(getString(R.string.oops));
                        sweetAlertDialog.setContentText(getString(R.string.active_flag_msg));
                        sweetAlertDialog.setCancelable(false);
                        sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                                editor = sharedPreferences.edit();
                                editor.putInt(MyUtils.APP_REG,0);
                                editor.putString(MyUtils.USER_NAME, "");
                                editor.putString(MyUtils.CLIENT_ACCESS_ID, "");
//                                editor.putInt(MyUtils.ENTITY_ID, 0);
                                editor.putInt(MyUtils.SUBSCRIBER_ID, 0);
                                editor.putString(MyUtils.SUBSCRIBER_FULL_NAME, "");
                                editor.putInt(MyUtils.AREA_ID, 0);

                                editor.commit();

                                finish();

                            }
                        });
                        sweetAlertDialog.show();
                    }
                }else {

                }


            }

            @Override
            public void onFailure(Call<ActiveFlagData> call, Throwable t) {

            }
        });

    }

    private void updateDialog() {
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(DashboardActivity.this, SweetAlertDialog.WARNING_TYPE);
        sweetAlertDialog.setTitleText(getString(R.string.update_title));
        sweetAlertDialog.setCancelable(false);
        sweetAlertDialog.setContentText("New Version of "+entityName+" MyApp is now Available. \n\nPlease Visit Play Store to Update!");
        if(isMandatory == 1){
            sweetAlertDialog.setConfirmText(getString(R.string.update_btn));
        }else if(isMandatory == 0){
            sweetAlertDialog.setCancelText(getString(R.string.cancel_btn));
            sweetAlertDialog.setConfirmText(getString(R.string.update_btn));
        }
        sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                final String appPackageName = getPackageName();
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.playstore_url) + appPackageName)));
//                sweetAlertDialog.dismiss();
            }
        });

        sweetAlertDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.dismiss();
            }
        });
        sweetAlertDialog.show();
    }

    /*public void updateUsername(String name){
        if(!TextUtils.isEmpty(name)) {
            tv_username.setText(name);
        }else {
            tv_username.setText("");
        }
    }*/

    public void updateLogo(String logo){
        MyUtils.l(TAG,"logo : "+logo);
        if(!TextUtils.isEmpty(logo)){
            String path = getString(R.string.ip)+"/assets/images/entity-logo/"+logo;
            MyUtils.l(TAG,"path : "+path);
//            Picasso.with(DashboardActivity.this).load(path).into(iv_logo);
        }
    }

    public void updateTitle(String title){
        if(!TextUtils.isEmpty(title) && title != null){
            tv_title.setText(title);
        }else {
            tv_title.setText("");
        }
    }

    private void applyFontToMenuItem(MenuItem mi) {
        Typeface font = Typeface.createFromAsset(this.getAssets(),"fonts/"+ this.getResources().getString(R.string.font_name));
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("" , font), 0 , mNewTitle.length(),  Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {

            editor = sharedPreferences.edit();
            editor.putInt(MyUtils.APP_REG,0);
            editor.putString(MyUtils.USER_NAME, "");
            editor.putString(MyUtils.CLIENT_ACCESS_ID, "");
//            editor.putInt(MyUtils.ENTITY_ID, 0);
            editor.putInt(MyUtils.SUBSCRIBER_ID, 0);
            editor.putString(MyUtils.SUBSCRIBER_FULL_NAME, "");
            editor.putInt(MyUtils.AREA_ID, 0);

            /*internet usage*/

            editor.putLong(MyUtils.TOTAL_DATA,0);
            editor.putLong(MyUtils.AVAILABLE_DATA,0);
            editor.putLong(MyUtils.USED_DATA,0);
            editor.putString(MyUtils.EXPIRATION_DATE,"");
            editor.putInt(MyUtils.REMAINING_DAYS,0);
            editor.putInt(MyUtils.TOTAL_DAYS,0);
            editor.putInt(MyUtils.IS_EXPIRED,0);

            /*subscriber_pckg_details*/
            editor.putString(MyUtils.SUBSCRIBER_FULL_NAME, "");
            editor.putInt(MyUtils.AREA_ID, 0);
            editor.putString(MyUtils.AUTH_TYPE, "");
            editor.putString(MyUtils.PACKAGE_NAME, "");
            editor.putString(MyUtils.FROM_DATE, "");
            editor.putString(MyUtils.TO_DATE, "");
            editor.putInt(MyUtils.PCKG_AMOUNT, 0);
            editor.putString(MyUtils.CURRENCY, "");
            editor.putInt(MyUtils.PACKAGE_ID, 0);
            editor.putInt(MyUtils.IS_CURRENT, 0);
            editor.putInt(MyUtils.PAYMENT_GATEWAY, 0);
            editor.putString(MyUtils.CONNECTION_TYPE, "");
            editor.putInt(MyUtils.IS_ONLINE_RENEW_ALLOWED, 0);
            editor.putString(MyUtils.ENTITY_LOGO, "");
            editor.putInt(MyUtils.ALLOW_CHANGE_PCKG, 0);
            editor.putString(MyUtils.IS_PCKG_AVAILABLE, "");
            editor.putInt(MyUtils.RENEWAL, 0);

            editor.commit();

            finish();

            return true;
        }else if(id == R.id.action_notification){

            Intent intent = new Intent(DashboardActivity.this,NotificationActivity.class);
            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        Fragment fragment = null;

        int id = item.getItemId();

        if (id == R.id.nav_home) {

            fragment = new HomeFragment();

        } else if (id == R.id.nav_profile) {

            fragment = new ProfileFragment();

        } else if (id == R.id.nav_pckage) {

            fragment = new PackageHistoryFragment();

        } else if (id == R.id.nav_transaction) {

            fragment = new MyTransactionFragment();

        } else if (id == R.id.nav_service) {

            fragment = new ServiceFragment();

        } else if (id == R.id.nav_help) {

            fragment = new HelpFragment();

        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

        } else {
            Log.e("MainActivity", "Error in creating fragment");
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void return_object(CommonWebResponse commonWebResponse, Context context, String method_name) {
        super.return_object(commonWebResponse, context, method_name);
//        fragment = new ProfileFragment(commonWebResponse.getProceed_status());

//        fragmentP.passData(commonWebResponse,context,method_name);
    }
}
