package mockSpaPOS.model;

import javafx.beans.property.*;
import javafx.collections.ObservableList;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class Customer {
    private CheckInGroup checkInGroup;
    private DepartmentItem checkInType;
    private Bill bill;
    private LocalDateTime outTime;
    private StringProperty status;
    private IntegerProperty lockerNumber;
    private StringProperty billTotal;


    public Customer(DepartmentItem checkInType, Locker locker){
        bill= new Bill(locker.getLockerNumber());
        this.checkInType=checkInType;
        checkInGroup= new CheckInGroup(this);
        initProperties(locker);

    }

    //create new CheckInGroup
    public Customer(DepartmentItem checkInType, Locker locker, ArrayList<Locker> lockers){
        bill= new Bill(locker.getLockerNumber());
        this.checkInType=checkInType;
        checkInGroup= new CheckInGroup(this);
        initProperties(locker);
    }

    //add customer to an already existing CheckInGroup if diff group adds group
    // (diff check in times otherwise)
    public Customer(DepartmentItem checkInType, Locker locker, CheckInGroup checkInGroup){
        bill= new Bill(locker.getLockerNumber());
        this.checkInType=checkInType;
        checkInGroup.addGroup(new CheckInGroup(this));
        this.checkInGroup=checkInGroup;
        initProperties(locker);
    }

    public Customer(DepartmentItem checkInType, Locker locker, CheckInGroup checkInGroup,ArrayList<Locker> lockers){
        bill= new Bill(locker.getLockerNumber());
        this.checkInType=checkInType;
        checkInGroup.addGroup(new CheckInGroup(this));
        this.checkInGroup=checkInGroup;
        initProperties(locker);
    }

    public void initProperties(Locker locker){
        status= new SimpleStringProperty(getCheckInTime().toString()+ ": In");
        lockerNumber=new SimpleIntegerProperty(locker.getLockerNumber());
        billTotal=new SimpleStringProperty(String.format("%.2f",bill.getBillTotal()));
        bill.billTotalProperty().addListener(o->{
            billTotal.set(String.format("%.2f",bill.getBillTotal()));
        });
    }



    public LocalTime getCheckInTime(){
        return checkInGroup.getCheckInTime().truncatedTo(ChronoUnit.SECONDS).toLocalTime();
    }

    public Bill getBill(){
        return bill;
    }

    public DepartmentItem getCheckInType(){
        return checkInType;
    }

    public CheckInGroup getCheckInGroup(){
        return checkInGroup;
    }

    public void checkOut(){
        outTime=LocalDateTime.now();
        status.set("Out: " + outTime.truncatedTo(ChronoUnit.SECONDS).toLocalTime().toString());
    }

    public String getStatus() {
        return status.get();
    }

    public StringProperty statusProperty() {
        return status;
    }

    public boolean isCheckedOut(){
        return (outTime!=null);
    }

    public int getLockerNumber() {
        return lockerNumber.get();
    }

    public IntegerProperty lockerNumberProperty() {
        return lockerNumber;
    }

    public StringProperty billTotalProperty(){
        return billTotal;
    }

    public String getBillTotal(){
        return billTotal.get();
    }

    public String getLockerNumberString(){
        return "Locker #" + lockerNumber.get();
    }

    public boolean equals(Object o){
//        if (! (o instanceof Customer)) return false;
//
//        return (getCheckInTime()==((Customer) o).getCheckInTime() &&
//                getLockerNumber()==((Customer) o).getLockerNumber());
        return this==o;
    }

    public void transferTo(int lockerNumber){
        this.lockerNumber.set(lockerNumber);
    }

    public ObservableList<BillItem> getBillList(){
        return bill.getItemList();
    }
}
