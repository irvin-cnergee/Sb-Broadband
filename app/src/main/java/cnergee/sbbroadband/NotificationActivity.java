package cnergee.sbbroadband;

import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import cnergee.sbbroadband.adapters.NotificationAdapter;
import cnergee.sbbroadband.retroObj.Notification;
import cnergee.sbbroadband.retroObj.NotificationData;
import cnergee.sbbroadband.utils.MyUtils;
import developer.app.interface_.AllAPIs;
import developer.app.loadingview.ProgressWheel;
import developer.app.webservices.APIClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Siddhesh on 5/25/2017.
 */

public class NotificationActivity extends AppCompatActivity {

    RecyclerView rv_notification;
    NotificationAdapter adapter = null;
    Toolbar toolbar;
    SharedPreferences sharedPreferences;
    SweetAlertDialog sweetAlertDialog;
    ProgressWheel progressBar;
    ImageView img_error;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_notification);

        initControls();

        sharedPreferences = getSharedPreferences(MyUtils.MY_PREF,MODE_PRIVATE);
        int entityId = sharedPreferences.getInt(MyUtils.ENTITY_ID,0);
        int subscriberId = sharedPreferences.getInt(MyUtils.SUBSCRIBER_ID,0);

        if(MyUtils.isOnline(NotificationActivity.this)){
            ws_notifications(entityId, subscriberId);
        }else {
//            Toast.makeText(this, getString(R.string.internet_error_msg), Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
            img_error.setVisibility(View.VISIBLE);
            rv_notification.setVisibility(View.GONE);
            img_error.setImageDrawable(getResources().getDrawable(R.drawable.no_internet));
        }
    }

    private void ws_notifications(int entityId, int subscriberId) {

        AllAPIs allAPIs = APIClient.getClient(NotificationActivity.this).create(AllAPIs.class);
        Call<NotificationData> notificationDataCall = allAPIs.getNotifications(entityId,subscriberId);
        notificationDataCall.enqueue(new Callback<NotificationData>() {
            @Override
            public void onResponse(Call<NotificationData> call, Response<NotificationData> response) {

                if(response.isSuccessful()){
                    progressBar.setVisibility(View.GONE);
                    img_error.setVisibility(View.GONE);
                    rv_notification.setVisibility(View.VISIBLE);

                    NotificationData notificationData = response.body();
                    int status = notificationData.status;
                    String msg = notificationData.message;
                    List<Notification> notificationList = notificationData.notification;

                    if(notificationList.size() > 0){
                        adapter = new NotificationAdapter(NotificationActivity.this, notificationList);
                        rv_notification.setAdapter(adapter);
                    }else {
                    /*sweetAlertDialog = new SweetAlertDialog(NotificationActivity.this, SweetAlertDialog.ERROR_TYPE);
                    sweetAlertDialog.setTitleText(getString(R.string.oops));
                    sweetAlertDialog.setContentText(msg);
                    sweetAlertDialog.setCancelable(false);
                    sweetAlertDialog.setConfirmText("Ok");
                    sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                            NotificationActivity.this.finish();
                        }
                    }).show();*/
                        progressBar.setVisibility(View.GONE);
                        img_error.setVisibility(View.VISIBLE);
                        rv_notification.setVisibility(View.GONE);
                        img_error.setImageDrawable(getResources().getDrawable(R.drawable.no_notification));
                    }
                }
            }

            @Override
            public void onFailure(Call<NotificationData> call, Throwable t) {
               /* sweetAlertDialog = new SweetAlertDialog(NotificationActivity.this, SweetAlertDialog.ERROR_TYPE);
                sweetAlertDialog.setTitleText(getString(R.string.oops));
                sweetAlertDialog.setContentText(getString(R.string.went_wrong));
                sweetAlertDialog.setCancelable(false);
                sweetAlertDialog.setConfirmText("Ok");
                sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                        NotificationActivity.this.finish();
                    }
                }).show();*/

                progressBar.setVisibility(View.GONE);
                img_error.setVisibility(View.VISIBLE);
                rv_notification.setVisibility(View.GONE);
                img_error.setImageDrawable(getResources().getDrawable(R.drawable.something_wrong));

            }
        });
    }

    private void initControls() {

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow);
        upArrow.setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        toolbar.setTitleTextColor(getColor(android.R.color.white));

        rv_notification = (RecyclerView)findViewById(R.id.rv_notification);
        progressBar = (ProgressWheel) findViewById(R.id.progressBar);
        img_error = (ImageView) findViewById(R.id.img_error);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        rv_notification.setLayoutManager(manager);
        rv_notification.setItemAnimator(new DefaultItemAnimator());

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,
                manager.getOrientation());
        rv_notification.addItemDecoration(dividerItemDecoration);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

}
