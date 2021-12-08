package cnergee.sbbroadband.retroObj;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CurrentPckgRateData {

    @SerializedName("amount")
    @Expose
    public Double amount;
    @SerializedName("currency")
    @Expose
    public String currency;
    @SerializedName("areaStatus")
    @Expose
    public Integer areaStatus;
    @SerializedName("packageStatus")
    @Expose
    public Integer packageStatus;

}