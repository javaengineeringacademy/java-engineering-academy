package academy.javaengineering.springcore;

import java.util.Optional;

/**
 * User repository interface.
 */
public interface UserRepository {
    Optional<User> findById(String id);
    void save(User user);
}
