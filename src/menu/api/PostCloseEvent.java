package menu.api;

import core.events.Event;

/**
 * Event class used to represent PostClose event. PostClose event is raised when PreClose event has been accepted and
 * all handling that should be performed by domain in control has been finished. It is propagated to other domains.
 */
public class PostCloseEvent extends Event {
    public PostCloseEvent() {
        super(EventType.POST_CLOSE.id);
    }
}
