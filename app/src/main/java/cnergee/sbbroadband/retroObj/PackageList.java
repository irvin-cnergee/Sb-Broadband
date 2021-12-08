package cnergee.sbbroadband.retroObj;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PackageList {

    @SerializedName("package_id")
    @Expose
    public Integer packageId;
    @SerializedName("package_name")
    @Expose
    public String packageName;
    @SerializedName("expiration")
    @Expose
    public String expiration;
    @SerializedName("expiration_unit")
    @Expose
    public Integer expirationUnit;
    @SerializedName("dl_data_rate")
    @Expose
    public String dlDataRate;
    @SerializedName("ul_data_rate")
    @Expose
    public String ulDataRate;
    @SerializedName("amount")
    @Expose
    public Double amount;
    @SerializedName("currency")
    @Expose
    public String currency;
    @SerializedName("allow_change_package")
    @Expose
    public Integer allowChangePackage;

}