package cnergee.sbbroadband.retroObj;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OnlineRenewResponse {

    @SerializedName("status")
    @Expose
    public Integer status;
    @SerializedName("pay_id")
    @Expose
    public String payId;

}