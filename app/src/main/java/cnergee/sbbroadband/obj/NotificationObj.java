package cnergee.sbbroadband.obj;

public class NotificationObj
{
    private String noti_id;

    private String msg;

    public String getNoti_id ()
    {
        return noti_id;
    }

    public void setNoti_id (String noti_id)
    {
        this.noti_id = noti_id;
    }

    public String getMsg ()
    {
        return msg;
    }

    public void setMsg (String msg)
    {
        this.msg = msg;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [noti_id = "+noti_id+", msg = "+msg+"]";
    }
}