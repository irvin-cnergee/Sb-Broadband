package cnergee.sbbroadband.obj;

public class RenewHostoryObj
{
    private String created_date;

    private String amount;

    private String txn_status;

    private String package_name;

    private String payment_mode;

    private int pg_txn_id;

    private String package_id;

    private String cheque_dd_no;

    private String currency;

    private String subscriber_txn_id;

    public String getCreated_date ()
    {
        return created_date;
    }

    public void setCreated_date (String created_date)
    {
        this.created_date = created_date;
    }

    public String getAmount ()
    {
        return amount;
    }

    public void setAmount (String amount)
    {
        this.amount = amount;
    }

    public String getTxn_status ()
    {
        return txn_status;
    }

    public void setTxn_status (String txn_status)
    {
        this.txn_status = txn_status;
    }

    public String getPackage_name ()
    {
        return package_name;
    }

    public void setPackage_name (String package_name)
    {
        this.package_name = package_name;
    }

    public String getPayment_mode ()
    {
        return payment_mode;
    }

    public void setPayment_mode (String payment_mode)
    {
        this.payment_mode = payment_mode;
    }

    public int getPg_txn_id ()
{
    return pg_txn_id;
}

    public void setPg_txn_id (int pg_txn_id)
    {
        this.pg_txn_id = pg_txn_id;
    }

    public String getPackage_id ()
    {
        return package_id;
    }

    public void setPackage_id (String package_id)
    {
        this.package_id = package_id;
    }

    public String getCheque_dd_no ()
    {
        return cheque_dd_no;
    }

    public void setCheque_dd_no (String cheque_dd_no)
    {
        this.cheque_dd_no = cheque_dd_no;
    }

    public String getCurrency ()
    {
        return currency;
    }

    public void setCurrency (String currency)
    {
        this.currency = currency;
    }

    public String getSubscriber_txn_id ()
    {
        return subscriber_txn_id;
    }

    public void setSubscriber_txn_id (String subscriber_txn_id)
    {
        this.subscriber_txn_id = subscriber_txn_id;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [created_date = "+created_date+", amount = "+amount+", txn_status = "+txn_status+", package_name = "+package_name+", payment_mode = "+payment_mode+", pg_txn_id = "+pg_txn_id+", package_id = "+package_id+", cheque_dd_no = "+cheque_dd_no+", currency = "+currency+", subscriber_txn_id = "+subscriber_txn_id+"]";
    }
}
