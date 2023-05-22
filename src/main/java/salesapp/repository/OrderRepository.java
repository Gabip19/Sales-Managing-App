package salesapp.repository;

import salesapp.domain.Order;
import salesapp.domain.Product;
import salesapp.domain.User;

import java.util.List;
import java.util.UUID;

public interface OrderRepository extends Repository<UUID, Order> {
    List<Order> findByAgent(User agent);
}
