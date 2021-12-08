package cnergee.sbbroadband.retroObj;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Language implements Serializable{

    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("language")
    @Expose
    public String language;
    @SerializedName("symbol")
    @Expose
    public String symbol;
    @SerializedName("is_default")
    @Expose
    public Integer isDefault;

}