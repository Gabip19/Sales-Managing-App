package salesapp.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import salesapp.domain.*;

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

    private Order currentOrder;

    public void initialize() {
        initProductTableView();
        initOrderItemTableView();
        products.setAll(srv.getAllProducts());
        productsTableView.getSelectionModel().selectedItemProperty().addListener((a, b, c) -> selectionChanged());
        addToCartBtn.setOnAction((e) -> addProductToCart());
        removeOrderItemBtn.setOnAction((e) -> removeOrderItem());
        placeOrderBtn.setOnAction((e) -> placeOrder());
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
}
