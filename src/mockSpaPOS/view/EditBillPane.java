package mockSpaPOS.view;

import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import mockSpaPOS.model.Bill;
import mockSpaPOS.model.BillItem;
import mockSpaPOS.model.Locker;

import java.util.Iterator;

public class EditBillPane extends GridPane {
    @FXML private Label label;
    @FXML private TableView tableView;
    @FXML private TableColumn lockerCol;
    @FXML private TableColumn itemCol;
    @FXML private TableColumn priceCol;
    @FXML private TableColumn quantCol;
    @FXML private TableColumn timeCol;
    private boolean set;
    private ObservableList<Locker> checkOutList;
    private ObservableList<BillItem> billItems;
    private ObservableList<Locker.LockerItem> lockerItems;

    public EditBillPane() throws Exception{
        FXMLLoader fxmlLoader=new FXMLLoader(getClass().getResource("editBillPaneFXML.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        fxmlLoader.load();

        checkOutList =FXCollections.observableArrayList();
        checkOutList.addListener((ListChangeListener.Change<? extends Locker> c)->{
            while (c.next()){
                if (c.wasAdded()){
                    Iterator iter= c.getAddedSubList().iterator();
                    while (iter.hasNext()){
                        lockerItems.addAll(((Locker)(iter.next())).getItemList());
                    }
                }else if (c.wasRemoved()){
                    Iterator iter= c.getRemoved().iterator();
                    while (iter.hasNext()) {
                        lockerItems.removeAll(((Locker)(iter.next())).getItemList());
                    }
                }
            }
        });
        set=false;
        lockerItems=FXCollections.observableArrayList();
        billItems=FXCollections.observableArrayList();
    }

    public void setBill(Bill bill){
        clear(label.getText());
        lockerCol.setVisible(false);
        itemCol.setCellValueFactory( new PropertyValueFactory<BillItem,String>("fullHier"));
        priceCol.setCellValueFactory( new PropertyValueFactory<BillItem,String>("pricep"));
        quantCol.setCellValueFactory( new PropertyValueFactory<BillItem,String>("quantity"));
        timeCol.setCellValueFactory( new PropertyValueFactory<BillItem,String>("timep"));
        addBill(bill);
        tableView.setItems(billItems);

        set=true;
    }

    public void setLocker(Locker locker){
        clear(label.getText());
        lockerCol.setVisible(true);
        addLocker(locker);
        lockerCol.setCellValueFactory(new PropertyValueFactory<Locker.LockerItem,String>("lockerNum"));
        itemCol.setCellValueFactory( new PropertyValueFactory<Locker.LockerItem,String>("fullHier"));
        priceCol.setCellValueFactory( new PropertyValueFactory<Locker.LockerItem,String>("pricep"));
        quantCol.setCellValueFactory( new PropertyValueFactory<Locker.LockerItem,String>("quantity"));
        timeCol.setCellValueFactory( new PropertyValueFactory<Locker.LockerItem,String>("timep"));

        tableView.setItems(lockerItems);
        set=true;
    }

    public StringProperty labelProperty(){
        return label.textProperty();
    }

    public void setLabel(String text){
        label.setText(text);
    }

    public void setSet(boolean b){
        set=b;
    }

    public boolean getSet(){
        return set;
    }

    public void clear(String text){
        tableView.getItems().clear();
        checkOutList.clear();
        label.setText(text);
        set=false;
    }

    public void addBill(Bill b){
        billItems.addAll(b.getItemList());
        b.getItemList().addListener((ListChangeListener.Change<? extends BillItem> c) -> {
            while (c.next()) {
                if (c.wasAdded()) {
                    billItems.addAll(c.getAddedSubList());
                } else if (c.wasRemoved()) {//might need tweaking
                    for (BillItem bI : c.getRemoved()) {
                        Iterator<BillItem> iter=billItems.iterator();
                        while (iter.hasNext()){
                            if (iter.next()==bI){
                                iter.remove();
                            }
                        }
                    }
                }
            }
        });
    }

    public void addBillItem(BillItem bI){
        billItems.add(bI);
    }

    public void addLocker(Locker l){
        if(! checkOutList.contains(l)) {
            checkOutList.add(l);
            lockerItems.addAll(l.getItemList());
            l.getItemList().addListener((ListChangeListener.Change<? extends Locker.LockerItem> c) -> {
                while (c.next()) {
                    if (c.wasAdded()) {
                        lockerItems.addAll(c.getAddedSubList());
                    } else if (c.wasRemoved()) {//might need tweaking
                        for (Locker.LockerItem lI : c.getRemoved()) {
                            Iterator<Locker.LockerItem> iter=lockerItems.iterator();
                            while (iter.hasNext()){
                                if (iter.next().getB()==lI.getB()){
                                    iter.remove();
                                }
                            }
                        }
                    }
                }
            });
        }
    }

    public ObservableList<Locker> getCheckOutList(){
        return checkOutList;
    }

    public TableView getTableView(){
        return tableView;
    }

    public ObservableList<Locker.LockerItem> getLockerItems(){
        return lockerItems;
    }
}
