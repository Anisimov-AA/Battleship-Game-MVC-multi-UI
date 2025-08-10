package battleship.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import battleship.model.IBattleshipModel;
import battleship.view.IBattleshipView;
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


}