package cnergee.sbbroadband.retroObj;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Cable {

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
    public int total;
    @SerializedName("currency")
    @Expose
    public String currency;
    @SerializedName("package_validity")
    @Expose
    public String packageValidity;
    @SerializedName("package_name")
    @Expose
    public String packageName;
    @SerializedName("connectionType")
    @Expose
    public Integer connectionType;
    @SerializedName("ip")
    @Expose
    public String ip;
    @SerializedName("auth_type")
    @Expose
    public String authType;
    @SerializedName("is_current")
    @Expose
    public Integer isCurrent;
    @SerializedName("product_id")
    @Expose
    public String productId;

}