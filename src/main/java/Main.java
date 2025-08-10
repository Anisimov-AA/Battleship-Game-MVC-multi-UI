import battleship.controller.BattleshipConsoleController;
import battleship.model.BattleshipModel;
import battleship.view.BattleshipConsoleView;
import java.io.InputStreamReader;

public class Main {

  public static void main(String[] args) {
    BattleshipConsoleController controller = new BattleshipConsoleController(
      new InputStreamReader(System.in),
      new BattleshipConsoleView(System.out)
    );

    controller.playGame(new BattleshipModel());
  }

}
