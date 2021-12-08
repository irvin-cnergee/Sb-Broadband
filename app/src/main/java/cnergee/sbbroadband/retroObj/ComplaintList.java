package cnergee.sbbroadband.retroObj;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ComplaintList {

    @SerializedName("complaint_id")
    @Expose
    public Integer complaintId;
    @SerializedName("complaint_title")
    @Expose
    public String complaintTitle;

}