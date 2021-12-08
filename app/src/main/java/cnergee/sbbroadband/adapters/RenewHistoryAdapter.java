package cnergee.sbbroadband.adapters;

import android.content.Context;
import android.content.SharedPreferences;
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
import cnergee.sbbroadband.retroObj.TransList;
import cnergee.sbbroadband.utils.MyUtils;

/**
 * Created by Siddhesh on 5/23/2017.
 */

public class RenewHistoryAdapter extends RecyclerView.Adapter<RenewHistoryAdapter.MyViewHolder> {

    List<TransList> hist_list = new ArrayList<>();
    Context context;
    String from = "";
    String username = "";
    SharedPreferences sharedPreferences;

    int lastPosition = -1;

    public RenewHistoryAdapter(Context ctx, List<TransList> r_list, String from) {
        context = ctx;
        hist_list = r_list;
        this.from = from;
        sharedPreferences = context.getSharedPreferences(MyUtils.MY_PREF,Context.MODE_PRIVATE);
        username = sharedPreferences.getString(MyUtils.USER_NAME,"");
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.renew_history_layout,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        try {
            if(hist_list.size() > 0) {
//                RenewHostoryObj hostoryObj = hist_list.get(position);

                final TransList hostoryObj = hist_list.get(position);
                final String strCurrentDate = hostoryObj.createdDate;
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
                final Date newDate = format.parse(strCurrentDate);

                format = new SimpleDateFormat("dd MMM yyyy");
                final String date = format.format(newDate);

//                holder.trans_date.setText(strCurrentDate);
                holder.trans_date.setText(date);
                holder.trans_amnt.setText(hostoryObj.amount+" "+hostoryObj.currency);
                holder.trans_no.setText(hostoryObj.packageName);
                holder.trans_mode.setText(hostoryObj.paymentMode);
                final String packageName = hostoryObj.packageName;

//                if(from.equals("MyTransaction")) {

                   /* holder.ll_trans_history.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
//                            Toast.makeText(context, "amount : " + hostoryObj.amount + " " + hostoryObj.currency, Toast.LENGTH_SHORT).show();
                            View v = LayoutInflater.from(context).inflate(R.layout.receipt,null);
                            final Dialog dialog = new Dialog(context);
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.setContentView(v);
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                            TextView tv_username = (TextView)v.findViewById(R.id.tv_username);
                            TextView tv_pckg_name = (TextView)v.findViewById(R.id.tv_pckg_name);
                            TextView tv_pckg_rate = (TextView)v.findViewById(R.id.tv_pckg_rate);
                            TextView tv_trans_date = (TextView)v.findViewById(R.id.tv_trans_date);
                            TextView tv_mode = (TextView)v.findViewById(R.id.tv_mode);
                            TextView tv_no_title = (TextView)v.findViewById(R.id.tv_no_title);
                            TextView tv_no = (TextView)v.findViewById(R.id.tv_no);
                            TextView tv_order_no = (TextView)v.findViewById(R.id.tv_order_no);
                            TextView tv_status = (TextView)v.findViewById(R.id.tv_status);
                            TextView tv_back = (TextView)v.findViewById(R.id.tv_back);
                            LinearLayout ll_dd_id = (LinearLayout) v.findViewById(R.id.ll_dd_id);

                            tv_username.setText(username);
                            tv_pckg_name.setText(packageName);
                            tv_trans_date.setText(date);
                            tv_pckg_rate.setText(hostoryObj.amount+" "+hostoryObj.currency);
                            tv_mode.setText(hostoryObj.paymentMode);
                            tv_status.setText(hostoryObj.txnStatus);
                            tv_order_no.setText(hostoryObj.subscriberTxnId+"");

                            if(hostoryObj.paymentMode.equalsIgnoreCase("cheque")){
                                ll_dd_id.setVisibility(View.VISIBLE);
                                tv_no_title.setText("Cheque Number");
                                tv_no.setText(hostoryObj.chequeDdNo);
                            }else if(hostoryObj.paymentMode.equalsIgnoreCase("dd")){
                                ll_dd_id.setVisibility(View.VISIBLE);
                                tv_no_title.setText("DD Number");
                                tv_no.setText(hostoryObj.chequeDdNo);
                            }else {
                                ll_dd_id.setVisibility(View.GONE);
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
                    });*/
//                }

                if(position >lastPosition) {

                    Animation animation = AnimationUtils.loadAnimation(context,
                            R.anim.fall_down);
                    holder.itemView.startAnimation(animation);
                    lastPosition = position;
                }

            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        if(from.equalsIgnoreCase("home")){
            if(hist_list.size()>3) {
                return 3;
            }else {
                return hist_list.size();
            }
        }else {
            return hist_list.size();
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView trans_no;
        TextView trans_date;
        TextView trans_mode;
        TextView trans_amnt;
        LinearLayout ll_trans_history;

        public MyViewHolder(View itemView) {
            super(itemView);

            trans_amnt = (TextView) itemView.findViewById(R.id.amnt);
            trans_date = (TextView) itemView.findViewById(R.id.trans_date);
            trans_mode = (TextView) itemView.findViewById(R.id.mode);
            trans_no = (TextView) itemView.findViewById(R.id.trans_no);
            ll_trans_history = (LinearLayout) itemView.findViewById(R.id.ll_trans_history);
        }
    }
}
