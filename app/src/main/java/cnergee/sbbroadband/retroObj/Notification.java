package cnergee.sbbroadband.retroObj;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Notification {

    @SerializedName("notifyId")
    @Expose
    public Integer notifyId;
    @SerializedName("subscriberId")
    @Expose
    public Integer subscriberId;
    @SerializedName("userId")
    @Expose
    public Object userId;
    @SerializedName("entityId")
    @Expose
    public Integer entityId;
    @SerializedName("notification_message")
    @Expose
    public String notificationMessage;
    @SerializedName("isNotify")
    @Expose
    public Integer isNotify;
    @SerializedName("createdBy")
    @Expose
    public Integer createdBy;
    @SerializedName("createdOn")
    @Expose
    public String createdOn;

}