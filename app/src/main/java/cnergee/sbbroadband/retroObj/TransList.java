package cnergee.sbbroadband.retroObj;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TransList {

    @SerializedName("subscriber_txn_id")
    @Expose
    public Integer subscriberTxnId;
    @SerializedName("payment_mode")
    @Expose
    public String paymentMode;
    @SerializedName("cheque_dd_no")
    @Expose
    public String chequeDdNo;
    @SerializedName("package_id")
    @Expose
    public Integer packageId;
    @SerializedName("package_name")
    @Expose
    public String packageName;
    @SerializedName("amount")
    @Expose
    public Integer amount;
    @SerializedName("currency")
    @Expose
    public String currency;
    @SerializedName("txn_status")
    @Expose
    public String txnStatus;
    @SerializedName("pg_txn_id")
    @Expose
    public Object pgTxnId;
    @SerializedName("created_date")
    @Expose
    public String createdDate;

}