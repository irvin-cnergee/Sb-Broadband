package cnergee.sbbroadband.retroObj;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CommonResolutionData {

    @SerializedName("fieldCount")
    @Expose
    public Integer fieldCount;
    @SerializedName("affectedRows")
    @Expose
    public Integer affectedRows;
    @SerializedName("insertId")
    @Expose
    public Integer insertId;
    @SerializedName("serverStatus")
    @Expose
    public Integer serverStatus;
    @SerializedName("warningCount")
    @Expose
    public Integer warningCount;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("protocol41")
    @Expose
    public Boolean protocol41;
    @SerializedName("changedRows")
    @Expose
    public Integer changedRows;

}