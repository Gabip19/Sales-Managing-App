package salesapp.repository.database;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;
import salesapp.domain.Product;
import salesapp.repository.ProductRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class ProductDBRepository implements ProductRepository {
    public ProductDBRepository() {}

    @Override
    public void add(Product elem) {
        throw new RuntimeException("Not implemented.\n");
    }

    @Override
    public void delete(UUID uuid) {
        throw new RuntimeException("Not implemented.\n");
    }

    @Override
    public void update(Product elem, UUID uuid) {
        throw new RuntimeException("Not implemented.\n");
    }

    @Override
    public Product findById(UUID uuid) {
        throw new RuntimeException("Not implemented.\n");
    }

    @Override
    public Iterable<Product> findAll() {
        List<Product> products = new ArrayList<>();

        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                products = session.createQuery("from Product p", Product.class).list();
                transaction.commit();
            } catch (RuntimeException e) {
                System.err.println("[PRODUCT REPO] Find all products failed: " + e.getMessage());
                if (transaction != null) {
                    transaction.rollback();
                }
            }
        }

        return products;
    }
}
