package cnergee.sbbroadband.obj;

import java.io.Serializable;

public class LanguageData implements Serializable {
    private int id;

    private String symbol;

    private int is_default;

    private String language;

    public int getId ()
    {
        return id;
    }

    public void setId (int id)
    {
        this.id = id;
    }

    public String getSymbol ()
    {
        return symbol;
    }

    public void setSymbol (String symbol)
    {
        this.symbol = symbol;
    }

    public int getIs_default ()
    {
        return is_default;
    }

    public void setIs_default (int is_default)
    {
        this.is_default = is_default;
    }

    public String getLanguage ()
    {
        return language;
    }

    public void setLanguage (String language)
    {
        this.language = language;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [id = "+id+", symbol = "+symbol+", is_default = "+is_default+", language = "+language+"]";
    }
}