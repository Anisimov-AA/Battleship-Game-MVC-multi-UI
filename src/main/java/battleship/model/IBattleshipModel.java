package battleship.model;

/**
 * A single game of Battleship played on a 10x10 grid, where the player aims to guess and hit all
 * the ships randomly placed on the grid. The player has a maximum of 50 guesses. The game ends when
 * either all ships are sunk or the guess limit is reached. Ships are revealed after the game ends.
 */
public interface IBattleshipModel {

  /**
   * Initializes the game by setting up the grids and randomly placing ships.
   */
  void startGame();

  /**
   * Processes the player's guess at the specified coordinate.
   * @param row the row index (0-based)
   * @param col the column index (0-based)
   * @return true if the guess was a hit, false otherwise
   * @throws IllegalArgumentException if the coordinates are out of bounds or the cell has already
   *                                  been guessed
   * @throws IllegalStateException    if the game is already over
   */
  boolean makeGuess(int row, int col);

  /**
   * Checks if the game is over.
   * @return true if all ships are sunk or the maximum number of guesses is reached, false otherwise
   */
  boolean isGameOver();

  /**
   * Checks if all ships have been sunk.
   * @return true if all ships are sunk, false otherwise
   */
  boolean areAllShipsSunk();

  /**
   * Gets the number of guesses the player has made so far.
   * @return the number of guesses made
   */
  int getGuessCount();

  /**
   * Gets the maximum number of guesses allowed.
   * @return the maximum number of guesses
   */
  int getMaxGuesses();

  /**
   * Retrieves the current state of the cell grid for display purposes.
   * @return a deep copy of the 2D array representing the cell grid state
   */
  CellState[][] getCellGrid();

  /**
   * Retrieves the current state of the ship grid after the game is over. The ship grid should not
   * be revealed during the game.
   * @return a deep copy of the 2D array representing the ship grid state
   * @throws IllegalStateException if the game is not over
   */
  ShipType[][] getShipGrid();
}
