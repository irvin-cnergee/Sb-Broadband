package developer.app.interface_;

import android.content.Context;

import developer.app.obj.CommonWebResponse;


/**
 * Created by Jyoti on 8/1/2016.
 */

public interface IReturn {

    public void return_object(CommonWebResponse commonWebResponse, Context context, String method_name);

    public void return_string(String response, Context context, String method_name);


}
