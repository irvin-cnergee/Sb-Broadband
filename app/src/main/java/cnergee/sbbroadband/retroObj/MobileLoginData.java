package cnergee.sbbroadband.retroObj;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MobileLoginData {

@SerializedName("status")
@Expose
public String status;
@SerializedName("message")
@Expose
public String message;
@SerializedName("subscriber")
@Expose
public List<Subscriber> subscriber = null;

}