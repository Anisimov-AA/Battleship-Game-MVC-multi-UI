package battleship.controller;

import battleship.model.IBattleshipModel;

/**
 * Represents a Controller for Battleship: handle user input and update the model accordingly;
 * convey outcomes to the user in some form.
 */
public interface IBattleshipController {
  /**
   * Execute a single game of Battleship given a BattleshipModel. When the game is over, the
   * playGame method ends.
   *
   * @param model the BattleshipModel to play the game with
   * @throws NullPointerException if the model is null
   */
  void playGame(IBattleshipModel model);
}
