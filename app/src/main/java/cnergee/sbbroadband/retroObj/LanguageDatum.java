package cnergee.sbbroadband.retroObj;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class LanguageDatum implements Serializable{

    @SerializedName("languages")
    @Expose
    public List<Language> languages = null;

}