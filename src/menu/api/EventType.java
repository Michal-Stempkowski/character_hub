package menu.api;

/**
 * Enum types for events in menu domain
 */
public enum EventType {
    PRE_CLOSE(0),
    POST_CLOSE(1),
    MENU_CHANGED(2);

    EventType(int id) {
        this.id = id;
    }

    public final int id;
}
