package menu.api;

import core.events.Event;

/**
 * Class representing all menu domain events.
 */
public class MenuEvent extends Event {
    public MenuEvent(EventType type, Object... args) {
        super(type.id, args);
    }
}
