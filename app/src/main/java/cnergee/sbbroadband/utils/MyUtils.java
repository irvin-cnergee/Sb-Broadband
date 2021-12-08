package cnergee.sbbroadband.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * Created by Siddhesh on 6/5/2017.
 */

public class MyUtils {

    private static final String TAG = "MyUtils";

    public static String SERIVICE_ITEM = "";
    public static String DATA_FOR="from";
    public static String MY_PREF="my_pref";
    public static String IS_LOGIN="is_login";
    public static String IS_REG="is_reg";
    public static String APP_REG="app_reg";
    public static String CRN="crn";
    public static String CLIENT_ACCESS_ID="client_access_id";
    public static String USER_NAME="user_name";
    public static String ENTITY_ID="entity_id";
    public static String SUBSCRIBER_ID="subscriber_id";
    public static String TOKEN="token";
    public static String IP_ADDRESS="ip_address";
    public static String ENTITY_NAME="entity_name";
    public static String IS_FIRST_TIME="is_first_time";
    public static boolean showlog = true;


    public static int POST = 1;
    public static int GET = 0;
    public static String imei = "";
    public static String platform = "";
    public static String phone_unique_id = "";
    public static String app_version = "";
    public static String androidOS="";
    public static String mobile_name = "";
    public static String manufacturer = "";

    /*internet usage*/
    public static String TOTAL_DATA = "total_data";
    public static String AVAILABLE_DATA = "available_data";
    public static String USED_DATA = "used_data";
    public static String EXPIRATION_DATE = "expiration_date";
    public static String REMAINING_DAYS = "remaining_days";
    public static String TOTAL_DAYS = "total_days";
    public static String IS_EXPIRED = "is_expired";

    /*subscriber_pckg_details*/
    public static String SUBSCRIBER_FULL_NAME="subscriber_full_name";
    public static String AREA_ID="area_id";
    public static String AUTH_TYPE="auth_type";
    public static String PACKAGE_NAME="package_name";
    public static String FROM_DATE="from_date";
    public static String TO_DATE="to_date";
    public static String PCKG_AMOUNT="pckg_amount";
    public static String CURRENCY="currency";
    public static String PACKAGE_ID="package_id";
    public static String IS_CURRENT="is_current";
    public static String PAYMENT_GATEWAY="payment_gateway";
    public static String CONNECTION_TYPE="connection_type";
    public static String IS_ONLINE_RENEW_ALLOWED="is_online_renew_allowed";
    public static String ENTITY_LOGO="entity_logo";
    public static String ALLOW_CHANGE_PCKG="allow_change_pckg";
    public static String IS_PCKG_AVAILABLE="IS_PCKG_AVAILABLE";
    public static String RENEWAL="RENEWAL";


    public static void l(String name, String value) {
        if (showlog) {
            // Log.d(name,value);

            if (value.length() > 4000) {
                Log.d(name, value.substring(0, 4000));
                // log(name,value.substring(4000));
            } else
                Log.d(name, value);
        }
    }

    public static boolean isOnline(Context context) {

        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        Class[] items = { String.class, Integer.class };

        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(context.CONNECTIVITY_SERVICE);

        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI")) {
                if (ni.isConnected()) {
                    haveConnectedWifi = true;
                    System.out.println("WIFI CONNECTION AVAILABLE");
                } else {
                    System.out.println("WIFI CONNECTION NOT AVAILABLE");
                }
            }
            if (ni.getTypeName().equalsIgnoreCase("MOBILE")) {
                if (ni.isConnected()) {
                    haveConnectedMobile = true;
                    System.out.println("MOBILE INTERNET CONNECTION AVAILABLE");
                } else {
                    System.out
                            .println("MOBILE INTERNET CONNECTION NOT AVAILABLE");
                }
            }

        }
        if(haveConnectedWifi || haveConnectedMobile){

        }
        else{

        }

        return haveConnectedWifi || haveConnectedMobile;

    }

    public static void getPhoneInfo(Context context) {

        PackageInfo pInfo = null;
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            imei = telephonyManager.getDeviceId();
            pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            app_version = pInfo.versionName;
            mobile_name = Build.MODEL;
            manufacturer = Build.MANUFACTURER;
            androidOS =  Build.VERSION.RELEASE;
            phone_unique_id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            platform = "Android";
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        MyUtils.l(TAG,"phone_name :"+mobile_name+" manufacturer :"+manufacturer+" phone_version :"+androidOS+" app_ver :"+app_version+" phone_unique_id :"+phone_unique_id+ "imei : "+imei);
    }


}
