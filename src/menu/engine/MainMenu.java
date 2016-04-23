package menu.engine;

import core.events.Event;
import menu.api.EventType;
import menu.api.MenuEvent;
import menu.api.PostCloseEvent;

/**
 * Class responsible for controlling main menu
 */
class MainMenu implements Menu{
    @Override
    public String toString() {
        return "MainMenu instance";
    }

    @Override
    public Event performPreClose() {
        return new PostCloseEvent();
    }
}
