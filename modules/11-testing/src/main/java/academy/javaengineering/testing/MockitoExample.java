package academy.javaengineering.testing;

/**
 * Mockito Example - Mocking, Stubbing, Verification.
 */
public class MockitoExample {

    public interface UserRepository {
        String findById(Long id);
        void save(String user);
    }

    private final UserRepository repository;

    public MockitoExample(UserRepository repository) {
        this.repository = repository;
    }

    public String getUser(Long id) {
        String user = repository.findById(id);
        return user != null ? user : "Unknown";
    }

    public void createUser(String user) {
        if (user == null || user.isEmpty()) {
            throw new IllegalArgumentException("User cannot be empty");
        }
        repository.save(user);
    }

    public static void main(String[] args) {
        System.out.println("Mockito Example - See tests for mocking");
    }
}
