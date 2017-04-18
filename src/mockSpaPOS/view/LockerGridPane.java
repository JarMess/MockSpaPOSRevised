package mockSpaPOS.view;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;

public class LockerGridPane extends GridPane{
    @FXML private LockerStatusGridPane lockerStatusGridPane;
    @FXML private LockerPageGridPane lockerPageGridPane;

    public LockerGridPane(){
        FXMLLoader fxmlLoader=new FXMLLoader(getClass().getResource("lockerGridPaneFXML.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try{
            fxmlLoader.load();
        }catch (Exception e){
            System.out.println(e);
        }
    }

    public ObservableList<Node> getLockerPageButtons(){
        return lockerPageGridPane.getChildren();
    }

    public ObservableList<Node> getLockerStatusButtons(){
        return lockerStatusGridPane.getChildren();
    }
}
