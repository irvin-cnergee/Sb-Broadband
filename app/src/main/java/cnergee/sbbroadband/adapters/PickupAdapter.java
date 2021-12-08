package cnergee.sbbroadband.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import cnergee.sbbroadband.R;
import cnergee.sbbroadband.obj.PickupObj;
import cnergee.sbbroadband.widgets.CircleView;
import cnergee.sbbroadband.widgets.MyTextView;

/**
 * Created by Jyoti on 6/16/2017.
 */

public class PickupAdapter  extends RecyclerView.Adapter<PickupAdapter.MyViewHolder> {

    ArrayList<PickupObj> al_pickups = new ArrayList<>();
    Context context;


    public PickupAdapter(Context ctx, ArrayList<PickupObj> json) {
        context = ctx;
        al_pickups = json;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rowitem_pickup,parent,false);
        return new PickupAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        PickupObj pickupObj=al_pickups.get(position);
        holder.tv_time.setText(pickupObj.getTime());
        holder.tv_name.setText(pickupObj.getName());
        //holder.tv_address.setText(pickupObj.getAddress());
        holder.tv_address.setText(context.getString(R.string.address));
        holder.tv_amount.setText(pickupObj.getAmount());

        if(al_pickups.get(position).getIspostpaid() == 1){
            holder.circleview.setTitleText("PO");
        }else {
            holder.circleview.setTitleText("PR");
        }

        holder.circleview.setFillColor((Color.rgb((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255))));
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        MyTextView tv_time,tv_name,tv_address,tv_amount;
        CircleView circleview;

        public MyViewHolder(View itemView) {
            super(itemView);

            tv_time = (MyTextView) itemView.findViewById(R.id.tv_time);
            circleview=(CircleView)itemView.findViewById(R.id.circleview);
            tv_name = (MyTextView) itemView.findViewById(R.id.tv_name);
            tv_address = (MyTextView) itemView.findViewById(R.id.tv_address);
            tv_amount = (MyTextView) itemView.findViewById(R.id.tv_amount);
        }
    }

    @Override
    public int getItemCount() {
        return al_pickups.size();
    }
}
