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

  @BeforeEach
  void setUp() {
    // Mocks are already created by @Mock annotation
    // Only need to create controller with empty input
    StringReader emptyInput = new StringReader("");
    controller = new BattleshipConsoleController(emptyInput, mockView);
  }

  @Test
  void playGame_shouldCallStartGame() {
    // control mock behavior - end game immediately (one loop only)
    when(mockModel.isGameOver()).thenReturn(true);
    // act
    controller.playGame(mockModel);
    // verify: was startGame() called on mockModel?
    verify(mockModel).startGame();
  }


}