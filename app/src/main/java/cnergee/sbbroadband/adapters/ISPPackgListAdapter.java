package cnergee.sbbroadband.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cnergee.sbbroadband.MakePaymentActivity;
import cnergee.sbbroadband.R;
import cnergee.sbbroadband.retroObj.PackageList;

/**
 * Created by Siddhesh on 5/24/2017.
 */

public class ISPPackgListAdapter extends RecyclerView.Adapter<ISPPackgListAdapter.MyViewHolder> {

    List<PackageList> pckg_List = new ArrayList<>();
    Context context;

    public ISPPackgListAdapter(FragmentActivity activity, List<PackageList> pckgList) {
        pckg_List = pckgList;
        context = activity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.internet_pckg_item,parent,false);
        return new MyViewHolder(view);
    }

    String validityUnit = "";

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

       /* PckgList pckg = pckg_List.get(position);
        Log.e("pkg_list size",":"+pckg_List.size());
        holder.tv_pckg_name.setText(pckg.getPckg_name());
        holder.tv_days.setText(pckg.getDays()+" "+pckg.getValidity_unit());
        holder.tv_pckg_amnt.setText(pckg.getAmount()+" "+pckg.getCurrency());*/


        final PackageList pckg = pckg_List.get(position);
        final int id = pckg.packageId;
        holder.tv_pckg_name.setText(pckg.packageName);

        if(pckg.expirationUnit == 1){
            if(Integer.parseInt(pckg.expiration) > 1){
                validityUnit = "hours";
            }else{
                validityUnit = "hour";
            }
        }else if(pckg.expirationUnit == 2){
            if(Integer.parseInt(pckg.expiration) > 1){
                validityUnit = "days";
            }else{
                validityUnit = "day";
            }
        }else if(pckg.expirationUnit == 3){
            if(Integer.parseInt(pckg.expiration) > 1){
                validityUnit = "months";
            }else{
                validityUnit = "month";
            }
        }

        holder.tv_days.setText(pckg.expiration+" "+validityUnit);
        holder.tv_pckg_amnt.setText(pckg.amount+" "+pckg.currency);

        holder.iv_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,MakePaymentActivity.class);
                intent.putExtra("from","change");
                intent.putExtra("packg_id",id);
                intent.putExtra("packg_name",pckg.packageName);
                intent.putExtra("packg_rate",pckg.amount);
                intent.putExtra("currency",pckg.currency);

                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return pckg_List.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_pckg_name;
        TextView tv_days;
        TextView tv_pckg_amnt;
        ImageView iv_send;

        public MyViewHolder(View itemView) {
            super(itemView);

            tv_days = (TextView)itemView.findViewById(R.id.tv_days);
            tv_pckg_name = (TextView)itemView.findViewById(R.id.tv_pckg_name);
            tv_pckg_amnt = (TextView)itemView.findViewById(R.id.tv_pckg_amnt);
            iv_send = (ImageView) itemView.findViewById(R.id.iv_send);
        }
    }
}
