package developer.app.interface_;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by Jyoti on 7/30/2016.
 */
public interface IPresenter {

    void web_response(ArrayList<String> response, Context context, String method_name);
}
