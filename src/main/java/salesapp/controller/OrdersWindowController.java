package salesapp.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import salesapp.domain.Order;

public class OrdersWindowController extends MainWindowController {

    public TableView<Order> ordersTableView;
    private final ObservableList<Order> orders = FXCollections.observableArrayList();
    public Button finishBtn;
    public Button cancelBtn;

    public void initialize() {
        initOrdersTableView();
        finishBtn.setOnAction(e -> markOrderAsFinished());
        cancelBtn.setOnAction(e -> cancelOrder());
        orders.setAll(srv.getAllOrdersForUser(currentUser));
    }

    private void markOrderAsFinished() {
        Order selectedOrder = ordersTableView.getSelectionModel().getSelectedItem();
        if (selectedOrder == null) return;
        Order finishedOrder = srv.finishOrder(selectedOrder);
        int index = orders.indexOf(selectedOrder);
        orders.set(index, finishedOrder);
    }

    private void cancelOrder() {
        Order selectedOrder = ordersTableView.getSelectionModel().getSelectedItem();
        if (selectedOrder == null) return;
        srv.cancelOrder(selectedOrder);
        orders.remove(selectedOrder);
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

        ordersTableView.getColumns().add(0, clientName);
        ordersTableView.getColumns().add(1, status);
        ordersTableView.getColumns().add(2, productNum);
        ordersTableView.setItems(orders);
    }
}
