package cnergee.sbbroadband.obj;

public class ProfileData
{
    private String subscriber_username;

    private String status;

    private String contact_no;

    private String middle_name;

    private String msg;

    private String city;

    private String country;

    private String first_name;

    private String nationality;

    private String email;

    private String address;

    private String dob;

    private String last_name;

    private String gender;

    public String getSubscriber_username ()
    {
        return subscriber_username;
    }

    public void setSubscriber_username (String subscriber_username)
    {
        this.subscriber_username = subscriber_username;
    }

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
    }

    public String getContact_no ()
    {
        return contact_no;
    }

    public void setContact_no (String contact_no)
    {
        this.contact_no = contact_no;
    }

    public String getMiddle_name ()
    {
        return middle_name;
    }

    public void setMiddle_name (String middle_name)
    {
        this.middle_name = middle_name;
    }

    public String getMsg ()
    {
        return msg;
    }

    public void setMsg (String msg)
    {
        this.msg = msg;
    }

    public String getCity ()
    {
        return city;
    }

    public void setCity (String city)
    {
        this.city = city;
    }

    public String getCountry ()
    {
        return country;
    }

    public void setCountry (String country)
    {
        this.country = country;
    }

    public String getFirst_name ()
    {
        return first_name;
    }

    public void setFirst_name (String first_name)
    {
        this.first_name = first_name;
    }

    public String getNationality ()
    {
        return nationality;
    }

    public void setNationality (String nationality)
    {
        this.nationality = nationality;
    }

    public String getEmail ()
    {
        return email;
    }

    public void setEmail (String email)
    {
        this.email = email;
    }

    public String getAddress ()
    {
        return address;
    }

    public void setAddress (String address)
    {
        this.address = address;
    }

    public String getDob ()
    {
        return dob;
    }

    public void setDob (String dob)
    {
        this.dob = dob;
    }

    public String getLast_name ()
    {
        return last_name;
    }

    public void setLast_name (String last_name)
    {
        this.last_name = last_name;
    }

    public String getGender ()
    {
        return gender;
    }

    public void setGender (String gender)
    {
        this.gender = gender;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [subscriber_username = "+subscriber_username+", status = "+status+", contact_no = "+contact_no+", middle_name = "+middle_name+", msg = "+msg+", city = "+city+", country = "+country+", first_name = "+first_name+", nationality = "+nationality+", email = "+email+", address = "+address+", dob = "+dob+", last_name = "+last_name+", gender = "+gender+"]";
    }
}