package battleship.view;

import battleship.model.CellState;
import battleship.model.ShipType;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Minimal, clean GUI for Battleship game.
 * Focuses on functionality with simple, elegant design.
 */
public class SwingBattleshipView extends JFrame implements IBattleshipView {

  private static final int GRID_SIZE = 10;
  private static final int CELL_SIZE = 40;

  // Minimal color scheme
  private static final Color BACKGROUND = Color.WHITE;
  private static final Color GRID_BACKGROUND = new Color(250, 250, 250);
  private static final Color UNKNOWN = new Color(200, 200, 200);
  private static final Color HIT = new Color(220, 50, 50);
  private static final Color MISS = new Color(100, 100, 100);
  private static final Color SHIP = new Color(60, 120, 180);
  private static final Color BORDER = new Color(150, 150, 150);
  private static final Color TEXT = new Color(50, 50, 50);

  // Components
  private JButton[][] gridButtons;
  private JLabel statusLabel;
  private JLabel statsLabel;

  public SwingBattleshipView() {
    initializeGUI();
  }

  private void initializeGUI() {
    setTitle("Battleship");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLayout(new BorderLayout(10, 10));
    setBackground(BACKGROUND);

    createInfoPanel();
    createGameGrid();
    createStatusPanel();

    pack();
    setLocationRelativeTo(null);
    setResizable(false);
  }

  /**
   * Creates minimal info panel with essential stats.
   */
  private void createInfoPanel() {
    JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
    infoPanel.setBackground(BACKGROUND);

    statsLabel = new JLabel("Guesses: 0 / 50");
    statsLabel.setFont(new Font("Arial", Font.PLAIN, 14));
    statsLabel.setForeground(TEXT);

    infoPanel.add(statsLabel);
    add(infoPanel, BorderLayout.NORTH);
  }

  /**
   * Creates clean game grid.
   */
  private void createGameGrid() {
    JPanel gridPanel = new JPanel(new GridLayout(GRID_SIZE + 1, GRID_SIZE + 1, 1, 1));
    gridPanel.setBackground(GRID_BACKGROUND);
    gridPanel.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(BORDER, 1),
        BorderFactory.createEmptyBorder(10, 10, 10, 10)
    ));

    gridButtons = new JButton[GRID_SIZE][GRID_SIZE];

    // Empty corner
    gridPanel.add(new JLabel());

    // Column headers
    for (int col = 0; col < GRID_SIZE; col++) {
      JLabel label = new JLabel(String.valueOf(col), SwingConstants.CENTER);
      label.setFont(new Font("Arial", Font.PLAIN, 12));
      label.setForeground(TEXT);
      gridPanel.add(label);
    }

    // Grid with row headers
    for (int row = 0; row < GRID_SIZE; row++) {
      // Row header
      JLabel label = new JLabel(String.valueOf((char)('A' + row)), SwingConstants.CENTER);
      label.setFont(new Font("Arial", Font.PLAIN, 12));
      label.setForeground(TEXT);
      gridPanel.add(label);

      // Grid buttons
      for (int col = 0; col < GRID_SIZE; col++) {
        JButton button = new JButton();
        button.setPreferredSize(new Dimension(CELL_SIZE, CELL_SIZE));
        button.setBackground(UNKNOWN);
        button.setBorder(BorderFactory.createLineBorder(BORDER, 1));
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 16));

        // Simple hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
          public void mouseEntered(java.awt.event.MouseEvent e) {
            if (button.isEnabled()) {
              button.setBackground(UNKNOWN.darker());
            }
          }
          public void mouseExited(java.awt.event.MouseEvent e) {
            if (button.isEnabled() && button.getBackground().equals(UNKNOWN.darker())) {
              button.setBackground(UNKNOWN);
            }
          }
        });

        gridButtons[row][col] = button;
        gridPanel.add(button);
      }
    }

    add(gridPanel, BorderLayout.CENTER);
  }

  /**
   * Creates simple status panel.
   */
  private void createStatusPanel() {
    JPanel statusPanel = new JPanel(new FlowLayout());
    statusPanel.setBackground(BACKGROUND);

    statusLabel = new JLabel("Welcome to Battleship! Click a cell to make your guess.");
    statusLabel.setFont(new Font("Arial", Font.PLAIN, 14));
    statusLabel.setForeground(TEXT);
    statusLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

    statusPanel.add(statusLabel);
    add(statusPanel, BorderLayout.SOUTH);
  }

  /**
   * Sets button click listener.
   */
  public void setButtonClickListener(ActionListener listener) {
    for (int row = 0; row < GRID_SIZE; row++) {
      for (int col = 0; col < GRID_SIZE; col++) {
        gridButtons[row][col].setActionCommand(row + "," + col);
        gridButtons[row][col].addActionListener(listener);
      }
    }
  }

  /**
   * Updates cell appearance.
   */
  public void updateCell(int row, int col, CellState state) {
    JButton button = gridButtons[row][col];

    switch (state) {
      case HIT:
        button.setBackground(HIT);
        button.setText("X");
        button.setForeground(Color.WHITE);
        button.setEnabled(false);
        break;
      case MISS:
        button.setBackground(MISS);
        button.setText("•");
        button.setForeground(Color.WHITE);
        button.setEnabled(false);
        break;
      case UNKNOWN:
      default:
        button.setBackground(UNKNOWN);
        button.setText("");
        button.setForeground(TEXT);
        button.setEnabled(true);
        break;
    }
  }

  public void disableAllButtons() {
    for (int row = 0; row < GRID_SIZE; row++) {
      for (int col = 0; col < GRID_SIZE; col++) {
        gridButtons[row][col].setEnabled(false);
      }
    }
  }

  public void revealShips(ShipType[][] shipGrid) {
    for (int row = 0; row < GRID_SIZE; row++) {
      for (int col = 0; col < GRID_SIZE; col++) {
        if (shipGrid[row][col] != null && gridButtons[row][col].getBackground().equals(UNKNOWN)) {
          gridButtons[row][col].setBackground(SHIP);
          gridButtons[row][col].setText("■");
          gridButtons[row][col].setForeground(Color.WHITE);
        }
      }
    }
  }

  // Interface implementations

  @Override
  public void displayWelcomeMessage() {
    statusLabel.setText("Welcome to Battleship! Click a cell to make your guess.");
  }

  @Override
  public void displayPromptMessage() {
    statusLabel.setText("Click a cell to make your guess.");
  }

  @Override
  public void displayCellGrid(CellState[][] cellGrid) {
    for (int row = 0; row < GRID_SIZE; row++) {
      for (int col = 0; col < GRID_SIZE; col++) {
        updateCell(row, col, cellGrid[row][col]);
      }
    }
  }

  @Override
  public void displayShipGrid(ShipType[][] shipGrid) {
    revealShips(shipGrid);
  }

  @Override
  public void displayGuessCount(int currentGuesses) {
    updateStats(currentGuesses, Integer.parseInt(statsLabel.getText().split(" / ")[1]));
  }

  @Override
  public void displayMaxGuesses(int maxGuesses) {
    int currentGuesses = Integer.parseInt(statsLabel.getText().split(" / ")[0].replace("Guesses: ", ""));
    updateStats(currentGuesses, maxGuesses);
  }

  private void updateStats(int currentGuesses, int maxGuesses) {
    statsLabel.setText("Guesses: " + currentGuesses + " / " + maxGuesses);
  }

  @Override
  public void displayErrorMessage(String message) {
    statusLabel.setText("Error: " + message);
    statusLabel.setForeground(Color.RED);

    // Reset color after 2 seconds
    Timer timer = new Timer(2000, e -> statusLabel.setForeground(TEXT));
    timer.setRepeats(false);
    timer.start();
  }

  @Override
  public void displayGameOver(boolean win) {
    disableAllButtons();

    statusLabel.setText(win ? "You Win! All ships destroyed!" : "Game Over! Out of guesses.");
    statusLabel.setForeground(win ? new Color(0, 120, 0) : Color.RED);

    // Simple restart dialog
    int choice = JOptionPane.showConfirmDialog(
        this,
        win ? "Congratulations! Play again?" : "Game over. Play again?",
        "Game Finished",
        JOptionPane.YES_NO_OPTION
    );

    if (choice == JOptionPane.YES_OPTION) {
      restartGame();
    } else {
      System.exit(0);
    }
  }

  @Override
  public void displayHitMessage() {
    statusLabel.setText("Hit! You hit a ship!");
    statusLabel.setForeground(HIT);
  }

  @Override
  public void displayMissMessage() {
    statusLabel.setText("Miss! Try again.");
    statusLabel.setForeground(new Color(100, 100, 100));
  }

  private void restartGame() {
    for (int row = 0; row < GRID_SIZE; row++) {
      for (int col = 0; col < GRID_SIZE; col++) {
        JButton button = gridButtons[row][col];
        button.setBackground(UNKNOWN);
        button.setText("");
        button.setForeground(TEXT);
        button.setEnabled(true);
      }
    }

    statusLabel.setText("New game started! Click a cell to make your guess.");
    statusLabel.setForeground(TEXT);
    statsLabel.setText("Guesses: 0 / 50");

    firePropertyChange("newGame", false, true);
  }
}