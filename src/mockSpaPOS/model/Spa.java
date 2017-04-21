package mockSpaPOS.model;

import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;

public class Spa {
    private ArrayList<Locker> lockerList;
    private ArrayList<Department> departmentList;
    private Department reception;
    private BusinessInfo businessInfo;

    private Bill totalSales;
    private ObservableList<Customer> checkOutCustomerList;

    public Spa(){
        lockerList= new ArrayList<>();
        departmentList = new ArrayList<>();

        initializeLockers(2000);
        initializeDepartments();
        initializeReception();
        businessInfo=new BusinessInfo();
        totalSales= new Bill();
        checkOutCustomerList= FXCollections.observableArrayList();
    }

    public StringProperty getLockerStatusPropertyAt(int index){

        return lockerList.get(index).displayStringProperty();
    }

    public Customer getCustomerAt(int index){
        return lockerList.get(index).getCustomer();
    }

    public void initializeLockers(int size){
        for (int i =1; i<=size; i++){
            lockerList.add(new Locker(i));
        }
    }

    public void initializeDepartments(){
        //current: create random departments and items
        //future: grab all item info from a file
        for (int i=1; i<=3; i++){
            Department d= new Department("Department " + i);
            for (int j=1; j<=5; j++) {
                Category c= new Category("Category " + j, d.getName());
                for(int k=1; k<=20; k++) {
                    c.addItem("Item " +  k, k);
                }
                d.addCategory(c);

            }
            departmentList.add(d);
        }
    }

    public void initializeReception(){
        reception= new Department("Reception");
        for(int i=1; i<=3; i++) {
            Category c = new Category("Type "+i, reception.getName());
            for(int j=1; j<=5;j++) {
                if (j==1 && i==1)
                    c.addItem("Free Visit", 0);
                else
                    c.addItem("Item " + j, j);
            }
            reception.addCategory(c);
        }

    }

    public ArrayList<Department> getDepartmentList(){
        return departmentList;
    }

    public ArrayList<Locker> getLockerList(){
        return lockerList;
    }

    public boolean checkIn(int lockerIndex, Customer customer){
        return lockerList.get(lockerIndex).setCustomer(customer);
    }

    public boolean transferCustomer(int fromIndex, int toIndex){
        if (getLocker(toIndex).isOccupied()) return false;
        return getLocker(toIndex).setCustomer(getLocker(fromIndex).removeCustomer());
    }

    public Locker getLocker(int index){
        return lockerList.get(index);

    }

    public void checkOutLocker(int index){
        checkOutCustomerList.add(lockerList.get(index).getCustomer());
        totalSales.addBill(lockerList.get(index).getCustomer().getBill());
        lockerList.get(index).checkOutCustomer();

    }

    public DepartmentItem getDepartmentItem(int dIndex, int cIndex, int iIndex){
        return departmentList.get(dIndex).getItem(cIndex,iIndex);
    }

    public Department getReception() {
        return reception;
    }

    public BusinessInfo getBusinessInfo() {
        return businessInfo;
    }
}
