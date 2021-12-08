package cnergee.sbbroadband.retroObj;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PckgListData {

@SerializedName("status")
@Expose
public Integer status;
@SerializedName("msg")
@Expose
public String msg;
@SerializedName("package_list")
@Expose
public List<PackageList> packageList = null;

}