package academy.javaengineering.testing;

import java.util.HashMap;
import java.util.Map;

/**
 * Integration Test Example - Repository Pattern, Service Layer.
 */
public class IntegrationTestExample {

    public interface UserRepository {
        void save(String id, String name);
        String findById(String id);
        void delete(String id);
    }

    public static class InMemoryUserRepository implements UserRepository {
        private final Map<String, String> store = new HashMap<>();

        @Override
        public void save(String id, String name) { store.put(id, name); }

        @Override
        public String findById(String id) { return store.get(id); }

        @Override
        public void delete(String id) { store.remove(id); }
    }

    public static class UserService {
        private final UserRepository repository;

        public UserService(UserRepository repository) {
            this.repository = repository;
        }

        public void createUser(String id, String name) {
            if (id == null || name == null) throw new IllegalArgumentException("Id and name required");
            repository.save(id, name);
        }

        public String getUser(String id) {
            String name = repository.findById(id);
            return name != null ? name : "User not found";
        }

        public void deleteUser(String id) {
            repository.delete(id);
        }
    }

    public static void main(String[] args) {
        UserRepository repo = new InMemoryUserRepository();
        UserService service = new UserService(repo);
        service.createUser("1", "John");
        System.out.println("User: " + service.getUser("1"));
        service.deleteUser("1");
        System.out.println("After delete: " + service.getUser("1"));
    }
}
