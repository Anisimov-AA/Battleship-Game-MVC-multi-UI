package battleship.controller;

import static org.junit.jupiter.api.Assertions.*;

import battleship.view.BattleshipConsoleView;
import java.io.InputStreamReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test class for BattleshipConsoleController's parseGuess functionality.
 * Tests the conversion of user input (e.g., "A5") to coordinate arrays.
 */
class BattleshipConsoleControllerparseGuessTest {

  BattleshipConsoleController controller;

  @BeforeEach
  void setUp() {
    controller = new BattleshipConsoleController(
        new InputStreamReader(System.in),
        new BattleshipConsoleView(System.out)
    );
  }

  /**
   * Tests parseGuess method with valid and invalid inputs.
   */
  @Test
  void TestParseGuessLogic() {

    // test valid inputs - should convert correctly
    assertArrayEquals(new int[]{0, 5}, controller.parseGuess("A5")); // A=row 0, 5=col 5
    assertArrayEquals(new int[]{9, 9}, controller.parseGuess("J9")); // J=row 9, 9=col 9

    // test invalid inputs - should throw IllegalArgumentException
    // invalid format: two letters instead of letter+number
    assertThrows(IllegalArgumentException.class, () -> controller.parseGuess("AA"));
    // invalid row: K is beyond valid range (A-J)
    assertThrows(IllegalArgumentException.class, () -> controller.parseGuess("K5"));
    // invalid format: too short (missing column)
    assertThrows(IllegalArgumentException.class, () -> controller.parseGuess("A"));
  }
}