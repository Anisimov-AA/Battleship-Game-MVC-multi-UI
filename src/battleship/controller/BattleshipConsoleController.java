package battleship.controller;

import battleship.model.IBattleshipModel;
import battleship.view.BattleshipConsoleView;
import battleship.view.IBattleshipView;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
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

      while (!model.isGameOver()) {
        // ask user for input
        view.displayPromptMessage();

        if (scanner.hasNextLine()) {
          String userInput = scanner.nextLine().trim();

          try {
            // parse user input to get coordinates
            int[] coordinates = parseGuess(userInput);

            // make the guess with parsed coordinates
            boolean wasHit = model.makeGuess(coordinates[0], coordinates[1]);

            // show the result to user
            if(wasHit) {
              view.displayHitMessage();
            } else {
              view.displayMissMessage();
            }

            // show updated game state
            displayGameState(model);
          } catch (IllegalArgumentException e) {
            // handle parsing errors or model validation errors
            view.displayErrorMessage(e.getMessage());
          }
        }
      }

      // show final result
      view.displayGameOver(model.areAllShipsSunk());
      view.displayShipGrid(model.getShipGrid());

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

  /**
   * Parses user input like "A5" into row and column coordinates.
   *
   * @param input the user input string (e.g., "A5", "B3")
   * @return an array with [row, col] where both are 0-based indices
   * @throws IllegalArgumentException if the input format is invalid
   */
  int[] parseGuess(String input) {
    // check format: exactly 2 characters
    if (input == null || input.length() != 2) {
      throw new IllegalArgumentException("Invalid input. Input two characters only (e.g., A5)");
    }

    char rowChar = input.charAt(0); // get first character (row letter)
    char colChar = input.charAt(1); // get second character (column number)

    // check if row is valid letter (A-J)
    if(rowChar < 'A' || rowChar > 'J') {
      throw new IllegalArgumentException("Row must be A-J");
    }

    // check if column is valid digit (0-9)
    if(colChar < '0' || colChar > '9') {
      throw new IllegalArgumentException("Column must be 0-9");
    }

    // convert letters to numbers
    int row = rowChar - 'A';
    int col = colChar - '0';

    return new int[]{row, col};
  }
}
