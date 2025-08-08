package battleship.controller;

import static org.junit.jupiter.api.Assertions.*;

import battleship.model.CellState;
import battleship.model.IBattleshipModel;
import battleship.model.ShipType;
import battleship.view.IBattleshipView;
import java.io.StringReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for BattleshipConsoleController behavior.
 * Uses controlled input/output to test controller without user interaction.
 */
class BattleshipConsoleControllerTest {

  private BattleshipConsoleController controller;
  private MockBattleshipModel mockModel;
  private MockBattleshipView mockView;

  @BeforeEach
  void setUp() {
    mockModel = new MockBattleshipModel();
    mockView = new MockBattleshipView();
  }

  /**
   * Tests that controller calls model.startGame() when playGame() is executed.
   *
   * Why: Controller's primary responsibility is to initialize the game
   * by calling the model's startGame() method.
   */
  @Test
  void playGame_shouldCallStartGame() {
    StringReader emptyInput = new StringReader("");
    controller = new BattleshipConsoleController(emptyInput, mockView);

    // execute the controller's main method
    controller.playGame(mockModel);

    // verify the controller called startGame() on the model
    assertTrue(mockModel.wasStartGameCalled(), "Controller must call model.startGame() to initialize the game");
  }

  /**
   * Tests that controller processes user input and calls model with correct coordinates.
   *
   * Why: Input parsing and model coordination is core controller functionality.
   */
  @Test
  void playGame_shouldCallMakeGuessWithParsedCoordinates() {
    // provide user input "A5"
    StringReader userInput = new StringReader("A5\n");
    controller = new BattleshipConsoleController(userInput, mockView);
    controller.playGame(mockModel);

    assertTrue(mockModel.wasMakeGuessCalled());
    assertTrue(mockModel.wasGuessAtCoordinates(0, 5));
  }

  /**
   * Tests that controller displays all required messages during game flow.
   *
   * Why: Verify controller provides complete user experience with all necessary displays.
   */
  @Test
  void playGame_shouldDisplayAllRequiredMessages() {
    StringReader userInput = new StringReader("A5\n");
    controller = new BattleshipConsoleController(userInput, mockView);
    controller.playGame(mockModel);

    // verify all expected display calls were made
    assertTrue(mockView.wasWelcomeMessageDisplayed(), "Should display welcome message");
    assertTrue(mockView.wasPromptMessageDisplayed(), "Should display prompt for user input");
    assertTrue(mockView.wasGuessCountDisplayed(), "Should display guess count");
    assertTrue(mockView.wasMaxGuessesDisplayed(), "Should display max guesses");
    assertTrue(mockView.wasCellGridDisplayed(), "Should display game grid");
    assertTrue(mockView.wasHitMessageDisplayed() || mockView.wasMissMessageDisplayed(), "Should display hit or miss result");
    assertTrue(mockView.wasGameOverDisplayed(), "Should display game over message");
  }

  /**
   * Test "AA", "A", etc.
   */
  @Test
  void playGame_shouldHandleInvalidFormat() {
    StringReader invalidInput = new StringReader("AA\n");
    controller = new BattleshipConsoleController(invalidInput, mockView);
    controller.playGame(mockModel);

    assertTrue(mockView.wasErrorMessageDisplayed(), "Should display error for invalid input");
    assertFalse(mockModel.wasMakeGuessCalled(), "Should not call makeGuess for invalid input");
  }

  /**
   * Test "K5", "Z3", etc.
   */
  @Test
  void playGame_shouldHandleInvalidRow() {
    StringReader invalidInput = new StringReader("K5\n");
    controller = new BattleshipConsoleController(invalidInput, mockView);
    controller.playGame(mockModel);

    assertTrue(mockView.wasErrorMessageDisplayed(), "Should display error for invalid input");
    assertFalse(mockModel.wasMakeGuessCalled(), "Should not call makeGuess for invalid input");
  }

  /**
   * Test "AB", "A@", etc.
   */
  @Test
  void playGame_shouldHandleInvalidColumn() {
    StringReader invalidInput = new StringReader("AB\n");
    controller = new BattleshipConsoleController(invalidInput, mockView);
    controller.playGame(mockModel);

    assertTrue(mockView.wasErrorMessageDisplayed(), "Should display error for invalid input");
    assertFalse(mockModel.wasMakeGuessCalled(), "Should not call makeGuess for invalid input");
  }

  private static class MockBattleshipModel implements IBattleshipModel {

    // Track method calls
    private boolean startGameCalled = false; // track if startGame was called
    private boolean makeGuessCalled = false; // track if makeGuess was called
    private int lastGuessRow = -1; // remember row coordinate
    private int lastGuessCol = -1; // remember column coordinate
    private boolean gameOver = false; // stops loop
    private int isGameOverCallCount = 0; // to enter the loop once

    @Override
    public void startGame() {
      this.startGameCalled = true; // record that this was called
    }

    @Override
    public boolean makeGuess(int row, int col) {
      this.makeGuessCalled = true; // record that makeGuess was called
      this.lastGuessRow = row; // remember the row that was guessed
      this.lastGuessCol = col; // remember the column that was guessed

      // return predictable result for testing: A5 (0,5) hits, everything else misses
      return row == 0 && col == 5;
    }

    @Override
    public boolean isGameOver() {
      isGameOverCallCount++; // count each time loop checks
      return gameOver || isGameOverCallCount >= 2; // allow one loop iteration
    }

    // Check if startGame was called
    public boolean wasStartGameCalled() {
      return startGameCalled;
    }

    // Check if makeGuess was called
    public boolean wasMakeGuessCalled() {
      return makeGuessCalled;
    }

    // Check specific coordinates
    public boolean wasGuessAtCoordinates(int row, int col) {
      return makeGuessCalled && lastGuessRow == row && lastGuessCol == col;
    }

    // Let test control when game ends
    public void setGameOver(boolean gameOver) {
      this.gameOver = gameOver;
    }

    // all other methods stay the same
    @Override public boolean areAllShipsSunk() { return false; }
    @Override public int getGuessCount() { return 0; }
    @Override public int getMaxGuesses() { return 50; }
    @Override public CellState[][] getCellGrid() { return new CellState[10][10]; }
    @Override public ShipType[][] getShipGrid() { return new ShipType[10][10]; }
  }

  private static class MockBattleshipView implements IBattleshipView {

    // Track all display methods the controller uses
    private boolean welcomeMessageDisplayed = false;
    private boolean promptMessageDisplayed = false;
    private boolean hitMessageDisplayed = false;
    private boolean missMessageDisplayed = false;
    private boolean errorMessageDisplayed = false;
    private boolean gameOverDisplayed = false;
    private boolean guessCountDisplayed = false;
    private boolean maxGuessesDisplayed = false;
    private boolean cellGridDisplayed = false;
    private boolean shipGridDisplayed = false;  // Track end-game ship reveal

    @Override
    public void displayWelcomeMessage() {
      welcomeMessageDisplayed = true;
    }

    @Override
    public void displayPromptMessage() {
      promptMessageDisplayed = true;
    }

    @Override
    public void displayHitMessage() {
      hitMessageDisplayed = true;
    }

    @Override
    public void displayMissMessage() {
      missMessageDisplayed = true;
    }

    @Override
    public void displayErrorMessage(String message) {
      errorMessageDisplayed = true;
    }

    @Override
    public void displayGameOver(boolean win) {
      gameOverDisplayed = true;
    }

    @Override
    public void displayGuessCount(int currentGuesses) {
      guessCountDisplayed = true;
    }

    @Override
    public void displayMaxGuesses(int maxGuesses) {
      maxGuessesDisplayed = true;
    }

    @Override
    public void displayCellGrid(CellState[][] cellGrid) {
      cellGridDisplayed = true;
    }

    @Override
    public void displayShipGrid(ShipType[][] shipGrid) {
      shipGridDisplayed = true;  // Track ship position reveal at game end
    }

    // Verification getters for all tracked methods
    public boolean wasWelcomeMessageDisplayed() { return welcomeMessageDisplayed; }
    public boolean wasPromptMessageDisplayed() { return promptMessageDisplayed; }
    public boolean wasHitMessageDisplayed() { return hitMessageDisplayed; }
    public boolean wasMissMessageDisplayed() { return missMessageDisplayed; }
    public boolean wasErrorMessageDisplayed() { return errorMessageDisplayed; }
    public boolean wasGameOverDisplayed() { return gameOverDisplayed; }
    public boolean wasGuessCountDisplayed() { return guessCountDisplayed; }
    public boolean wasMaxGuessesDisplayed() { return maxGuessesDisplayed; }
    public boolean wasCellGridDisplayed() { return cellGridDisplayed; }
    public boolean wasShipGridDisplayed() { return shipGridDisplayed; }  // NEW
  }
}