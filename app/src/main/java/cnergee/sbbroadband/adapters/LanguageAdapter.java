package cnergee.sbbroadband.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cnergee.sbbroadband.LoginActivity;
import cnergee.sbbroadband.R;
import cnergee.sbbroadband.retroObj.Language;

/**
 * Created by Siddhesh on 8/2/2017.
 */

public class LanguageAdapter extends BaseAdapter {

    List<Language> langs = new ArrayList<>();
    Context context;
    LayoutInflater inflater = null;

    public LanguageAdapter(LoginActivity loginActivity, List<Language> langs) {
        context = loginActivity;
        this.langs = langs;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return langs.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    class Holder{
        TextView tv;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View cview = null;
        cview = inflater.inflate(R.layout.row_item_comp,null);
        Holder holder = new Holder();
        holder.tv = (TextView) cview.findViewById(R.id.tv_name);

//        LanguageData data = langs.get(i);
        Language data = langs.get(i);

        holder.tv.setText(data.language);

        return cview;
    }
}
