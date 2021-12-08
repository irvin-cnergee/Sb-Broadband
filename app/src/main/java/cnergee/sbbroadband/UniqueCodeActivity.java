package cnergee.sbbroadband;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import cnergee.sbbroadband.retroObj.EntityDatum;
import cnergee.sbbroadband.utils.MyUtils;
import developer.app.interface_.AllAPIs;
import developer.app.webservices.APIClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UniqueCodeActivity extends AppCompatActivity {

    private static final String TAG = "UniqueCodeActivity";

    EditText et_unique;
    Button btn_submit;
    AllAPIs allAPIs;
    SweetAlertDialog sweetAlertDialog;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unique_code);

        initControls();

        sharedPreferences = getSharedPreferences(MyUtils.MY_PREF,MODE_PRIVATE);
        editor = sharedPreferences.edit();

        allAPIs = APIClient.getClient(UniqueCodeActivity.this).create(AllAPIs.class);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateCode();
            }
        });

    }

    private void initControls() {

        et_unique = (EditText)findViewById(R.id.et_unique);
        btn_submit = (Button)findViewById(R.id.btn_submit);
    }

    private void validateCode() {

        String code = et_unique.getText().toString().trim();

        boolean isCancel = false;
        View requestFocus = null;

        if(TextUtils.isEmpty(code)){
            et_unique.setError("Please enter Unique code");
            isCancel = true;
            requestFocus = et_unique;
        }

        if(isCancel){
            requestFocus.requestFocus();
        }else {
            if(MyUtils.isOnline(UniqueCodeActivity.this)) {
                wsGetDetails(code);
            }else {
                Toast.makeText(this, getString(R.string.internet_error_msg), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void wsGetDetails(String code) {

        sweetAlertDialog = new SweetAlertDialog(UniqueCodeActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        sweetAlertDialog.setCancelable(false);
        sweetAlertDialog.setTitleText(getString(R.string.please_wait));
        sweetAlertDialog.setContentText("");
        sweetAlertDialog.show();

        final Call<List<EntityDatum>> entityApi = allAPIs.getEntityData(code);
        entityApi.enqueue(new Callback<List<EntityDatum>>() {
            @Override
            public void onResponse(Call<List<EntityDatum>> call, Response<List<EntityDatum>> response) {

                if(response.isSuccessful()){
                    sweetAlertDialog.dismiss();

                    List<EntityDatum> entityDatum = response.body();

                    if(entityDatum.size() > 0) {
                        for (int i = 0; i < 1; i++) {
                            String entityName = entityDatum.get(i).entityName;
                            String serverIp = entityDatum.get(i).serverIp;
                            int entityId = entityDatum.get(i).entityId;

                            MyUtils.l(TAG, "entityName : " + entityName + " serverIp : " + serverIp + " entityId : " + entityId);

                            editor.putInt(MyUtils.ENTITY_ID,entityId);
                            editor.putString(MyUtils.IP_ADDRESS,serverIp);
                            editor.putString(MyUtils.ENTITY_NAME,entityName);
                            editor.commit();

                            moveToLogin();

                        }
                    }else {
                        sweetAlertDialog.changeAlertType(SweetAlertDialog.WARNING_TYPE);
                        sweetAlertDialog.setTitleText(getString(R.string.oops));
                        sweetAlertDialog.setContentText("Please enter valid unique code");
                        sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                                et_unique.setText("");
                            }
                        }).show();
                    }

                }
            }

            @Override
            public void onFailure(Call<List<EntityDatum>> call, Throwable t) {
                sweetAlertDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                sweetAlertDialog.setTitleText(getString(R.string.oops));
                sweetAlertDialog.setContentText(getString(R.string.went_wrong));
                sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();;
                    }
                });
            }
        });

    }

    private void moveToLogin() {

        editor.putInt(MyUtils.IS_FIRST_TIME,0);
        editor.commit();

        Intent intent = new Intent(UniqueCodeActivity.this,LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
