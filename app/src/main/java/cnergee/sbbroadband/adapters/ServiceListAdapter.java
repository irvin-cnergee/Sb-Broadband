package cnergee.sbbroadband.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import cnergee.sbbroadband.R;
import cnergee.sbbroadband.ServiceDashboardActivity;
import cnergee.sbbroadband.obj.ServiceData;
import cnergee.sbbroadband.widgets.CircleView;

/**
 * Created by Siddhesh on 6/28/2017.
 */

public class ServiceListAdapter extends RecyclerView.Adapter<ServiceListAdapter.MyViewHolder> {

    ArrayList<ServiceData> list = new ArrayList<>();
    Context context;

    public ServiceListAdapter(ServiceDashboardActivity serviceDashboardActivity, ArrayList<ServiceData> serviceList) {
        context = serviceDashboardActivity;
        list = serviceList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.servcie_list,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ServiceData data = list.get(position);

        holder.tv_adrs.setText(data.getAddress());
        holder.tv_subname.setText(data.getSubname());
        holder.tv_tckt.setText(data.getTckt());

        holder.cv.setTitleText(String.valueOf(data.getSubname().charAt(0)));
        holder.cv.setFillColor((Color.rgb((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255))));

        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(context, ServiceDetailsActivity.class);
//                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_subname, tv_adrs, tv_tckt;
        CircleView cv;

        public MyViewHolder(View itemView) {
            super(itemView);

            tv_subname = (TextView)itemView.findViewById(R.id.tv_subname);
            tv_adrs = (TextView)itemView.findViewById(R.id.tv_adrs);
            tv_tckt = (TextView)itemView.findViewById(R.id.tv_tckt);
            cv = (CircleView) itemView.findViewById(R.id.circleview);

        }
    }
}
