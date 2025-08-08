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
    mockModel.setGameOver(false);

    controller.playGame(mockModel);

    assertTrue(mockModel.wasMakeGuessCalled());
    assertTrue(mockModel.wasGuessAtCoordinates(0, 5));
  }

  private static class MockBattleshipModel implements IBattleshipModel {

    // Track method calls
    private boolean startGameCalled = false; // track if startGame was called
    private boolean makeGuessCalled = false; // track if makeGuess was called
    private int lastGuessRow = -1; // remember row coordinate
    private int lastGuessCol = -1; // remember column coordinate
    private boolean gameOver = true; // stops loop
    private int guessCount = 0; // track how many guesses made

    @Override
    public void startGame() {
      this.startGameCalled = true; // record that this was called
    }

    @Override
    public boolean makeGuess(int row, int col) {
      this.makeGuessCalled = true; // record that makeGuess was called
      this.lastGuessRow = row; // remember the row that was guessed
      this.lastGuessCol = col; // remember the column that was guessed
      this.guessCount++;

      // return predictable result for testing: A5 (0,5) hits, everything else misses
      return row == 0 && col == 5;
    }

    @Override
    public boolean isGameOver() {
      return gameOver || guessCount >= 1;
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
    @Override public void displayWelcomeMessage() {}
    @Override public void displayPromptMessage() {}
    @Override public void displayCellGrid(CellState[][] cellGrid) {}
    @Override public void displayShipGrid(ShipType[][] shipGrid) {}
    @Override public void displayGuessCount(int currentGuesses) {}
    @Override public void displayMaxGuesses(int maxGuesses) {}
    @Override public void displayErrorMessage(String message) {}
    @Override public void displayGameOver(boolean win) {}
    @Override public void displayHitMessage() {}
    @Override public void displayMissMessage() {}
  }
}