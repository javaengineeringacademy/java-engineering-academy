package academy.javaengineering.patterns.enterprise.dao;

import java.util.List;
import java.util.Optional;

/**
 * DAO interface for User data access operations.
 * Provides CRUD operations and queries at the data-access level.
 */
public interface UserDao {

    User create(User user);

    Optional<User> findById(Long id);

    List<User> findAll();

    Optional<User> findByName(String name);

    List<User> findByEmailDomain(String domain);

    User update(User user);

    boolean delete(Long id);

    boolean exists(Long id);

    int count();
}
