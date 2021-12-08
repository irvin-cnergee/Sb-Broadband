package cnergee.sbbroadband;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cnergee.sbbroadband.adapters.PickupAdapter;
import cnergee.sbbroadband.obj.PickupObj;
import cnergee.sbbroadband.widgets.MyEditText;

public class CollectionDashboradActivity extends AppCompatActivity {

    Toolbar toolbar;
    ArrayList<PickupObj> pickup_list;
    RecyclerView rv_pickup;
    PickupAdapter pickupAdapter;
    EditText met_search;
    LinearLayout ll_search;
    ImageView iv_search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection_dashborad);

        initControls();

    }

    public void initControls() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow);
        upArrow.setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));

        rv_pickup=(RecyclerView)findViewById(R.id.rv_pickup);
        met_search=(MyEditText)findViewById(R.id.met_search);
        ll_search=(LinearLayout) findViewById(R.id.ll_search);
        //iv_search=(ImageView)findViewById(R.id.iv_search);

        RecyclerView.LayoutManager manager = new LinearLayoutManager(CollectionDashboradActivity.this);
        rv_pickup.setLayoutManager(manager);
        rv_pickup.setItemAnimator(new DefaultItemAnimator());

        try {
            pickupAdapter = new PickupAdapter(CollectionDashboradActivity.this,getJson());
        } catch (Exception e) {
            e.printStackTrace();
        }
        rv_pickup.setAdapter(pickupAdapter);

        rv_pickup.addOnItemTouchListener(
                new RecyclerItemClickListener(CollectionDashboradActivity.this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        // TODO Handle item click
                    Intent intent=new Intent(CollectionDashboradActivity.this,InformationActivity.class);
                    startActivity(intent);
                    }
                })
        );

        met_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                s = s.toString().toLowerCase();
                final ArrayList<PickupObj> filteredList = new ArrayList<>();

                for (int i = 0; i < pickup_list.size(); i++) {

                    final String text = pickup_list.get(i).getName().toLowerCase();
                    if (text.startsWith(s.toString())) {
                        filteredList.add(pickup_list.get(i));
                    }
                }
                Log.e("filteredList ",":"+filteredList.size());

                if(filteredList.size()==0){
/*
                    iv_search.setVisibility(View.VISIBLE);

                    met_search.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT, 0.9f));

                    iv_search.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT, 0.1f));*/
                }

                rv_pickup.setAdapter(new PickupAdapter(CollectionDashboradActivity.this,filteredList));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.dashboard, menu);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }else if(item.getItemId()== R.id.action_profile){
            Intent intent=new Intent(CollectionDashboradActivity.this,ProfileActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }



    public  ArrayList<PickupObj> getJson() throws Exception {
        String res= "[\n" +
                "  {\n" +
                "    \"time\": \"3:31 PM\",\n" +
                "    \"isPostpaid\": 1,\n" +
                "    \"username\": \"Siddhesh Rane\",\n" +
                "    \"Address\": \"abc soc., dombivli\",\n" +
                "    \"amount\": \"₹ 2000\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"time\": \"3:35 PM\",\n" +
                "    \"isPostpaid\": 0,\n" +
                "    \"username\": \"Priyanka M.\",\n" +
                "    \"Address\": \"abc soc., Kopar\",\n" +
                "    \"amount\": \"₹ 2200\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"time\": \"4:31 PM\",\n" +
                "    \"isPostpaid\": 0,\n" +
                "    \"username\": \"Jyoti Sukre\",\n" +
                "    \"Address\": \"abc soc., Ghansoli\",\n" +
                "    \"amount\": \"₹ 2050\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"time\": \"3:31 PM\",\n" +
                "    \"isPostpaid\": 1,\n" +
                "    \"username\": \"Priti\",\n" +
                "    \"Address\": \"abc soc., Uran\",\n" +
                "    \"amount\": \"₹ 1500\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"time\": \"3:31 PM\",\n" +
                "    \"isPostpaid\": 0,\n" +
                "    \"username\": \"Shweta kulkarni\",\n" +
                "    \"Address\": \"abc soc., Nerul\",\n" +
                "    \"amount\": \"₹ 1800\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"time\": \"3:31 PM\",\n" +
                "    \"isPostpaid\": 0,\n" +
                "    \"username\": \"Dilesh Bhoir\",\n" +
                "    \"Address\": \"abc soc., Panvel\",\n" +
                "    \"amount\": \"₹ 2000\"\n" +
                "  }\n" +
                "]";

        try {
            JSONArray jsonArray = new JSONArray(res);
            pickup_list = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                PickupObj pickupObj=new PickupObj();
                pickupObj.setTime(jsonObject.optString("time",""));
                pickupObj.setIspostpaid(jsonObject.optInt("isPostpaid",0));
                pickupObj.setName(jsonObject.optString("username",""));
                pickupObj.setAddress(jsonObject.optString("Address",""));
                pickupObj.setAmount(jsonObject.optString("amount",""));

                pickup_list.add(pickupObj);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
       return  pickup_list;
    }
}
