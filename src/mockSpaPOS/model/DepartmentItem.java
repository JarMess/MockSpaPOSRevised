package mockSpaPOS.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class DepartmentItem {
    private StringProperty name;
    private DoubleProperty price;
    private String hierarchy;
    //private double itemId;

    public DepartmentItem(String name, double price, String hierarchy) {
        this.name= new SimpleStringProperty(name);
        this.price=new SimpleDoubleProperty(price);
        this.hierarchy=hierarchy;
    }

    public boolean equals(Object o){
        if (!(o instanceof DepartmentItem)) return false;
        return (getFullHier().equals(((DepartmentItem)o).getFullHier()));
    }

    public String getFullHier(){
        return hierarchy +": " + name.get();
    }

    public double getPrice(){
        return price.get();
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public DoubleProperty priceProperty() {
        return price;
    }

    public String getHierarchy() {
        return hierarchy;
    }
}
