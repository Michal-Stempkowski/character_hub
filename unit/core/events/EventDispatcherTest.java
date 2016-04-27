package core.events;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.NoSuchElementException;
import java.util.function.Consumer;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

/**
 * Test suite for event dispatcher
 */
public class EventDispatcherTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private static final int eventType = 0;
    private static final int domain = 0xBABE;
    private static final int targetDomains = 0xBABE;

    private Event caughtEvent = null;

    private Consumer<Event> makeCallback() {
        return (event) -> caughtEvent = event;
    }

    @Test
    public void should_be_able_to_register_new_listener() throws Exception {
        // Given:
        EventDispatcher uut = new EventDispatcher();
        uut.registerListener(eventType, makeCallback());
        Event e = new Event(eventType);

        // When:
        uut.notifyEvent(e);

        // Then:
        assertThat(caughtEvent, is(equalTo(e)));
    }

    @Test
    public void should_be_able_to_unregister_listener() throws Exception {
        // Given:
        EventDispatcher uut = new EventDispatcher();
        EventConnection conn = uut.registerListener(eventType, makeCallback());
        Event e = new Event(eventType);
        uut.unregisterListener(conn);

        // When:
        uut.notifyEvent(e);

        // Then:
        assertThat(caughtEvent, is(nullValue()));
    }

    @Test
    public void unregister_not_existing_connection__exception_is_raised() throws Exception {
        // Given:
        EventDispatcher uut = new EventDispatcher();
        EventConnection conn = new EventConnection(eventType, makeCallback());
        thrown.expect(NoSuchElementException.class);
        // When/Then:
        uut.unregisterListener(conn);
    }

    @Test
    public void unregister_not_existing_connection__exception_is_raised__even_if_there_is_another() throws Exception {
        // Given:
        EventDispatcher uut = new EventDispatcher();
        uut.registerListener(eventType, makeCallback());
        EventConnection conn = new EventConnection(eventType, makeCallback());
        thrown.expect(NoSuchElementException.class);
        // When/Then:
        uut.unregisterListener(conn);
    }

}