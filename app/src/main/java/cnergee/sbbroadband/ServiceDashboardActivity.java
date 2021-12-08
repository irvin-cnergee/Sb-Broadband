package cnergee.sbbroadband;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cnergee.sbbroadband.adapters.ServiceListAdapter;
import cnergee.sbbroadband.obj.ServiceData;

public class ServiceDashboardActivity extends AppCompatActivity {

    RecyclerView rv_service;
    ArrayList<ServiceData> serviceList ;
    ServiceListAdapter adapter;
    ToggleButton toggleButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_dashboard);

        initControls();

        LinearLayoutManager manager = new LinearLayoutManager(ServiceDashboardActivity.this);
        rv_service.setLayoutManager(manager);
        rv_service.setItemAnimator(new DefaultItemAnimator());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,
                manager.getOrientation());
        rv_service.addItemDecoration(dividerItemDecoration);
        try {
            adapter = new ServiceListAdapter(ServiceDashboardActivity.this,getServiceList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        rv_service.setAdapter(adapter);

        toggleButton.setChecked(true);
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){


                    try {
                        adapter = new ServiceListAdapter(ServiceDashboardActivity.this,getServiceList());
                        rv_service.setAdapter(adapter);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }else {
                    try {
                        adapter = new ServiceListAdapter(ServiceDashboardActivity.this,getTvList());
                        rv_service.setAdapter(adapter);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

            }
        });






    }

    private void initControls() {

        rv_service = (RecyclerView)findViewById(R.id.rv_service);

        toggleButton = (ToggleButton) findViewById(R.id.toggle);

    }

    public ArrayList<ServiceData> getServiceList() throws JSONException {

        String result = "[\n" +
                "  {\n" +
                "    \"subname\": \"Siddhesh Rane\",\n" +
                "    \"address\": \"05, Amazon Villa, Bandra(West)\",\n" +
                "    \"tckt\": \"INDEV0265398\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"subname\": \"Jyoti Sukre\",\n" +
                "    \"address\": \"A-21, Maata Heights, Ghansoli(West)\",\n" +
                "    \"tckt\": \"INDEV0265389\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"subname\": \"Diya Pathak\",\n" +
                "    \"address\": \"15, Baban Town, Kamothe\",\n" +
                "    \"tckt\": \"INDEV0265312\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"subname\": \"Preeti Koli\",\n" +
                "    \"address\": \"B-12, SK Arcade, Mora\",\n" +
                "    \"tckt\": \"INDEV0265385\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"subname\": \"Dilesh Bhoir\",\n" +
                "    \"address\": \"A-05, Dilesh Niwas, Uran\",\n" +
                "    \"tckt\": \"INDEV0265338\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"subname\": \"Priyanka Mudliyar\",\n" +
                "    \"address\": \"25, Santoshi Maa Villa, Kopar\",\n" +
                "    \"tckt\": \"INDEV0265356\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"subname\": \"Rahul Shukla\",\n" +
                "    \"address\": \"C-02, Radha Niwas, Dombivli(West)\",\n" +
                "    \"tckt\": \"INDEV0265378\"\n" +
                "  }\n" +
                "]";

        JSONArray jsonArray = new JSONArray(result);
        serviceList = new ArrayList<>();
        serviceList.clear();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject object = jsonArray.getJSONObject(i);

            ServiceData obj = new ServiceData();
            obj.setAddress(object.optString("address"));
            obj.setSubname(object.optString("subname"));
            obj.setTckt(object.optString("tckt"));

            serviceList.add(obj);
        }

        return serviceList;
    }

    public ArrayList<ServiceData> getTvList() throws JSONException {

        String result = "[\n" +
                "  {\n" +
                "    \"subname\": \"Priyanka Pawar\",\n" +
                "    \"address\": \"Shraddha Tower, Airoli\",\n" +
                "    \"tckt\": \"INDEV0265398\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"subname\": \"Sagar Shinde\",\n" +
                "    \"address\": \"Ambika Heights, Near Panpatti, Ghansoli\",\n" +
                "    \"tckt\": \"INDEV0265389\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"subname\": \"Bhushan Mhatre\",\n" +
                "    \"address\": \"Mhatre mansion, Panvel\",\n" +
                "    \"tckt\": \"INDEV0265312\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"subname\": \"Shweta Kulkarni\",\n" +
                "    \"address\": \"Spruha Villa, Ulwe\",\n" +
                "    \"tckt\": \"INDEV0265385\"\n" +
                "  }\n" +
                "]";

        JSONArray jsonArray = new JSONArray(result);
        serviceList = new ArrayList<>();
        serviceList.clear();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject object = jsonArray.getJSONObject(i);

            ServiceData obj = new ServiceData();
            obj.setAddress(object.optString("address"));
            obj.setSubname(object.optString("subname"));
            obj.setTckt(object.optString("tckt"));

            serviceList.add(obj);
        }

        return serviceList;
    }
}
