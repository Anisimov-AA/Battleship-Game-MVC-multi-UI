package battleship.view;

import battleship.model.CellState;
import battleship.model.ShipType;
import java.io.IOException;
import java.util.Objects;

/**
 * The BattleshipConsoleView class implements the BattleshipView interface and provides a text-based
 * user interface for the Battleship game. It displays game state information and messages to the
 * user.
 */
public class BattleshipConsoleView implements IBattleshipView {
  private final Appendable out;

  /**
   * Constructor for the BattleshipConsoleView class.
   *
   * @param out the output destination for game state and messages
   * @throws NullPointerException if the Appendable is null
   */
  public BattleshipConsoleView(Appendable out) {
    this.out = Objects.requireNonNull(out, "Appendable can't be null");
  }

  @Override
  public void displayGameOver(boolean win) throws IOException {
    if (win) {
      out.append("Congratulations! You have sunk all the ships!\n");
    } else {
      out.append("Game Over! You have reached the maximum number of guesses.\n");
    }
  }

  @Override
  public void displayMissMessage() throws IOException {
    out.append("It's a MISS!\n");
  }

  @Override
  public void displayHitMessage() throws IOException {
    out.append("It's a HIT!\n");
  }


  @Override
  public void displayPromptMessage() throws IOException {
    out.append("Enter your guess (row and column, e.g., A5): ");
  }

  @Override
  public void displayWelcomeMessage() throws IOException {
    out.append("Welcome to Battleship!\n");
  }

  @Override
  public void displayCellGrid(CellState[][] cellGrid) throws IOException {
    out.append("Current Game Grid:\n");
    out.append("  ");
    for (int i = 0; i < cellGrid[0].length; i++) {
      out.append(i + " ");
    }
    out.append("\n");
    for (int i = 0; i < cellGrid.length; i++) {
      out.append((char) ('A' + i) + " ");
      for (int j = 0; j < cellGrid[i].length; j++) {
        out.append(cellGrid[i][j].getSymbol() + " ");
      }
      out.append("\n");
    }
  }

  @Override
  public void displayShipGrid(ShipType[][] shipGrid) throws IOException {
    out.append("Final Ship Positions:\n");
    out.append("  ");
    for (int i = 0; i < shipGrid[0].length; i++) {
      out.append(i + " ");
    }
    out.append("\n");
    for (int i = 0; i < shipGrid.length; i++) {
      out.append((char) ('A' + i) + " ");
      for (int j = 0; j < shipGrid[i].length; j++) {
        out.append(shipGrid[i][j] == null ? "- " : shipGrid[i][j].getSymbol() + " ");
      }
      out.append("\n");
    }
  }

  @Override
  public void displayGuessCount(int currentGuesses) throws IOException {
    out.append("Guesses Made: ").append(String.valueOf(currentGuesses)).append("\n");
  }

  @Override
  public void displayMaxGuesses(int maxGuesses) throws IOException {
    out.append("Maximum Guesses Allowed: ").append(String.valueOf(maxGuesses)).append("\n");
  }

  @Override
  public void displayErrorMessage(String message) throws IOException {
    out.append("Error: ").append(message).append("\n");
  }
}
