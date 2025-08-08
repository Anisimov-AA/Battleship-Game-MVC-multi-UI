package battleship.view;

import battleship.model.CellState;
import battleship.model.ShipType;
import java.io.IOException;

/**
 * The BattleshipView interface defines the methods required for displaying the game state and
 * interacting with the player. It handles all user interface aspects, such as rendering the game
 * grid, capturing user input, and displaying messages.
 */
public interface IBattleshipView {

  /**
   * Displays a welcome message to the player at the start of the game.
   *
   * @throws IOException if an I/O error occurs
   */
  void displayWelcomeMessage() throws IOException;

  /**
   * Displays a prompt message to the player, indicating that they should enter a guess.
   *
   * @throws IOException if an I/O error occurs
   */
  void displayPromptMessage() throws IOException;

  /**
   * Displays the current state of the player's grid, showing hits, misses, and unknown cells.
   *
   * @param cellGrid a 2D array representing the cell grid state
   * @throws IOException if an I/O error occurs
   */
  void displayCellGrid(CellState[][] cellGrid) throws IOException;

  /**
   * Displays the ship grid after the game has ended, revealing the positions of all ships.
   *
   * @param shipGrid a 2D array representing the ship grid state
   * @throws IOException if an I/O error occurs
   */
  void displayShipGrid(ShipType[][] shipGrid) throws IOException;

  /**
   * Displays the current number of guesses the player has made.
   *
   * @param currentGuesses the current number of guesses made
   * @throws IOException if an I/O error occurs
   */
  void displayGuessCount(int currentGuesses) throws IOException;

  /**
   * Displays the maximum number of guesses allowed for the game.
   *
   * @param maxGuesses the maximum number of guesses allowed
   * @throws IOException if an I/O error occurs
   */
  void displayMaxGuesses(int maxGuesses) throws IOException;

  /**
   * Displays an error message when an invalid input or operation occurs.
   *
   * @param message the error message to display
   * @throws IOException if an I/O error occurs
   */
  void displayErrorMessage(String message) throws IOException;

  /**
   * Displays a message indicating that the game is over.
   *
   * @param win true if the player won the game, false if the player lost
   * @throws IOException if an I/O error occurs
   */
  void displayGameOver(boolean win) throws IOException;

  /**
   * Displays a message indicating that the player made a hit.
   *
   * @throws IOException if an I/O error occurs
   */
  void displayHitMessage() throws IOException;

  /**
   * Displays a message indicating that the player made a miss.
   *
   * @throws IOException if an I/O error occurs
   */
  void displayMissMessage() throws IOException;
}
