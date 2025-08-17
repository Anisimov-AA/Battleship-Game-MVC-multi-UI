import battleship.controller.SwingBattleshipController;
import battleship.model.BattleshipModel;
import battleship.view.SwingBattleshipView;

public class SwingApp {

  public static void main(String[] args) {
    BattleshipModel model = new BattleshipModel();
    SwingBattleshipController controller = new SwingBattleshipController(
        model,
        new SwingBattleshipView()
    );

    controller.playGame(model);
  }
}
