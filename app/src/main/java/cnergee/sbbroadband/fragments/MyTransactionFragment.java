package cnergee.sbbroadband.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import cnergee.sbbroadband.BasicFragment;
import cnergee.sbbroadband.DashboardActivity;
import cnergee.sbbroadband.R;
import cnergee.sbbroadband.adapters.RenewHistoryAdapter;
import cnergee.sbbroadband.adapters.TransactionHistoryAdapter;
import cnergee.sbbroadband.obj.RenewHostoryObj;
import cnergee.sbbroadband.retroObj.TransHistory;
import cnergee.sbbroadband.retroObj.TransactionData;
import cnergee.sbbroadband.utils.MyUtils;
import developer.app.interface_.AllAPIs;
import developer.app.loadingview.ProgressWheel;
import developer.app.webservices.APIClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Siddhesh on 5/25/2017.
 */

public class MyTransactionFragment extends BasicFragment {

    RecyclerView rv_renew_history;
    RenewHistoryAdapter historyAdapter = null;
    TransactionHistoryAdapter transAdapter = null;
    ArrayList<RenewHostoryObj> history_list;
    ProgressWheel progressBar;

    private static final String TAG = "MyTransactionFragment";

    AllAPIs api;
    SharedPreferences sharedPreferences;

    int entityId = 0;
    int subscriberId = 0;

    ImageView img_error;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.content_my_transaction,container,false);

        api = APIClient.getClient(getActivity()).create(AllAPIs.class);

        ((DashboardActivity)getActivity()).updateTitle(getString(R.string.trans_title));

        sharedPreferences = (SharedPreferences) getActivity().getSharedPreferences(MyUtils.MY_PREF,MODE_PRIVATE);
        entityId = sharedPreferences.getInt(MyUtils.ENTITY_ID,0);
        subscriberId = sharedPreferences.getInt(MyUtils.SUBSCRIBER_ID,0);

        rv_renew_history = (RecyclerView)view.findViewById(R.id.rv_trans);
        progressBar = (ProgressWheel)view.findViewById(R.id.progressBar);
        img_error = (ImageView)view.findViewById(R.id.img_error);

        if(MyUtils.isOnline(getActivity())) {
            ws_getTransactions();
        }else {

            progressBar.setVisibility(View.GONE);
            rv_renew_history.setVisibility(View.GONE);
            img_error.setVisibility(View.VISIBLE);

            img_error.setImageDrawable(getResources().getDrawable(R.drawable.no_internet));
        }

        return view;
    }

    /*private void ws_getTransactions() {

        rv_renew_history.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        Call<TransactionHistoryAdapter> transApi = api.getTransactioHistory(subscriberId,entityId);
        transApi.enqueue(new Callback<TransactionHistoryAdapter>() {
            @Override
            public void onResponse(Call<TransactionHistoryAdapter> call, Response<TransactionHistoryAdapter> response) {

                rv_renew_history.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);

                TransactionHistoryAdapter history = response.body();

                int status = history.status;
                String msg = history.msg;
                List<TransList> transList = history.transList;

                if(status == 0){
                    new SweetAlertDialog(getActivity(),SweetAlertDialog.ERROR_TYPE)
                    .setTitleText(getString(R.string.oops))
                    .setContentText(getString(R.string.went_wrong))
                    .setConfirmText("Ok")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                        }
                    }).show();
                }else {
                    if(transList.size() > 0){

                        RecyclerView.LayoutManager manager = new LinearLayoutManager(getActivity());
                        rv_renew_history.setLayoutManager(manager);
                        rv_renew_history.setItemAnimator(new DefaultItemAnimator());

                        try {
                            historyAdapter = new RenewHistoryAdapter(getActivity(),transList,"MyTransaction");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        rv_renew_history.setAdapter(historyAdapter);

                    }else{
                        new SweetAlertDialog(getActivity(),SweetAlertDialog.ERROR_TYPE)
                                .setTitleText(getString(R.string.oops))
                                .setContentText(getString(R.string.no_data))
                                .setConfirmText("Ok")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.dismiss();
                                    }
                                }).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<TransactionHistoryAdapter> call, Throwable t) {

            }
        });

    }*/


    private void ws_getTransactions() {

        rv_renew_history.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        img_error.setVisibility(View.GONE);

        Call<TransactionData> transApi = api.getTransactioHistory(subscriberId,entityId);
        transApi.enqueue(new Callback<TransactionData>() {
            @Override
            public void onResponse(Call<TransactionData> call, Response<TransactionData> response) {

                if(response.isSuccessful()) {
                    rv_renew_history.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    img_error.setVisibility(View.GONE);

                    TransactionData history = response.body();

                    int status = history.status;
                    String msg = history.message;
                    List<TransHistory> transList = history.transHistory;

                    if (status == 0) {
                    /*new SweetAlertDialog(getActivity(),SweetAlertDialog.ERROR_TYPE)
                            .setTitleText(getString(R.string.oops))
                            .setContentText(getString(R.string.went_wrong))
                            .setConfirmText("Ok")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                }
                            }).show();*/

                        progressBar.setVisibility(View.GONE);
                        rv_renew_history.setVisibility(View.GONE);
                        img_error.setVisibility(View.VISIBLE);

                        img_error.setImageDrawable(getResources().getDrawable(R.drawable.no_transaction));

                    } else {
                        if (transList.size() > 0) {

                            LinearLayoutManager manager = new LinearLayoutManager(getActivity());
                            rv_renew_history.setLayoutManager(manager);
                            rv_renew_history.setHasFixedSize(true);
                            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rv_renew_history.getContext(),
                                    manager.getOrientation());
                            rv_renew_history.addItemDecoration(dividerItemDecoration);
//                        rv_renew_history.setItemAnimator(new DefaultItemAnimator());

                            try {
                                transAdapter = new TransactionHistoryAdapter(getActivity(), transList, "MyTransaction");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            rv_renew_history.setAdapter(transAdapter);

                        } else {
                            /*new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText(getString(R.string.oops))
                                    .setContentText(getString(R.string.no_data))
                                    .setConfirmText("Ok")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            sweetAlertDialog.dismiss();
                                        }
                                    }).show();*/

                            progressBar.setVisibility(View.GONE);
                            rv_renew_history.setVisibility(View.GONE);
                            img_error.setVisibility(View.VISIBLE);

                            img_error.setImageDrawable(getResources().getDrawable(R.drawable.no_transaction));
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<TransactionData> call, Throwable t) {
                /*new SweetAlertDialog(getActivity(),SweetAlertDialog.ERROR_TYPE)
                        .setTitleText(getString(R.string.oops))
                        .setContentText("webservice error : "+t)
                        .setConfirmText("Ok")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                            }
                        }).show();*/

                progressBar.setVisibility(View.GONE);
                rv_renew_history.setVisibility(View.GONE);
                img_error.setVisibility(View.VISIBLE);

                img_error.setImageDrawable(getResources().getDrawable(R.drawable.something_wrong));

            }
        });

    }

}
