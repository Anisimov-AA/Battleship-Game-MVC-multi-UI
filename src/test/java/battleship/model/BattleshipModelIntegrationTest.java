package battleship.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for complete battleship game functionality.
 * Tests how ship placement, player interaction, and game state work together.
 */
class BattleshipModelIntegrationTest {

  private BattleshipModel model;

  @BeforeEach
  void setUp() {
    model = new BattleshipModel();
  }

  /**
   * Tests a complete winning game scenario - player finds and sinks all ships.
   */
  @Test
  void completeGame_playerWinsByFindingAllShips() {
    model.startGame();

    // verify initial state
    assertFalse(model.isGameOver(), "Game should not be over initially");
    assertEquals(0, model.getGuessCount(), "Should start with 0 guesses");
    assertFalse(model.areAllShipsSunk(), "No ships should be sunk initially");

    // player systematically searches and hits all ships
    ShipType[][] shipGrid = model.getShipGridForTesting();
    int hitCount = 0;

    for (int row = 0; row < 10; row++) {
      for (int col = 0; col < 10; col++) {
        if (shipGrid[row][col] != null) {
          // Hit a ship
          assertTrue(model.makeGuess(row, col), "Should return true when hitting ship");
          assertEquals(CellState.HIT, model.getCellGrid()[row][col], "Cell should be marked as HIT");
          hitCount++;
        }
      }
    }

    // verify win condition
    assertTrue(model.areAllShipsSunk(), "All ships should be sunk");
    assertTrue(model.isGameOver(), "Game should be over");
    assertEquals(17, hitCount, "Should have hit exactly 17 ship cells");
    assertEquals(17, model.getGuessCount(), "Should have made exactly 17 guesses");

    // verify ship grid is now accessible
    assertDoesNotThrow(() -> model.getShipGrid(), "Should be able to access ship grid after win");

    // verify can't make more guesses
    assertThrows(IllegalStateException.class, () -> model.makeGuess(9, 9),
        "Should not be able to make more guesses after win");
  }

  /**
   * Tests a complete losing game scenario - player runs out of guesses.
   */
  @Test
  void completeGame_playerLosesByRunningOutOfGuesses() {
    model.startGame();

    // make 50 guesses without hitting all ships
    int guessCount = 0;
    for (int row = 0; row < 10 && guessCount < 50; row++) {
      for (int col = 0; col < 10 && guessCount < 50; col++) {
        boolean hit = model.makeGuess(row, col);
        guessCount++;

        // verify guess tracking
        assertEquals(guessCount, model.getGuessCount(), "Guess count should increment");

        // verify cell state
        CellState expectedState = hit ? CellState.HIT : CellState.MISS;
        assertEquals(expectedState, model.getCellGrid()[row][col], "Cell should be marked correctly");
      }
    }

    // verify loss condition
    assertEquals(50, model.getGuessCount(), "Should have made exactly 50 guesses");
    assertTrue(model.isGameOver(), "Game should be over after 50 guesses");

    // verify ship grid is now accessible
    assertDoesNotThrow(() -> model.getShipGrid(), "Should be able to access ship grid after loss");

    // verify can't make more guesses
    assertThrows(IllegalStateException.class, () -> model.makeGuess(0, 0),
        "Should not be able to make more guesses after loss");
  }

  /**
   * Tests mixed gameplay with hits, misses, and proper state tracking.
   */
  @Test
  void completeGame_mixedHitsAndMisses() {
    model.startGame();
    ShipType[][] shipGrid = model.getShipGridForTesting();

    int hits = 0;
    int misses = 0;

    // make some strategic guesses
    for (int row = 0; row < 5; row++) {
      for (int col = 0; col < 5; col++) {
        boolean isHit = model.makeGuess(row, col);

        if (isHit) {
          hits++;
          assertNotNull(shipGrid[row][col], "Hit should correspond to ship location");
          assertEquals(CellState.HIT, model.getCellGrid()[row][col], "Hit cell should be marked HIT");
        } else {
          misses++;
          assertNull(shipGrid[row][col], "Miss should correspond to empty location");
          assertEquals(CellState.MISS, model.getCellGrid()[row][col], "Miss cell should be marked MISS");
        }
      }
    }

    // verify counts
    assertEquals(25, model.getGuessCount(), "Should have made 25 guesses");
    assertEquals(hits + misses, model.getGuessCount(), "Hits plus misses should equal total guesses");

    // game should not be over yet (unlikely to hit all ships in first 25 guesses)
    assertFalse(model.isGameOver(), "Game should not be over after 25 guesses");

    // verify grid accessibility
    assertThrows(IllegalStateException.class, () -> model.getShipGrid(),
        "Should not be able to access ship grid during active game");
  }

  /**
   * Tests game restart functionality - all systems should reset properly.
   */
  @Test
  void completeGame_restartResetsAllSystems() {
    model.startGame();

    // play some of the game
    model.makeGuess(0, 0);
    model.makeGuess(1, 1);
    model.makeGuess(2, 2);

    // verify game state is modified
    assertTrue(model.getGuessCount() > 0, "Should have made some guesses");

    CellState[][] gridBeforeRestart = model.getCellGrid();
    boolean foundNonUnknown = false;
    for (int row = 0; row < 10; row++) {
      for (int col = 0; col < 10; col++) {
        if (gridBeforeRestart[row][col] != CellState.UNKNOWN) {
          foundNonUnknown = true;
          break;
        }
      }
      if (foundNonUnknown) break;
    }
    assertTrue(foundNonUnknown, "Should have some non-UNKNOWN cells");

    // restart game
    model.startGame();

    // verify all systems reset
    assertEquals(0, model.getGuessCount(), "Guess count should reset");
    assertFalse(model.isGameOver(), "Game should not be over after restart");
    assertFalse(model.areAllShipsSunk(), "Ships should not be sunk after restart");

    // verify player grid reset
    CellState[][] gridAfterRestart = model.getCellGrid();
    for (int row = 0; row < 10; row++) {
      for (int col = 0; col < 10; col++) {
        assertEquals(CellState.UNKNOWN, gridAfterRestart[row][col],
            "All cells should be UNKNOWN after restart");
      }
    }

    // verify ships are placed (should have 17 total ship cells)
    ShipType[][] newShipGrid = model.getShipGridForTesting();
    int shipCells = 0;
    for (int row = 0; row < 10; row++) {
      for (int col = 0; col < 10; col++) {
        if (newShipGrid[row][col] != null) {
          shipCells++;
        }
      }
    }
    assertEquals(17, shipCells, "Should have 17 ship cells after restart");
  }

  /**
   * Tests error handling across all systems working together.
   */
  @Test
  void completeGame_errorHandlingWorksAcrossSystems() {
    model.startGame();

    // test bounds checking works with game state
    assertFalse(model.isGameOver(), "Game should be active");
    assertThrows(IllegalArgumentException.class, () -> model.makeGuess(-1, 0),
        "Should reject out of bounds guess during active game");

    // make a guess and test duplicate checking
    model.makeGuess(5, 5);
    assertThrows(IllegalArgumentException.class, () -> model.makeGuess(5, 5),
        "Should reject duplicate guess");

    // end the game and test state protection
    hitAllShips();
    assertTrue(model.isGameOver(), "Game should be over");

    // should still reject bad coordinates even when game is over
    assertThrows(IllegalStateException.class, () -> model.makeGuess(0, 0),
        "Should reject any guess when game is over");

    // but should allow ship grid access
    assertDoesNotThrow(() -> model.getShipGrid(),
        "Should allow ship grid access when game is over");
  }

  /**
   * Helper method to hit all ships and end the game.
   */
  private void hitAllShips() {
    ShipType[][] shipGrid = model.getShipGridForTesting();

    for (int row = 0; row < 10; row++) {
      for (int col = 0; col < 10; col++) {
        if (shipGrid[row][col] != null && model.getCellGrid()[row][col] == CellState.UNKNOWN) {
          model.makeGuess(row, col);
        }
      }
    }
  }
}