package salesapp.repository.database;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;
import salesapp.domain.User;
import salesapp.repository.UserRepository;

import java.util.UUID;

@Component
public class UserDBRepository implements UserRepository {
    public UserDBRepository() {}

    @Override
    public void add(User elem) {
        throw new RuntimeException("Not implemented.\n");
    }

    @Override
    public void delete(UUID uuid) {
        throw new RuntimeException("Not implemented.\n");
    }

    @Override
    public void update(User elem, UUID uuid) {
        throw new RuntimeException("Not implemented.\n");
    }

    @Override
    public User findById(UUID uuid) {
        throw new RuntimeException("Not implemented.\n");
    }

    @Override
    public Iterable<User> findAll() {
        throw new RuntimeException("Not implemented.\n");
    }

    @Override
    public User findByUsername(String username) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                String queryString = "from User u where u.username = ?1";
                User user = session.createQuery(queryString, User.class)
                        .setParameter(1, username)
                        .setMaxResults(1)
                        .uniqueResult();
                transaction.commit();
                return user;
            } catch (RuntimeException e) {
                System.err.println("[USER REPO] Find user by username failed: " + e.getMessage());
                if (transaction != null) {
                    transaction.rollback();
                }
            }
        }
        return null;
    }
}
