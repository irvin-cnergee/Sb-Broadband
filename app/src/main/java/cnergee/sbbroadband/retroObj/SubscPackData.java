package cnergee.sbbroadband.retroObj;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Siddhesh on 8/9/2017.
 */

public class SubscPackData {

    @SerializedName("details")
    @Expose
    public List<SubscriberPckDatum> details = null;
}
