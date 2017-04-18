package mockSpaPOS.model;

import javafx.beans.property.DoubleProperty;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class Customer {
    private CheckInGroup lockerGroup;
    private DepartmentItem checkInType;
    private Bill bill;
    private LocalDateTime outTime;

    public Customer(DepartmentItem checkInType, Locker locker){
        bill= new Bill();
        this.checkInType=checkInType;
        lockerGroup= new CheckInGroup(locker);
    }

    //create new CheckInGroup
    public Customer(DepartmentItem checkInType, ArrayList<Locker> lockers){
        bill= new Bill();
        this.checkInType=checkInType;
        lockerGroup= new CheckInGroup(lockers);
    }

    //add customer to an already existing CheckInGroup if diff group adds group
    // (diff check in times otherwise)
    public Customer(DepartmentItem checkInType, Locker locker, CheckInGroup lockerGroup){
        bill= new Bill();
        this.checkInType=checkInType;
        lockerGroup.addGroup(new CheckInGroup(locker));
        this.lockerGroup=lockerGroup;
    }

    public Customer(DepartmentItem checkInType, ArrayList<Locker> lockers, CheckInGroup lockerGroup){
        bill= new Bill();
        this.checkInType=checkInType;
        lockerGroup.addGroup(new CheckInGroup(lockers));
        this.lockerGroup=lockerGroup;
    }

    public DoubleProperty billTotalProperty(){
        return bill.billTotalProperty();
    }

    public double getBillTotal(){
        return bill.getBillTotal();
    }

    public LocalTime getCheckInTime(){
        return lockerGroup.getCheckInTime().truncatedTo(ChronoUnit.SECONDS).toLocalTime();
    }

    public Bill getBill(){
        return bill;
    }

    public DepartmentItem getCheckInType(){
        return checkInType;
    }

    public CheckInGroup getLockerGroup(){
        return lockerGroup;
    }

    public void checkOut(){
        outTime=LocalDateTime.now();
    }
}
