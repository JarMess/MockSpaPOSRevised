package mockSpaPOS.view.popup;

import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import mockSpaPOS.model.BillItem;
import mockSpaPOS.model.Locker;

public class LockerPopUp extends GridPane{
    @FXML Label tableTitle;
    @FXML TableView<BillItem> tableView;
    @FXML TableColumn lockerCol;
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

        lockerCol.setCellValueFactory(new PropertyValueFactory<BillItem,String>("custLockerNumber"));
        itemCol.setCellValueFactory( new PropertyValueFactory<BillItem,String>("fullHier"));
        priceCol.setCellValueFactory( new PropertyValueFactory<BillItem,String>("pricep"));
        quantCol.setCellValueFactory( new PropertyValueFactory<BillItem,String>("quantity"));
        timeCol.setCellValueFactory( new PropertyValueFactory<BillItem,String>("timep"));
    }

    public StringProperty titleProperty(){
        return tableTitle.textProperty();
    }

    public void setLocker(Locker locker){
        tableTitle.setText(locker.getLockerNumberString());
        if(locker.isOccupied())
            tableView.setItems(locker.getCustomer().getBill().getItemList());
        else
            tableView.setItems(FXCollections.observableArrayList());

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
