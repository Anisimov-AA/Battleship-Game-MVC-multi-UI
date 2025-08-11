package battleship.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
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
   * 1. Game Initialization
   * Controller must coordinate game startup
   */
  @Test
  void playGame_shouldCallStartGame() {
    // control mock behavior - end game immediately
    when(mockModel.isGameOver()).thenReturn(true);

    StringReader emptyInput = new StringReader("");
    controller = new BattleshipConsoleController(emptyInput, mockView);

    controller.playGame(mockModel);

    verify(mockModel).startGame();
  }

  /**
   * 2. Input Processing
   * Does "A5" become makeGuess(0, 5)?
   */
  @Test
  void playGame_shouldParseInputAndCallMakeGuess() {
    // control mock behavior - allow one guess, then end
    when(mockModel.isGameOver()).thenReturn(false, true);

    StringReader input = new StringReader("A5\n");
    controller = new BattleshipConsoleController(input, mockView);

    controller.playGame(mockModel);

    verify(mockModel).makeGuess(0, 5);  // Verify A5 → makeGuess(0, 5)
  }

  // 3. Error Handling
  /**
   * Test "AA", "A", etc.
   */
  @Test
  void playGame_shouldHandleInvalidFormat() throws IOException {
    // control mock behavior - allow one loop iteration, then end
    when(mockModel.isGameOver()).thenReturn(false, true);

    StringReader invalidInput = new StringReader("AA\n");
    controller = new BattleshipConsoleController(invalidInput, mockView);

    controller.playGame(mockModel);

    verify(mockView).displayErrorMessage(anyString()); // error shown
    verify(mockModel, never()).makeGuess(anyInt(), anyInt()); // // makeGuess should NOT be called
  }

  /**
   * Test "K5", "Z3", etc.
   */
  @Test
  void playGame_shouldHandleInvalidRow() throws IOException {
    // control mock behavior - allow one loop iteration, then end
    when(mockModel.isGameOver()).thenReturn(false, true);

    StringReader invalidInput = new StringReader("K5\n");
    controller = new BattleshipConsoleController(invalidInput, mockView);

    controller.playGame(mockModel);

    verify(mockView).displayErrorMessage(anyString());
    verify(mockModel, never()).makeGuess(anyInt(), anyInt());
  }

  /**
   * Test "AB", "A@", etc.
   */
  @Test
  void playGame_shouldHandleInvalidColumn() throws IOException {
    // control mock behavior - allow one loop iteration, then end
    when(mockModel.isGameOver()).thenReturn(false, true);

    StringReader invalidInput = new StringReader("A@\n");
    controller = new BattleshipConsoleController(invalidInput, mockView);

    controller.playGame(mockModel);

    verify(mockView).displayErrorMessage(anyString());
    verify(mockModel, never()).makeGuess(anyInt(), anyInt());
  }

  /**
   * 4. User Flow
   * Does the complete game experience work?
   */
  @Test
  void playGame_shouldProvideCompleteUserExperience() throws IOException {
    when(mockModel.isGameOver()).thenReturn(false, true);
    when(mockModel.makeGuess(0, 5)).thenReturn(true);
    when(mockModel.getGuessCount()).thenReturn(0, 1);
    when(mockModel.getMaxGuesses()).thenReturn(50);
    when(mockModel.getCellGrid()).thenReturn(new CellState[10][10]);
    when(mockModel.areAllShipsSunk()).thenReturn(true);
    when(mockModel.getShipGrid()).thenReturn(new ShipType[10][10]);

    StringReader input = new StringReader("A5\n");
    controller = new BattleshipConsoleController(input, mockView);

    controller.playGame(mockModel);

    // verify complete user experience
    verify(mockView).displayWelcomeMessage();                                       // Game start
    verify(mockView, atLeast(1)).displayGuessCount(anyInt());  // Game state
    verify(mockView, atLeast(1)).displayMaxGuesses(50);        // Game state
    verify(mockView, atLeast(1)).displayCellGrid(any());       // Game state
    verify(mockView).displayPromptMessage();                                       // User input
    verify(mockView).displayHitMessage();                                          // Result
    verify(mockView).displayGameOver(true);                                   // Game end
    verify(mockView).displayShipGrid(any());                                       // Final reveal
  }
}