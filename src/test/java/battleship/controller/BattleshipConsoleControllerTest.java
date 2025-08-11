package battleship.controller;

import static org.junit.jupiter.api.Assertions.*;

import battleship.model.CellState;
import battleship.model.IBattleshipModel;
import battleship.model.ShipType;
import battleship.view.IBattleshipView;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Manual mock tests for BattleshipConsoleController
 * Uses controlled input/output to test controller without user interaction
 */
class BattleshipConsoleControllerTest {

  private BattleshipConsoleController controller;
  private MockModel mockModel;
  private MockView mockView;

  @BeforeEach
  void setUp() {
    mockModel = new MockModel();
    mockView = new MockView();
  }

  /**
   * 1. Game Initialization
   * Controller must coordinate game startup
   */
  @Test
  void playGame_shouldCallStartGame() {
    StringReader emptyInput = new StringReader("");
    controller = new BattleshipConsoleController(emptyInput, mockView);

    controller.playGame(mockModel);

    assertTrue(mockModel.startGameCalled, "Controller should call startGame()");
  }

  /**
   * 2. Input Processing
   * Does "A5" become makeGuess(0, 5)?
   */
  @Test
  void playGame_shouldParseInputAndCallMakeGuess() {
    StringReader input = new StringReader("A5\n");
    controller = new BattleshipConsoleController(input, mockView);

    controller.playGame(mockModel);

    assertTrue(mockModel.makeGuessCalled, "Controller should call makeGuess()");
    assertEquals(0, mockModel.lastGuessRow, "A5 should be parsed as row=0");
    assertEquals(5, mockModel.lastGuessCol, "A5 should be parsed as col=5");
  }

  // 3. Error Handling
  /**
   * Test "AA", "A", etc.
   */
  @Test
  void playGame_shouldHandleInvalidFormat() {
    StringReader invalidInput = new StringReader("AA\n");
    controller = new BattleshipConsoleController(invalidInput, mockView);

    controller.playGame(mockModel);

    assertTrue(mockView.log.contains("error"), "Should display error for invalid input");
    assertFalse(mockModel.makeGuessCalled, "Should not call makeGuess for invalid input");
  }

  /**
   * Test "K5", "Z3", etc.
   */
  @Test
  void playGame_shouldHandleInvalidRow() {
    StringReader invalidInput = new StringReader("K5\n");
    controller = new BattleshipConsoleController(invalidInput, mockView);

    controller.playGame(mockModel);

    assertTrue(mockView.log.contains("error"), "Should display error for invalid input");
    assertFalse(mockModel.makeGuessCalled, "Should not call makeGuess for invalid input");
  }

  /**
   * Test "AB", "A@", etc.
   */
  @Test
  void playGame_shouldHandleInvalidColumn() {
    StringReader invalidInput = new StringReader("AB\n");
    controller = new BattleshipConsoleController(invalidInput, mockView);

    controller.playGame(mockModel);

    assertTrue(mockView.log.contains("error"), "Should display error for invalid input");
    assertFalse(mockModel.makeGuessCalled, "Should not call makeGuess for invalid input");
  }

  /**
   * 4. User Flow
   * Does the complete game experience work?
   */
  @Test
  void playGame_shouldProvideCompleteUserExperience() {
    StringReader input = new StringReader("A5\n");
    controller = new BattleshipConsoleController(input, mockView);

    controller.playGame(mockModel);

    // verify complete user experience sequence
    List<String> expected = List.of(
        "welcome",
        "guessCount:0",
        "maxGuesses:50",
        "cellGrid",
        "prompt",
        "hit",
        "guessCount:1",
        "maxGuesses:50",
        "cellGrid",
        "gameOver you win",
        "shipGrid"
    );
    assertEquals(expected, mockView.log, "Should provide complete user experience");
  }

  /**
   * Minimal mock model for testing controller behavior.
   * Only tracks essential calls and provides controllable behavior.
   */
  private static class MockModel implements IBattleshipModel {

    // Method Call Tracking
    boolean startGameCalled = false;
    boolean makeGuessCalled = false;
    int lastGuessRow = -1;
    int lastGuessCol = -1;

    @Override
    public void startGame() {
      startGameCalled = true; // record the call
    }

    @Override
    public boolean makeGuess(int row, int col) {
      makeGuessCalled = true; // record the call
      lastGuessRow = row;     // record the row that was guessed
      lastGuessCol = col;     // record the column that was guessed

      return true;  // always hit for simplicity
    }

    @Override
    public boolean isGameOver() {
      return makeGuessCalled; // end after one guess
    }

    @Override
    public int getGuessCount() {
      return makeGuessCalled ? 1 : 0;
    }

    @Override
    public boolean areAllShipsSunk() {
      return makeGuessCalled;  // win after one guess
    }

    // Simple defaults for other methods
    @Override public int getMaxGuesses() { return 50; }
    @Override public CellState[][] getCellGrid() { return new CellState[10][10]; }
    @Override public ShipType[][] getShipGrid() { return new ShipType[10][10]; }
  }

  /**
   * Sequential logging view mock for testing controller behavior.
   * Records every method call in order for comprehensive verification.
   */
  private static class MockView implements IBattleshipView {

    // Record calls in sequence
    final List<String> log = new ArrayList<>();

    @Override
    public void displayWelcomeMessage() {
      log.add("welcome");
    }

    @Override
    public void displayPromptMessage() {
      log.add("prompt");
    }

    @Override
    public void displayHitMessage() {
      log.add("hit");
    }

    @Override
    public void displayMissMessage() {
      log.add("miss");
    }

    @Override
    public void displayErrorMessage(String message) {
      log.add("error");  // simple - just record that error was shown
    }

    @Override
    public void displayGameOver(boolean win) {
      log.add(win ? "gameOver you win" : "gameOver you lose");
    }

    @Override
    public void displayGuessCount(int count) {
      log.add("guessCount:" + count);  // include the actual count
    }

    @Override
    public void displayMaxGuesses(int max) {
      log.add("maxGuesses:" + max);
    }

    @Override
    public void displayCellGrid(CellState[][] grid) {
      log.add("cellGrid");  // just record the call, not the content
    }

    @Override
    public void displayShipGrid(ShipType[][] grid) {
      log.add("shipGrid");
    }
  }
}