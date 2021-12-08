package cnergee.sbbroadband.retroObj;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SubscriberPckDatum {

    @SerializedName("subscriberId")
    @Expose
    public Integer subscriberId;
    @SerializedName("subscriberName")
    @Expose
    public String subscriberName;
    @SerializedName("firstName")
    @Expose
    public String firstName;
    @SerializedName("lastName")
    @Expose
    public String lastName;
    @SerializedName("areaId")
    @Expose
    public Integer areaId;
    @SerializedName("entityId")
    @Expose
    public Integer entityId;
    @SerializedName("connectionType")
    @Expose
    public String connectionType;
    @SerializedName("package_id")
    @Expose
    public Integer packageId;
    @SerializedName("valid_from")
    @Expose
    public String validFrom;
    @SerializedName("valid_to")
    @Expose
    public String validTo;
    @SerializedName("total")
    @Expose
    public Integer total;
    @SerializedName("currency")
    @Expose
    public String currency;
    @SerializedName("auth_type")
    @Expose
    public String authType;
    @SerializedName("is_current")
    @Expose
    public Integer isCurrent;
    @SerializedName("package_name")
    @Expose
    public String packageName;
    @SerializedName("payment_gateway")
    @Expose
    public Integer payment_gateway;
    @SerializedName("allow_online_renew")
    @Expose
    public Integer isOnlineRenew;
    @SerializedName("entity_logo")
    @Expose
    public String entityLogo;
    @SerializedName("allow_change_package")
    @Expose
    public Integer allow_change_package;
    @SerializedName("renewal")
    @Expose
    public Integer renewal;
    @SerializedName("isPckgAvailable")
    @Expose
    public Integer isPckgAvailable;

}