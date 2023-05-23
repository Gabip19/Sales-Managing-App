package salesapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import salesapp.domain.Order;
import salesapp.domain.OrderStatus;
import salesapp.domain.Product;
import salesapp.domain.User;
import salesapp.repository.OrderRepository;
import salesapp.repository.ProductRepository;
import salesapp.repository.UserRepository;

import java.util.ArrayList;
import java.util.Collection;

@Component
public class Service {
    private final UserRepository userRepo;
    private final ProductRepository productRepo;
    private final OrderRepository orderRepo;

    @Autowired
    public Service(UserRepository userRepo, ProductRepository productRepo, OrderRepository orderRepo) {
        this.userRepo = userRepo;
        this.productRepo = productRepo;
        this.orderRepo = orderRepo;
    }

    public User attemptLogin(User user) {
        User existingUser = userRepo.findByUsername(user.getUsername());

        if (existingUser == null) {
            throw new IllegalArgumentException("Invalid username.\n");
        }

        if (!existingUser.getPassword().equals(user.getPassword())) {
            throw new IllegalArgumentException("Invalid password.\n");
        }

        return existingUser;
    }

    public ArrayList<Product> getAllProducts() {
        return new ArrayList<>((Collection<Product>) productRepo.findAll());
    }

    public void placeOrder(Order order) {
        orderRepo.add(order);
        order.getOrderItems().forEach(orderItem -> {
            Product product = orderItem.getProduct();
            product.setStock(product.getStock() - orderItem.getQuantity());
            productRepo.update(product, product.getId());
        });
    }

    public ArrayList<Order> getAllOrdersForUser(User agent) {
        return new ArrayList<>(orderRepo.findByAgent(agent));
    }

    public Order finishOrder(Order order) {
        order.setStatus(OrderStatus.FINISHED);
        orderRepo.update(order, order.getId());
        return order;
    }

    public void cancelOrder(Order order) {
        orderRepo.delete(order.getId());
    }

    public ArrayList<Order> getAllOrders() {
        return new ArrayList<>((Collection<Order>) orderRepo.findAll());
    }
}
