package core.events;

import java.util.*;
import java.util.function.Consumer;
import java.util.logging.Logger;

/**
 * Class responsible for managing system events. It groups events into some categories, to which subscribers can subscribe.
 * It allows events from different components to remain separated from each other.
 *
 * Event dispatcher is a singleton.
 */
public class EventDispatcher {
    private final Map<Integer, List<EventConnection>> connections;
    private Logger logger = Logger.getLogger(EventDispatcher.class.getName());

    public  EventDispatcher() {
        connections = new HashMap<>();
    }

    public EventConnection registerListener(int eventType, Consumer<Event> eventConsumer) {
        EventConnection conn = new EventConnection(eventType, eventConsumer);
        List<EventConnection> conns = connections.getOrDefault(eventType, new LinkedList<>());
        conns.add(conn);
        connections.put(eventType, conns);

        return conn;
    }

    public void notifyEvent(Event e) {
        List<EventConnection> conns = connections.getOrDefault(e.eventType, new LinkedList<>());
        if (conns.isEmpty()) {
            logger.warning("No listeners for event " + e.getClass().getName());
        }
        conns.forEach(x -> x.getEventConsumer().accept(e));
    }

    public void unregisterListener(EventConnection conn) throws Exception{
        boolean removed = Optional.ofNullable(connections.get(conn.getEventType()))
                .orElseThrow(NoSuchElementException::new)
                .removeIf(x -> x.getConnectionId() == conn.getConnectionId());

        if (!removed) {
            throw new NoSuchElementException();
        }
    }
}

