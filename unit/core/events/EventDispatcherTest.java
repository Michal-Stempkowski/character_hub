package core.events;

import org.junit.Before;
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

    private Event caughtEvent = null;
    private EventDispatcher uut;

    private Consumer<Event> makeCallback() {
        return (event) -> caughtEvent = event;
    }

    @Before
    public void setUp() throws Exception {
        uut = new EventDispatcher();
    }

    @Test
    public void shouldBeAbleToRegisterNewListener() throws Exception {
        // Given:
        uut.registerListener(eventType, makeCallback());
        Event e = new Event(eventType);

        // When:
        uut.notifyEvent(e);

        // Then:
        assertThat(caughtEvent, is(equalTo(e)));
    }

    @Test
    public void shouldBeAbleToUnregisterListener() throws Exception {
        // Given:
        EventConnection conn = uut.registerListener(eventType, makeCallback());
        Event e = new Event(eventType);
        uut.unregisterListener(conn);

        // When:
        uut.notifyEvent(e);

        // Then:
        assertThat(caughtEvent, is(nullValue()));
    }

    @Test
    public void onUnregisterNotExistingConnectionExceptionIsRaised() throws Exception {
        // Given:
        EventConnection conn = new EventConnection(eventType, makeCallback());
        thrown.expect(NoSuchElementException.class);
        // When/Then:
        uut.unregisterListener(conn);
    }

    @Test
    public void onUnregisterNotExistingConnectionExceptionIsRaisedEvenIfThereIsAnother() throws Exception {
        // Given:
        uut.registerListener(eventType, makeCallback());
        EventConnection conn = new EventConnection(eventType, makeCallback());
        thrown.expect(NoSuchElementException.class);
        // When/Then:
        uut.unregisterListener(conn);
    }

}