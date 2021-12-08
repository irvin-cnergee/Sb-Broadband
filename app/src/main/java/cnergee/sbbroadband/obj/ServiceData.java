package cnergee.sbbroadband.obj;

public class ServiceData
{
    private String tckt;

    private String address;

    private String subname;

    public String getTckt ()
    {
        return tckt;
    }

    public void setTckt (String tckt)
    {
        this.tckt = tckt;
    }

    public String getAddress ()
    {
        return address;
    }

    public void setAddress (String address)
    {
        this.address = address;
    }

    public String getSubname ()
    {
        return subname;
    }

    public void setSubname (String subname)
    {
        this.subname = subname;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [tckt = "+tckt+", address = "+address+", subname = "+subname+"]";
    }
}

			