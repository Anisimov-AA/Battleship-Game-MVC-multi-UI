package battleship.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BattleshipModelGameStateTest {

  private BattleshipModel model;

  @BeforeEach
  void setUp() {
    model = new BattleshipModel();
    model.startGame();
  }

  /**
   * Tests that game is not over initially.
   */
  @Test
  void isGameOver_shouldReturnFalseInitially() {
    assertFalse(model.isGameOver());
  }

  /**
   * Tests that areAllShipsSunk() returns false initially.
   */
  @Test
  void areAllShipsSunk_shouldReturnFalseInitially() {
    assertFalse(model.areAllShipsSunk());
  }

  /**
   * Tests that game is over when all ships are sunk.
   */
  @Test
  void isGameOver_shouldReturnTrueWhenAllShipsSunk() {
    // hit all ship positions
    hitAllShips();

    assertTrue(model.areAllShipsSunk(), "All ships should be sunk");
    assertTrue(model.isGameOver(), "Game should be over when all ships sunk");
  }

  /**
   * Tests that game is over when max guesses reached.
   */
  @Test
  void isGameOver_shouldReturnTrueWhenMaxGuessesReached() {
    // make 50 guesses without hitting all ships
    make50Guesses();

    assertEquals(50, model.getGuessCount(), "Should have made 50 guesses");
    assertTrue(model.isGameOver(), "Game should be over after 50 guesses");
  }

  /**
   * Tests that makeGuess() throws exception when game is over due to all ships sunk.
   */
  @Test
  void makeGuess_shouldThrowExceptionWhenGameOver() {
    // hit all ships to end game
    hitAllShips();
    assertTrue(model.isGameOver(), "Game should be over");

    // find an unguessed cell and try to guess it
    for (int row = 0; row < 10; row++) {
      for (int col = 0; col < 10; col++) {
        if (model.getCellGrid()[row][col] == CellState.UNKNOWN) {
          final int finalRow = row;
          final int finalCol = col;
          assertThrows(IllegalStateException.class, () -> model.makeGuess(finalRow, finalCol),
              "Should throw exception when game is over");
          return;
        }
      }
    }
  }

  /**
   * Tests that getShipGrid() throws exception when game is not over.
   */
  @Test
  void getShipGrid_shouldThrowExceptionWhenGameNotOver() {
    assertFalse(model.isGameOver(), "Game should not be over initially");

    assertThrows(IllegalStateException.class, () -> model.getShipGrid(),
        "Should throw exception when accessing ship grid during active game");
  }

  /**
   * Tests that getShipGrid() works when game is over due to win.
   */
  @Test
  void getShipGrid_shouldWorkWhenGameOver() {
    // Hit all ships to end game
    hitAllShips();
    assertTrue(model.isGameOver(), "Game should be over");

    // Should be able to access ship grid
    assertDoesNotThrow(() -> model.getShipGrid(),
        "Should be able to access ship grid when game is over");

    ShipType[][] shipGrid = model.getShipGrid();
    assertNotNull(shipGrid, "Ship grid should not be null");

    // Verify it contains ships
    boolean hasShips = false;
    for (int row = 0; row < 10; row++) {
      for (int col = 0; col < 10; col++) {
        if (shipGrid[row][col] != null) {
          hasShips = true;
          break;
        }
      }
      if (hasShips) break;
    }
    assertTrue(hasShips, "Ship grid should contain ships");
  }

  /**
   * Helper method to hit all ship positions to end the game.
   */
  private void hitAllShips() {
    ShipType[][] shipGrid = model.getShipGridForTesting();

    for (int row = 0; row < 10; row++) {
      for (int col = 0; col < 10; col++) {
        if (shipGrid[row][col] != null) {
          model.makeGuess(row, col);
        }
      }
    }
  }

  /**
   * Helper method to make 50 guesses without hitting all ships
   */
  private void make50Guesses() {
    for (int row = 0; row < 10 && model.getGuessCount() < 50; row++) {
      for (int col = 0; col < 10 && model.getGuessCount() < 50; col++) {
        model.makeGuess(row, col);
      }
    }
  }

}