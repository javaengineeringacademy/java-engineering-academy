package academy.javaengineering.testing.practices;

import java.util.*;

/**
 * Integration Testing Exercises
 * Practice testing with real dependencies
 */
class IntegrationTestingExercises {

    // ============================================
    // Components for Integration Testing
    // ============================================

    static class User {
        private Long id;
        private String name;
        private String email;

        User(String name, String email) {
            this.name = name;
            this.email = email;
        }

        User(Long id, String name, String email) {
            this.id = id;
            this.name = name;
            this.email = email;
        }

        Long getId() { return id; }
        void setId(Long id) { this.id = id; }
        String getName() { return name; }
        String getEmail() { return email; }
    }

    interface UserRepository {
        User save(User user);
        Optional<User> findById(Long id);
        List<User> findAll();
        void deleteById(Long id);
    }

    static class InMemoryUserRepository implements UserRepository {
        private final List<User> users = new ArrayList<>();
        private long nextId = 1;

        @Override
        public User save(User user) {
            if (user.getId() == null) user.setId(nextId++);
            users.removeIf(u -> u.getId().equals(user.getId()));
            users.add(user);
            return user;
        }

        @Override
        public Optional<User> findById(Long id) {
            return users.stream().filter(u -> u.getId().equals(id)).findFirst();
        }

        @Override
        public List<User> findAll() {
            return new ArrayList<>(users);
        }

        @Override
        public void deleteById(Long id) {
            users.removeIf(u -> u.getId().equals(id));
        }
    }

    static class UserService {
        private final UserRepository userRepository;

        UserService(UserRepository userRepository) {
            this.userRepository = userRepository;
        }

        User createUser(String name, String email) {
            User user = new User(name, email);
            return userRepository.save(user);
        }

        Optional<User> getUserById(Long id) {
            return userRepository.findById(id);
        }

        List<User> getAllUsers() {
            return userRepository.findAll();
        }

        void deleteUser(Long id) {
            userRepository.deleteById(id);
        }
    }

    // ============================================
    // Exercise 1: Full CRUD Operations
    // ============================================
    // TODO: Test Create, Read, Update, Delete operations

    /*
     * @Test
     * void testCreateUser() {
     *     // Create a user and verify it's saved
     * }
     * 
     * @Test
     * void testReadUser() {
     *     // Save a user and retrieve by ID
     * }
     * 
     * @Test
     * void testDeleteUser() {
     *     // Save a user, delete it, verify it's gone
     * }
     */

    // ============================================
    // Exercise 2: Service Integration
    // ============================================
    // TODO: Test service layer with real repository

    /*
     * @Test
     * void testServiceCreatesUser() {
     *     // Use UserService with InMemoryRepository
     *     // Create user and verify
     * }
     */

    // ============================================
    // Exercise 3: Error Handling
    // ============================================
    // TODO: Test error scenarios

    /*
     * @Test
     * void testFindNonExistentUser() {
     *     // Try to find user that doesn't exist
     *     // Verify Optional.empty() is returned
     * }
     */

    // ============================================
    // Exercise 4: Multiple Operations
    // ============================================
    // TODO: Test sequences of operations

    /*
     * @Test
     * void testMultipleUsers() {
     *     // Create multiple users
     *     // Verify all are retrieved
     * }
     */

    // ============================================
    // Exercise 5: Data Consistency
    // ============================================
    // TODO: Verify data consistency across operations

    /*
     * @Test
     * void testDataConsistency() {
     *     // Create user, update, verify update persisted
     * }
     */

    public static void main(String[] args) {
        System.out.println("=== Integration Testing Exercises ===");
        System.out.println("Practice testing with real dependencies.");
    }
}
