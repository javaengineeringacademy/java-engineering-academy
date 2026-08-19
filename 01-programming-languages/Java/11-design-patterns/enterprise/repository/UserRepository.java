package academy.javaengineering.patterns.enterprise.repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface defining data access operations for User entities.
 * Implementations can be in-memory, JDBC, JPA, etc.
 */
public interface UserRepository {

    User save(User user);

    Optional<User> findById(Long id);

    Optional<User> findByEmail(String email);

    List<User> findAll();

    List<User> findByNameContaining(String namePart);

    boolean deleteById(Long id);

    long count();

    boolean existsByEmail(String email);
}
