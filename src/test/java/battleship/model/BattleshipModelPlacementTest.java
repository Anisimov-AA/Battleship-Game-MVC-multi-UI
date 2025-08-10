package battleship.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;

/**
 * Tests for ship placement functionality.
 */
class BattleshipModelPlacementTest {

  private static final int EXPECTED_TOTAL_SHIP_CELLS = 17; // 5+4+3+3+2

  private BattleshipModel model;

  @BeforeEach
  void setUp() {
    model = new BattleshipModel();
  }

  /**
   * Tests that ship placement works correctly by verifying:
   * - All 5 ships are placed with correct sizes
   * - Ships don't overlap (total cells = 17)
   * - Ships stay within grid bounds
   *
   * Repeated 10 times to test random placement reliability.
   */
  @RepeatedTest(10)
  void startGame_shouldPlaceShipsCorrectly() {
    model.startGame();
    ShipType[][] grid = model.getShipGridForTesting();

    // verify each ship is placed with correct size
    assertEquals(ShipType.AIRCRAFT_CARRIER.getSize(), countShipCells(grid, ShipType.AIRCRAFT_CARRIER));
    assertEquals(ShipType.BATTLESHIP.getSize(), countShipCells(grid, ShipType.BATTLESHIP));
    assertEquals(ShipType.SUBMARINE.getSize(), countShipCells(grid, ShipType.SUBMARINE));
    assertEquals(ShipType.DESTROYER.getSize(), countShipCells(grid, ShipType.DESTROYER));
    assertEquals(ShipType.PATROL_BOAT.getSize(), countShipCells(grid, ShipType.PATROL_BOAT));

    // verify no overlaps (total cells should equal sum of ship sizes)
    assertEquals(EXPECTED_TOTAL_SHIP_CELLS, countTotalShipCells(grid));
  }

  /**
   * Counts how many cells on the grid contain the specified ship type.
   *
   * @param grid the ship grid to search
   * @param shipType the type of ship to count
   * @return the number of cells occupied by the specified ship type
   */
  private int countShipCells(ShipType[][] grid, ShipType shipType) {
    int count = 0;
    for (int row = 0; row < grid.length; row++) {
      for (int col = 0; col < grid[row].length; col++) {
        if (grid[row][col] == shipType) {
          count++;
        }
      }
    }
    return count;
  }

  /**
   * Counts the total number of cells occupied by any ship on the grid.
   *
   * @param grid the ship grid to search
   * @return the total number of non-null cells (cells containing ships)
   */
  private int countTotalShipCells(ShipType[][] grid) {
    int count = 0;
    for (int row = 0; row < 10; row++) {
      for (int col = 0; col < 10; col++) {
        if (grid[row][col] != null) count++;
      }
    }
    return count;
  }
}