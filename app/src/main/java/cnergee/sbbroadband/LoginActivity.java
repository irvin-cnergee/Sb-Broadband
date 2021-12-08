package cnergee.sbbroadband;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import cnergee.sbbroadband.adapters.LanguageAdapter;
import cnergee.sbbroadband.fragments.EmailFragment;
import cnergee.sbbroadband.fragments.MobileFragment;
import cnergee.sbbroadband.retroObj.Language;
import cnergee.sbbroadband.retroObj.LanguageDatum;
import cnergee.sbbroadband.utils.MyUtils;
import cnergee.sbbroadband.widgets.MyTextView;
import developer.app.interface_.AllAPIs;
import developer.app.webservices.APIClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends BaseActivity  {

    private static final String TAG = "LoginActivity";

    ViewPager viewPager;
    TabLayout tabLayout;
    Spinner spin_lang;
    String[] lang = new String[]{"English","Hindi","Spanish","Arabic","Italian", "Japnese"};
//    ArrayList<LanguageData> langs = new ArrayList<>();
    List<Language> langs ;
    String appVersion = "";
    int isMandatory;
    SharedPreferences sharedPreferences;
    String entityName = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initControls();

        sharedPreferences = getSharedPreferences(MyUtils.MY_PREF,MODE_PRIVATE);
        entityName = sharedPreferences.getString(MyUtils.ENTITY_NAME,"");

        appVersion = getIntent().getStringExtra("appVersion");
        isMandatory = getIntent().getIntExtra("isMandatory",0);

        MyUtils.getPhoneInfo(LoginActivity.this);

        MyUtils.l(TAG,"appVersion : "+appVersion);

        if(!TextUtils.isEmpty(appVersion) && !appVersion.equals(null)) {

            if (!appVersion.equalsIgnoreCase(MyUtils.app_version)) {
                updateDialog();
            } else {

            }
        }


       /* Bundle bundle = getIntent().getExtras();
        langs = getIntent().getParcelableExtra("lang");*/
//        langs = getIntent().getParcelableExtra("lang");
        /*Bundle bundle = getIntent().getExtras();
        langs = (List<Language>) bundle.getSerializable("lang");*/
       /* LanguageDatum datum = new LanguageDatum();
        MyUtils.l(TAG,"datum : "+datum.languages);*/


       /*if(MyUtils.isOnline(LoginActivity.this)){
           ws_getLanguages();
       }else{
           Toast.makeText(LoginActivity.this, getString(R.string.internet_error_msg), Toast.LENGTH_SHORT).show();
       }*/

        spin_lang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                ((MyTextView) adapterView.getChildAt(0)).setTextColor(Color.WHITE);
                Toast.makeText(getApplicationContext(),"langs "+ langs.get(position).id, Toast.LENGTH_LONG).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void updateDialog() {
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.WARNING_TYPE);
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

    private void ws_getLanguages() {

        langs  = new ArrayList<>();

        AllAPIs api = APIClient.getClient(LoginActivity.this).create(AllAPIs.class);

        Call<LanguageDatum> langApi = api.getLanguages("");
        langApi.enqueue(new Callback<LanguageDatum>() {
            @Override
            public void onResponse(Call<LanguageDatum> call, Response<LanguageDatum> response) {

                LanguageDatum datum = response.body();

                langs = datum.languages;

                LanguageAdapter adapter = new LanguageAdapter(LoginActivity.this,langs);
                spin_lang.setAdapter(adapter);
                spin_lang.getBackground().setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_ATOP);

            }

            @Override
            public void onFailure(Call<LanguageDatum> call, Throwable t) {

            }
        });


    }

    private void initControls() {

        viewPager = (ViewPager)findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout)findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        changeTabsFont();

        spin_lang = (Spinner)findViewById(R.id.spin_lang);

    }
    private void changeTabsFont() {

        ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);
        int tabsCount = vg.getChildCount();
        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            int tabChildsCount = vgTab.getChildCount();
            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    Typeface font = Typeface.createFromAsset(this.getAssets(),"fonts/"+ this.getResources().getString(R.string.font_name));
                    ((TextView) tabViewChild).setTypeface(font, Typeface.NORMAL);
                }
            }
        }
    }


    private void setupViewPager(ViewPager viewPager){

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new MobileFragment(),"Mobile Number");
        adapter.addFrag(new EmailFragment(),"Username");
        viewPager.setAdapter(adapter);
    }


    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

}
