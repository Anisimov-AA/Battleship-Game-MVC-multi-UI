package battleship.controller;

import battleship.model.IBattleshipModel;
import battleship.view.IBattleshipView;
import java.io.IOException;
import java.util.Scanner;

/**
 * Console-based controller for the Battleship game. Handles user input and updates the view based
 * on the model's state.
 */
public class BattleshipConsoleController implements IBattleshipController {

  private final Scanner scanner; // to read user input many times during game loop
  private final IBattleshipView view; // to display things many times during game

  /**
   * Creates a new console controller
   * @param input where to read from (keyboard, test string, etc.)
   * @param view how to display things (console, test capture, etc.)
   */
  public BattleshipConsoleController(Readable input, IBattleshipView view) {
    if (input == null || view == null) {
      throw new IllegalArgumentException("Input and view cannot be null");
    }

    this.scanner = new Scanner(input);
    this.view = view;
  }

  /**
   * What does "play a game" involve?
   * 1. Show welcome message
   * 2. Loop: Ask user for input → Get their guess → Make guess → Show result
   * 3. Keep looping until game over
   * 4. Show final results
   *
   * @param model takes model as parameter because different games might use different models
   */
  @Override
  public void playGame(IBattleshipModel model) {
    // view methods throw IOException, so we need to handle it
    try {
      // start the game
      model.startGame();

      // show welcome message
      view.displayWelcomeMessage();

      // how game state
      displayGameState(model);

      // temporary message
      view.displayErrorMessage("Game loop not implemented yet");

    } catch (IOException e) { // if view has trouble displaying (disk full, broken console, etc.)
      System.err.println("Display error: " + e.getMessage());
    }
  }

  /**
   * Helper method that displays the current game state information
   * @param model the battleship model to get current game state from
   * @throws IOException if there's an error displaying information to the user
   */
  private void displayGameState(IBattleshipModel model) throws IOException {
    view.displayGuessCount(model.getGuessCount());
    view.displayMaxGuesses(model.getMaxGuesses());
    view.displayCellGrid(model.getCellGrid());
  }

}
