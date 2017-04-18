package mockSpaPOS.view;

import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class CategoryVBox extends VBox{
    @FXML VBox catButtonBox;
    @FXML Label departmentLabel;

    public CategoryVBox() throws Exception{
        FXMLLoader fxmlLoader= new FXMLLoader(getClass().getResource("categoryVBoxFXML.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        fxmlLoader.load();
    }

    public VBox getCatButtonBox(){
        return catButtonBox;
    }

    public void setLabel(String text){
        departmentLabel.textProperty().set(text);
    }
}
