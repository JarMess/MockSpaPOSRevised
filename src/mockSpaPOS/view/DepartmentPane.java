package mockSpaPOS.view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;

public class DepartmentPane extends HBox {
    @FXML private CategoryVBox catVBox;
    @FXML private ItemVBox itemVBox;

    public DepartmentPane() throws Exception{
        FXMLLoader fxmlLoader= new FXMLLoader(getClass().getResource("departmentPaneFXML.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        fxmlLoader.load();
    }

    public CategoryVBox getCatVBox(){
        return catVBox;
    }

    public ItemVBox getItemVBox(){
        return itemVBox;
    }

}
