package mockSpaPOS.model;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class CheckInGroup {
    private LocalDateTime checkInTime;
    private ArrayList<Customer> customerGroup;  //keeps track of all lockers checked in together
    private ArrayList<CheckInGroup> checkInGroups;  //keeps track of diff times when combining bills
    private boolean checkedOut;

    public CheckInGroup(Customer customer){
        checkInTime=LocalDateTime.now();
        customerGroup=new ArrayList<>();
        customerGroup.add(customer);
        checkInGroups=new ArrayList<>();
        checkInGroups.add(this);
    }

    public CheckInGroup(ArrayList<Customer> customerGroup){
        checkInTime=LocalDateTime.now();
        customerGroup.addAll(customerGroup);
        checkInGroups=new ArrayList<>();
        checkInGroups.add(this);
    }

//    public boolean changeFromTo(Locker from, Locker to){
//        if (customerGroup.remove(from)) {
//            customerGroup.add(to);
//            return true;
//        }
//        return false;
//    }

    public void add(Customer customer){
        customerGroup.add(customer);
    }

    public void add(ArrayList<Customer> customerGroup){
        this.customerGroup.addAll(customerGroup);
    }

    public void addGroup(CheckInGroup checkInGroup){
        if (!(checkInGroups.contains(checkInGroup))) {
            checkInGroups.add(checkInGroup);
            checkInGroup.setCheckInGroups(checkInGroups);
            add(checkInGroup.customerGroup);
        }
        else {// if checkInGroups contains checkInGroup(same lockers) then its the same group;
            checkInGroup=this;
        }
    }

    public void setCheckInGroups(ArrayList<CheckInGroup> checkInGroups){
        this.checkInGroups=checkInGroups;
    }

    public LocalDateTime getCheckInTime(){
        return checkInTime;
    }

    public boolean equals(Object o) {
        //only need to check that 1 of the lockers in teh group are the same
        //(shouldnt be possible to have another group with a locker from checkin group)
        //(if one of the lockers in o arent in current group, rest shouldnt be)
        if (! (o instanceof CheckInGroup))return false;
        if (customerGroup.equals(((CheckInGroup)o).customerGroup)) return true;

        for (Customer c: customerGroup){
            for (Customer c2: ((CheckInGroup)o).customerGroup)
                if (c.equals(c2)) return true;
        }
        return false;
    }

    public ArrayList<Customer> getCustomerGroup() {
        return customerGroup;
    }

//    public ArrayList<Customer> getCustomerGroup(){
//        ArrayList<Customer> customerAL= new ArrayList<>();
//        for(Locker l : getLockerGroup()){
//            customerAL.add(l.getCustomer());
//        }
//        return customerAL;
//    }
}
