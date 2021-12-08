package cnergee.sbbroadband.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import cnergee.sbbroadband.DashboardActivity;
import cnergee.sbbroadband.R;
import cnergee.sbbroadband.adapters.PackageHistoryAdapter;
import cnergee.sbbroadband.adapters.PackageHistoryData;
import cnergee.sbbroadband.utils.MyUtils;
import developer.app.interface_.AllAPIs;
import developer.app.loadingview.ProgressWheel;
import developer.app.webservices.APIClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Siddhesh on 11/3/2017.
 */

public class PackageHistoryFragment extends Fragment {

    AllAPIs api;
    SharedPreferences sharedPreferences;
    ProgressWheel progressBar;
    RecyclerView rv_pckg_history;
    PackageHistoryAdapter historyAdapter = null;

    int entityId = 0;
    int subscriberId = 0;

    ImageView img_error;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.package_history_content,container,false);

        api = APIClient.getClient(getActivity()).create(AllAPIs.class);

        ((DashboardActivity)getActivity()).updateTitle(getString(R.string.pckg_history));

        sharedPreferences = (SharedPreferences) getActivity().getSharedPreferences(MyUtils.MY_PREF,MODE_PRIVATE);
        entityId = sharedPreferences.getInt(MyUtils.ENTITY_ID,0);
        subscriberId = sharedPreferences.getInt(MyUtils.SUBSCRIBER_ID,0);

        rv_pckg_history = (RecyclerView)view.findViewById(R.id.rv_pckg_history);
        progressBar = (ProgressWheel)view.findViewById(R.id.progressBar);
        img_error = (ImageView) view.findViewById(R.id.img_error);

        if(MyUtils.isOnline(getActivity())) {
            ws_getPackageHistory();
        }else {

            progressBar.setVisibility(View.GONE);
            img_error.setVisibility(View.VISIBLE);
            rv_pckg_history.setVisibility(View.GONE);

            img_error.setImageDrawable(getResources().getDrawable(R.drawable.no_internet));
        }

        return view;
    }

    private void ws_getPackageHistory() {

        rv_pckg_history.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        img_error.setVisibility(View.GONE);

        Call<PackageHistoryData> pckApi = api.getPackageHistory(subscriberId);
        pckApi.enqueue(new Callback<PackageHistoryData>() {
            @Override
            public void onResponse(Call<PackageHistoryData> call, Response<PackageHistoryData> response) {

                if(response.isSuccessful()){
                    rv_pckg_history.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    img_error.setVisibility(View.GONE);

                    PackageHistoryData data = response.body();
                    List<PackageHistoryData.History> list = data.history;

                    if(list.size() > 0){
                        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
                        rv_pckg_history.setLayoutManager(manager);
                        rv_pckg_history.setHasFixedSize(true);

                        try {
                            historyAdapter = new PackageHistoryAdapter(getActivity(),list);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        rv_pckg_history.setAdapter(historyAdapter);
                    }else {

                        progressBar.setVisibility(View.GONE);
                        img_error.setVisibility(View.VISIBLE);
                        rv_pckg_history.setVisibility(View.GONE);

                        img_error.setImageDrawable(getResources().getDrawable(R.drawable.no_history));

                    }
                }
            }

            @Override
            public void onFailure(Call<PackageHistoryData> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                img_error.setVisibility(View.VISIBLE);
                rv_pckg_history.setVisibility(View.GONE);

                img_error.setImageDrawable(getResources().getDrawable(R.drawable.something_wrong));
            }
        });
    }
}
