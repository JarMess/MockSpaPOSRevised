package mockSpaPOS.view.popup;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import mockSpaPOS.model.Bill;
import mockSpaPOS.model.BillItem;
import mockSpaPOS.model.Locker;
import mockSpaPOS.model.Spa;
import mockSpaPOS.view.ReceptionPane;

public class CheckOutBillPopUp extends GridPane{
    @FXML private TextArea textArea;
    @FXML private Button payButton;
    @FXML private Button cancelButton;
    private String header;
    private Spa mainSpa;
    private String lockerString;
    private String Lockers;
    private Bill bill;
    private ReceptionPane rPane;
    private String guests;

    public CheckOutBillPopUp(Spa mainSpa, ReceptionPane rPane){
        FXMLLoader fxmlLoader= new FXMLLoader(getClass().getResource("checkOutBillPopUpFXML.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (Exception e){
            System.out.println(e);
        }

        this.mainSpa=mainSpa;
        this.rPane=rPane;

        setHeader();
        setBillNLockers(rPane.getCheckingOut());
        setTextArea();
        setButtons();
    }

    private void setHeader(){
        header= mainSpa.getBusinessInfo().getCompany()+"\n"+
                mainSpa.getBusinessInfo().getAddress() +"\n" +
                mainSpa.getBusinessInfo().getCitystatezip() +"\n"+
                "-----------------------------------------------\n";
    }

    private void setBillNLockers(ObservableList<Locker> checkingOut){
        bill= new Bill();
        int totalGuests=0;
        StringBuffer sb= new StringBuffer("Lockers: ");
        for (Locker l : checkingOut) {
            sb.append(l.getLockerNumber());
            sb.append("  ");
            bill.addBill(l.getCustomer().getBill());
            totalGuests++;
        }

        sb.append("\nTotal Guests: ");
        sb.append(totalGuests);
        sb.append("\n-----------------------------------------------\n");
        guests=sb.toString();

    }

    private void setTextArea(){
        StringBuffer sb= new StringBuffer(header+guests);
        ObservableList<BillItem> billItems= bill.getItemList();
        for (BillItem b: billItems){
            sb.append(b.getQuantity());
            sb.append("x  ");
            sb.append(b.getFullHier());
            sb.append("\n\t@ $");
            int count= 27;
            String s=String.format("%.2f",b.getPrice());
            sb.append(s);
            sb.append(" each");
            count -= s.length();

            s= String.format("%.2f", b.getPrice()*b.getQuantity());
            for(int i=count; i> s.length();i-- )
                sb.append(" ");
            sb.append(s);
            sb.append("\n\n");

        }
        sb.append("\nSubtotal:  \t");
        String s= String.format("%.2f",bill.getBillTotal());
        for (int i =27; i>s.length();i--)
            sb.append(" ");
        sb.append(s);

        sb.append("\nTax (%");
        sb.append(String.format("%.3f", (bill.getTaxPercent() *100)));
        sb.append("):\t");
        s=String.format("%.2f", bill.getTaxPercent()* bill.getBillTotal());
        for (int i =27; i>s.length();i--)
            sb.append(" ");
        sb.append(s);

        sb.append("\nTotal:     \t");
        s=String.format("%.2f",(1+bill.getTaxPercent())*bill.getBillTotal());
        for (int i =27; i>s.length();i--)
            sb.append(" ");
        sb.append(s);

        textArea.setFont(Font.font("Monospaced", 12));

        textArea.setText(sb.toString());

    }

    private void setButtons(){
        cancelButton.setOnAction(e->{
            ((Stage)getScene().getWindow()).close();
        });

        payButton.setOnAction(e->{
            rPane.checkOut();
            ((Stage)getScene().getWindow()).close();
        });
    }
}
