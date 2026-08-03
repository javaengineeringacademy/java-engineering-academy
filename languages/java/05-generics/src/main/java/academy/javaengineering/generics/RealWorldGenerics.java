package academy.javaengineering.generics;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Demonstrates real-world enterprise patterns using generics.
 *
 * <p>This class shows practical generic patterns used in enterprise
 * applications for data access, service layers, and utility functions.</p>
 */
public class RealWorldGenerics {

  /**
   * Generic repository interface for data access.
   *
   * @param <T>  the entity type
   * @param <ID> the identifier type
   */
  public interface Repository<T, ID> {
    Optional<T> findById(ID id);

    List<T> findAll();

    T save(T entity);

    void deleteById(ID id);
  }

  /**
   * In-memory repository implementation.
   *
   * @param <T>  the entity type
   * @param <ID> the identifier type
   */
  public static class InMemoryRepository<T, ID> implements Repository<T, ID> {
    private final List<T> entities = new ArrayList<>();
    private final Function<T, ID> idExtractor;

    /**
     * Creates a repository with an ID extraction function.
     *
     * @param idExtractor function to extract ID from entity
     */
    public InMemoryRepository(Function<T, ID> idExtractor) {
      this.idExtractor = idExtractor;
    }

    @Override
    public Optional<T> findById(ID id) {
      return entities.stream()
          .filter(e -> idExtractor.apply(e).equals(id))
          .findFirst();
    }

    @Override
    public List<T> findAll() {
      return List.copyOf(entities);
    }

    @Override
    public T save(T entity) {
      entities.add(entity);
      return entity;
    }

    @Override
    public void deleteById(ID id) {
      entities.removeIf(e -> idExtractor.apply(e).equals(id));
    }
  }

  /**
   * Generic service interface.
   *
   * @param <T>  the domain type
   * @param <ID> the identifier type
   */
  public interface Service<T, ID> {
    Optional<T> findById(ID id);

    List<T> findByPredicate(Predicate<T> predicate);

    T create(T entity);
  }

  /**
   * Generic service implementation.
   *
   * @param <T>  the domain type
   * @param <ID> the identifier type
   */
  public static class GenericService<T, ID> implements Service<T, ID> {
    private final Repository<T, ID> repository;

    /**
     * Creates a service with the given repository.
     *
     * @param repository the data repository
     */
    public GenericService(Repository<T, ID> repository) {
      this.repository = repository;
    }

    @Override
    public Optional<T> findById(ID id) {
      return repository.findById(id);
    }

    @Override
    public List<T> findByPredicate(Predicate<T> predicate) {
      return repository.findAll().stream()
          .filter(predicate)
          .toList();
    }

    @Override
    public T create(T entity) {
      return repository.save(entity);
    }
  }

  /**
   * Simple entity class for demonstration.
   *
   * @param id   the entity ID
   * @param name the entity name
   */
  public record User(String id, String name) {

  }

  /**
   * Generic result wrapper for operation outcomes.
   *
   * @param <T> the success type
   */
  public sealed interface Result<T> permits
      Result.Success, Result.Failure {

    record Success<T>(T value) implements Result<T> {

    }

    record Failure<T>(String message) implements Result<T> {

    }

    /**
     * Maps the result using the provided function.
     *
     * @param mapper the mapping function
     * @param <U>    the new type
     * @return the mapped result
     */
    default <U> Result<U> map(Function<T, U> mapper) {
      return switch (this) {
        case Success<T> success -> new Success<>(mapper.apply(success.value()));
        case Failure<T> failure -> new Failure<>(failure.message());
      };
    }
  }

  /**
   * Demonstrates real-world generic patterns.
   *
   * @param args command-line arguments
   */
  public static void main(String[] args) {
    // Repository pattern
    Repository<User, String> userRepo =
        new InMemoryRepository<>(User::id);

    userRepo.save(new User("1", "Alice"));
    userRepo.save(new User("2", "Bob"));

    System.out.println("Find user 1: " + userRepo.findById("1"));
    // Expected: Find user 1: Optional[User[id=1, name=Alice]]

    System.out.println("All users: " + userRepo.findAll());
    // Expected: All users: [User[id=1, name=Alice], User[id=2, name=Bob]]

    // Service pattern
    Service<User, String> userService =
        new GenericService<>(userRepo);

    var alice = userService.findById("1");
    System.out.println("Service find: " + alice);
    // Expected: Service find: Optional[User[id=1, name=Alice]]

    List<User> filtered = userService.findByPredicate(
        u -> u.name().startsWith("A"));
    System.out.println("Filtered users: " + filtered);
    // Expected: Filtered users: [User[id=1, name=Alice]]

    // Result pattern
    Result<String> success = new Result.Success<>("Operation successful");
    Result<String> failure = new Result.Failure<>("Something went wrong");

    Result<Integer> mappedSuccess = success.map(String::length);
    Result<Integer> mappedFailure = failure.map(String::length);

    System.out.println("Success mapped: " + mappedSuccess);
    // Expected: Success mapped: Success[value=20]
    System.out.println("Failure mapped: " + mappedFailure);
    // Expected: Failure mapped: Failure[message=Something went wrong]
  }
}
