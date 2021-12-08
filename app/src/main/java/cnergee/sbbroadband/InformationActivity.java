package cnergee.sbbroadband;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cnergee.sbbroadband.obj.Complaint;
import cnergee.sbbroadband.utils.MyUtils;
import cnergee.sbbroadband.widgets.MyButton;
import cnergee.sbbroadband.widgets.MyEditText;
import cnergee.sbbroadband.widgets.MyTextView;
import cnergee.sbbroadband.widgets.SmoothCheckBox;

public class InformationActivity extends AppCompatActivity {


    Toolbar toolbar;
    LinearLayout ll_upgrade_pkg,ll_renew,ll_upgrade_status,ll_Launch_comp;
    MyTextView tv_totalamount;
    SmoothCheckBox cb_callback,cb_doorlock,cb_shifted,cb_service;
    LinearLayout ll_comment,ll_paypick;
    SmoothCheckBox[] chkArray;
    MyEditText et_comment;
    ArrayList<Complaint> comp_list;
    ArrayList<String> compln_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        initControls();
    }

    public void initControls(){
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ll_upgrade_pkg=(LinearLayout)findViewById(R.id.ll_upgrade_pkg);
        ll_renew=(LinearLayout)findViewById(R.id.ll_renew);
        ll_upgrade_status=(LinearLayout)findViewById(R.id.ll_upgrade_status);
        ll_Launch_comp=(LinearLayout)findViewById(R.id.ll_Launch_comp);
        tv_totalamount=(MyTextView)findViewById(R.id.tv_totalamount);

        tv_totalamount.append(getResources().getString(R.string.total_rs));
        SpannableString ss1=  new SpannableString(tv_totalamount.getText().toString());
        ss1.setSpan(new RelativeSizeSpan(2f), 12, tv_totalamount.length(), 0); // set size
        ss1.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.info_font)), 12, tv_totalamount.length(), 0);
        tv_totalamount.setText(ss1);

        ll_upgrade_pkg.setOnClickListener(onClickListener);
        ll_renew.setOnClickListener(onClickListener);
        ll_upgrade_status.setOnClickListener(onClickListener);
        ll_Launch_comp.setOnClickListener(onClickListener);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow);
        upArrow.setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));

        compln_name = new ArrayList<>();
        compln_name.add("Select Complaint Type");
        compln_name.add("Package Problem");
        compln_name.add("Shifting to other Zone");
        compln_name.add("Shifting to other Area");
        compln_name.add("Modify Mobile No");
    }

    public View.OnClickListener onClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(v==ll_upgrade_pkg){
                Intent intent = new Intent(InformationActivity.this,CollectionPaymentActivity.class);
                intent.putExtra(MyUtils.DATA_FOR,"upgradepackage");
                startActivity(intent);
            }

            if(v==ll_renew){
                Intent intent = new Intent(InformationActivity.this,CollectionPaymentActivity.class);
                intent.putExtra(MyUtils.DATA_FOR,"renew");
                startActivity(intent);
            }

            if(v==ll_upgrade_status){
                final Dialog dialog = new Dialog(InformationActivity.this);
                dialog.setContentView(R.layout.dialog_update_status);
                MyButton btn_submit=(MyButton)dialog.findViewById(R.id.btn_submit);
                chkArray=new SmoothCheckBox[4];
                cb_callback=(SmoothCheckBox)dialog.findViewById(R.id.cb_callback);
                cb_doorlock=(SmoothCheckBox)dialog.findViewById(R.id.cb_doorlock);
                cb_shifted=(SmoothCheckBox)dialog.findViewById(R.id.cb_shifted);
                cb_service=(SmoothCheckBox)dialog.findViewById(R.id.cb_service);
                ll_comment=(LinearLayout) dialog.findViewById(R.id.ll_comment);
                ll_paypick=(LinearLayout)dialog.findViewById(R.id.ll_paypick);
                et_comment=(MyEditText)dialog.findViewById(R.id.et_comment);
                chkArray[0]=cb_callback; chkArray[1]=cb_doorlock; chkArray[2]=cb_shifted; chkArray[3]=cb_service;

                btn_submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

              /*  cb_callback.setOnCheckedChangeListener(new SmoothCheckBox.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(SmoothCheckBox checkBox, boolean isChecked) {
                        final int checkedId = checkBox.getId();
                        SmoothCheckBox current;
                        for (int i = 0; i < chkArray.length; i++) {
                            current= chkArray[i];
                            if(current.getId()==checkedId){
                            }else{
                                for(int j=0;j<chkArray.length;j++){
                                    if(checkedId!=chkArray[j].getId()){
                                        int curremt_check=checkedId;
                                        int not_check=chkArray[j].getId();
                                        SmoothCheckBox not_selected=chkArray[j];
                                        not_selected.setChecked(false);
                                    }
                                }
                            }
                        }
                    }
                });*/

                cb_callback.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final int checkedId = v.getId();
                        for (int i = 0; i < chkArray.length; i++) {
                            final SmoothCheckBox current = chkArray[i];
                            if (current.getId() == checkedId) {
                                current.setChecked(true);
                            } else {
                                current.setChecked(false);
                            }
                        }
                        if(cb_callback.isChecked()) {
                            ll_paypick.setVisibility(View.VISIBLE);
                            ll_comment.setVisibility(View.GONE);
                        }else{
                            ll_paypick.setVisibility(View.GONE);
                        }
                        Log.e("Callback ",":"+checkedId);
                    }
                });

                cb_doorlock.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final int checkedId = v.getId();
                        for (int i = 0; i < chkArray.length; i++) {
                            final SmoothCheckBox current = chkArray[i];
                            if (current.getId() == checkedId) {
                                current.setChecked(true);
                            } else {
                                current.setChecked(false);
                            }
                        }
                        if(cb_doorlock.isChecked()) {
                            ll_paypick.setVisibility(View.GONE);
                            ll_comment.setVisibility(View.GONE);
                        }else{
                            ll_paypick.setVisibility(View.GONE);
                            ll_comment.setVisibility(View.GONE);
                        }
                        Log.e("Doorlock ",":"+checkedId);
                    }
                });

                cb_shifted.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final int checkedId = v.getId();
                        for (int i = 0; i < chkArray.length; i++) {
                            final SmoothCheckBox current = chkArray[i];
                            if (current.getId() == checkedId) {
                                current.setChecked(true);
                            } else {
                                current.setChecked(false);
                            }
                        }
                        if(cb_shifted.isChecked() || cb_service.isChecked()) {
                            et_comment.setText("");
                            ll_comment.setVisibility(View.VISIBLE);
                            ll_paypick.setVisibility(View.GONE);
                        }else{
                            ll_comment.setVisibility(View.GONE);
                        }
                        Log.e("Shifted ",":"+checkedId);
                    }
                });

                cb_service.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final int checkedId = v.getId();
                        for (int i = 0; i < chkArray.length; i++) {
                            final SmoothCheckBox current = chkArray[i];
                            if (current.getId() == checkedId) {
                                current.setChecked(true);
                            } else {
                                current.setChecked(false);
                            }
                        }
                        if(cb_shifted.isChecked() || cb_service.isChecked()) {
                            et_comment.setText("");
                            ll_comment.setVisibility(View.VISIBLE);
                            ll_paypick.setVisibility(View.GONE);
                        }else{
                            ll_comment.setVisibility(View.GONE);
                        }
                        Log.e("Service  ",":"+checkedId);
                    }
                });

                Window window = dialog.getWindow();
                window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                dialog.show();
            }

            if(v==ll_Launch_comp){
                final Dialog dialog = new Dialog(InformationActivity.this);
                dialog.setContentView(R.layout.dialog_complaint);
                Spinner sp_complaint=(Spinner)dialog.findViewById(R.id.sp_complaint);
                MyButton btn_submit=(MyButton)dialog.findViewById(R.id.btn_submit);

                try {
//                    ComplaintAdapter complaintAdapter=new ComplaintAdapter(InformationActivity.this,R.layout.row_item_comp,getJson());
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(InformationActivity.this, R.layout.row_item_comp, R.id.tv_name,compln_name);
                    sp_complaint.setAdapter(adapter);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                btn_submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                Window window = dialog.getWindow();
                window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                dialog.show();
            }
        }
    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.dashboard, menu);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_information, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
         if(id == R.id.action_map){
            Intent intent = new Intent(InformationActivity.this,MapActivity.class);
            startActivity(intent);
            return true;
        }
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


   /* final SmoothCheckBox scb = (SmoothCheckBox) findViewById(R.id.scb);
    scb.setOnCheckedChangeListener(new SmoothCheckBox.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(SmoothCheckBox checkBox, boolean isChecked) {
            Log.d("SmoothCheckBox", String.valueOf(isChecked));
        }
    });*/

    public ArrayList<Complaint> getJson() throws Exception {
        String res= "[\n" +
                "  {\n" +
                "    \"id\":\"0\",\n" +
                "    \"name\":\"Select Complaint Type\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\":\"1\",\n" +
                "    \"name\":\"Package Problem\"\n" +
                "  },\n" +
                "   {\n" +
                "    \"id\":\"2\",\n" +
                "    \"name\":\"Shifting to other Zone\"\n" +
                "  },\n" +
                "   {\n" +
                "    \"id\":\"3\",\n" +
                "    \"name\":\"Shifting to other room\"\n" +
                "  },\n" +
                "   {\n" +
                "    \"id\":\"4\",\n" +
                "    \"name\":\"Shifting to other Area\"\n" +
                "  },\n" +
                "   {\n" +
                "    \"id\":\"5\",\n" +
                "    \"name\":\"Mobile no Change\"\n" +
                "  },\n" +
                "   {\n" +
                "    \"id\":\"6\",\n" +
                "    \"name\":\"Modify Emaild ID\"\n" +
                "  }\n" +
                "]";

        try {
            JSONArray jsonArray = new JSONArray(res);
            comp_list = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Complaint complaint=new Complaint();
                complaint.setComp_id(jsonObject.optInt("id",0));
                complaint.setComp_msg(jsonObject.optString("name",""));
                comp_list.add(complaint);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return  comp_list;
    }


}
