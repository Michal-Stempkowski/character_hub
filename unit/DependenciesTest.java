import org.junit.Test;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Test suite for event dispatcher
 */
public class DependenciesTest {
    @Test
    public void mockito_dependency_check() throws Exception {
        List mockedList = mock(List.class);

        mockedList.add("one");
        mockedList.clear();

        verify(mockedList).add("one");
        verify(mockedList).clear();
    }

}