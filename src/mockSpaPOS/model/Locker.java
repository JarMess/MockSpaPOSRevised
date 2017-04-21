package mockSpaPOS.model;

import javafx.beans.InvalidationListener;
import javafx.beans.property.*;

public class Locker {
    private BooleanProperty occupied;
    private BooleanProperty lost;
    private StringProperty displayString;
    private Customer customer;
    private InvalidationListener updateListener;
    private IntegerProperty lockerNumber;

    public Locker(int lockerNumber){
        this.lockerNumber=new SimpleIntegerProperty(lockerNumber);
        occupied= new SimpleBooleanProperty(false);
        lost= new SimpleBooleanProperty(false);
        displayString =new SimpleStringProperty();
        updateListener= (o-> {
            updateDisplayString();
        });

        setLockerListeners();
        updateDisplayString();

        customer=null;
    }

    public boolean setCustomer(Customer customer){
        if (isOccupied()||customer==null) return false;
        this.customer=customer;
        occupied.set(true);
        addListenerToBill();
        return true;
    }

    private void addListenerToBill(){
        customer.getBill().billTotalProperty().addListener(updateListener);
    }

    public Customer getCustomer(){
        return customer;
    }

    public boolean isOccupied() {
        return occupied.get();
    }

    public BooleanProperty occupiedProperty() {
        return occupied;
    }

    public String getDisplayString() {
        return displayString.get();
    }

    public StringProperty displayStringProperty() {
        return displayString;
    }

    private void setLockerListeners(){
        occupied.addListener(updateListener);
        lost.addListener(updateListener);
    }

    public boolean isLost() {
        return lost.get();
    }

    public BooleanProperty lostProperty() {
        return lost;
    }

    private void updateDisplayString(){
        //currently: displayString must be updated every time any aspect of locker status is updated
        //future: possibly update only aspect that is being updated - > add children to buttons
        StringBuffer sb= new StringBuffer();
        sb.append(getLockerNumberString());
        if (!(occupied.get())){
            if (lost.get())
                sb.append("\nKey Lost!!");
            else
                sb.append("\nLocker Available");
        } else {

            if (!(lost.get())) {
                sb.append("\nCheck-In: ");
                sb.append(customer.getCheckInTime());
            } else
                sb.append("\nKeyLost!!");
            sb.append("\nBill Total: $");
            sb.append(String.format("%.2f",customer.getBill().getBillTotal()));
        }
        displayString.set(sb.toString());
    }

    public Customer removeCustomer(){
        if (customer==null) return null;
        Customer removeCustomer= customer;
        customer.getBill().billTotalProperty().removeListener(updateListener);
        customer=null;
        occupied.set(false);
        return removeCustomer;
    }

    public void setLost(boolean lost){
        this.lost.set(lost);
    }

    public String getLockerNumberString(){
        StringBuffer sb=new StringBuffer();
        sb.append("Locker #");
        sb.append(lockerNumber.get());
        return sb.toString();
    }

    public int getLockerNumber() {
        return lockerNumber.get();
    }

    public IntegerProperty lockerNumberProperty() {
        return lockerNumber;
    }

    public void checkOutCustomer(){
        customer.checkOut();
        customer=null;
        occupied.set(false);

    }

    public boolean equals(Object o){
        if (!(o instanceof Locker)) return false;
        return lockerNumber.get()==((Locker)o).getLockerNumber();
    }

}

