package cnergee.sbbroadband.adapters;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PackageHistoryData {

    @SerializedName("subscriber")
    @Expose
    public Subscriber subscriber;
    @SerializedName("history")
    @Expose
    public List<History> history = null;
    @SerializedName("expiryDate")
    @Expose
    public String expiryDate;
    @SerializedName("isExpired")
    @Expose
    public Integer isExpired;
    @SerializedName("packageName")
    @Expose
    public String packageName;
    @SerializedName("packageId")
    @Expose
    public Integer packageId;
    @SerializedName("packageType")
    @Expose
    public Integer packageType;

    public class Subscriber {

        @SerializedName("id")
        @Expose
        public Integer id;
        @SerializedName("group_id")
        @Expose
        public Integer groupId;
        @SerializedName("subscriber_first_name")
        @Expose
        public String subscriberFirstName;
        @SerializedName("subscriber_last_name")
        @Expose
        public String subscriberLastName;
        @SerializedName("subscriber_middle_name")
        @Expose
        public String subscriberMiddleName;
        @SerializedName("subscriber_username")
        @Expose
        public String subscriberUsername;
        @SerializedName("subscriber_password")
        @Expose
        public String subscriberPassword;
        @SerializedName("my_page_username")
        @Expose
        public String myPageUsername;
        @SerializedName("my_page_password")
        @Expose
        public String myPagePassword;
        @SerializedName("is_wifi")
        @Expose
        public Integer isWifi;
        @SerializedName("billing_scheme")
        @Expose
        public String billingScheme;
        @SerializedName("billing_cycle")
        @Expose
        public String billingCycle;
        @SerializedName("connection_media")
        @Expose
        public Integer connectionMedia;
        @SerializedName("nas_type")
        @Expose
        public String nasType;
        @SerializedName("connection_type")
        @Expose
        public String connectionType;
        @SerializedName("authentication_type")
        @Expose
        public String authenticationType;
        @SerializedName("mac_count_allowed")
        @Expose
        public Integer macCountAllowed;
        @SerializedName("mac_binding_enable")
        @Expose
        public Integer macBindingEnable;
        @SerializedName("nas_ip_enable")
        @Expose
        public Integer nasIpEnable;
        @SerializedName("nas_port_enable")
        @Expose
        public Integer nasPortEnable;
        @SerializedName("vlan_binding_enable")
        @Expose
        public Integer vlanBindingEnable;
        @SerializedName("allow_online_renewal")
        @Expose
        public Integer allowOnlineRenewal;
        @SerializedName("allow_self_tos_update")
        @Expose
        public Integer allowSelfTosUpdate;
        @SerializedName("subscriber_email")
        @Expose
        public String subscriberEmail;
        @SerializedName("dob")
        @Expose
        public String dob;
        @SerializedName("gender")
        @Expose
        public String gender;
        @SerializedName("contact_no")
        @Expose
        public String contactNo;
        @SerializedName("id_proof_type")
        @Expose
        public String idProofType;
        @SerializedName("id_proof_no")
        @Expose
        public String idProofNo;
        @SerializedName("id_proof_file")
        @Expose
        public String idProofFile;
        @SerializedName("address_proof_type")
        @Expose
        public String addressProofType;
        @SerializedName("address_proof_no")
        @Expose
        public String addressProofNo;
        @SerializedName("address_proof_file")
        @Expose
        public String addressProofFile;
        @SerializedName("visit_date")
        @Expose
        public Object visitDate;
        @SerializedName("other_charges_type")
        @Expose
        public String otherChargesType;
        @SerializedName("other_charges")
        @Expose
        public String otherCharges;
        @SerializedName("comment")
        @Expose
        public String comment;
        @SerializedName("additional_info")
        @Expose
        public String additionalInfo;
        @SerializedName("area_id")
        @Expose
        public Integer areaId;
        @SerializedName("location")
        @Expose
        public String location;
        @SerializedName("building")
        @Expose
        public String building;
        @SerializedName("tower")
        @Expose
        public String tower;
        @SerializedName("floor")
        @Expose
        public String floor;
        @SerializedName("flat_no")
        @Expose
        public String flatNo;
        @SerializedName("postal_code")
        @Expose
        public String postalCode;
        @SerializedName("address_line_1")
        @Expose
        public String addressLine1;
        @SerializedName("address_line_2")
        @Expose
        public String addressLine2;
        @SerializedName("premise_type")
        @Expose
        public String premiseType;
        @SerializedName("enquiry_source")
        @Expose
        public String enquirySource;
        @SerializedName("field_agent_name")
        @Expose
        public String fieldAgentName;
        @SerializedName("entity_id")
        @Expose
        public Integer entityId;
        @SerializedName("installed_device")
        @Expose
        public String installedDevice;
        @SerializedName("device_serial_no")
        @Expose
        public String deviceSerialNo;
        @SerializedName("installation_charges")
        @Expose
        public String installationCharges;
        @SerializedName("auth_ip_address")
        @Expose
        public String authIpAddress;
        @SerializedName("auth_mac_address")
        @Expose
        public String authMacAddress;
        @SerializedName("wallet_balance")
        @Expose
        public Integer walletBalance;
        @SerializedName("company_name")
        @Expose
        public String companyName;
        @SerializedName("caf_number")
        @Expose
        public String cafNumber;
        @SerializedName("caf_date")
        @Expose
        public String cafDate;
        @SerializedName("simultaneous_sessions")
        @Expose
        public Integer simultaneousSessions;
        @SerializedName("currency")
        @Expose
        public Object currency;
        @SerializedName("unpaid_invoices_allowed")
        @Expose
        public Integer unpaidInvoicesAllowed;
        @SerializedName("static_ip")
        @Expose
        public Object staticIp;
        @SerializedName("status")
        @Expose
        public Integer status;
        @SerializedName("created_on")
        @Expose
        public String createdOn;
        @SerializedName("created_by")
        @Expose
        public Integer createdBy;
        @SerializedName("updated_on")
        @Expose
        public Object updatedOn;
        @SerializedName("updated_by")
        @Expose
        public Object updatedBy;
        @SerializedName("radiusSubscriberId")
        @Expose
        public Integer radiusSubscriberId;
        @SerializedName("PONumber")
        @Expose
        public Object pONumber;
        @SerializedName("subscriber_address_proof_Status")
        @Expose
        public Object subscriberAddressProofStatus;
        @SerializedName("subscriber_address_Status_update_by")
        @Expose
        public Object subscriberAddressStatusUpdateBy;
        @SerializedName("subscriber_address_Status_update_on")
        @Expose
        public Object subscriberAddressStatusUpdateOn;
        @SerializedName("subscriber_id_proof_Status")
        @Expose
        public Object subscriberIdProofStatus;
        @SerializedName("subscriber_id_Status_update_by")
        @Expose
        public Object subscriberIdStatusUpdateBy;
        @SerializedName("subscriber_id_Status_update_on")
        @Expose
        public Object subscriberIdStatusUpdateOn;
        @SerializedName("is_recurring_amount")
        @Expose
        public Integer isRecurringAmount;
        @SerializedName("area_name")
        @Expose
        public String areaName;

    }


    public class History {

        @SerializedName("renew_id")
        @Expose
        public Integer renewId;
        @SerializedName("package_id")
        @Expose
        public Integer packageId;
        @SerializedName("valid_from")
        @Expose
        public String validFrom;
        @SerializedName("valid_to")
        @Expose
        public String validTo;
        @SerializedName("renewal_type")
        @Expose
        public String renewalType;
        @SerializedName("is_current")
        @Expose
        public Integer isCurrent;
        @SerializedName("package_name")
        @Expose
        public String packageName;
        @SerializedName("created_at")
        @Expose
        public String createdAt;
        @SerializedName("amount_due")
        @Expose
        public double amountDue;
        @SerializedName("actual_amount")
        @Expose
        public double actualAmount;
        @SerializedName("changed_amount")
        @Expose
        public double changedAmount;
        @SerializedName("currency")
        @Expose
        public String currency;

    }

}