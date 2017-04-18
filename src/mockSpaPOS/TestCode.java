package mockSpaPOS;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

public class TestCode {
    public static void main(String[] args) {
        ObservableList<Integer> int1= FXCollections.observableArrayList();
        ObservableList<Integer> int2= FXCollections.observableArrayList();
        ObservableList<Integer> int3= FXCollections.observableArrayList();

        int1.addListener((ListChangeListener.Change<? extends Integer> c)->{
            while (c.next()){
                if (c.wasAdded()) {
                    int2.addAll(c.getAddedSubList());
                    System.out.println("added to int1");
                }
            }
        });

        int2.addListener((ListChangeListener.Change<? extends Integer> c)->{
            while (c.next()){
                if (c.wasAdded()) {
                    int3.addAll(c.getAddedSubList());
                    System.out.println("added to int2");
                }
            }
        });

        int1.add(new Integer(10));

        System.out.println(int3.get(0));




    }
}
