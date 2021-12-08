package cnergee.sbbroadband.retroObj;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProfileData {

    @SerializedName("status")
    @Expose
    public Integer status;
    @SerializedName("msg")
    @Expose
    public String msg;
    @SerializedName("subscriber_username")
    @Expose
    public String subscriberUsername;
    @SerializedName("first_name")
    @Expose
    public String firstName;
    @SerializedName("middle_name")
    @Expose
    public String middleName;
    @SerializedName("last_name")
    @Expose
    public String lastName;
    @SerializedName("dob")
    @Expose
    public String dob;
    @SerializedName("gender")
    @Expose
    public String gender;
    @SerializedName("contact_no")
    @Expose
    public String contactNo;
    @SerializedName("email")
    @Expose
    public String email;
    @SerializedName("address_line_1")
    @Expose
    public String address_line_1;
    @SerializedName("address_line_2")
    @Expose
    public String address_line_2;
    @SerializedName("nationality")
    @Expose
    public String nationality;
    @SerializedName("country")
    @Expose
    public String country;
    @SerializedName("city")
    @Expose
    public String city;

}