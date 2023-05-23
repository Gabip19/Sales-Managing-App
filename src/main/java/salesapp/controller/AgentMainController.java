package salesapp.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import salesapp.domain.*;

import java.io.IOException;

public class AgentMainController extends MainWindowController {

    public TableView<Product> productsTableView;
    private final ObservableList<Product> products = FXCollections.observableArrayList();
    public TextField nameField;
    public TextArea descriptionField;
    public TextField priceField;
    public Spinner<Integer> quantityField;
    public Button addToCartBtn;
    public TableView<OrderItem> orderItemsTableView;
    private final ObservableList<OrderItem> currentOrderItems = FXCollections.observableArrayList();
    public TextField clientNameField;
    public Button removeOrderItemBtn;
    public Button placeOrderBtn;
    public Button viewOrdersBtn;

    private Order currentOrder;

    public void initialize() {
        initProductTableView();
        initOrderItemTableView();
        products.setAll(srv.getAllProducts());
        productsTableView.getSelectionModel().selectedItemProperty().addListener((a, b, c) -> selectionChanged());
        addToCartBtn.setOnAction(e -> addProductToCart());
        removeOrderItemBtn.setOnAction(e -> removeOrderItem());
        placeOrderBtn.setOnAction(e -> placeOrder());
        viewOrdersBtn.setOnAction(e -> openOrdersWindow());
        currentOrder = new Order(OrderStatus.PENDING, currentUser);
    }

    private void addProductToCart() {
        Product selectedProduct = productsTableView.getSelectionModel().getSelectedItem();
        if (selectedProduct == null) return;
        OrderItem orderItem = new OrderItem(selectedProduct, quantityField.getValue());
        currentOrder.addOrderItem(orderItem);
        currentOrderItems.add(orderItem);
    }

    private void placeOrder() {
        if (currentOrder.getOrderItems().size() == 0) {
            return;
        }

        String clientName = clientNameField.getText();
        if (clientName.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Client name is empty.").show();
            return;
        }

        currentOrder.setClientName(clientName);
        currentOrder.setStatus(OrderStatus.PLACED);

        srv.placeOrder(currentOrder);
        currentOrder = new Order(OrderStatus.PENDING, currentUser);
        currentOrderItems.clear();
    }

    private void removeOrderItem() {
        OrderItem selectedProduct = orderItemsTableView.getSelectionModel().getSelectedItem();
        if (selectedProduct == null) return;
        currentOrderItems.remove(selectedProduct);
        currentOrder.removeOrderItem(selectedProduct);
    }

    private void selectionChanged() {
        Product selectedItem = productsTableView.getSelectionModel().getSelectedItem();
        nameField.setText(selectedItem.getName());
        descriptionField.setText(selectedItem.getDescription());
        priceField.setText(String.valueOf(selectedItem.getPrice()));
        quantityField.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, selectedItem.getStock()));
        quantityField.getValueFactory().setValue(0);
    }

    private void initProductTableView() {
        TableColumn<Product, String> name = new TableColumn<>("Name");
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<Product, Double> price = new TableColumn<>("Price");
        price.setCellValueFactory(new PropertyValueFactory<>("price"));
        TableColumn<Product, Integer> stock = new TableColumn<>("Stock");
        stock.setCellValueFactory(new PropertyValueFactory<>("stock"));

        productsTableView.getColumns().add(0, name);
        productsTableView.getColumns().add(1, price);
        productsTableView.getColumns().add(2, stock);
        productsTableView.setItems(products);
    }

    private void initOrderItemTableView() {
        TableColumn<OrderItem, String> productName = new TableColumn<>("Product");
        productName.setCellValueFactory(
            cellData -> new SimpleStringProperty(cellData.getValue().getProduct().getName())
        );
        TableColumn<OrderItem, Integer> quantity = new TableColumn<>("Price");
        quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        orderItemsTableView.getColumns().add(0, productName);
        orderItemsTableView.getColumns().add(1, quantity);
        orderItemsTableView.setItems(currentOrderItems);
    }

    private void openOrdersWindow() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/orders-window.fxml"));
        Scene scene;
        try {
            scene = new Scene(loader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Stage stage = new Stage();
        stage.setTitle("Orders");
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }

    private void reloadProductList() {
        products.setAll(srv.getAllProducts());
    }
}
