package battleship.controller;

import battleship.model.IBattleshipModel;
import battleship.view.SwingBattleshipView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Swing-based controller for the Battleship game.
 * Handles GUI interactions and coordinates between the model and view.
 */
public class SwingBattleshipController implements IBattleshipController, ActionListener, PropertyChangeListener {

  private final IBattleshipModel model;
  private final SwingBattleshipView view;

  /**
   * Creates a new Swing controller with the given model and view.
   *
   * @param model the battleship game model
   * @param view the swing GUI view
   */
  public SwingBattleshipController(IBattleshipModel model, SwingBattleshipView view) {
    if (model == null || view == null) {
      throw new IllegalArgumentException("Model and view cannot be null");
    }

    this.model = model;
    this.view = view;

    // Set up event listeners
    view.setButtonClickListener(this);
    view.addPropertyChangeListener(this);
  }

  /**
   * Starts and manages the GUI-based Battleship game.
   * Initializes the game and makes the GUI visible.
   */
  @Override
  public void playGame(IBattleshipModel model) {
    // Start the game
    this.model.startGame();

    // Initialize the view
    updateGameDisplay();
    view.displayWelcomeMessage();

    // Show the GUI
    view.setVisible(true);
  }

  /**
   * Handles button clicks on the game grid.
   * Processes user guesses when cells are clicked.
   *
   * @param e the action event from button click
   */
  @Override
  public void actionPerformed(ActionEvent e) {
    if (model.isGameOver()) {
      return; // Ignore clicks when game is over
    }

    try {
      // Parse button coordinates from action command
      String[] coords = e.getActionCommand().split(",");
      int row = Integer.parseInt(coords[0]);
      int col = Integer.parseInt(coords[1]);

      // Make the guess
      boolean hit = model.makeGuess(row, col);

      // Update the display
      updateGameDisplay();

      // Show immediate feedback
      if (hit) {
        view.displayHitMessage();
      } else {
        view.displayMissMessage();
      }

      // Check if game is over
      if (model.isGameOver()) {
        // Small delay to show the hit/miss result before game over
        javax.swing.Timer timer = new javax.swing.Timer(1500, evt -> {
          view.displayShipGrid(model.getShipGrid());
          view.displayGameOver(model.areAllShipsSunk());
        });
        timer.setRepeats(false);
        timer.start();
      }

    } catch (IllegalArgumentException ex) {
      // Handle duplicate guesses or invalid coordinates
      view.displayErrorMessage(ex.getMessage());
    } catch (IllegalStateException ex) {
      // Handle game over state
      view.displayErrorMessage("Game is already over");
    } catch (Exception ex) {
      // Handle unexpected errors
      view.displayErrorMessage("An unexpected error occurred");
    }
  }

  /**
   * Handles property change events from the view (e.g., new game requests).
   *
   * @param evt the property change event
   */
  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    if ("newGame".equals(evt.getPropertyName())) {
      startNewGame();
    }
  }

  /**
   * Starts a new game by resetting the model and updating the display.
   */
  private void startNewGame() {
    model.startGame();
    updateGameDisplay();
    view.displayWelcomeMessage();
  }

  /**
   * Updates the game display with current model state.
   * Refreshes guess count, max guesses, and cell grid.
   */
  private void updateGameDisplay() {
    view.displayGuessCount(model.getGuessCount());
    view.displayMaxGuesses(model.getMaxGuesses());
    view.displayCellGrid(model.getCellGrid());
  }
}