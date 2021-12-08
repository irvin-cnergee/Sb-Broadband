package cnergee.sbbroadband.obj;

public class PickupObj
{
    private String amount;

    private int ispostpaid;

    private String time;

    private String address;

    private String name;

    public String getAmount ()
    {
        return amount;
    }

    public void setAmount (String amount)
    {
        this.amount = amount;
    }

    public int getIspostpaid ()
    {
        return ispostpaid;
    }

    public void setIspostpaid (int ispostpaid)
    {
        this.ispostpaid = ispostpaid;
    }

    public String getTime ()
    {
        return time;
    }

    public void setTime (String time)
    {
        this.time = time;
    }

    public String getAddress ()
    {
        return address;
    }

    public void setAddress (String address)
    {
        this.address = address;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [amount = "+amount+", ispostpaid = "+ispostpaid+", time = "+time+", address = "+address+", name = "+name+"]";
    }
}