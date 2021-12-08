package cnergee.sbbroadband.retroObj;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EntityDatum {

@SerializedName("id")
@Expose
public Integer id;
@SerializedName("entity_name")
@Expose
public String entityName;
@SerializedName("server_ip")
@Expose
public String serverIp;
@SerializedName("entity_id")
@Expose
public Integer entityId;

}