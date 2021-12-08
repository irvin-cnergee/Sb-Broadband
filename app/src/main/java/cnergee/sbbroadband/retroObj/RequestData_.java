package cnergee.sbbroadband.retroObj;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RequestData_ {

    @SerializedName("subscriber_id")
    @Expose
    public Integer subscriber_id;
    @SerializedName("subscriberName")
    @Expose
    public String subscriberName;

}