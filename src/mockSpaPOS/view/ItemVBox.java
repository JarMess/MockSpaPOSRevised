package mockSpaPOS.view;

import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class ItemVBox extends VBox {
    @FXML Label categoryLabel;
    @FXML StackPane itemStackPane;

    public ItemVBox() throws Exception{
        FXMLLoader fxmlLoader=new FXMLLoader(getClass().getResource("itemVBoxFXML.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        fxmlLoader.load();
    }

    public StringProperty labelProperty(){
        return categoryLabel.textProperty();
    }

    public StackPane getItemStackPane(){
        return itemStackPane;
    }
}
