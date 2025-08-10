package battleship.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.atLeast;

import battleship.model.CellState;
import battleship.model.IBattleshipModel;
import battleship.model.ShipType;
import battleship.view.IBattleshipView;
import java.io.IOException;
import java.io.StringReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Controller tests using Mockito framework.
 * Demonstrates automatic mock creation and behavior verification.
 */
@ExtendWith(MockitoExtension.class) // tell JUnit to use Mockito
class BattleshipConsoleControllerMockitoTest {

  @Mock  // Mockito implements ALL interface methods IBattleshipModel and returns default values
  private IBattleshipModel mockModel;
  @Mock  // Mockito implements ALL interface methods IBattleshipView and returns default values
  private IBattleshipView mockView;
  private BattleshipConsoleController controller;

  /**
   * Tests that controller calls model.startGame() when playGame() is executed
   */
  @Test
  void playGame_shouldCallStartGame() {
    // mocks are already created by @Mock annotation
    // only need to create controller with empty input
    StringReader emptyInput = new StringReader("");
    controller = new BattleshipConsoleController(emptyInput, mockView);

    // control mock behavior - allow one loop iteration, then end
    when(mockModel.isGameOver()).thenReturn(true); // loop NEVER runs because condition is false

    // act
    controller.playGame(mockModel);

    // verify: was startGame() called on mockModel?
    verify(mockModel).startGame();
  }

  /**
   * Tests that controller processes user input and calls model with correct coordinates
   *
   * User Input: "A5"
   *      ↓
   * parseGuess("A5") converts to [0, 5]
   *      ↓
   * model.makeGuess(0, 5) gets called
   *      ↓
   * verify(mockModel).makeGuess(0, 5) ← Checks this exact call happened
   */
  @Test
  void playGame_shouldParseInputAndCallMakeGuess() {
    // provide user input "A5"
    StringReader userInput = new StringReader("A5\n");
    controller = new BattleshipConsoleController(userInput, mockView);

    // control mock behavior - allow one loop iteration, then end
    when(mockModel.isGameOver()).thenReturn(false, true); // first false, then true

    // act
    controller.playGame(mockModel);

    // verify - check that A5 was parsed as row=0, col=5
    verify(mockModel).makeGuess(0, 5);
  }

  /**
   * Tests that controller displays all required messages during game flow.
  */
  @Test
  void playGame_shouldDisplayAllRequiredMessages() throws IOException {
    // provide user input "A5"
    StringReader userInput = new StringReader("A5\n");
    controller = new BattleshipConsoleController(userInput, mockView);

    // control mock behavior - allow one guess, then end game
    when(mockModel.isGameOver()).thenReturn(false, true);
    when(mockModel.getGuessCount()).thenReturn(0, 1);  // Before and after guess
    when(mockModel.getMaxGuesses()).thenReturn(50);
    when(mockModel.getCellGrid()).thenReturn(createEmptyGrid());
    when(mockModel.makeGuess(0, 5)).thenReturn(true);  // A5 is a hit
    when(mockModel.areAllShipsSunk()).thenReturn(true);  // Player wins
    when(mockModel.getShipGrid()).thenReturn(createEmptyShipGrid());

    // act
    controller.playGame(mockModel);

    // verify all display calls were made
    verify(mockView).displayWelcomeMessage();              // Game start
    verify(mockView, atLeast(1)).displayGuessCount(anyInt());  // Game state
    verify(mockView, atLeast(1)).displayMaxGuesses(50);       // Game state
    verify(mockView, atLeast(1)).displayCellGrid(any());      // Game state
    verify(mockView).displayPromptMessage();                  // User input prompt
    verify(mockView).displayHitMessage();                     // Guess result
    verify(mockView).displayGameOver(true);                   // Game end
    verify(mockView).displayShipGrid(any());                  // Final ship positions
  }

  // Helper methods for creating test grids
  private CellState[][] createEmptyGrid() {
    CellState[][] grid = new CellState[10][10];
    for (int row = 0; row < 10; row++) {
      for (int col = 0; col < 10; col++) {
        grid[row][col] = CellState.UNKNOWN;
      }
    }
    return grid;
  }

  private ShipType[][] createEmptyShipGrid() {
    return new ShipType[10][10];  // All nulls by default
  }

  /**
   * Test "AA", "A", etc.
   */
  @Test
  void playGame_shouldHandleInvalidFormat() throws IOException {
    // provide invalid input "AA" (two letters)
    StringReader invalidInput = new StringReader("AA\n");
    controller = new BattleshipConsoleController(invalidInput, mockView);

    // control mock behavior - allow one loop iteration, then end
    when(mockModel.isGameOver()).thenReturn(false, true);

    // act
    controller.playGame(mockModel);

    // verify error handling
    verify(mockView).displayErrorMessage(anyString()); // error message should be s
    verify(mockModel, never()).makeGuess(anyInt(), anyInt()); // // makeGuess should NOT be called
    verify(mockModel).startGame(); // but startGame should still be called
  }

  /**
   * Test "K5", "Z3", etc.
   */
  @Test
  void playGame_shouldHandleInvalidRow() throws IOException {
    StringReader invalidInput = new StringReader("K5\n");
    controller = new BattleshipConsoleController(invalidInput, mockView);
    when(mockModel.isGameOver()).thenReturn(false, true);

    controller.playGame(mockModel);

    verify(mockView).displayErrorMessage(anyString());
    verify(mockModel, never()).makeGuess(anyInt(), anyInt());
  }

  /**
   * Test "AB", "A@", etc.
   */
  @Test
  void playGame_shouldHandleInvalidColumn() throws IOException {
    StringReader invalidInput = new StringReader("A@\n");
    controller = new BattleshipConsoleController(invalidInput, mockView);
    when(mockModel.isGameOver()).thenReturn(false, true);

    controller.playGame(mockModel);

    verify(mockView).displayErrorMessage(anyString());
    verify(mockModel, never()).makeGuess(anyInt(), anyInt());
  }
}