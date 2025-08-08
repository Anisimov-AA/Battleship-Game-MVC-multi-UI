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

    StringReader emptyInput = new StringReader("");
    controller = new BattleshipConsoleController(emptyInput, mockView);
  }

  // Minimal mock implementations

  private static class MockBattleshipModel implements IBattleshipModel {
    @Override public void startGame() {}
    @Override public boolean makeGuess(int row, int col) { return false; }
    @Override public boolean isGameOver() { return true; }
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