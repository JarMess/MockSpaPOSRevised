package mockSpaPOS.model;

import java.util.ArrayList;

public class Category {
    private ArrayList<DepartmentItem> itemList;
    private String fromDep;
    private String name;

    public Category(String name, String fromDep){
        this.name=name;
        this.fromDep=fromDep;
        itemList=new ArrayList<>();
    }

    public void addItem(String name, double price){
        itemList.add(new DepartmentItem(name, price, getHier()));
    }

    private String getHier(){
        return fromDep + ": " + name;
    }

    public ArrayList<DepartmentItem> getItemList(){
        return itemList;
    }

    public DepartmentItem getItemAt(int index){
        return itemList.get(index);
    }

    public String getName(){
        return name;
    }

}
