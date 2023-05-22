package salesapp;

import org.hibernate.Session;
import org.hibernate.Transaction;
import salesapp.domain.*;
import salesapp.repository.HibernateUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Main {
    public static void main(String[] args) {
//        User user = new User("User2", "User2", "user2", "user2", false);
//        user.setId(UUID.fromString("f6ad6fbf-dbeb-467f-ba5f-592a3f05aa2f"));
//
//        Product prod1 = new Product("prod2", "prod2", 23.2f, 10);
//        prod1.setId(UUID.fromString("36dbb6dc-5c69-4d99-aeba-66bd22bd4154"));
//        Product prod2 = new Product("prod3", "prod3", 24.5f, 12);
//        prod2.setId(UUID.fromString("cc31a88a-a561-4b3c-9a6c-6f2fe0c0b37c"));
//
//        OrderItem orderItem1 = new OrderItem(prod1, 10);
//        OrderItem orderItem2 = new OrderItem(prod2, 5);
//
//        Order order = new Order("Andrei", OrderStatus.PENDING, user);
//        order.addOrderItem(orderItem1);
//        order.addOrderItem(orderItem2);

        List<Order> elems = new ArrayList<>();

        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                elems = session.createQuery("from Order o", Order.class).list();
                transaction.commit();
//                System.out.println(elems.get(0).getProduct().getName());
            } catch (RuntimeException e) {
                System.err.println("[DONOR REPO] Find all donors failed: " + e.getMessage());
                if (transaction != null) {
                    transaction.rollback();
                }
            }
        }
    }
}