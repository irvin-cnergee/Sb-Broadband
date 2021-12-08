package cnergee.sbbroadband.retroObj;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Due {

    @SerializedName("subscriber_dues_id")
    @Expose
    public Integer dueId;
    @SerializedName("entity_id")
    @Expose
    public Integer entityId;
    @SerializedName("subscriber_id")
    @Expose
    public Integer subscriberId;
    @SerializedName("amount_due")
    @Expose
    public Integer amountDue;
    @SerializedName("currency")
    @Expose
    public String currency;
    @SerializedName("package_id")
    @Expose
    public Integer packageId;
    @SerializedName("amount_for")
    @Expose
    public String amountFor;
    @SerializedName("due_date")
    @Expose
    public String dueDate;
    @SerializedName("due_status")
    @Expose
    public Integer dueStatus;
    @SerializedName("renewal_id")
    @Expose
    public Integer renewalId;
    @SerializedName("subscriber_dues_created_on")
    @Expose
    public String subscriberDuesCreatedOn;
    @SerializedName("subscriber_dues_created_by")
    @Expose
    public Integer subscriberDuesCreatedBy;
    @SerializedName("subscriber_dues_modified_on")
    @Expose
    public Object subscriberDuesModifiedOn;
    @SerializedName("subscriber_dues_modified_by")
    @Expose
    public Object subscriberDuesModifiedBy;
    @SerializedName("is_scheduled")
    @Expose
    public Integer isScheduled;

}