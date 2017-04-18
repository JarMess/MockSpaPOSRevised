package mockSpaPOS.view;

import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

public class MenuGridPane extends GridPane {
    private Button totalSales;
    private Button viewCustomers;
    private Button checkOut;
    private Button departmentScheduler;
    private Button lockerStatusViewer;

    public MenuGridPane() {
        super();
        totalSales = new Button("Total Sales");
        totalSales.setPrefSize(160, 50);
        viewCustomers = new Button("View Customers");
        viewCustomers.setPrefSize(160, 50);
        checkOut= new Button("Check Out / Edit Sales");
        checkOut.setPrefSize(160, 50);
        departmentScheduler= new Button("Department scheduler");
        departmentScheduler.setPrefSize(160,50);
        lockerStatusViewer= new Button("Locker Statuses");
        lockerStatusViewer.setPrefSize(160,50);

        super.add(checkOut, 0, 0);
        super.add(totalSales,1,0);
        super.add(viewCustomers, 2,0);
        super.add(departmentScheduler, 3,0);
        super.add(lockerStatusViewer, 4, 0);
    }
}
