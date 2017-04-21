package mockSpaPOS.view;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import mockSpaPOS.SpaPOSController;
import mockSpaPOS.model.*;
import mockSpaPOS.view.popup.CheckInBillPopUp;
import mockSpaPOS.view.popup.CheckOutBillPopUp;

import java.util.ArrayList;
import java.util.Iterator;

public class ReceptionPane extends GridPane{
    @FXML private TableView<AvailableLocker> availableTable;
    @FXML private TableColumn lockerNumCol;
    @FXML private TableColumn typeCol;
    @FXML private TableColumn priceCol;
    @FXML private HBox checkInTypeHBox;
    @FXML private StackPane checkInItemButtonStack;
    @FXML private Button removeButton;
    @FXML private Button checkInButton;
    @FXML private Button addCheckInButton;
    @FXML private TableView<Customer> inUseTable;
    @FXML private TableColumn lockNumCol;
    @FXML private TableColumn timeCol;
    @FXML private TableColumn totalCol;
    @FXML private Button removeLockerButton;
    @FXML private Button removeGroupButton;
    @FXML private Button addBillButton;
    @FXML private Button checkOutButton;
    private Spa mainSpa;
    private ObservableList<AvailableLocker> availableData;
    private AvailableLocker availableLocker;
    private ObservableList<Customer> inUseData;
    private SpaPOSController mainCont;
    private ReadOnlyIntegerProperty inUseSelection;

    public ReceptionPane(SpaPOSController mainCont){
        FXMLLoader fxmlLoader=new FXMLLoader(getClass().getResource("receptionPaneFXML.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        }catch (Exception err){
            System.out.println(err);
        }

        this.mainSpa=mainCont.getMainModel();
        this.mainCont=mainCont;

        initCheckInItems();
        initAvailableTable();
        initFinalCheckInButtons();
        initInUseTable();
        initCheckOutButtons();
    }

    private void initCheckInItems(){
        int index=0;
        for (Category c:mainSpa.getReception().getCategoryList()){
            int index1=index;
            Button b= new Button(c.getName());
            b.setPrefSize(90,65);
            b.setOnAction(e->{
                showOnlyCat(index1);
            });
            checkInTypeHBox.getChildren().add(b);
            GridPane g= new GridPane();
            g.setVgap(5);
            g.setHgap(5);
            int count=0;
            for (DepartmentItem i: c.getItemList()){
                Button itemButton=new Button(i.getName()+"\n$"+String.format("%.2f",i.getPrice()));
                itemButton.setPrefSize(75,60);
                itemButton.setOnAction(e->{
                    try{
                        AvailableLocker locker= availableTable.getSelectionModel().getSelectedItem();
                        locker.setCheckInItem(i);
                        setRowToFirstAvailable();

                    }catch (Exception err){
                        //System.out.println("must input locker first");
                    }
                });
                g.add(itemButton,count%5,count/5);
                count++;
            }
            checkInItemButtonStack.getChildren().add(g);
            index++;
        }

        showOnlyCat(0);
    }

    private void showOnlyCat(int index){
        for (Node g: checkInItemButtonStack.getChildren()){
            g.setVisible(false);
        }
        checkInItemButtonStack.getChildren().get(index).setVisible(true);
    }

    private void initAvailableTable(){
        availableData= FXCollections.observableArrayList();
        availableTable.setItems(availableData);
        lockerNumCol.setCellValueFactory( new PropertyValueFactory<AvailableLocker, String>("lockerNumber"));
        typeCol.setCellValueFactory( new PropertyValueFactory<AvailableLocker, String>("checkInType"));
        priceCol.setCellValueFactory( new PropertyValueFactory<AvailableLocker, String>("checkInPrice"));

    }

    private void initInUseTable(){
        inUseData= FXCollections.observableArrayList();
        inUseTable.setItems(inUseData);
        lockNumCol.setCellValueFactory( new PropertyValueFactory<Locker, String>("lockerNumber"));
        timeCol.setCellValueFactory( new PropertyValueFactory<Locker, String>("status"));
        totalCol.setCellValueFactory( new PropertyValueFactory<Locker, String>("billTotal"));
        inUseSelection=inUseTable.getSelectionModel().selectedIndexProperty();
        inUseSelection.addListener(o->{
            updatePreviewBill();
        });

    }

    private void initCheckOutButtons(){
        removeLockerButton.setOnAction(e->{
            EditBillPane currentBP= mainCont.getCheckOutPane().getCurrentBillPane();
            TableView<BillItem> currentTV=currentBP.getTableView();
            BillItem bI = currentTV.getSelectionModel().getSelectedItem();
            Customer selected=null;
            ObservableList<Customer> checkOutList=currentBP.getCheckOutList();
            if (!checkOutList.isEmpty()) {
                if (bI==null){ //nothing is selected select the one currently checking out
                    selected=checkOutList.get(0);
                }
                else{
                    selected = mainSpa.getLocker(bI.getCustLockerNumber()-1).getCustomer();
                }

                //remove all customers from list with highlighted lockernumber
                Iterator<Customer> iter= checkOutList.iterator();
                ObservableList<BillItem> billItems= currentBP.getBillItems();
                while(iter.hasNext()){
                    Customer next=iter.next();
                    if(next.equals(selected)) {
                        for(BillItem billFor: next.getBill().getItemList()){
                            billItems.remove(billFor);
                        }
                        iter.remove();
                    }

                }

                if(checkOutList.isEmpty()){
                    currentBP.setLabel("Current Bill");
                    mainCont.setCurrentCustomer(null);

                }else{
                    currentBP.setLabel("Currently Checking Out: "+checkOutList.get(0).getLockerNumberString());
                    mainCont.setCurrentCustomer(checkOutList.get(0));//next customer in line next to check out
                }
            }// if checkout list is empty cant remove customer from it

        });

        removeGroupButton.setOnAction(e->{
            int inUseIndex=inUseTable.getSelectionModel().getFocusedIndex();
            Customer selected=inUseData.get(inUseIndex);
            ArrayList<Customer> custGroup=selected.getCheckInGroup().getCustomerGroup();

            EditBillPane currentBP= mainCont.getCheckOutPane().getCurrentBillPane();
            TableView<BillItem> currentTV=currentBP.getTableView();
            ObservableList<Customer> checkOutList= currentBP.getCheckOutList();
            for(Customer c: custGroup){
                inUseData.remove(c); //remove all customers in custGroup

                if (!checkOutList.isEmpty()){ //look through checkout list to see if customer is there and remove
                    Iterator<Customer> iter= checkOutList.iterator();

                    while (iter.hasNext()){
                        if (iter.next().equals(c)){
                            iter.remove();
                        }
                    }
                }
            }

            //update preview and current
            EditBillPane previewBP=mainCont.getCheckOutPane().getPreviewBillPane();
            if(checkOutList.isEmpty()){
                currentBP.setLabel("Current Bill");
                mainCont.setCurrentCustomer(null);
                if (inUseData.isEmpty()){
                    previewBP.clear("Preview Bill");
                    mainCont.setPreviewBill(null);
                }
                else {
                    inUseTable.getSelectionModel().focus(0);
                    updatePreviewBill();
                }
            }
            else {
                currentBP.setLabel("Currently Checking Out: "+checkOutList.get(0).getLockerNumberString());
                mainCont.setCurrentCustomer(checkOutList.get(0));

                inUseTable.getSelectionModel().focus(0);
                updatePreviewBill();
            }
        });

        addBillButton.setOnAction(e->{
            Customer selected= mainSpa.getLocker(
                    inUseTable.getSelectionModel().getSelectedItem().getLockerNumber()-1)
                    .getCustomer();
            if(!isBeingCheckedOut(selected)){
                mainCont.getCheckOutPane().getCurrentBillPane()
                        .addCustomer(selected);
            }
        });

        checkOutButton.setOnAction(e->{
            if (!getCheckingOut().isEmpty()) {
                Scene s = new Scene(new CheckOutBillPopUp(mainSpa, this), 500, 670);
                Stage st = new Stage();
                st.setScene(s);
                st.show();
            }
            else{
                //no lockers checking out
            }
        });
    }

    private void updatePreviewBill(){
        if (!inUseData.isEmpty()){
            Customer c = inUseTable.getSelectionModel().getSelectedItem();
            EditBillPane previewBP= mainCont.getCheckOutPane().getPreviewBillPane();
            if (!c.isCheckedOut()) {

                if (!isBeingCheckedOut(c)) {
                    mainCont.setPreviewBill(c);
                    previewBP.setBill(c.getBill());
                    previewBP.setLabel(c.getLockerNumberString());

                } else {
                    mainCont.setPreviewBill(null);
                    previewBP.clear(c.getLockerNumberString() + " is being checked out");
                }
            } else {
                mainCont.setPreviewBill(null);
                previewBP.clear("Customer is already checked out");
            }
        }
        else{
            //in use empty
        }
    }

    private boolean isBeingCheckedOut(Customer c){
        return mainCont.getCheckOutPane().getCurrentBillPane().getCheckOutList().contains(c);
    }

    public TableView getAvailableTable() {
        return availableTable;
    }

    public TableColumn getLockerNumCol() {
        return lockerNumCol;
    }

    public TableColumn getTypeCol() {
        return typeCol;
    }

    public TableColumn getPriceCol() {
        return priceCol;
    }

    public boolean addLocker(int index){
        if (!mainSpa.getLocker(index).isOccupied()){
            return addAvailable(index);
        } else{

            return addInUse(index);
        }

    }

    private boolean addAvailable(int index){
        AvailableLocker locker= new AvailableLocker(index);
        if (availableData.contains(locker)) return false;
        availableData.add(locker);
        setRowToFirstAvailable();
        return true;
    }

    public boolean addInUse(int index){
        Customer inputCustomer= mainSpa.getLocker(index).getCustomer();
        EditBillPane currentBP= mainCont.getCheckOutPane().getCurrentBillPane();
        if(!(inUseData.contains(inputCustomer))) //only add to current bill pane if its not already there
            inUseData.addAll(inputCustomer.getCheckInGroup().getCustomerGroup());

        if (currentBP.getCheckOutList().isEmpty()) {// current bill pane not set
            mainCont.setCurrentCustomer(inputCustomer);
            currentBP.setCustomer(inputCustomer);
            currentBP.setLabel("Currently Checking Out: "
                    + inputCustomer.getLockerNumberString());
        } else {
            currentBP.setLabel("Currently Checking Out: "
                    + inputCustomer.getLockerNumberString());
            if (!currentBP.getCheckOutList().contains(inputCustomer))
                currentBP.addCustomer(inputCustomer);
        }
        return true;
    }

    private void setRowToFirstAvailable(){
        int index=getFirstAvailable();
        availableTable.getSelectionModel().select(index);
    }
    private int getFirstAvailable(){
        int index=0;
        for (AvailableLocker a: availableData){
            if (a.getCheckInItem()==null)
                return index;
            index++;
        }
        return index;
    }

    private void initFinalCheckInButtons(){
        removeButton.setOnAction(e->{
            try {
                availableData.remove(availableTable.getSelectionModel().getFocusedIndex());
            } catch (Exception err){
                //System.out.println("no lockers currently checking in");
            }
        });

        checkInButton.setOnAction(e->{
            try{
                if (!availableData.isEmpty()) {
                    Scene s = new Scene(new CheckInBillPopUp(mainSpa, this), 500, 670);
                    Stage st = new Stage();
                    st.setScene(s);
                    st.show();
                }
                else throw new Exception("no items in list");
            } catch( Exception err){
                //System.out.println("no lockers currently checking in or items have not been set" );
            }
        });
    }

    public ObservableList<AvailableLocker> getAvailableData() {
        return availableData;
    }

    public void checkOut(){
        EditBillPane currentBP=mainCont.getCheckOutPane().getCurrentBillPane();
        ObservableList<Customer> checkOut=currentBP.getCheckOutList();

        for (Customer c: checkOut){
            mainSpa.checkOutLocker(c.getLockerNumber()-1);
        }

        currentBP.clear("Current Bill:");
        setNextAvailableCheckOut();
    }

    private void setNextAvailableCheckOut(){
        if (!inUseData.isEmpty()){

        }
    }

    public ObservableList<Customer> getCheckingOut(){
        return mainCont.getCheckOutPane().getCurrentBillPane().getCheckOutList();
    }

    public void checkIn(){
        ArrayList<Locker> alist=new ArrayList<>();
        for (AvailableLocker al: availableData)
            alist.add(al.getLocker());

        AvailableLocker aL= availableData.get(0);
        Customer c= new Customer(aL.getCheckInItem(), aL.getLocker(), alist);

        mainSpa.checkIn(aL.getLockerNumber()-1,c);
        for (int i=1;i<availableData.size(); i++){
            aL=availableData.get(i);
            mainSpa.checkIn(aL.getLockerNumber()-1,new Customer(aL.getCheckInItem(),aL.getLocker(),c.getCheckInGroup(),alist));
        }

        availableData.clear();
    }

    public void clearTables(){
        availableTable.getItems().clear();
        inUseTable.getItems().clear();
    }

    public class AvailableLocker {
        private IntegerProperty lockerNumber;
        private StringProperty checkInType;
        private StringProperty checkInPrice;
        private Locker locker;
        private DepartmentItem checkInItem;

        public AvailableLocker(int lockerNumber) {
            this.lockerNumber = mainSpa.getLocker(lockerNumber).lockerNumberProperty();
            locker = mainSpa.getLocker(lockerNumber);
            checkInType = new SimpleStringProperty();
            checkInPrice = new SimpleStringProperty();
        }

        public void setCheckInItem(DepartmentItem checkInItem) {
            this.checkInItem = checkInItem;
            setCheckInType(checkInItem.getName());
            setCheckInPrice(String.format("%.2f", checkInItem.getPrice()));

        }

        public void setCheckInType(String checkInType) {
            this.checkInType.set(checkInType);
        }

        public void setCheckInPrice(String checkInPrice) {
            this.checkInPrice.set(checkInPrice);
        }

        public int getLockerNumber() {
            return lockerNumber.get();
        }

        public IntegerProperty lockerNumberProperty() {
            return lockerNumber;
        }

        public String getCheckInType() {
            return checkInType.get();
        }

        public StringProperty checkInTypeProperty() {
            return checkInType;
        }

        public String getCheckInPrice() {
            return checkInPrice.get();
        }

        public StringProperty checkInPriceProperty() {
            return checkInPrice;
        }

        public DepartmentItem getCheckInItem() {
            return checkInItem;
        }

        public boolean equals(Object o) {
            if (!(o instanceof AvailableLocker)) return false;
            return (getLockerNumber() == ((AvailableLocker) o).getLockerNumber());
        }

        public Locker getLocker() {
            return locker;
        }
    }

}
