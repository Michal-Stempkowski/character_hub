package menu.engine;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

/**
 * Created by Dell on 22.04.2016.
 */
public class MainMenuTest {
    @Test
    public final void shouldHaveProperNaming() throws Exception {
        Menu m = new MainMenu();
        assertThat(m.toString(), is(equalTo("MainMenu instance")));
    }

}