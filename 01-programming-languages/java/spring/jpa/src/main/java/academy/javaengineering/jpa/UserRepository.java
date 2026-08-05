package academy.javaengineering.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * JPA Repository demonstrating CRUD operations and custom queries.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Simple derived queries
    Optional<User> findByEmail(String email);
    List<User> findByName(String name);
    List<User> findByNameContainingIgnoreCase(String name);
    boolean existsByEmail(String email);

    // JPQL queries
    @Query("SELECT u FROM User u WHERE u.role = :role")
    List<User> findByRole(@Param("role") UserRole role);

    @Query("SELECT u FROM User u WHERE u.email LIKE %:domain")
    List<User> findByEmailDomain(@Param("domain") String domain);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.posts WHERE u.id = :id")
    Optional<User> findByIdWithPosts(@Param("id") Long id);

    // Native SQL queries
    @Query(value = "SELECT * FROM users WHERE created_at > DATE_SUB(NOW(), INTERVAL 30 DAY)", nativeQuery = true)
    List<User> findRecentUsers();

    // Aggregate queries
    @Query("SELECT COUNT(u) FROM User u WHERE u.role = :role")
    long countByRole(@Param("role") UserRole role);
}
