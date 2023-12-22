package ink.drewf.salemstats.game;

import javafx.beans.property.SimpleStringProperty;

import java.util.List;

public class Death
{
    private final SimpleStringProperty time;
    private final SimpleStringProperty name;
    private SimpleStringProperty cause = new SimpleStringProperty("");
    public Death(String time, String name, List<String> causes)
    {
        this.time = new SimpleStringProperty(time);
        this.name = new SimpleStringProperty(name);
        cause = new SimpleStringProperty(causesString(causes));
    }

    private String causesString(List<String> cause)
    {
        return String.join(", ", cause);
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
