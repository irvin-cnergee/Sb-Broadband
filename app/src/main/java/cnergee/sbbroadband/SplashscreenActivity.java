package cnergee.sbbroadband;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

import com.google.firebase.iid.FirebaseInstanceId;

import java.util.Arrays;

import cnergee.sbbroadband.retroObj.VersionResponse;
import cnergee.sbbroadband.utils.MyUtils;
import developer.app.interface_.AllAPIs;
import developer.app.webservices.APIClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashscreenActivity extends BaseActivity {

    private static final String TAG = "SplashscreenActivity";

    private static final String PERMISSIONS_REQUIRED[] = new String[]{
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.CALL_PHONE
    };
    private SplashscreenActivity mActivity;
    private static final int REQUEST_PERMISSIONS = 100;
    boolean isNxt = false;
    int isLogin ;
    AllAPIs allAPIs;
    int categoryId = 1;
    String appVersion = "";
    int isMandatory ;
    int isFirstTime = 1;
    int appReg ;
//    ArrayList<LanguageData> lang_list;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        mActivity = this;

        allAPIs = APIClient.getClient(SplashscreenActivity.this).create(AllAPIs.class);

        SharedPreferences sharedPreferences = getSharedPreferences(MyUtils.MY_PREF,MODE_PRIVATE);
//        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mActivity);
        editor = sharedPreferences.edit();

        appReg = sharedPreferences.getInt(MyUtils.APP_REG,0);
        isFirstTime = sharedPreferences.getInt(MyUtils.IS_FIRST_TIME,1);

        MyUtils.l(TAG,"appreg : "+ appReg+" isFirstTime : "+isFirstTime);

        checkPermissions();

    }

    private void getUpdate(int categoryId, final String app_version) {

        Call<VersionResponse> updateApi = allAPIs.checkUpdate(categoryId,app_version);
        updateApi.enqueue(new Callback<VersionResponse>() {
            @Override
            public void onResponse(Call<VersionResponse> call, Response<VersionResponse> response) {

               if(response.isSuccessful()){
                   VersionResponse verResponse = response.body();
                   if(verResponse.appVersion == null){
                       appVersion = "";
                   }else {
                       appVersion = verResponse.appVersion;
                   }
                   isMandatory = verResponse.isCompulsory;
                   startNxtActivity();
               }
            }

            @Override
            public void onFailure(Call<VersionResponse> call, Throwable t) {
                startNxtActivity();
            }
        });
    }

    public void startNxtActivity(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

//                if(isFirstTime == 0) {
                    if (appReg == 1) {
//                if(true){
                        Intent intent = new Intent(SplashscreenActivity.this, DashboardActivity.class);
                        intent.putExtra("appVersion", appVersion);
                        intent.putExtra("isMandatory", isMandatory);
                        startActivity(intent);
                        finish();
                    } else {
                        Intent intent = new Intent(SplashscreenActivity.this, LoginActivity.class);
                        intent.putExtra("appVersion", appVersion);
                        intent.putExtra("isMandatory", isMandatory);
                        startActivity(intent);
                        finish();
                    }
                /*}else {
                    Intent intent = new Intent(SplashscreenActivity.this, UniqueCodeActivity.class);
                    startActivity(intent);
                    finish();
                }*/

            }
        },3000);
    }

    private void checkPermissions() {
        boolean permissionsGranted = checkPermission(PERMISSIONS_REQUIRED);
        if (permissionsGranted) {
//            Toast.makeText(this, "You've granted all required permissions!", Toast.LENGTH_SHORT).show();
//            startNxtActivity();
            String token = FirebaseInstanceId.getInstance().getToken();

            MyUtils.l(TAG,"token : "+token);
            editor.putString(MyUtils.TOKEN,token);
            editor.commit();

            MyUtils.getPhoneInfo(SplashscreenActivity.this);
            if(MyUtils.isOnline(SplashscreenActivity.this) && (appReg == 1)){
                getUpdate(categoryId, MyUtils.app_version);
            }else {
                startNxtActivity();
            }
        } else {
            boolean showRationale = true;
            for (String permission: PERMISSIONS_REQUIRED) {
                showRationale = ActivityCompat.shouldShowRequestPermissionRationale(this, permission);
                if (!showRationale) {
                    break;
                }
            }

            String dialogMsg = showRationale ? "We need some permissions to run this APP!" : "You've declined the required permissions, please grant them from your phone settings";

            new AlertDialog.Builder(this)
                    .setTitle("Permissions Required")
                    .setMessage(dialogMsg)
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(mActivity, PERMISSIONS_REQUIRED, REQUEST_PERMISSIONS);
                        }
                    }).create().show();
        }
    }

    private boolean checkPermission(String permissions[]) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MyUtils.l("MainActivity", "requestCode: " + requestCode);
        MyUtils.l("MainActivity", "Permissions:" + Arrays.toString(permissions));

        if (requestCode == REQUEST_PERMISSIONS) {

            MyUtils.l("MainActivity", "grantResults: " + Arrays.toString(grantResults));
            boolean hasGrantedPermissions = true;
            for (int i=0; i<grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    hasGrantedPermissions = false;
                    break;
                }
            }

            if (!hasGrantedPermissions) {
                finish();
//                Toast.makeText(mActivity, "Declined", Toast.LENGTH_SHORT).show();
            }else {
//                Toast.makeText(mActivity, "Granted", Toast.LENGTH_SHORT).show();
//                startNxtActivity();
                String token = FirebaseInstanceId.getInstance().getToken();

                MyUtils.l(TAG,"token : "+token);
                editor.putString(MyUtils.TOKEN,token);
                editor.commit();

                MyUtils.getPhoneInfo(SplashscreenActivity.this);
                if(MyUtils.isOnline(SplashscreenActivity.this) && (appReg == 1)){
                    getUpdate(categoryId, MyUtils.app_version);
                }else {
                    startNxtActivity();
                }
            }

        } else {
            finish();
        }
    }
}
