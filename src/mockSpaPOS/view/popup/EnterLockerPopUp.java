package mockSpaPOS.view.popup;

import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;


public class EnterLockerPopUp extends GridPane{
    @FXML private TextField textField;
    @FXML private Button enterButton;

    public EnterLockerPopUp() throws Exception{
        FXMLLoader fxmlLoader= new FXMLLoader(getClass().getResource("enterLockerPopUpFXML.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        fxmlLoader.load();
    }

    public Button getEnterButton(){
        return enterButton;
    }

    public StringProperty textFieldProperty(){
        return textField.textProperty();
    }
}
