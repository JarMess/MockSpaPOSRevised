package mockSpaPOS.view.popup;/**
 * Created by James on 3/30/2017.
 */

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TestPopUp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setScene(new Scene(new NotYetPopUp(),300,200));
        primaryStage.show();

        Stage stage=new Stage();
        stage.setScene(new Scene(new LockerPopUp(),600,800 ));
        stage.show();

        Stage stage1=new Stage();
        stage1.setScene(new Scene(new EnterLockerPopUp(),350,200 ));
        stage1.show();

    }
}
