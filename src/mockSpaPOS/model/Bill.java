package mockSpaPOS.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

public class Bill {
    private ObservableList<BillItem> itemList;
    private DoubleProperty billTotal;
    public static final double taxPercent=.08875;

    public Bill(){
        itemList= FXCollections.observableArrayList();
        billTotal= new SimpleDoubleProperty(0);
        addItemListListener();
    }

    public void addBill(Bill bill){
        itemList.addAll(bill.getItemList());
    }

    public void addItem(DepartmentItem item){
        if (! (itemList.contains(item) && !itemList.isEmpty())) {
            BillItem newItem= new BillItem(item);
            itemList.add(newItem);
            newItem.quantityProperty().addListener(o->{
                updateBillTotal();
            });
        }else{
            itemList.get(itemList.indexOf(item)).incrementQuantity();
        }
    }

    public ObservableList<BillItem> getItemList(){
        return itemList;
    }

    public double getBillTotal() {
        return billTotal.get();
    }

    public DoubleProperty billTotalProperty() {
        return billTotal;
    }

    private void addItemListListener(){
        itemList.addListener((ListChangeListener.Change<? extends BillItem> c)-> {
            while (c.next()) {
                if (c.wasUpdated() || c.wasAdded() || c.wasRemoved()) {
                    updateBillTotal();
                }
            }
        });
    }

    private void updateBillTotal(){
        //once again redoing the bill total calc every time an item is added
        //can add one item to bill total
        double total=0;
        for(BillItem item: itemList){
            total+= item.getPrice()*item.getQuantity();
        }
        billTotal.set(total);
    }

    public void removeAll(){
        itemList.clear();
    }

    public static double getTaxPercent() {
        return taxPercent;
    }
}
