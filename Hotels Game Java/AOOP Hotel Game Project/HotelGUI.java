
import HotelGameModel.HotelGameModel;
import HotelGameView.HotelGameView;
import HotelGameController.HotelGameController;

public class HotelGUI {
    public static void main(String[] args) {
        // Create a new model
        HotelGameModel model = new HotelGameModel();

        // Create a new view using the model
        HotelGameView view = new HotelGameView(model);

        // Create a new controller using the model and view
        HotelGameController controller = new HotelGameController(model, view);

        // Start the game
        controller.playGame();
    }
}