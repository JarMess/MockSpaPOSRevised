package mockSpaPOS.model;

import java.util.ArrayList;

public class Department {
    private String name;
    private ArrayList<Category> catList;

    public Department(String name){
        this.name=name;
        catList=new ArrayList<>();
    }

    public void addCategory(String catName){
        catList.add(new Category(catName,name));
    }

    public void addCategory(Category cat){
        catList.add(cat);
    }

    public ArrayList<Category> getCategoryList(){
        return catList;
    }

    public Category getCategoryAt(int index){
        return catList.get(index);
    }

    public ArrayList<DepartmentItem> getItemList(int catIndex){
        return catList.get(catIndex).getItemList();
    }

    public DepartmentItem getItem(int catIndex, int iIndex){
        return catList.get(catIndex).getItemAt(iIndex);
    }

    public String getName(){
        return name;
    }

}
