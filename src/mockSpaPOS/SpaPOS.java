package mockSpaPOS;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SpaPOS extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Spa POS");
        primaryStage.setScene(new Scene(new SpaPOSController(), 1600, 1000));
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
