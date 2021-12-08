package cnergee.sbbroadband.adapters;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cnergee.sbbroadband.NotificationActivity;
import cnergee.sbbroadband.R;
import cnergee.sbbroadband.obj.NotificationObj;
import cnergee.sbbroadband.retroObj.Notification;

/**
 * Created by Siddhesh on 5/26/2017.
 */

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {

    Context context;
    List<Notification> notification_list = new ArrayList<>();
    private List<NotificationObj> itemsPendingRemoval;

    private static final int PENDING_REMOVAL_TIMEOUT = 3000; // 3sec
    private Handler handler = new Handler(); // hanlder for running delayed runnables
    HashMap<String, Runnable> pendingRunnables = new HashMap<>(); // map of items to pending runnables, so we can cancel a removal if need be


    public NotificationAdapter(NotificationActivity notificationActivity, List<Notification> notificationJson) {

        context = notificationActivity;
        notification_list = notificationJson;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_item,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Notification notification = notification_list.get(position);

        holder.tv_notify_item.setText(notification.notificationMessage);


    }

    @Override
    public int getItemCount() {
        return notification_list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_notify_item;


        public MyViewHolder(View itemView) {
            super(itemView);

            tv_notify_item = (TextView)itemView.findViewById(R.id.notification_item);

        }
    }

    /*private void undoOpt(NotificationObj customer) {
        Runnable pendingRemovalRunnable = pendingRunnables.get(customer);
        pendingRunnables.remove(customer);
        if (pendingRemovalRunnable != null)
            handler.removeCallbacks(pendingRemovalRunnable);
        itemsPendingRemoval.remove(customer);
        // this will rebind the row in "normal" state
        notifyItemChanged(notification_list.indexOf(customer));
    }

    public void pendingRemoval(int position) {

        final NotificationObj obj = notification_list.get(position);
        if (!itemsPendingRemoval.contains(obj)) {
            itemsPendingRemoval.add(obj);
            // this will redraw row in "undo" state
            notifyItemChanged(position);
            // let's create, store and post a runnable to remove the data
            Runnable pendingRemovalRunnable = new Runnable() {
                @Override
                public void run() {
                    remove(notification_list.indexOf(obj));
                }
            };
            handler.postDelayed(pendingRemovalRunnable, PENDING_REMOVAL_TIMEOUT);
            pendingRunnables.put(obj.getNoti_id(), pendingRemovalRunnable);
        }
    }

    public void remove(int position) {
        NotificationObj obj = notification_list.get(position);
        if (itemsPendingRemoval.contains(obj)) {
            itemsPendingRemoval.remove(obj);
        }
        if (notification_list.contains(obj)) {
            notification_list.remove(position);
            notifyItemRemoved(position);
        }
    }

    public boolean isPendingRemoval(int position) {
        NotificationObj obj = notification_list.get(position);
        return itemsPendingRemoval.contains(obj);
    }
*/
}
