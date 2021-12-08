package cnergee.sbbroadband;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import cn.pedant.SweetAlert.SweetAlertDialog;
import cnergee.sbbroadband.utils.MyUtils;

public class WebOnlinePaymentActivity extends AppCompatActivity {

    private static final String TAG = "WebOnlinePaymentActivity";

    String payment_id = "";
    TextView tv_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_online_payment);

        payment_id = getIntent().getStringExtra("payId");

        WebView browser = (WebView) findViewById(R.id.web);
        tv_back = (TextView) findViewById(R.id.tv_back);
        browser.setWebViewClient(new MyBrowser());

        SharedPreferences sharedPreferences = getSharedPreferences(MyUtils.MY_PREF,MODE_PRIVATE);
        String ip = sharedPreferences.getString(MyUtils.IP_ADDRESS,"");

//        String url = getString(R.string.payment_url)+payment_id;
//        String url = "http://"+ip+":"+getString(R.string.port)+"/payment?id="+payment_id;
        String url = getString(R.string.ip)+"/payment?id="+payment_id;
        MyUtils.l(TAG,"url : "+url);

        browser.getSettings().setJavaScriptEnabled(true);
        browser.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        browser.getSettings().setUseWideViewPort(true);
        browser.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        browser.getSettings().setLoadWithOverviewMode(true);
        browser.addJavascriptInterface(new MyJavaScriptInterface(), "HTMLOUT");
        browser.loadUrl(url);

        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WebOnlinePaymentActivity.this,DashboardActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
    }

    private class MyBrowser extends WebViewClient{


        SweetAlertDialog sweetAlertDialog ;

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
//
//            view.loadUrl(url);
            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onLoadResource(WebView view, String url) {
//            super.onLoadResource(view, url);

            /*if(sweetAlertDialog == null) {
                sweetAlertDialog = new SweetAlertDialog(WebOnlinePaymentActivity.this, SweetAlertDialog.PROGRESS_TYPE);
                sweetAlertDialog.setTitleText(getString(R.string.please_wait));
                sweetAlertDialog.setContentText("");
                sweetAlertDialog.setCancelable(false);
                sweetAlertDialog.show();
            }
*/
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

            MyUtils.l(TAG,"on pg finish : "+url);

            if(url.contains("/payment-response") || url.contains("/payment?id=")){
                tv_back.setVisibility(View.VISIBLE);
            }else {
                tv_back.setVisibility(View.GONE);
            }

            /*if(sweetAlertDialog.isShowing()) {
                sweetAlertDialog.dismiss();
                sweetAlertDialog = null;
            }*/
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);

            /*if(sweetAlertDialog.isShowing()) {
                sweetAlertDialog.dismiss();
                sweetAlertDialog = null;
            }*/

        }
    }

    class MyJavaScriptInterface {
        @JavascriptInterface
        public void processHTML(String html) {
            // process the html as needed by the app
            MyUtils.l(TAG,"html : "+html);
            Log.e("Web view","Called  JavaScriptInterface");
            System.out.println(html);
            String[] actual_response = null;
            Log.e("HTML", "is" + html);
            if (html != null) {
                if (html.length() > 0) {
                    String s = html;
                    Log.e("Before", "Split" + s);
                    actual_response = TextUtils.split(html, "S\\@gar123\\$");

                    Log.e("actual response",""+actual_response);

                    MyUtils.l("Response  1","is"+actual_response[1]);
                    MyUtils.l("Response  2","is"+actual_response[2]);

//                    parse_PayUMoney(actual_response[1]);
                }
            }
        }
    }

    @Override
    public void onBackPressed() {

    }
}
