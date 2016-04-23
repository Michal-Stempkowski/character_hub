package menu.engine;

import core.events.Event;
import menu.api.EventType;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class MainMenuTest {

    @Test
    public final void shouldHaveProperNaming() throws Exception {
        assertThat(uut.toString(), is(equalTo("MainMenu instance")));
    }

    @Test
    public final void onPreCloseShouldReturnPostClose() throws Exception {
        Event result = uut.performPreClose();
        assertThat(result.eventType, is(equalTo(EventType.POST_CLOSE.id)));
    }

    private Menu uut = new MainMenu();
}