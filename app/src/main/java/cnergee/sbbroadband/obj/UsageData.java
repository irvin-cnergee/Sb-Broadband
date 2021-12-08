package cnergee.sbbroadband.obj;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UsageData {

    @SerializedName("allotedData")
    @Expose
    public long allotedData;
    @SerializedName("remainingData")
    @Expose
    public long remainingData;
    @SerializedName("usedData")
    @Expose
    public long usedData;
    @SerializedName("expiry_dt")
    @Expose
    public String expiryDt;
    @SerializedName("remainingDays")
    @Expose
    public Integer remainingDays;
    @SerializedName("totaldays")
    @Expose
    public Integer totaldays;
    @SerializedName("isExpired")
    @Expose
    public Integer isExpired;

}