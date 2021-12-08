package cnergee.sbbroadband;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;

import java.util.ArrayList;

import cnergee.sbbroadband.utils.MyUtils;
import cnergee.sbbroadband.widgets.MyButton;

public class CollectionPaymentActivity extends AppCompatActivity implements View.OnClickListener{

    RadioGroup radiopaymode,radioPayType;
    MyButton btn_submit;
    LinearLayout ll_bank_detail,ll_updatefrom;
    String data_for;
    LinearLayout ll_partial_info;
    Spinner sp_allowdays;

    ArrayList<String> al_allow=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection_payment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initControls();
      }

      public void initControls(){
          final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
          radiopaymode= (RadioGroup) findViewById(R.id.radiopaymode);
          radioPayType= (RadioGroup) findViewById(R.id.radioPayType);

          radiopaymode.setOnCheckedChangeListener(checkedChangeListener);
          radioPayType.setOnCheckedChangeListener(checkedChangeListener);

          ll_bank_detail=(LinearLayout)findViewById(R.id.ll_bank_detail);
          ll_updatefrom=(LinearLayout)findViewById(R.id.ll_updatefrom);
          ll_partial_info=(LinearLayout)findViewById(R.id.ll_partial_info);

          sp_allowdays=(Spinner)findViewById(R.id.sp_allowdays);

          btn_submit=(MyButton)findViewById(R.id.btn_submit);
          btn_submit.setOnClickListener(this);

          data_for=getIntent().getStringExtra(MyUtils.DATA_FOR);

          if(data_for.equalsIgnoreCase("upgradepackage")){
              ll_updatefrom.setVisibility(View.VISIBLE);
          }else{
              ll_updatefrom.setVisibility(View.GONE);
          }

          setSupportActionBar(toolbar);
          getSupportActionBar().setDisplayHomeAsUpEnabled(true);

          final CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
          AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);

          appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
              boolean isShow = false;
              int scrollRange = -1;

              @Override
              public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                  if (scrollRange == -1) {
                      scrollRange = appBarLayout.getTotalScrollRange();
                  }
                  if (scrollRange + verticalOffset == 0) {
                      collapsingToolbarLayout.setTitle(getString(R.string.title_activity_make_payment));
                      toolbar.setBackground(getDrawable(R.drawable.bg));
                      isShow = true;
                  } else if(isShow) {
                      collapsingToolbarLayout.setTitle(" ");
//                    collapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(android.R.color.transparent));
                      toolbar.setTitle(" ");
                      toolbar.setBackground(getDrawable(android.R.color.transparent));//carefull there should a space between double quote otherwise it wont work
                      isShow = false;
                  }
              }
          });

          al_allow.add("Allowed Days");
          al_allow.add("1 Days");
          al_allow.add("2 Days");
          al_allow.add("3 Days");
          al_allow.add("4 Days");
          al_allow.add("5 Days");
          al_allow.add("6 Days");
      }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

  /*  RadioGroup.OnCheckedChangeListener onCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            CompoundButton cb = (CompoundButton) group.findViewById(checkedId);
            if(cb!=null && cb.isChecked()){
                if(cb.getText().toString().equalsIgnoreCase("Cash")){
                    ll_bank_detail.setVisibility(View.GONE);
                }else{
                    ll_bank_detail.setVisibility(View.VISIBLE);
                }
            }
        }
    };*/

    public RadioGroup.OnCheckedChangeListener checkedChangeListener=new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (group.getId()){
                case R.id.radiopaymode:
                    CompoundButton cb = (CompoundButton) group.findViewById(checkedId);
                    if(cb!=null && cb.isChecked()){
                        if(cb.getText().toString().equalsIgnoreCase("Cash")){
                            ll_bank_detail.setVisibility(View.GONE);
                        }else{
                            ll_bank_detail.setVisibility(View.VISIBLE);
                        }
                    }
                    break;

                case R.id.radioPayType:
                    CompoundButton cb_type = (CompoundButton) group.findViewById(checkedId);
                    if(cb_type!=null && cb_type.isChecked()){
                        if(cb_type.getText().toString().equalsIgnoreCase("Partial Payment")){
                            ll_partial_info.setVisibility(View.VISIBLE);
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(CollectionPaymentActivity.this, R.layout.row_item_comp, R.id.tv_name,al_allow);
                            sp_allowdays.setAdapter(adapter);
                        }else{
                            ll_partial_info.setVisibility(View.GONE);
                        }
                    }
                    break;

                default:
                    break;
            }
        }
    };


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_submit:
                this.finish();
                Intent intent=new Intent(CollectionPaymentActivity.this,ReceiptActivity.class);
                intent.putExtra("data_from","collection");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            break;
            default:
                break;
        }
    }
}
