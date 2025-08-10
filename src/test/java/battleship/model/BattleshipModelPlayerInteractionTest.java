package battleship.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for player interaction system.
 * Verifies guess processing, input validation, and grid state tracking.
 */
class BattleshipModelPlayerInteractionTest {

  private BattleshipModel model;

  @BeforeEach
  void setUp() {
    model = new BattleshipModel();
    model.startGame();
  }

  /**
   * Tests that makeGuess() returns true when hitting a ship and updates grid correctly.
   */
  @Test
  void makeGuess_shouldReturnTrueAndMarkHitWhenShipPresent() {
    ShipType[][] shipGrid = model.getShipGridForTesting();

    // find a cell with a ship
    for (int row = 0; row < 10; row++) {
      for (int col = 0; col < 10; col++) {
        if (shipGrid[row][col] != null) {

          assertTrue(model.makeGuess(row, col), "makeGuess() should return true when hitting a ship");
          assertEquals(CellState.HIT, model.getCellGrid()[row][col], "Cell should be marked as HIT");

          return;
        }
      }
    }
    fail("No ships found on grid");
  }

  /**
   * Tests that makeGuess() returns false when missing and updates grid correctly.
   */
  @Test
  void makeGuess_shouldReturnFalseAndMarkMissWhenNoShip() {
    ShipType[][] shipGrid = model.getShipGridForTesting();

    // find a cell without a ship
    for (int row = 0; row < 10; row++) {
      for (int col = 0; col < 10; col++) {
        if (shipGrid[row][col] == null) {

          assertFalse(model.makeGuess(row, col), "makeGuess should return false when missing");
          assertEquals(CellState.MISS, model.getCellGrid()[row][col], "Cell should be marked as MISS");

          return;
        }
      }
    }
    fail("No empty cells found on grid");
  }

  /**
   * Tests that makeGuess() throws exception for out-of-bounds coordinates.
   */
  @Test
  void makeGuess_shouldThrowExceptionForOutOfBounds() {
    assertThrows(IllegalArgumentException.class, () -> model.makeGuess(-1, 0), "Negative row should throw exception");
    assertThrows(IllegalArgumentException.class, () -> model.makeGuess(10, 0), "Row too large should throw exception");
    assertThrows(IllegalArgumentException.class, () -> model.makeGuess(0, -1), "Negative column should throw exception");
    assertThrows(IllegalArgumentException.class, () -> model.makeGuess(0, 10), "Column too large should throw exception");
    assertThrows(IllegalArgumentException.class, () -> model.makeGuess(-1, -1), "Both negative should throw exception");
    assertThrows(IllegalArgumentException.class, () -> model.makeGuess(10, 10), "Both too large should throw exception");
  }

  /**
   * Tests that makeGuess() throws exception when guessing same cell twice.
   */
  @Test
  void makeGuess_shouldThrowExceptionForDuplicateGuess() {
    // make first guess
    model.makeGuess(0, 0);
    // try to guess same cell again
    assertThrows(IllegalArgumentException.class, () -> model.makeGuess(0, 0),
        "Guessing same cell twice should throw exception");
  }

  /**
   * Tests that guess count increments correctly with each guess.
   */
  @Test
  void makeGuess_shouldIncrementGuessCount() {
    assertEquals(0, model.getGuessCount(), "Initial guess count should be 0");

    model.makeGuess(0, 0);
    assertEquals(1, model.getGuessCount(), "Guess count should be 1 after first guess");

    model.makeGuess(0, 1);
    assertEquals(2, model.getGuessCount(), "Guess count should be 2 after second guess");

    model.makeGuess(1, 0);
    assertEquals(3, model.getGuessCount(), "Guess count should be 3 after third guess");
  }

  /**
   * Tests that startGame() resets guess count to zero.
   */
  @Test
  void startGame_shouldResetGuessCount() {
    // make some guesses
    model.makeGuess(0, 0);
    model.makeGuess(0, 1);
    assertTrue(model.getGuessCount() > 0, "Should have made some guesses");

    // start new game
    model.startGame();
    assertEquals(0, model.getGuessCount(), "Guess count should reset to 0 after startGame()");
  }

  /**
   * Tests that startGame() resets player grid to all UNKNOWN.
   */
  @Test
  void startGame_shouldResetPlayerGrid() {
    // Make some guesses to change player grid
    model.makeGuess(0, 0);
    model.makeGuess(0, 1);

    // Verify grid has been modified
    CellState[][] gridBeforeReset = model.getCellGrid();
    assertTrue(gridBeforeReset[0][0] != CellState.UNKNOWN || gridBeforeReset[0][1] != CellState.UNKNOWN,
        "Grid should have some non-UNKNOWN cells");

    // Start new game
    model.startGame();

    // Verify all cells are UNKNOWN again
    CellState[][] gridAfterReset = model.getCellGrid();
    for (int row = 0; row < 10; row++) {
      for (int col = 0; col < 10; col++) {
        assertEquals(CellState.UNKNOWN, gridAfterReset[row][col],
            "All cells should be UNKNOWN after startGame()");
      }
    }
  }
}