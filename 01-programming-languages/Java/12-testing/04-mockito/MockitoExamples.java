package testing;

/**
 * MockitoExamples - Mocking, stubbing, verification
 *
 * Covers:
 * - Creating mocks
 * - Stubbing methods
 * - Verification
 * - Argument matching
 * - Spy vs Mock
 */
public class MockitoExamples {

    // Simple service interface for demonstration
    interface UserService {
        String findUserById(int id);
        boolean saveUser(String name);
        void deleteUser(int id);
    }

    // Simple repository interface
    interface UserRepository {
        String findById(int id);
        void save(String name);
        void delete(int id);
        int count();
    }

    // Service implementation
    static class UserServiceImpl implements UserService {
        private UserRepository repository;

        public UserServiceImpl(UserRepository repository) {
            this.repository = repository;
        }

        @Override
        public String findUserById(int id) {
            return repository.findById(id);
        }

        @Override
        public boolean saveUser(String name) {
            if (name == null || name.isEmpty()) {
                return false;
            }
            repository.save(name);
            return true;
        }

        @Override
        public void deleteUser(int id) {
            repository.delete(id);
        }
    }

    // Manual mock implementation
    static class MockUserRepository implements UserRepository {
        private java.util.Map<Integer, String> storage = new java.util.HashMap<>();
        private int saveCount = 0;
        private int deleteCount = 0;

        @Override
        public String findById(int id) {
            return storage.get(id);
        }

        @Override
        public void save(String name) {
            storage.put(saveCount + 1, name);
            saveCount++;
        }

        @Override
        public void delete(int id) {
            storage.remove(id);
            deleteCount++;
        }

        @Override
        public int count() {
            return storage.size();
        }

        public int getSaveCount() {
            return saveCount;
        }

        public int getDeleteCount() {
            return deleteCount;
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Mockito Concepts ===\n");

        System.out.println("1. Creating Mocks:");
        System.out.println("   UserService mock = mock(UserService.class);");
        System.out.println("   @Mock UserService mockService;");
        System.out.println("   @InjectMocks UserServiceImpl service;");
        System.out.println();

        System.out.println("2. Stubbing:");
        System.out.println("   when(mock.findUserById(1)).thenReturn(\"John\");");
        System.out.println("   when(mock.saveUser(anyString())).thenReturn(true);");
        System.out.println("   when(mock.findUserById(999)).thenThrow(new RuntimeException());");
        System.out.println();

        System.out.println("3. Verification:");
        System.out.println("   verify(mock).deleteUser(1);");
        System.out.println("   verify(mock, times(2)).findUserById(1);");
        System.out.println("   verify(mock, never()).deleteUser(999);");
        System.out.println();

        System.out.println("4. Argument Matchers:");
        System.out.println("   any(), anyInt(), anyString()");
        System.out.println("   eq(value), argThat(predicate)");
        System.out.println("   startWith(\"prefix\"), contains(\"text\")");
        System.out.println();

        // Demonstrate with manual mock
        System.out.println("=== Manual Mock Example ===\n");
        manualMockDemo();
    }

    static void manualMockDemo() {
        MockUserRepository mockRepo = new MockUserRepository();
        UserServiceImpl service = new UserServiceImpl(mockRepo);

        // Stub behavior: pre-populate data
        mockRepo.save("Alice");
        mockRepo.save("Bob");

        // Test findUserById
        String user = service.findUserById(1);
        System.out.println("Find user 1: " + user);
        assert "Alice".equals(user) : "Should find Alice";

        // Test saveUser
        boolean saved = service.saveUser("Charlie");
        System.out.println("Save Charlie: " + saved);
        assert saved : "Save should succeed";
        assert mockRepo.getSaveCount() == 3 : "Should have 3 saves";

        // Test deleteUser
        service.deleteUser(1);
        System.out.println("Delete user 1");
        assert mockRepo.getDeleteCount() == 1 : "Should have 1 delete";
        assert mockRepo.findById(1) == null : "User 1 should be deleted";

        // Verify interactions
        System.out.println("\nVerification:");
        System.out.println("  Save count: " + mockRepo.getSaveCount());
        System.out.println("  Delete count: " + mockRepo.getDeleteCount());
        System.out.println("  Total users: " + mockRepo.count());
    }
}