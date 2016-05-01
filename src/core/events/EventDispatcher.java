package core.events;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Class responsible for managing system events. It groups events into some categories, to which subscribers can subscribe.
 * It allows events from different components to remain separated from each other.
 *
 * Event dispatcher is a singleton.
 */
public class EventDispatcher {
    private final Map<Integer, List<EventConnection>> connections = new HashMap<>();
    private Logger logger = Logger.getLogger(EventDispatcher.class.getName());
    private ExecutorService executor = Executors.newCachedThreadPool();
    private final Object mutex = new Object();

    public  EventDispatcher() {
        logger.info("EventDispatcher created");
    }

    public void gentleShutdown(Duration finalizationTime, Duration lastResortTime) throws InterruptedException {
        logger.info("Gentle shutdown has begun");
        executor.shutdown();
        if (!executor.awaitTermination(finalizationTime.toMillis(), TimeUnit.MILLISECONDS)) {
            logger.warning("Last resort shutdown has begun!");
            executor.shutdownNow();
            if (!executor.awaitTermination(lastResortTime.toMillis(), TimeUnit.MILLISECONDS)) {
                logger.severe("Last resort failed, performing emergency shutdown!!!");
                System.exit(1);
            }
        }
    }

    public EventConnection registerListener(int eventType, Consumer<Event> eventConsumer) {
        logger.fine("Registering listener for event " + Integer.toString(eventType));
        EventConnection conn = new EventConnection(eventType, eventConsumer);
        synchronizedListenersUpdate(conn);
        return conn;
    }

    private void synchronizedListenersUpdate(EventConnection conn) {
        logger.finer(String.format("synchronizedListenersUpdate - entering (%s)", conn.getConnectionId().toString()));
        synchronized (mutex) {
            List<EventConnection> conns = connections.getOrDefault(conn.getEventType(), new LinkedList<>());
            conns.add(conn);
            connections.put(conn.getEventType(), conns);
            logger.finer(String.format("synchronizedListenersUpdate - leaving (%s)", conn.getConnectionId().toString()));
        }
    }

    public EventBlocker notifyEvent(Event e) {
        return new EventBlocker(synchronizedGetEventListeners(e).stream()
                .map(x -> executor.submit(() -> x.getEventConsumer().accept(e)))
                .collect(Collectors.toList()));
    }

    private List<EventConnection> synchronizedGetEventListeners(Event e) {
        logger.finer(String.format("synchronizedGetEventListeners - entering (%s)", e.eventType));
        synchronized (mutex) {
            List<EventConnection> conns = connections.getOrDefault(e.eventType, new LinkedList<>());
            if (conns.isEmpty()) {
                logger.warning("No listeners for event " + e.getClass().getName());
            }

            try {
                return new ArrayList<>(conns);
            }
            finally {
                logger.finer(String.format("synchronizedGetEventListeners - leaving (%s)", e.eventType));
            }
        }
    }

    public void unregisterListener(EventConnection conn) throws Exception{
        if (!synchronizedRemoveListener(conn)) {
            throw new NoSuchElementException();
        }
    }

    private boolean synchronizedRemoveListener(EventConnection conn) {
        logger.finer(String.format("synchronizedRemoveListener - entering (%s)", conn.getConnectionId().toString()));
        synchronized (mutex) {
            try {
                return Optional.ofNullable(connections.get(conn.getEventType()))
                        .orElseThrow(NoSuchElementException::new)
                        .removeIf(x -> x.getConnectionId() == conn.getConnectionId());
            }
            finally {
                logger.finer(String.format(
                        "synchronizedRemoveListener - leaving (%s)", conn.getConnectionId().toString()));
            }

        }
    }
}

