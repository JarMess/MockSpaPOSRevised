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
    @FXML private TableView<Locker> inUseTable;
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
    private ObservableList<Locker> inUseData;
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
        timeCol.setCellValueFactory( new PropertyValueFactory<Locker, String>("checkInTime"));
        totalCol.setCellValueFactory( new PropertyValueFactory<Locker, String>("billTotal"));
        inUseSelection=inUseTable.getSelectionModel().selectedIndexProperty();
        inUseSelection.addListener(o->{
            updatePreviewBill();
        });

    }

    private void initCheckOutButtons(){
        removeLockerButton.setOnAction(e->{
            try {
                EditBillPane currentBP= mainCont.getCheckOutPane().getCurrentBillPane();
                TableView<Locker.LockerItem> tV=currentBP.getTableView();
                Locker.LockerItem lI = tV.getSelectionModel().getSelectedItem();
                if (lI==null){ //nothing is selected select the one currently checking out
                    if (!tV.getItems().isEmpty()) {
                        lI = tV.getItems().get(0);
                    }
                }

                //remove all lockerItems with highlighted lockernumber
                Iterator<Locker.LockerItem> iter= currentBP.getLockerItems().iterator();
                while(iter.hasNext()){

                    if(iter.next().equals(lI))
                        iter.remove();
                }
                int lockerNum=lI.getLockerNum();
                //remove from arrayList currently selected locker
                ObservableList<Locker> lockerAL=currentBP.getCheckOutList();
                for (int i=0;i<lockerAL.size();i++){
                    if (lockerNum == lockerAL.get(i).getLockerNumber())
                        lockerAL.remove(i);
                }

                if(lockerAL.isEmpty()){
                    currentBP.setLabel("Current Bill");
                    mainCont.setCurrentCustomer(null);

                }else{
                    currentBP.setLabel("Currently Checking Out: "+lockerAL.get(0).getLockerNumberString());
                    mainCont.setCurrentCustomer(mainSpa.getLocker(tV.getItems().get(0).getLockerNum()-1));
                }
            } catch (Exception err){
//                System.out.println(err);
            }
        });

        removeGroupButton.setOnAction(e->{
            int inUseIndex=inUseTable.getSelectionModel().getFocusedIndex();
            Locker selected=inUseData.get(inUseIndex);
            ArrayList<Locker> group=selected.getCustomer().getLockerGroup().getLockerGroup();

            EditBillPane currentBP= mainCont.getCheckOutPane().getCurrentBillPane();
            TableView<Locker.LockerItem> tV=currentBP.getTableView();
            ObservableList<Locker> lockerAL= currentBP.getCheckOutList();
            for(Locker l: group){
                inUseData.remove(l);

                if (!lockerAL.isEmpty()){ //look through currentBill list to see if locker is there and remove
                    Iterator<Locker> iter= lockerAL.iterator();

                    while (iter.hasNext()){
                        if (iter.next().equals(l)){
                            iter.remove();
                        }
                    }
                }
            }

            EditBillPane previewBP=mainCont.getCheckOutPane().getPreviewBillPane();

            if(lockerAL.isEmpty()){
                currentBP.setLabel("Current Bill");
                try{
                    mainCont.setCurrentCustomer(null);
                }catch(Exception err){
//                    System.out.println(err);
                }
                previewBP.clear("Preview Bill");
                mainCont.setPreviewCustomer(null);
            }
            else {
                currentBP.setLabel("Currently Checking Out: "+lockerAL.get(0).getLockerNumberString());
                try{
                    mainCont.setCurrentCustomer(mainSpa.getLocker(tV.getItems().get(0).getLockerNum()-1));
                } catch (Exception err) {
//                    System.out.println(err);
                }

                inUseTable.getSelectionModel().focus(0);
                updatePreviewBill();
            }




        });

        addBillButton.setOnAction(e->{
            Locker selected= inUseTable.getSelectionModel().getSelectedItem();
            if(!isBeingCheckedOut(selected)){
                //add locker to currentBP
            }
        });

        checkOutButton.setOnAction(e->{
            try{
                if (!getCheckingOut().isEmpty()) {
                    Scene s = new Scene(new CheckOutBillPopUp(mainSpa, this), 500, 670);
                    Stage st = new Stage();
                    st.setScene(s);
                    st.show();
                }
                else throw new Exception("no lockers checking out in list");
            } catch( Exception err){
                //System.out.println("no lockers currently checking in or items have not been set" );
            }

        });
    }

    private void updatePreviewBill(){
        Locker l = inUseTable.getSelectionModel().getSelectedItem();
        if (!isBeingCheckedOut(l)){
            mainCont.setPreviewCustomer(l);
            mainCont.getCheckOutPane().setPreviewBillPane(l.getCustomer().getBill());
            mainCont.getCheckOutPane().getPreviewBillPane().setLabel(l.getLockerNumberString());

        }
        else{
            mainCont.getCheckOutPane().getPreviewBillPane()
                    .clear(l.getLockerNumberString()+ " is being checked out");
        }
    }

    private boolean isBeingCheckedOut(Locker l){
        return mainCont.getCheckOutPane().getCurrentBillPane().getCheckOutList().contains(l);
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
        if(!(inUseData.contains(mainSpa.getLocker(index))))
            inUseData.addAll(mainSpa.getLocker(index).getCustomer().getLockerGroup().getLockerGroup());

        if ( mainCont.getCheckOutPane().getCurrentBillPane().getCheckOutList().isEmpty()) {
            try {
                mainCont.setCurrentCustomer(mainSpa.getLocker(index));

            }catch(Exception err){
                System.out.println(err);
            }
            mainCont.getCheckOutPane().setCurrentBillPane(mainSpa.getLocker(index));
            mainCont.getCheckOutPane().getCurrentBillPane().setLabel("Currently Checking Out: "
                    + mainSpa.getLocker(index).getLockerNumberString());
        } else {
            mainCont.getCheckOutPane().getCurrentBillPane().addLocker(mainSpa.getLocker(index));
        }
        return true;
    }

    private boolean allCheckedIn(){
        for (AvailableLocker a:availableData){
            if(a.getCheckInItem()==null) return false;
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
        ObservableList<Locker> checkOut=currentBP.getCheckOutList();

        for (Locker l: checkOut){
            mainSpa.checkOutLocker(l.getLockerNumber()-1);
        }
    }

    public ObservableList<Locker> getCheckingOut(){
        return mainCont.getCheckOutPane().getCurrentBillPane().getCheckOutList();
    }

    public void checkIn(){
        ArrayList<Locker> alist=new ArrayList<>();
        for (AvailableLocker al: availableData)
            alist.add(al.getLocker());

        AvailableLocker aL= availableData.get(0);
        Customer c= new Customer(aL.getCheckInItem(),alist);

        aL.getLocker().setCustomer(c);
        for (int i=1;i<availableData.size(); i++){
            aL=availableData.get(i);
            aL.getLocker().setCustomer(new Customer(aL.getCheckInItem(),alist,c.getLockerGroup()));
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
