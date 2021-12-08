package cnergee.sbbroadband.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cnergee.sbbroadband.R;
import cnergee.sbbroadband.retroObj.TransHistory;
import cnergee.sbbroadband.utils.MyUtils;
import cnergee.sbbroadband.widgets.CircleView;

/**
 * Created by Siddhesh on 11/3/2017.
 */

public class TransactionHistoryAdapter extends RecyclerView.Adapter<TransactionHistoryAdapter.MyViewHolder>{

    private static final String TAG = "TransactionHistoryAdapter";
    Context context;
    List<TransHistory> list = new ArrayList<>();
    String from = "";
    int lastPosition = -1;

    public TransactionHistoryAdapter(FragmentActivity activity, List<TransHistory> transList, String myTransaction) {
       try {
           context = activity;
           list = transList;
           from = myTransaction;
           MyUtils.l(TAG,"from : "+ from);
       }catch (Exception r){
           r.printStackTrace();
       }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.trans_history_item,parent,false);

        Animation animation = AnimationUtils.loadAnimation(parent.getContext(), R.anim.push_left_in);
//        animation.setDuration(500);
        view.startAnimation(animation);

        return new MyViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        try {
            if (list.size() > 0){

                try {

                    String date = "";
                    final TransHistory hostoryObj = list.get(position);


                    final String strCurrentDate = hostoryObj.transactionDate;
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    final Date newDate = format.parse(strCurrentDate);
                    format = new SimpleDateFormat("dd MMM yyyy");
                    date = format.format(newDate);

                    holder.trans_date.setText(date);
//                if(hostoryObj.amount != null) {
                    holder.trans_amnt.setText(hostoryObj.amount + " " + hostoryObj.currency);
//                }
                    holder.trans_status.setText(hostoryObj.transactionStatus);
                    holder.trans_mode.setText(hostoryObj.paymentMode);
                    if(hostoryObj.transactionType.equals("CR")) {
                        holder.circleview.setTitleText(hostoryObj.transactionType);
                        holder.circleview.setFillColor(context.getResources().getColor(R.color.gray));
                        holder.circleview.setStrokeColor(context.getResources().getColor(R.color.gray));
                    }

                   // setAnimation(holder.container, position);

                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void setAnimation(View viewToAnimate, int position)
    {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.push_left_in);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
//        Toast.makeText(context, "count : "+list.size(), Toast.LENGTH_SHORT).show();
        if(from.equalsIgnoreCase("home")){
            if(list.size()>3) {
                return 3;
            }else {
                return list.size();
            }
        }else {
            return list.size();
        }

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        CircleView circleview;
        TextView trans_status;
        TextView trans_date;
        TextView trans_mode;
        TextView trans_amnt;
        LinearLayout container;

        public MyViewHolder(View itemView) {
            super(itemView);
            trans_amnt      = (TextView) itemView.findViewById(R.id.amnt);
            trans_date      = (TextView) itemView.findViewById(R.id.trans_date);
            trans_mode      = (TextView) itemView.findViewById(R.id.trans_mode);
            trans_status    = (TextView) itemView.findViewById(R.id.trans_status);
            circleview      = (CircleView) itemView.findViewById(R.id.circleview);
            container       = (LinearLayout) itemView.findViewById(R.id.container);
        }
    }
}
