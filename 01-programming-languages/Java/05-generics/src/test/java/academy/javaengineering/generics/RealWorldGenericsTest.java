package academy.javaengineering.generics;

import academy.javaengineering.generics.RealWorldGenerics.GenericService;
import academy.javaengineering.generics.RealWorldGenerics.InMemoryRepository;
import academy.javaengineering.generics.RealWorldGenerics.Result;
import academy.javaengineering.generics.RealWorldGenerics.Service;
import academy.javaengineering.generics.RealWorldGenerics.User;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for real-world generic patterns.
 */
@DisplayName("Real World Generics Tests")
class RealWorldGenericsTest {

  @Nested
  @DisplayName("Repository Tests")
  class RepositoryTests {

    private InMemoryRepository<User, String> repository;

    @BeforeEach
    void setUp() {
      repository = new InMemoryRepository<>(User::id);
      repository.save(new User("1", "Alice"));
      repository.save(new User("2", "Bob"));
    }

    @Test
    @DisplayName("Should find user by ID")
    void shouldFindById() {
      Optional<User> user = repository.findById("1");
      assertTrue(user.isPresent());
      assertEquals("Alice", user.get().name());
    }

    @Test
    @DisplayName("Should return empty for missing ID")
    void shouldReturnEmptyForMissingId() {
      Optional<User> user = repository.findById("999");
      assertFalse(user.isPresent());
    }

    @Test
    @DisplayName("Should find all users")
    void shouldFindAll() {
      List<User> users = repository.findAll();
      assertEquals(2, users.size());
    }

    @Test
    @DisplayName("Should delete user by ID")
    void shouldDeleteById() {
      repository.deleteById("1");
      assertFalse(repository.findById("1").isPresent());
      assertEquals(1, repository.findAll().size());
    }
  }

  @Nested
  @DisplayName("Service Tests")
  class ServiceTests {

    private Service<User, String> service;

    @BeforeEach
    void setUp() {
      var repo = new InMemoryRepository<User, String>(User::id);
      repo.save(new User("1", "Alice"));
      repo.save(new User("2", "Bob"));
      service = new GenericService<>(repo);
    }

    @Test
    @DisplayName("Should find user via service")
    void shouldFindViaService() {
      Optional<User> user = service.findById("1");
      assertTrue(user.isPresent());
      assertEquals("Alice", user.get().name());
    }

    @Test
    @DisplayName("Should filter users by predicate")
    void shouldFilterByPredicate() {
      List<User> filtered = service.findByPredicate(
          u -> u.name().startsWith("A"));
      assertEquals(1, filtered.size());
      assertEquals("Alice", filtered.getFirst().name());
    }

    @Test
    @DisplayName("Should create new user")
    void shouldCreateUser() {
      User created = service.create(new User("3", "Charlie"));
      assertEquals("Charlie", created.name());
      assertEquals(3, service.findByPredicate(u -> true).size());
    }
  }

  @Nested
  @DisplayName("Result Pattern Tests")
  class ResultTests {

    @Test
    @DisplayName("Should create success result")
    void shouldCreateSuccess() {
      Result<String> result = new Result.Success<>("done");
      assertTrue(result instanceof Result.Success);
    }

    @Test
    @DisplayName("Should create failure result")
    void shouldCreateFailure() {
      Result<String> result = new Result.Failure<>("error");
      assertTrue(result instanceof Result.Failure);
    }

    @Test
    @DisplayName("Should map success result")
    void shouldMapSuccess() {
      Result<String> success = new Result.Success<>("hello");
      Result<Integer> mapped = success.map(String::length);
      assertTrue(mapped instanceof Result.Success);
      assertEquals(5, ((Result.Success<Integer>) mapped).value());
    }

    @Test
    @DisplayName("Should preserve failure on map")
    void shouldPreserveFailureOnMap() {
      Result<String> failure = new Result.Failure<>("oops");
      Result<Integer> mapped = failure.map(String::length);
      assertTrue(mapped instanceof Result.Failure);
      assertEquals("oops", ((Result.Failure<Integer>) mapped).message());
    }
  }
}
