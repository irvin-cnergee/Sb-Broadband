package cnergee.sbbroadband.obj;

public class PckgList
{
    int pckg_id;

    private String currency = "";

    private String amount;

    private String days;

    private String pckg_name;

    String validity = "";

    String validity_unit = "";

    public String getAmount ()
    {
        return amount;
    }

    public void setAmount (String amount)
    {
        this.amount = amount;
    }

    public String getDays ()
    {
        return days;
    }

    public void setDays (String days)
    {
        this.days = days;
    }

    public String getPckg_name ()
    {
        return pckg_name;
    }

    public void setPckg_name (String pckg_name)
    {
        this.pckg_name = pckg_name;
    }

    public int getPckg_id() {
        return pckg_id;
    }

    public void setPckg_id(int pckg_id) {
        this.pckg_id = pckg_id;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getValidity() {
        return validity;
    }

    public void setValidity(String validity) {
        this.validity = validity;
    }

    public String getValidity_unit() {
        return validity_unit;
    }

    public void setValidity_unit(String validity_unit) {
        this.validity_unit = validity_unit;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [amount = "+amount+", days = "+days+", pckg_name = "+pckg_name+", packg_id = "+pckg_id+", currency = "+currency+", validity = "+validity+", validity_unit = "+validity_unit+"]";
    }
}

			