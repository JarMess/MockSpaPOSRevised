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
import mockSpaPOS.model.Customer;

public class EditBillPane extends GridPane {
    @FXML private Label label;
    @FXML private TableView<BillItem> tableView;
    @FXML private TableColumn lockerCol;
    @FXML private TableColumn itemCol;
    @FXML private TableColumn priceCol;
    @FXML private TableColumn quantCol;
    @FXML private TableColumn timeCol;
    private ObservableList<Customer> checkOutList;
    private ObservableList<BillItem> billItems;

    public EditBillPane() throws Exception{
        FXMLLoader fxmlLoader=new FXMLLoader(getClass().getResource("editBillPaneFXML.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        fxmlLoader.load();

        billItems=FXCollections.observableArrayList();
        checkOutList =FXCollections.observableArrayList();
        lockerCol.setCellValueFactory(new PropertyValueFactory<BillItem,String>("custLockerNumber"));
        itemCol.setCellValueFactory( new PropertyValueFactory<BillItem,String>("fullHier"));
        priceCol.setCellValueFactory( new PropertyValueFactory<BillItem,String>("pricep"));
        quantCol.setCellValueFactory( new PropertyValueFactory<BillItem,String>("quantity"));
        timeCol.setCellValueFactory( new PropertyValueFactory<BillItem,String>("timep"));
        tableView.setItems(billItems);
    }

    public void setBill(Bill bill){
        clear(label.getText());
        lockerCol.setVisible(false);
        addBill(bill);
    }

    public void addBill(Bill bill){// add bill to table
        billItems.addAll(bill.getItemList());
        bill.removeListener();
        bill.addListener((ListChangeListener.Change<? extends BillItem> c)->{
            while (c.next()){
                if (c.wasAdded()){
                    billItems.addAll(c.getAddedSubList());
                }else if (c.wasRemoved()){
                    billItems.removeAll(c.getRemoved());
                }
            }
        });
    }

    public void setCustomer(Customer c){
        clear(label.getText());
        lockerCol.setVisible(true);
        checkOutList.add(c);
        addBill(c.getBill());
    }

    public void addCustomer(Customer c){
        checkOutList.add(c);
        addBill(c.getBill());
    }

    public void addBillItem(BillItem bI){
        billItems.add(bI);
    }

    public void clear(String text){
        checkOutList.clear();
        billItems.clear();
        label.setText(text);
    }

    public StringProperty labelProperty(){
        return label.textProperty();
    }

    public void setLabel(String text){
        label.setText(text);
    }

    public ObservableList<Customer> getCheckOutList(){
        return checkOutList;
    }

    public TableView getTableView(){
        return tableView;
    }

    public ObservableList<BillItem> getBillItems() {
        return billItems;
    }
}
