package salesapp.repository;

import salesapp.domain.User;

import java.util.UUID;

public interface UserRepository extends Repository<UUID, User> {
    User findByUsername(String username);
}
