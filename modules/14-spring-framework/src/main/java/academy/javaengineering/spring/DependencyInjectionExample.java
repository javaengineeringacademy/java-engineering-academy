package academy.javaengineering.spring;

/**
 * Dependency Injection - Constructor, Setter, Field Injection.
 */
public class DependencyInjectionExample {

    public interface UserRepository {
        String findById(Long id);
    }

    public static class UserRepositoryImpl implements UserRepository {
        @Override
        public String findById(Long id) { return "User-" + id; }
    }

    public static class UserService {
        private final UserRepository repository;

        public UserService(UserRepository repository) {
            this.repository = repository;
        }

        public String getUser(Long id) { return repository.findById(id); }
    }

    public static void main(String[] args) {
        UserRepository repo = new UserRepositoryImpl();
        UserService service = new UserService(repo);
        System.out.println("User: " + service.getUser(1L));
    }
}
