package sanity;

import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Test suite for dependencies resolving.
 */
public class DependenciesTest {
    @Test
    public void mockitoDependencyCheck() throws Exception {
        List mockedList = mock(List.class);
        assertThat(mockedList, not(nullValue()));

        mockedList.add("one");
        mockedList.clear();

        verify(mockedList).add("one");
        verify(mockedList).clear();
    }

}