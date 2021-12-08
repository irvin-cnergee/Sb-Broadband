package cnergee.sbbroadband.retroObj;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class TransactionData implements Serializable{

@SerializedName("status")
@Expose
public Integer status;
@SerializedName("message")
@Expose
public String message;
@SerializedName("transHistory")
@Expose
public List<TransHistory> transHistory = null;

}