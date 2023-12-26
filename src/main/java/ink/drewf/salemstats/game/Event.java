package ink.drewf.salemstats.game;

import javafx.beans.property.SimpleStringProperty;

public class Event
{
    private final SimpleStringProperty time;
    private final SimpleStringProperty event;

    public Event(String time, String event)
    {
        this.time = new SimpleStringProperty(time);
        this.event = new SimpleStringProperty(event);
    }

    public String getTime() {
        return time.get();
    }

    public void setTime(String time) {
        this.time.set(time);
    }

    public String getEvent() {
        return event.get();
    }

    public void setEvent(String event) {
        this.event.set(event);
    }
}
