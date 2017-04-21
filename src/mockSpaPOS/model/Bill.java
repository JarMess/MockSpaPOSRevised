package mockSpaPOS.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

public class Bill {
    private ObservableList<BillItem> itemList;
    private DoubleProperty billTotal;
    public static final double taxPercent=.08875;
    private IntegerProperty customerLockerNum;
    private ListChangeListener<BillItem> listChangeListener;

    public Bill(){
        itemList= FXCollections.observableArrayList();
        billTotal= new SimpleDoubleProperty(0);
        listChangeListener=new ListChangeListener<BillItem>() {
            @Override
            public void onChanged(Change<? extends BillItem> c) {

            }
        };
        addItemListListener();
    }

    public Bill(int lockerNumber){
        itemList= FXCollections.observableArrayList();
        billTotal= new SimpleDoubleProperty(0);
        listChangeListener=new ListChangeListener<BillItem>() {
            @Override
            public void onChanged(Change<? extends BillItem> c) {

            }
        };
        customerLockerNum=new SimpleIntegerProperty(lockerNumber);
        addItemListListener();
    }

    public void addBill(Bill bill){
        ObservableList<BillItem>  billOL=bill.getItemList();
        for (BillItem bI: billOL){
            if (bI.custLockerNumberProperty()==null)// only time lockernumber is null is if its in preview
                bI.setCustLockerNumber(customerLockerNum);
            //dont do anything if lockernumber is already set, we want to know where the purchase came from.
        }
        itemList.addAll(billOL);
    }

    public void addItem(DepartmentItem item){
        if (! (itemList.contains(item) && !itemList.isEmpty())) {
            BillItem newItem= new BillItem(item,customerLockerNum);
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

    public int getCustomerLockerNum() {
        return customerLockerNum.get();
    }

    public IntegerProperty customerLockerNumProperty() {
        return customerLockerNum;
    }

    public void addListener(ListChangeListener<BillItem> listChangeListener){
        this.listChangeListener=listChangeListener;
        itemList.addListener(this.listChangeListener);
    }

    public void removeListener(){
        itemList.removeListener(listChangeListener);
    }
}
