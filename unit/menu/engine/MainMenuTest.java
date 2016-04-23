package menu.engine;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public class MainMenuTest {
    @Test
    public final void shouldHaveProperNaming() throws Exception {
        Menu m = new MainMenu();
        assertThat(m.toString(), is(equalTo("MainMenu instance")));
    }

}