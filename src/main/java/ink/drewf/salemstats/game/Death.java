package ink.drewf.salemstats.game;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Death
{
    private final SimpleStringProperty time;
    private final SimpleStringProperty name;
    private final SimpleStringProperty cause;
    public Death(String time, String name, String cause)
    {
        this.time = new SimpleStringProperty(time);
        this.name = new SimpleStringProperty(name);
        this.cause = new SimpleStringProperty(cause);
    }

    public String getTime()
    {
        return time.get();
    }

    public void setTime(String time)
    {
        this.time.set(time);
    }

    public String getName()
    {
        return name.get();
    }

    public void setName(String name)
    {
        this.name.set(name);
    }

    public String getCause()
    {
        return cause.get();
    }

    public void setCause(String cause)
    {
        this.cause.set(cause);
    }
}
