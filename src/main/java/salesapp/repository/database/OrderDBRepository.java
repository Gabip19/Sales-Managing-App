package salesapp.repository.database;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;
import salesapp.domain.Order;
import salesapp.repository.OrderRepository;

import java.util.UUID;

@Component
public class OrderDBRepository implements OrderRepository {
    public OrderDBRepository() {}

    @Override
    public void add(Order elem) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                session.persist(elem);
                transaction.commit();
            } catch (RuntimeException e) {
                System.err.println("[ORDER REPO] Add order failed: " + e.getMessage());
                if (transaction != null) {
                    transaction.rollback();
                }
            }
        }
    }

    @Override
    public void delete(UUID uuid) {
        throw new RuntimeException("Not implemented.\n");
    }

    @Override
    public void update(Order elem, UUID uuid) {
        throw new RuntimeException("Not implemented.\n");
    }

    @Override
    public Order findById(UUID uuid) {
        throw new RuntimeException("Not implemented.\n");
    }

    @Override
    public Iterable<Order> findAll() {
        throw new RuntimeException("Not implemented.\n");
    }
}
