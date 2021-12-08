package cnergee.sbbroadband.retroObj;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ActiveFlagData {

    @SerializedName("status")
    @Expose
    public Integer status;
    @SerializedName("msg")
    @Expose
    public String msg;
    @SerializedName("ActiveFlag")
    @Expose
    public Integer activeFlag;

}