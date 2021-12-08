package cnergee.sbbroadband.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cnergee.sbbroadband.R;
import cnergee.sbbroadband.utils.MyUtils;

/**
 * Created by Siddhesh on 11/3/2017.
 */

public class PackageHistoryAdapter extends RecyclerView.Adapter<PackageHistoryAdapter.MyViewHolder> {

    List<PackageHistoryData.History> list = new ArrayList<>();
    Context context;

    String pckg_name = "";
    String logo = "";
    String ip = "";

    public PackageHistoryAdapter(FragmentActivity activity, List<PackageHistoryData.History> list) {
        context = activity;
        this.list = list;

        SharedPreferences sharedPreferences = context.getSharedPreferences(MyUtils.MY_PREF,Context.MODE_PRIVATE);
        logo = sharedPreferences.getString(MyUtils.ENTITY_LOGO,"");
        ip = sharedPreferences.getString(MyUtils.IP_ADDRESS,"");
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.package_history_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        try {
            final PackageHistoryData.History history = list.get(position);

            pckg_name = history.packageName;
            final String amount = history.changedAmount+" "+history.currency;
            final String renew_dt = changeDateFormat(history.createdAt);
            final String start_dt = changeDateFormat(history.validFrom);
            final String end_dt = changeDateFormat(history.validTo);

            holder.tv_pckg_date.setText(renew_dt);
            holder.tv_amnt.setText(amount);
            holder.tv_package.setText(pckg_name);
            if (history.isCurrent == 0) {
                holder.tv_status.setText("Advance Renewal");
            }else if (history.isCurrent == 1) {
                holder.tv_status.setText("Current Active");
            }else if (history.isCurrent == 2) {
                holder.tv_status.setText("Expired");
            }else if (history.isCurrent == 3) {
                holder.tv_status.setText("Renewal Reversed");
            }

            holder.ll_pckg_history.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                            Toast.makeText(context, "amount : " + hostoryObj.amount + " " + hostoryObj.currency, Toast.LENGTH_SHORT).show();
                    View v = LayoutInflater.from(context).inflate(R.layout.receipt,null);
                    final Dialog dialog = new Dialog(context);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(v);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                    TextView tv_pckg_name = (TextView)v.findViewById(R.id.tv_package);
                    TextView tv_pckg_rate = (TextView)v.findViewById(R.id.tv_pckg_amount);
                    TextView tv_renew_date = (TextView)v.findViewById(R.id.tv_renew_date);
                    TextView tv_start_date = (TextView)v.findViewById(R.id.tv_start_date);
                    TextView tv_end_date = (TextView)v.findViewById(R.id.tv_end_date);
                    /*TextView tv_no = (TextView)v.findViewById(R.id.tv_no);
                    TextView tv_order_no = (TextView)v.findViewById(R.id.tv_order_no);
                    TextView tv_status = (TextView)v.findViewById(R.id.tv_status);*/
                    TextView tv_back = (TextView)v.findViewById(R.id.tv_back);
                    ImageView iv_logo = (ImageView) v.findViewById(R.id.iv_logo);
//                    LinearLayout ll_dd_id = (LinearLayout) v.findViewById(R.id.ll_dd_id);

                    if(!TextUtils.isEmpty(logo)){
                        String path = context.getString(R.string.ip)+"/assets/images/entity-logo/"+logo;
                        Picasso.with(context).load(path).into(iv_logo);
                    }

                    tv_pckg_name.setText(history.packageName);
                    tv_pckg_rate.setText(history.changedAmount+" "+history.currency);
                    tv_renew_date.setText(changeDateFormat(history.createdAt));

                    if(TextUtils.isEmpty(start_dt)){
                        tv_start_date.setText("NA");
                    }else {
                        tv_start_date.setText(changeDateFormat(history.validFrom));
                    }

                    if(TextUtils.isEmpty(end_dt)){
                        tv_end_date.setText("NA");
                    }else {
                        tv_end_date.setText(changeDateFormat(history.validTo));
                    }


                    tv_back.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });

                    dialog.show();
//                            context.startActivity(new Intent(context, TransPopupActivity.class));
                }
            });


        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private String changeDateFormat(String dt) {

        String date = "";

        try {
           if(dt != null){
               final String strCurrentDate = dt;
               SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
               final Date newDate = format.parse(strCurrentDate);
               format = new SimpleDateFormat("dd MMM yyyy");
               date = format.format(newDate);
           }
        }catch (Exception e){
            e.printStackTrace();
        }

        return date;

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_package;
        TextView tv_pckg_date;
        TextView tv_status;
        TextView tv_amnt;
        LinearLayout ll_pckg_history;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_package = (TextView) itemView.findViewById(R.id.tv_package);
            tv_pckg_date = (TextView) itemView.findViewById(R.id.tv_pckg_date);
            tv_status = (TextView) itemView.findViewById(R.id.tv_status);
            tv_amnt = (TextView) itemView.findViewById(R.id.tv_amnt);
            ll_pckg_history = (LinearLayout) itemView.findViewById(R.id.ll_pckg_history);
        }
    }
}
