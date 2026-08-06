package academy.javaengineering.minibanking.repository;

import java.util.List;
import java.util.Optional;

/**
 * Generic repository interface for entity persistence.
 *
 * <p>Engineering Decision: Using Generics (T extends Object).
 * WHY: Generics provide compile-time type safety and eliminate casting.
 * A repository interface can be implemented for any entity type while
 * maintaining type safety throughout the application.</p>
 *
 * <p>Engineering Decision: Interface instead of abstract class.
 * WHY: Java allows single inheritance but multiple interface implementation.
 * Interfaces enable polymorphism across unrelated class hierarchies
 * (e.g., AccountRepository and future TransactionRepository).</p>
 *
 * <p>Engineering Topics Demonstrated:
 * - Generics (type parameter T)
 * - Interface contracts
 * - Optional for null-safe returns
 * - Dependency Inversion Principle (high-level modules depend on abstractions)</p>
 *
 * @param <T> the entity type this repository manages
 */
public interface AccountRepository<T> {

    /**
     * Persists a new entity or updates an existing one.
     *
     * @param entity the entity to save
     * @return the saved entity
     */
    T save(T entity);

    /**
     * Finds an entity by its unique identifier.
     *
     * <p>Engineering Decision: Return Optional instead of null.
     * WHY: Optional makes the possibility of absence explicit in the type system,
     * forcing callers to handle the empty case. This prevents NullPointerExceptions.</p>
     *
     * @param id the unique identifier
     * @return Optional containing the entity if found, empty otherwise
     */
    Optional<T> findById(String id);

    /**
     * Retrieves all entities.
     *
     * @return list of all entities
     */
    List<T> findAll();

    /**
     * Deletes an entity by its identifier.
     *
     * @param id the unique identifier of the entity to delete
     * @return true if entity was found and deleted, false otherwise
     */
    boolean delete(String id);

    /**
     * Checks if an entity exists with the given ID.
     *
     * @param id the unique identifier to check
     * @return true if entity exists
     */
    boolean existsById(String id);

    /**
     * Returns the count of all entities.
     *
     * @return number of entities
     */
    long count();
}
