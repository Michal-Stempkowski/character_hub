package core.events;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Basic system event
 */
public class Event {
    public final int eventType;
    protected final List<Object> args;

    public Event(int eventType, Object...args) {
        this.eventType = eventType;
        this.args = new ArrayList<>(Arrays.asList(args));
    }
}
