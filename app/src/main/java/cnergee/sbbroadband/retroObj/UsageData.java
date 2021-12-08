package cnergee.sbbroadband.retroObj;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UsageData {

    @SerializedName("allotedData")
    @Expose
    public Integer allotedData;
    @SerializedName("remainingData")
    @Expose
    public Integer remainingData;
    @SerializedName("expiry_dt")
    @Expose
    public String expiryDt;
    @SerializedName("remainingDays")
    @Expose
    public Integer remainingDays;
    @SerializedName("totaldays")
    @Expose
    public Integer totaldays;

}