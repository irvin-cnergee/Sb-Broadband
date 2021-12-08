package developer.app.webservices;

import android.content.Context;

import cnergee.sbbroadband.R;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Siddhesh on 8/4/2017.
 */

public class APIClient {

    private static final String TAG = "APIClient";

    private static Retrofit retrofit = null;

    public static Retrofit getClient(Context context){

     /*   SharedPreferences sharedPreferences = context.getSharedPreferences(MyUtils.MY_PREF,Context.MODE_PRIVATE);
        String ip = sharedPreferences.getString(MyUtils.IP_ADDRESS,"");
        int isFirstTime = sharedPreferences.getInt(MyUtils.IS_FIRST_TIME,1);

        MyUtils.l(TAG,"ip : "+ip);

        String url = "";
        if(isFirstTime == 1){
//            url = "http://103.54.183.112:4010/mobile/";
            url = "http://103.54.183.99:"+context.getString(R.string.port)+"/mobile/";
        }else {
            url = "http://" + ip + ":"+context.getString(R.string.port)+"/mobile/";
        }*/


        String url = context.getString(R.string.ip)+"/mobile/";

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        retrofit = new Retrofit.Builder()
//                .baseUrl("http://103.54.183.99:9095/mobile/")
//                .baseUrl("http://103.54.183.112:4010/mobile/")
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        return retrofit;
    }
}
