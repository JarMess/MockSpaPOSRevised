package mockSpaPOS.model;

import javafx.beans.InvalidationListener;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.util.Iterator;

public class Locker {
    private BooleanProperty occupied;
    private BooleanProperty lost;
    private StringProperty displayString;
    private Customer customer;
    private InvalidationListener updateListener;
    private IntegerProperty lockerNumber;
    private StringProperty checkInTime;
    private StringProperty billTotal;
    private InvalidationListener updateBillListener;
    private ObservableList<LockerItem> lockerItems;

    public Locker(int lockerNumber){
        this.lockerNumber=new SimpleIntegerProperty(lockerNumber);
        occupied= new SimpleBooleanProperty(false);
        lost= new SimpleBooleanProperty(false);
        displayString =new SimpleStringProperty();
        checkInTime=new SimpleStringProperty();
        billTotal=new SimpleStringProperty();
        lockerItems= FXCollections.observableArrayList();

        updateListener= (o-> {
            updateDisplayString();
        });

        updateBillListener= (o-> {
            setProperties();
        });

        setLockerListeners();
        updateDisplayString();

        customer=null;
    }

    public boolean setCustomer(Customer customer){
        if (isOccupied()||customer==null) return false;
        this.customer=customer;
        occupied.set(true);
        setCustomerListener();
        setProperties();
        addListenerToBill();
        return true;
    }

    public boolean setCustomer(DepartmentItem checkInType){
        if (isOccupied()) return false;
        this.customer=new Customer(checkInType,this);
        occupied.set(true);
        setCustomerListener();
        setProperties();
        addListenerToBill();
        return true;
    }

    private void setProperties(){
        checkInTime.set(customer.getCheckInTime().toString());
        billTotal.set(String.format("%.2f",customer.getBillTotal()));
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
//        occupied.addListener(o->{
//            updateDisplayString();
//        });
//
//        lost.addListener(o->{
//            updateDisplayString();
//        });
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
            sb.append(String.format("%.2f",customer.getBillTotal()));
        }
        displayString.set(sb.toString());
    }

    private void setCustomerListener(){

        customer.billTotalProperty().addListener(updateListener);
        customer.billTotalProperty().addListener(updateBillListener);
    }

    public Customer removeCustomer(){
        if (customer==null) return null;
        Customer removeCustomer= customer;
        customer.billTotalProperty().removeListener(updateListener);
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

    public String getCheckInTime() {
        return checkInTime.get();
    }

    public StringProperty checkInTimeProperty() {
        return checkInTime;
    }

    public String getBillTotal() {
        return billTotal.get();
    }

    public StringProperty billTotalProperty() {
        return billTotal;
    }

    public ObservableList<LockerItem> getItemList(){
        return lockerItems;
    }

    public void addListenerToBill(){
        setItemList();
        customer.getBill().getItemList().addListener((ListChangeListener.Change<? extends BillItem> c)->{
            while (c.next()) {
                if (c.wasAdded()){
                    addToItemList(c);
                }
                else if (c.wasRemoved()) {
                    removeFromItemList(c);
                }
            }
        });
    }

    public void setItemList(){
        lockerItems.clear();
        for(BillItem b: customer.getBill().getItemList()){
            lockerItems.add(new LockerItem(b));
        }
    }

    public void addToItemList(ListChangeListener.Change<? extends BillItem> c){

        for(BillItem b: c.getAddedSubList()){
            lockerItems.add(new LockerItem(b));
        }
    }

    public void removeFromItemList(ListChangeListener.Change<? extends BillItem> c){
        for(BillItem b: c.getRemoved()){
            Iterator<LockerItem> iter= lockerItems.iterator();
            while (iter.hasNext()){
                if (iter.next().getB()==b)
                    iter.remove();
            }
        }
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

    public class LockerItem{
        private IntegerProperty lockerNum,quantity;
        private StringProperty fullHier,pricep,timep;
        private BillItem b;
        public LockerItem(BillItem b){
            this.b=b;
            lockerNum=lockerNumberProperty();
            fullHier=b.fullHierProperty();
            pricep=b.pricepProperty();
            quantity=b.quantityProperty();
            timep=b.timepProperty();
        }

        public boolean equals(Object o){
            if (!(o instanceof LockerItem)) return false;
            return lockerNum.get()==((LockerItem)o).getLockerNum();
        }

        public int getLockerNum() {
            return lockerNumber.get();
        }

        public IntegerProperty lockerNumProperty() {
            return lockerNumber;
        }

        public int getQuantity() {
            return quantity.get();
        }

        public IntegerProperty quantityProperty() {
            return quantity;
        }

        public String getFullHier() {
            return fullHier.get();
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

        public BillItem getB() {
            return b;
        }
    }
}

