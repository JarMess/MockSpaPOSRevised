package mockSpaPOS.view;

import javafx.scene.layout.GridPane;


public class LockerPageGridPane extends GridPane{
    public LockerPageGridPane(){
        super();
        for (int i=0; i <2;i++)
            for (int j=0; j<10; j++){
                int from=(i*1000+j*100) +1;
                int to =(i*1000+j*100)+100;
                LockerPageButton temp= new LockerPageButton(from +" - " + to);
                add(temp,j,i);

            }
    }
}
