package salesapp.repository.database;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;
import salesapp.domain.Order;
import salesapp.domain.User;
import salesapp.repository.OrderRepository;

import java.util.ArrayList;
import java.util.List;
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
    public List<Order> findByAgent(User agent) {
        List<Order> orders;

        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                orders = session.createQuery("from Order o where o.agent.id = ?1", Order.class)
                        .setParameter(1, agent.getId())
                        .list();
                transaction.commit();
                return orders;
            } catch (RuntimeException e) {
                System.err.println("[ORDER REPO] Find all orders by agent failed: " + e.getMessage());
                if (transaction != null) {
                    transaction.rollback();
                }
            }
        }

        return new ArrayList<>();
    }

    @Override
    public void delete(UUID id) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                Order order = session.get(Order.class, id);
                session.remove(order);
                transaction.commit();
            } catch (RuntimeException e) {
                System.err.println("[ORDER REPO] Update order failed: " + e.getMessage());
                if (transaction != null) {
                    transaction.rollback();
                }
            }
        }
    }

    @Override
    public void update(Order elem, UUID id) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                Order order = session.get(Order.class, id);
                order.setStatus(elem.getStatus());
                order.setClientName(elem.getClientName());
                session.merge(order);
                transaction.commit();
            } catch (RuntimeException e) {
                System.err.println("[ORDER REPO] Update order failed: " + e.getMessage());
                if (transaction != null) {
                    transaction.rollback();
                }
            }
        }
    }

    @Override
    public Order findById(UUID uuid) {
        throw new RuntimeException("Not implemented.\n");
    }

    @Override
    public Iterable<Order> findAll() {
        List<Order> orders;

        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                orders = session.createQuery("from Order o", Order.class).list();
                transaction.commit();
                return orders;
            } catch (RuntimeException e) {
                System.err.println("[ORDER REPO] Find all orders failed: " + e.getMessage());
                if (transaction != null) {
                    transaction.rollback();
                }
            }
        }

        return new ArrayList<>();
    }
}
