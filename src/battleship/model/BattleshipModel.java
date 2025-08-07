package battleship.model;

import java.util.Random;

/**
 * An implementation of the Battleship game model
 * Manages a single game on a 10x10 grid with 5 ships
 */
public class BattleshipModel implements IBattleshipModel {

  private static final int GRID_SIZE = 10;

  // Internal grid showing where ships actually are
  private final ShipType[][] shipGrid;
  // Ships to place
  private static final ShipType[] SHIP_TYPES = {
      ShipType.AIRCRAFT_CARRIER,
      ShipType.BATTLESHIP,
      ShipType.SUBMARINE,
      ShipType.DESTROYER,
      ShipType.PATROL_BOAT
  };
  private final Random random;

  // What the player can see (hits/misses/unknown)
  private final CellState[][] playerGrid;
  private int guessCount; // Current number of guesses made by the player

  /**
   * Creates a new game with custom random generator (mainly for testing)
   *
   * @param random the random generator to use, or null for default
   */
  public BattleshipModel(Random random) {
    this.shipGrid = new ShipType[GRID_SIZE][GRID_SIZE];
    this.random = random != null ? random : new Random();

    this.playerGrid = new CellState[GRID_SIZE][GRID_SIZE];
    this.guessCount = 0;
  }

  /**
   * Creates a new game with default random placement
   */
  public BattleshipModel() {
    this(new Random());
  }

  @Override
  public void startGame() {
    clearGrids();
    placeShipsRandomly();
    this.guessCount = 0;
  }

  /**
   * Clears both ship and player grids for a new game
   * Sets all ship grid cells to null and all player grid cells to UNKNOWN
   */
  private void clearGrids() {
    for (int row = 0; row < GRID_SIZE; row++) {
      for (int col = 0; col < GRID_SIZE; col++) {
        shipGrid[row][col] = null; // mark cell as empty
        playerGrid[row][col] = CellState.UNKNOWN; // mark cell as unknown
      }
    }
  }

  /**
   * Places all ships randomly without overlapping
   * Tries multiple random positions for each ship until placement succeeds
   */
  private void placeShipsRandomly() {
    for (ShipType ship : SHIP_TYPES) {
      boolean placed = false;

      // keep trying random positions until we find a valid spot
      while(!placed) {
        // generate random starting position and orientation
        int row = this.random.nextInt(GRID_SIZE);
        int col = this.random.nextInt(GRID_SIZE);
        boolean horizontal = random.nextBoolean(); // true = horizontal, false = vertical

        if(canPlaceShip(row, col, horizontal, ship)) {
          placeShip(row, col, horizontal, ship);
          placed = true;
        }
      }
    }
  }

  /**
   * Checks if a ship can be placed at the given position.
   * Validates grid boundaries and checks for overlapping ships.
   *
   * @param row the starting row position
   * @param col the starting column position
   * @param horizontal true for horizontal, false for vertical placement
   * @param ship the type of ship to place
   * @return true if ship can be placed, false otherwise
   */
  private boolean canPlaceShip(int row, int col, boolean horizontal, ShipType ship){
    // first check if ship fits within grid boundaries
    if (horizontal) {
      // for horizontal ships, check right boundary
      if (col + ship.getSize() > GRID_SIZE) return false;
    } else {
      // for vertical ships, check bottom boundary
      if (row + ship.getSize() > GRID_SIZE) return false;
    }

    // then check for overlapping with existing ships
    for (int i = 0; i < ship.getSize(); i++) {
      // calculate position of each ship segment
      int checkRow = horizontal ? row : row + i;
      int checkCol = horizontal ? col + i : col;

      // if any cell is already occupied, we can't place here
      if (shipGrid[checkRow][checkCol] != null) {
        return false;
      }
    }

    return true; // all checks passed
  }

  /**
   * Places a ship on the grid at the specified position and orientation.
   *
   * @param row the starting row position
   * @param col the starting column position
   * @param horizontal true for horizontal, false for vertical placement
   * @param ship the type of ship to place
   */
  private void placeShip(int row, int col, boolean horizontal, ShipType ship){
    // mark all cells occupied by this ship
    for (int i = 0; i < ship.getSize(); i++) {
      // calculate position of each ship segment
      int placeRow = horizontal ? row : row + i;
      int placeCol = horizontal ? col + i : col;

      shipGrid[placeRow][placeCol] = ship; // place ship segment
    }
  }

  @Override
  public boolean makeGuess(int row, int col) {
    // game over check
    if(isGameOver()) {
      throw new IllegalStateException("Game is over");
    }

    // bounds check
    if(row < 0 || row >= GRID_SIZE || col < 0 || col >= GRID_SIZE) {
      throw new IllegalArgumentException("Coordinates out of bounds: (" + row + ", " + col + ")");
    }

    // check if cell already guessed
    if(playerGrid[row][col] != CellState.UNKNOWN) {
      throw new IllegalArgumentException("Cell already guessed: (" + row + ", " + col + ")");
    }

    // update guess counter
    this.guessCount++;

    // update player grid (
    // check if there's a ship at this position
    if (shipGrid[row][col] != null) {
      playerGrid[row][col] = CellState.HIT;
      return true;
    } else {
      playerGrid[row][col] = CellState.MISS;
      return false;
    }
  }

  @Override
  public boolean isGameOver() {
    return false;
  }

  @Override
  public boolean areAllShipsSunk() {
    throw new UnsupportedOperationException("Not implemented yet");
  }

  @Override
  public int getGuessCount() {
    return this.guessCount;
  }

  @Override
  public int getMaxGuesses() {
    throw new UnsupportedOperationException("Not implemented yet");
  }

  @Override
  public CellState[][] getCellGrid() {
    CellState[][] copy = new CellState[GRID_SIZE][GRID_SIZE];
    for (int row = 0; row < GRID_SIZE; row++) {
      for (int col = 0; col < GRID_SIZE; col++) {
        copy[row][col] = playerGrid[row][col];
      }
    }

    return copy;
  }

  @Override
  public ShipType[][] getShipGrid() {
    // create deep copy to prevent external modification
    ShipType[][] copy = new ShipType[GRID_SIZE][GRID_SIZE];
    for (int row = 0; row < GRID_SIZE; row++) {
      for (int col = 0; col < GRID_SIZE; col++) {
        copy[row][col] = shipGrid[row][col]; // copy each cell
      }
    }

    return copy;
  }

  /**
   * Returns ship grid for testing purposes only.
   * This method bypasses game state checks and should only be used in tests.
   * Package-private access ensures it's only available to tests in same package.
   */
  ShipType[][] getShipGridForTesting() {
    // Same implementation as getShipGrid but without game state checks
    ShipType[][] copy = new ShipType[GRID_SIZE][GRID_SIZE];
    for (int row = 0; row < GRID_SIZE; row++) {
      for (int col = 0; col < GRID_SIZE; col++) {
        copy[row][col] = shipGrid[row][col];
      }
    }
    return copy;
  }
}
