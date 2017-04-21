package mockSpaPOS.view;

import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import mockSpaPOS.model.Bill;
import mockSpaPOS.model.BillItem;
import mockSpaPOS.model.Customer;
import mockSpaPOS.model.Locker;


public class CheckOutPane extends GridPane{
    @FXML private EditBillPane currentBillPane;
    @FXML private EditBillPane previewBillPane;
    @FXML private VBox chooseDepVbox;
    @FXML private StackPane departmentStackPane;
    @FXML private Button setLockerButton;
    @FXML private Button checkOutButton;
    @FXML private Button addItemsButton;

    public CheckOutPane() throws Exception{
        FXMLLoader fxmlLoader=new FXMLLoader(getClass().getResource("checkOutPaneFXML.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        fxmlLoader.load();
    }

    public void showDep(int index){
        for(Node pane: departmentStackPane.getChildren()){
            pane.setVisible(false);
        }
        departmentStackPane.getChildren().get(index).setVisible(true);
    }

    public VBox getDepVBOX(){
        return chooseDepVbox;
    }

    public StackPane getdepStackPane(){
        return departmentStackPane;
    }

    public Button getSetLockerButton() {
        return setLockerButton;
    }

    public Button getCheckOutButton() {
        return checkOutButton;
    }

    public Button getAddItemsButton() {
        return addItemsButton;
    }

    public EditBillPane getPreviewBillPane() {
        return previewBillPane;
    }

    public EditBillPane getCurrentBillPane() {
        return currentBillPane;
    }
}
