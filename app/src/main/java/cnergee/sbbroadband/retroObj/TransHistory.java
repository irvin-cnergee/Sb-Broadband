package cnergee.sbbroadband.retroObj;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TransHistory implements Serializable {

@SerializedName("transactionType")
@Expose
public String transactionType;
@SerializedName("amount")
@Expose
public double amount;
@SerializedName("currency")
@Expose
public String currency;
@SerializedName("transactionDate")
@Expose
public String transactionDate;
@SerializedName("paymentMode")
@Expose
public String paymentMode;
@SerializedName("transactionStatus")
@Expose
public String transactionStatus;

}