package cnergee.sbbroadband.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

import cnergee.sbbroadband.R;
import cnergee.sbbroadband.obj.Complaint;
import cnergee.sbbroadband.widgets.MyTextView;

public class ComplaintAdapter extends ArrayAdapter<Complaint>
{
    ArrayList<Complaint> complaints =new ArrayList<Complaint>();
    Context context;

    public ComplaintAdapter(Context context, int resource, ArrayList<Complaint> complaints)
    {
        super(context,resource,complaints);
        this.context=context;
        this.complaints=complaints;
    }


    public View getView(int position, View compView, ViewGroup compGroup)
    {

        Complaint comp=(Complaint)this.getItem(position);
        ViewHolder holder;

        if(compView==null)
        {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            compView=inflater.inflate(R.layout.row_item_comp, compGroup, false);
            holder=new ViewHolder();
            holder.tv_name=(MyTextView) compView.findViewById(R.id.tv_name);
            compView.setTag(holder);
        }

        else
        {
            holder=(ViewHolder)compView.getTag();
        }

        holder.tv_name.setText(comp.getComp_msg());

        return compView;
    }

    public static class ViewHolder
    {

        MyTextView tv_name;

    }

}
