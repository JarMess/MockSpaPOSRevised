package mockSpaPOS.view;

import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;

public class LockerStatusGridPane extends GridPane {

    public LockerStatusGridPane(){
        super();
        for (int i=0; i <10;i++)
            for (int j=0; j<10; j++){
                Button lockerStatusButton=new Button();
                lockerStatusButton.setPrefSize(140,65);
                add(lockerStatusButton,j,i);

            }


    }
}
