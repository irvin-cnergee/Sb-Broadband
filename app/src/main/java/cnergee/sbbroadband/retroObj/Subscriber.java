package cnergee.sbbroadband.retroObj;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Subscriber {

    @SerializedName("entity_id")
    @Expose
    public int entityId;
    @SerializedName("client_access_id")
    @Expose
    public String clientAccessId;
    @SerializedName("subscriber_status")
    @Expose
    public int subscriberStatus;
    @SerializedName("subscriber_id")
    @Expose
    public int subscriberId;
    @SerializedName("subscriber_area_id")
    @Expose
    public int subscriberAreaId;
    @SerializedName("subscriber_username")
    @Expose
    public String subscriberUsername;
    @SerializedName("subscriber_email")
    @Expose
    public String subscriberEmail;

}