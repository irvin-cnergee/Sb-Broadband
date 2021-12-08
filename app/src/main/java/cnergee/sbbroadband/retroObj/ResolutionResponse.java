package cnergee.sbbroadband.retroObj;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResolutionResponse {

    @SerializedName("reply")
    @Expose
    public String reply;
    @SerializedName("authdate")
    @Expose
    public String authdate;

}