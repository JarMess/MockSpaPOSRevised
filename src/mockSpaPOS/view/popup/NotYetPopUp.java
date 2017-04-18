package mockSpaPOS.view.popup;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class NotYetPopUp extends VBox{
    @FXML Button okayButton;

    public NotYetPopUp() throws Exception{
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("notYetPopUpFXML.fxml"));
            fxmlLoader.setRoot(this);
            fxmlLoader.setController(this);
            fxmlLoader.load();
        }catch (Exception e){
            System.out.println(e);
        }
    }

    @FXML
    public void clickedOkay(){
        ((Stage) okayButton.getScene().getWindow()).close();
    }
}
