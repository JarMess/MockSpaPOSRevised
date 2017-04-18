package mockSpaPOS.view.popup;


import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;

public class LockerPopUp extends GridPane{
    @FXML Label tableTitle;
    @FXML TableView tableView;
    @FXML TableColumn itemCol;
    @FXML TableColumn priceCol;
    @FXML TableColumn quantCol;
    @FXML TableColumn timeCol;
    @FXML Button checkInButton;
    @FXML Button checkOutButton;
    @FXML Button lostWatchButton;

    public LockerPopUp() throws Exception{
        FXMLLoader fxmlLoader= new FXMLLoader(getClass().getResource("lockerPopUpFXML.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        fxmlLoader.load();
    }

    public StringProperty titleProperty(){
        return tableTitle.textProperty();
    }

    public Button getCheckInButton() {
        return checkInButton;
    }

    public Button getCheckOutButton() {
        return checkOutButton;
    }

    public Button getLostWatchButton() {
        return lostWatchButton;
    }
}
