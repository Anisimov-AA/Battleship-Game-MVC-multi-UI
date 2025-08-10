package battleship.model;

/**
 * Represents the possible states of a cell on the game grid.
 * <ul>
 *   <li>UNKNOWN: The cell has not been guessed yet.</li>
 *   <li>HIT: The cell was guessed and contained a ship.</li>
 *   <li>MISS: The cell was guessed and did not contain a ship.</li>
 * </ul>
 */
public enum CellState {
  UNKNOWN("_"),
  HIT("H"),
  MISS("M");

  private final String symbol;

  CellState(String symbol) {
    this.symbol = symbol;
  }

  /**
   * Returns the symbol representing the cell state.
   * @return the symbol representing the cell state
   */
  public String getSymbol() {
    return symbol;
  }
}
