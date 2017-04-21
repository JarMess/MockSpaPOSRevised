package mockSpaPOS;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import mockSpaPOS.model.*;
import mockSpaPOS.view.*;
import mockSpaPOS.view.popup.EnterLockerPopUp;
import mockSpaPOS.view.popup.LockerPopUp;
import mockSpaPOS.view.popup.NotYetPopUp;

import java.util.concurrent.locks.Lock;

public class SpaPOSController extends GridPane{
    @FXML private MenuGridPane menuGridPane;
    @FXML private StackPane mainStackPane;
    @FXML private LockerGridPane lockerGridPane;
    @FXML private CheckOutPane checkOutPane;
    private Spa mainModel;
    private FXMLLoader fxmlLoader;
    private StackPane depStackPane;
    private Bill previewBill;
    private LockerPopUp lockerPopUp;
    private Stage lockerPopUpStage;
    private Scene lockerPopUpScene;
    private EnterLockerPopUp enterLockerPopUp;
    private Stage enterLockerPopUpStage;
    private Scene enterLockerPopUPScene;
    private Customer currentCustomer;
    private Customer previewCustomer;
    private Stage notYetStage;
    private Scene notYetScene;
    private NotYetPopUp notYetPopUp;
    private ReceptionPane receptionPane;

    public SpaPOSController() throws Exception{
        initializeValues();
        initializeLockerButtons();
        initializeMenuButtons();
        initializeCheckOutPane();
        initializePreviewBillPane();
        clearCheckOut();
    }

    private void initializeValues()throws Exception{
        fxmlLoader=new FXMLLoader(getClass().getResource("view/spaPOSFXML.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        fxmlLoader.load();
        mainModel=new Spa();
        previewBill=new Bill();
        depStackPane=checkOutPane.getdepStackPane();
        lockerPopUp=new LockerPopUp();
        lockerPopUpStage =new Stage();
        lockerPopUpScene=new Scene(lockerPopUp, 600,800 );
        lockerPopUpStage.setScene(lockerPopUpScene);
        enterLockerPopUp=new EnterLockerPopUp();
        enterLockerPopUPScene=new Scene(enterLockerPopUp, 350,200);
        enterLockerPopUpStage=new Stage();
        enterLockerPopUpStage.setScene(enterLockerPopUPScene);
        notYetPopUp= new NotYetPopUp();
        notYetScene= new Scene(notYetPopUp, 300,200);
        notYetStage= new Stage();
        notYetStage.setScene(notYetScene);
    }

    private void initializeLockerButtons(){
        initLockerPageButtons();
        bindLockerStatusButtons(0);
    }

    private void initializeMenuButtons(){
        ((Button)menuGridPane.getChildren().get(0)).setOnAction(e->{
            for(Node pane: mainStackPane.getChildren()){
                pane.setVisible(false);
            }
            mainStackPane.getChildren().get(1).setVisible(true);

            checkOutPane.showDep(0);
            setItemGPaneVisible(0,0);
            clearCheckOut();
        });

        ((Button)menuGridPane.getChildren().get(4)).setOnAction(e->{
            for(Node pane: mainStackPane.getChildren()){
                pane.setVisible(false);
            }
            mainStackPane.getChildren().get(0).setVisible(true);
            clearCheckOut();
        });

        //set up other menu buttons
        ((Button)menuGridPane.getChildren().get(1)).setOnAction(e->{
            notYetStage.show();
        });
        ((Button)menuGridPane.getChildren().get(2)).setOnAction(e->{
            notYetStage.show();
        });
        ((Button)menuGridPane.getChildren().get(3)).setOnAction(e->{
            notYetStage.show();
        });

    }

    private void initializeCheckOutPane(){
        initChooseDep();
        initDepartmentStackPane();
        initCheckOutButtons();
    }

    private void initCheckOutButtons(){
        checkOutPane.getAddItemsButton().setOnAction(e->{
            if (currentCustomer==null)
                enterLockerPopUpStage.show();
            else {//current customer is set
                currentCustomer.getBill().addBill(previewBill);
                if (previewCustomer!=null) { //preview customer is only set in reception pane
                    //transfer bill from preview customer to current customer
                    previewBill.removeAll();
                }
                else{
                    previewBill=new Bill();
                    checkOutPane.getPreviewBillPane().setBill(previewBill);
                }
            }

        });
        checkOutPane.getSetLockerButton().setOnAction(e -> {
            enterLockerPopUpStage.show();
        });
        checkOutPane.getCheckOutButton();

        enterLockerPopUp.getEnterButton().setOnAction(e->{
            int index=0;
            try{
                index=Integer.parseInt(enterLockerPopUp.textFieldProperty().get())-1;
                if (index>=0 && index<=1999){
                    if (mainModel.getLocker(index).isOccupied()){
                        if (!(checkOutPane.getdepStackPane().getChildren().get(mainModel.getDepartmentList().size())
                                .isVisible())) { // if reception is not visible
                            setCurrentCustomer(mainModel.getLocker(index).getCustomer());
                            checkOutPane.getCurrentBillPane().setCustomer(currentCustomer);
                            checkOutPane.getCurrentBillPane().setLabel(mainModel.getLocker(index).getLockerNumberString());
                            enterLockerPopUpStage.close();
                        }
                        else{ //reception is visible
                            enterLockerPopUp.textFieldProperty().set("");
                            checkOutPane.showDep(mainModel.getDepartmentList().size());
                            //checkOutPane.setCurrentBillPane(mainModel.getLocker(index));
                            receptionPane.addLocker(index);
                        }
                    }
                    else{
                        enterLockerPopUp.textFieldProperty().set("");
                        checkOutPane.showDep(mainModel.getDepartmentList().size());
                        //checkOutPane.setCurrentBillPane(mainModel.getLocker(index));
                        receptionPane.addLocker(index);
                    }
//                    enterLockerPopUpStage.close();
                }
            } catch (NumberFormatException err) {
                enterLockerPopUp.textFieldProperty().set("");
                //System.out.println(err);
            }
        });
    }

    public void setCurrentCustomer(Customer customer){
        currentCustomer=customer;
    }

    private void initializePreviewBillPane(){
        checkOutPane.getPreviewBillPane().setBill(previewBill);
    }

    private void initLockerPageButtons(){
        for(int i=0;i<20;i++){
            int index=i;
            ((Button)lockerGridPane.getLockerPageButtons().get(i)).setOnAction(e->{
                bindLockerStatusButtons(index);
            });
        }
    }

    private void bindLockerStatusButtons(int page){

        for (int i=0;i<100;i++){
            int index=i;
            ((Button)lockerGridPane.getLockerStatusButtons().get(i)).textProperty()
                    .bind(mainModel.getLockerStatusPropertyAt(page*100+i));
            ((Button)lockerGridPane.getLockerStatusButtons().get(i)).setOnAction(e->{
                lockerPopUpStage.close();
                lockerStatusButtonAction(page,index);
            });
        }
    }

    private void lockerStatusButtonAction (int page, int index){
        lockerPopUpStage.show();
        lockerPopUp.setLocker(mainModel.getLocker(100*page + index));
        initLockerStatusPopUp(100*page + index);
    }

    private void initLockerStatusPopUp(int lockerNum){
        Locker locker=mainModel.getLocker(lockerNum);
        lockerPopUp.getCheckInButton().setOnAction(event -> {
//            if (!locker.setCustomer()){
//                //add code to transfer
//            }
            //if not checked in:
            for(Node pane: mainStackPane.getChildren()){
                pane.setVisible(false);
            }
            mainStackPane.getChildren().get(1).setVisible(true);
            checkOutPane.showDep(mainModel.getDepartmentList().size());
            receptionPane.addLocker(lockerNum);
            lockerPopUpStage.close();
            //else transfer:
        });
        lockerPopUp.getCheckOutButton().setOnAction(e->{
            //setLocker
            //setCheckoutpane visible to check out
        });
        lockerPopUp.getLostWatchButton().setOnAction(e->{
            locker.setLost(!locker.isLost());
        });
    }

    private void initChooseDep(){
        int count=0;
        for(Department d: mainModel.getDepartmentList()){
            int index=count++;
            Button button= new Button(d.getName());
            button.setPrefSize(100,80);
            button.setMinHeight(80);

            checkOutPane.getDepVBOX().getChildren().add(button);
            button.setOnAction(e-> { //Department buttons
                if (checkOutPane.getdepStackPane().getChildren().get(mainModel.getDepartmentList().size()).isVisible()){
                    //if clicked on dep buttons from reception
                    previewBill=new Bill();
                    previewCustomer=null;

                    checkOutPane.getPreviewBillPane().setBill(previewBill);
                    checkOutPane.getPreviewBillPane().setLabel("Preview Bill");
                }
                checkOutPane.showDep(index);
                setItemGPaneVisible(index,0);

            });
        }
        int index=count++;
        Button button= new Button("Reception");
        button.setPrefSize(100,80);
        button.setMinHeight(80);
        checkOutPane.getDepVBOX().getChildren().add(button);
        // for reception department
        button.setOnAction(e->{
            checkOutPane.showDep(index);
            if (currentCustomer!=null){
                receptionPane.addInUse(currentCustomer.getLockerNumber()-1);
            }
            //set only the first cat of items visible;
        });
    }

    private void initDepartmentStackPane() {
        int dCount=0;

        for(Department d:mainModel.getDepartmentList()){
            try{
                int i=dCount++;
                int cCount=0;
                DepartmentPane dpane= new DepartmentPane();
                dpane.setVisible(false);
                dpane.getCatVBox().setLabel(d.getName());
                //dpane.getCatVBox().getCatButtonBox();
                depStackPane.getChildren().add(dpane);

                for (Category c: d.getCategoryList()){
                    int j=cCount++;
                    int iCount=0;
                    Button button=new Button(c.getName());
                    button.setPrefSize(100,75);
                    button.setOnAction(e->{
                        setItemGPaneVisible(i,j);
                    });
                    dpane.getCatVBox().getCatButtonBox().getChildren().add(button);
                    dpane.getItemVBox().labelProperty().set(c.getName());
                    GridPane gPane=new GridPane();
                    gPane.setHgap(5);
                    gPane.setVgap(5);
                    gPane.setVisible(false);
                    dpane.getItemVBox().getItemStackPane().getChildren().add(gPane);

                    for (DepartmentItem item: c.getItemList()){
                        int k=iCount++;
                        Button iButton = new Button(item.getName() +"\n$"+String.format("%.2f",item.getPrice()));
                        iButton.setPrefSize(90,90);
                        gPane.add(iButton, k%5,k/5);
                        iButton.setOnAction(e->{
                            addItemToBill(i,j,k);
                        });
                    }
                }
            }catch (Exception e){
                System.err.println(e);
            }

        }

        //add Reception to dep stack pane
        receptionPane=new ReceptionPane(this);
        receptionPane.setVisible(false);
        depStackPane.getChildren().add(receptionPane);
    }

    private void setItemGPaneVisible(int dIndex, int cIndex){
//        for(Node dPane: depStackPane.getChildren()) {
//            dPane.setVisible(false);
//        }
//        depStackPane.getChildren().get(dIndex).setVisible(true);

        ObservableList<Node> itemStackPaneChildren=((DepartmentPane)depStackPane.getChildren().get(dIndex)).getItemVBox().getItemStackPane().getChildren();

        for(Node iGPane: itemStackPaneChildren){
            iGPane.setVisible(false);
        }
        itemStackPaneChildren.get(cIndex).setVisible(true);

        ((DepartmentPane)depStackPane.getChildren().get(dIndex)).getItemVBox().labelProperty()
                .set(mainModel.getDepartmentList().get(dIndex).getCategoryAt(cIndex).getName());

    }

    private void addItemToBill(int dIndex, int cIndex, int iIndex){
        previewBill.addItem(mainModel.getDepartmentItem(dIndex,cIndex,iIndex));
    }



    public Spa getMainModel(){
        return mainModel;
    }

    public CheckOutPane getCheckOutPane() {
        return checkOutPane;
    }

    public Bill getPreviewBill() {
        return previewBill;
    }

    public void clearCheckOut(){
        checkOutPane.getCurrentBillPane().clear("Current Bill");
        checkOutPane.getPreviewBillPane().clear("Preview Bill");
        receptionPane.clearTables();
        currentCustomer=null;

        previewCustomer=null;
        previewBill= new Bill();
        checkOutPane.getPreviewBillPane().setBill(previewBill);
    }

    public void setPreviewBill(Customer c){
        previewCustomer=c;
        if ( c!=null){
            previewBill=c.getBill();
        }
        else {
            previewBill=new Bill();
        }
    }

}
