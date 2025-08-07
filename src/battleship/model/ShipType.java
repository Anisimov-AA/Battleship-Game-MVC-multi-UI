package battleship.model;

/**
 * Enum representing different types of ships in the Battleship game. Each ship has a size and a
 * corresponding symbol.
 */
public enum ShipType {
  // Enum values
  AIRCRAFT_CARRIER(5, "A"), // Aircraft Carrier size 5
  BATTLESHIP(4, "B"),       // Battleship size 4
  SUBMARINE(3, "S"),        // Submarine size 3
  DESTROYER(3, "D"),        // Destroyer size 3
  PATROL_BOAT(2, "P");      // Patrol Boat size 2

  // Instance fields
  private final int size;
  private final String symbol;

  // Constructor
  ShipType(int size, String symbol) {
    this.size = size;
    this.symbol = symbol;
  }

  /**
   * Returns the size of the ship.
   * @return the size of the ship
   */
  public int getSize() {
    return size;
  }

  /**
   * Returns the symbol representing the ship.
   * @return the symbol representing the ship
   */
  public String getSymbol() {
    return symbol;
  }

  @Override
  public String toString() {
    return name() + " (Size: " + size + ")";
  }
}
