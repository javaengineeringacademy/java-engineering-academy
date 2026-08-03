package academy.javaengineering.jpa;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service demonstrating JPA operations with transaction management.
 */
@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public UserService(UserRepository userRepository, PostRepository postRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    // CRUD Operations
    public User createUser(String name, String email) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already exists: " + email);
        }
        User user = new User(name, email);
        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<User> findByIdWithPosts(Long id) {
        return userRepository.findByIdWithPosts(id);
    }

    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User updateUser(Long id, String name, String email) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("User not found: " + id));
        user.setName(name);
        user.setEmail(email);
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    // Custom Queries
    @Transactional(readOnly = true)
    public List<User> findByRole(UserRole role) {
        return userRepository.findByRole(role);
    }

    @Transactional(readOnly = true)
    public List<User> searchByName(String name) {
        return userRepository.findByNameContainingIgnoreCase(name);
    }

    @Transactional(readOnly = true)
    public long countByRole(UserRole role) {
        return userRepository.countByRole(role);
    }

    // Transaction Management Example
    @Transactional
    public User createUserWithProfile(String name, String email, String location) {
        User user = new User(name, email);
        UserProfile profile = new UserProfile();
        profile.setLocation(location);
        user.setProfile(profile);
        return userRepository.save(user);
    }

    @Transactional
    public void transferPosts(Long fromUserId, Long toUserId) {
        User fromUser = userRepository.findById(fromUserId)
            .orElseThrow(() -> new IllegalArgumentException("Source user not found"));
        User toUser = userRepository.findById(toUserId)
            .orElseThrow(() -> new IllegalArgumentException("Target user not found"));

        List<Post> posts = postRepository.findByAuthorId(fromUserId);
        for (Post post : posts) {
            post.setAuthor(toUser);
        }
        postRepository.saveAll(posts);
    }
}
