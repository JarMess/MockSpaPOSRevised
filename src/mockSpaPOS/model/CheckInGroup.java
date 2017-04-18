package mockSpaPOS.model;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class CheckInGroup {
    private LocalDateTime checkInTime;
    private ArrayList<Locker> lockerGroup;  //keeps track of all lockers checked in together
    private ArrayList<CheckInGroup> checkInGroups;  //keeps track of diff times when combining bills
    private boolean checkedOut;

    public CheckInGroup(Locker locker){
        checkInTime=LocalDateTime.now();
        lockerGroup=new ArrayList<>();
        lockerGroup.add(locker);
        checkInGroups=new ArrayList<>();
        checkInGroups.add(this);
    }

    public CheckInGroup(ArrayList<Locker> lockerGroup){
        checkInTime=LocalDateTime.now();
        this.lockerGroup=lockerGroup;
        checkInGroups=new ArrayList<>();
        checkInGroups.add(this);
    }

    public boolean changeFromTo(Locker from, Locker to){
        if (lockerGroup.remove(from)) {
            lockerGroup.add(to);
            return true;
        }
        return false;
    }

    public void add(Locker locker){
        lockerGroup.add(locker);
    }

    public void add(ArrayList<Locker> lockerGroup){
        lockerGroup.addAll(lockerGroup);
    }

    public void addGroup(CheckInGroup checkInGroup){
        if (!(checkInGroups.contains(checkInGroup))) {
            checkInGroups.add(checkInGroup);
            checkInGroup.setCheckInGroups(checkInGroups);
            add(checkInGroup.lockerGroup);
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
        if (lockerGroup.equals(((CheckInGroup)o).lockerGroup)) return true;

        for (Locker l: lockerGroup){
            for (Locker l2: ((CheckInGroup)o).lockerGroup)
                if (l.equals(l2)) return true;
        }
        return false;
    }

    public ArrayList<Locker> getLockerGroup() {
        return lockerGroup;
    }
}
