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
   * @param model the battleship model to use for the game
   * @throws IllegalArgumentException if the model is null
   */
  @Override
  public void playGame(IBattleshipModel model) {
    if (model == null) {
      throw new IllegalArgumentException("Model cannot be null");
    }

    // view methods throw IOException, so we need to handle it
    try {
      // start the game
      model.startGame();

      // show welcome message and initial game state
      view.displayWelcomeMessage();
      view.displayGuessCount(model.getGuessCount());
      view.displayMaxGuesses(model.getMaxGuesses());
      view.displayCellGrid(model.getCellGrid());

      // main game loop
      while (!model.isGameOver()) {
        // ask user for input
        view.displayPromptMessage();

        // handle case where input stream ends
        if (!scanner.hasNextLine()) {
          break;
        }

        // case insensitive + trim whitespace
        String userInput = scanner.nextLine().trim().toUpperCase();

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
          view.displayGuessCount(model.getGuessCount());
          view.displayMaxGuesses(model.getMaxGuesses());
          view.displayCellGrid(model.getCellGrid());
        } catch (IllegalArgumentException e) {
          // bad input - user can fix it and continue:
          // "AA" → show error → ask for input again
          view.displayErrorMessage(e.getMessage());
        } catch (IllegalStateException e) {
          // game over - no point asking for more input:
          // game over → show error → stop asking for input
          view.displayErrorMessage(e.getMessage());
          break;
        }
      }

      // game is over - display final results
      view.displayGameOver(model.areAllShipsSunk());
      view.displayShipGrid(model.getShipGrid());

    } catch (IOException e) {
      // if view has trouble displaying (disk full, broken console, etc.)
      throw new RuntimeException("I/O error during game execution: " + e.getMessage(), e);
    }
  }

  /**
   * Parses user input like "A5" into row and column coordinates.
   *
   * @param input the user input string (e.g., "A5", "B3")
   * @return an array with [row, col] where both are 0-based indices
   * @throws IllegalArgumentException if the input format is invalid
   */
  int[] parseGuess(String input) {
    // format validation
    if (input == null || input.length() != 2) {
      throw new IllegalArgumentException("Invalid format. Use format like A5");
    }

    char rowChar = input.charAt(0); // get first character (row letter)
    char colChar = input.charAt(1); // get second character (column number)

    if (rowChar < 'A' || rowChar > 'J') {
      throw new IllegalArgumentException("Row must be A-J");  // User sees this instead
    }

    if (colChar < '0' || colChar > '9') {
      throw new IllegalArgumentException("Column must be 0-9");
    }

    // convert letters to numbers
    return new int[]{rowChar - 'A', colChar - '0'};
  }
}
