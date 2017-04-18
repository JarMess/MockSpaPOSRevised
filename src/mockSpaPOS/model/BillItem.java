package mockSpaPOS.model;

import javafx.beans.property.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class BillItem extends DepartmentItem{

    private LocalTime time;
    private IntegerProperty quantity;
    private StringProperty fullHier;
    private StringProperty pricep;
    private StringProperty timep;

    public BillItem(DepartmentItem item){
        super(item.getName(),item.getPrice(),item.getHierarchy());
        time=LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).toLocalTime();
        quantity= new SimpleIntegerProperty(1);
        fullHier= new SimpleStringProperty(getFullHier());
        pricep= new SimpleStringProperty(String.format("%.2f",getPrice()));
        timep= new SimpleStringProperty(time.toString());
    }


    public int getQuantity() {
        return quantity.get();
    }

    public IntegerProperty quantityProperty() {
        return quantity;
    }

    public void incrementQuantity(){
        int increase=getQuantity()+1;
        quantity.set(increase);
    }

    public StringProperty fullHierProperty() {
        return fullHier;
    }

    public String getPricep() {
        return pricep.get();
    }

    public StringProperty pricepProperty() {
        return pricep;
    }

    public String getTimep() {
        return timep.get();
    }

    public StringProperty timepProperty() {
        return timep;
    }
}
