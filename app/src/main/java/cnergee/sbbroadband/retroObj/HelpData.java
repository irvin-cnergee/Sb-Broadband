package cnergee.sbbroadband.retroObj;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HelpData {

    @SerializedName("status")
    @Expose
    public Integer status;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("entityId")
    @Expose
    public Integer entityId;
    @SerializedName("content")
    @Expose
    public String content;
    @SerializedName("entityStatus")
    @Expose
    public Integer entityStatus;
    @SerializedName("created_on")
    @Expose
    public String createdOn;
    @SerializedName("created_by")
    @Expose
    public Integer createdBy;
    @SerializedName("updated_on")
    @Expose
    public String updatedOn;
    @SerializedName("updated_by")
    @Expose
    public Integer updatedBy;
    @SerializedName("content_for")
    @Expose
    public String contentFor;
}