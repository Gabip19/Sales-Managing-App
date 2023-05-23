package salesapp.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import salesapp.domain.Order;

public class AdminMainController extends MainWindowController {
    public TableView<Order> ordersTableView;
    private final ObservableList<Order> orders = FXCollections.observableArrayList();

    public void initialize() {
        initOrdersTableView();
        orders.setAll(srv.getAllOrders());
    }

    private void initOrdersTableView() {
        TableColumn<Order, String> clientName = new TableColumn<>("Client");
        clientName.setCellValueFactory(new PropertyValueFactory<>("clientName"));
        TableColumn<Order, String> status = new TableColumn<>("Status");
        status.setCellValueFactory(new PropertyValueFactory<>("status"));
        TableColumn<Order, String> productNum = new TableColumn<>("No. Products");
        productNum.setCellValueFactory(
                cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getOrderItems().size()))
        );
        TableColumn<Order, String> agent = new TableColumn<>("Agent");
        agent.setCellValueFactory(
                cellData -> new SimpleStringProperty(
                    String.valueOf(cellData.getValue().getAgent().getUsername())
                )
        );

        ordersTableView.getColumns().add(0, clientName);
        ordersTableView.getColumns().add(1, status);
        ordersTableView.getColumns().add(2, productNum);
        ordersTableView.getColumns().add(3, agent);
        ordersTableView.setItems(orders);
    }
}
