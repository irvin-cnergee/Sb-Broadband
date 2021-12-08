package cnergee.sbbroadband.retroObj;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VersionResponse {

    @SerializedName("app_version")
    @Expose
    public String appVersion;
    @SerializedName("is_compulsory")
    @Expose
    public Integer isCompulsory;

}