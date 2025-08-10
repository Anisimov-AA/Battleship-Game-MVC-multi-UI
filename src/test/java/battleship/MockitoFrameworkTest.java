package battleship;

import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

/**
 * Verifies Mockito framework is properly configured and working.
 */
class MockitoFrameworkTest {
  @Test
  void mockList() {
    var list = mock(java.util.List.class);
    when(list.size()).thenReturn(42);
    System.out.println(list.size()); // should print 42
  }
}
