package academy.javaengineering.springsecurity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Demonstrates UserDetailsService implementations including
 * InMemoryUserDetailsManager, JDBC user details, and custom implementations.
 */
public class UserDetailsServiceExample {

    // User record for custom implementation
    public record UserRecord(
            String username,
            String password,
            boolean enabled,
            List<String> roles
    ) {
        public UserRecord {
            Objects.requireNonNull(username, "Username cannot be null");
            Objects.requireNonNull(password, "Password cannot be null");
            Objects.requireNonNull(roles, "Roles cannot be null");
        }

        public List<GrantedAuthority> getAuthorities() {
            return roles.stream()
                    .map(role -> (GrantedAuthority) new SimpleGrantedAuthority(role))
                    .toList();
        }
    }

    // Custom UserDetailsService backed by a simple in-memory store
    public static class CustomInMemoryUserDetailsService implements UserDetailsService {
        private final Map<String, UserRecord> users = new ConcurrentHashMap<>();
        private final PasswordEncoder passwordEncoder;

        public CustomInMemoryUserDetailsService(PasswordEncoder passwordEncoder) {
            this.passwordEncoder = passwordEncoder;
        }

        @Override
        public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
            UserRecord record = users.get(username);
            if (record == null) {
                throw new UsernameNotFoundException("User not found: " + username);
            }
            return User.builder()
                    .username(record.username())
                    .password(record.password())
                    .accountExpired(false)
                    .accountLocked(false)
                    .credentialsExpired(false)
                    .disabled(!record.enabled())
                    .authorities(record.getAuthorities())
                    .build();
        }

        public void addUser(String username, String rawPassword, boolean enabled, String... roles) {
            String encodedPassword = passwordEncoder.encode(rawPassword);
            UserRecord record = new UserRecord(
                    username, encodedPassword, enabled, List.of(roles));
            users.put(username, record);
        }

        public boolean removeUser(String username) {
            return users.remove(username) != null;
        }

        public boolean updateUser(String username, boolean enabled) {
            UserRecord existing = users.get(username);
            if (existing == null) {
                return false;
            }
            UserRecord updated = new UserRecord(
                    existing.username(), existing.password(), enabled, existing.roles());
            users.put(username, updated);
            return true;
        }

        public boolean changePassword(String username, String newPassword) {
            UserRecord existing = users.get(username);
            if (existing == null) {
                return false;
            }
            String encoded = passwordEncoder.encode(newPassword);
            UserRecord updated = new UserRecord(
                    existing.username(), encoded, existing.enabled(), existing.roles());
            users.put(username, updated);
            return true;
        }

        public List<String> getAllUsernames() {
            return new ArrayList<>(users.keySet());
        }

        public boolean userExists(String username) {
            return users.containsKey(username);
        }
    }

    // Simulated JDBC UserDetailsService
    public static class SimulatedJdbcUserDetailsService implements UserDetailsService {
        private final Map<String, UserRecord> database = new ConcurrentHashMap<>();
        private final PasswordEncoder passwordEncoder;

        public SimulatedJdbcUserDetailsService(PasswordEncoder passwordEncoder) {
            this.passwordEncoder = passwordEncoder;
            initializeDatabase();
        }

        private void initializeDatabase() {
            addUser("admin", "admin123", true, "ROLE_ADMIN", "ROLE_USER");
            addUser("user", "user123", true, "ROLE_USER");
            addUser("manager", "manager123", true, "ROLE_MANAGER", "ROLE_USER");
            addUser("disabled", "disabled123", false, "ROLE_USER");
        }

        private void addUser(String username, String password, boolean enabled, String... roles) {
            database.put(username, new UserRecord(
                    username,
                    passwordEncoder.encode(password),
                    enabled,
                    List.of(roles)
            ));
        }

        @Override
        public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
            // Simulates: SELECT * FROM users WHERE username = ?
            UserRecord record = database.get(username);
            if (record == null) {
                throw new UsernameNotFoundException("No user found with username: " + username);
            }

            // Simulates: SELECT authorities FROM user_authorities WHERE username = ?
            return User.builder()
                    .username(record.username())
                    .password(record.password())
                    .disabled(!record.enabled())
                    .authorities(record.getAuthorities())
                    .build();
        }

        public void insertUser(String username, String password, boolean enabled, String... roles) {
            database.put(username, new UserRecord(
                    username,
                    passwordEncoder.encode(password),
                    enabled,
                    List.of(roles)
            ));
        }

        public boolean deleteUser(String username) {
            return database.remove(username) != null;
        }

        public boolean updateEnabled(String username, boolean enabled) {
            UserRecord existing = database.get(username);
            if (existing == null) {
                return false;
            }
            database.put(username, new UserRecord(
                    existing.username(), existing.password(), enabled, existing.roles()));
            return true;
        }

        public int getUserCount() {
            return database.size();
        }
    }

    // UserDetailsService decorator for logging
    public static class LoggingUserDetailsService implements UserDetailsService {
        private final UserDetailsService delegate;
        private final List<String> logs = new ArrayList<>();

        public LoggingUserDetailsService(UserDetailsService delegate) {
            this.delegate = delegate;
        }

        @Override
        public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
            logs.add("Loading user: " + username);
            try {
                UserDetails user = delegate.loadUserByUsername(username);
                logs.add("Successfully loaded user: " + username);
                return user;
            } catch (UsernameNotFoundException e) {
                logs.add("Failed to load user: " + username + " - " + e.getMessage());
                throw e;
            }
        }

        public List<String> getLogs() {
            return List.copyOf(logs);
        }

        public void clearLogs() {
            logs.clear();
        }
    }

    // UserDetailsService chain for fallback loading
    public static class ChainedUserDetailsService implements UserDetailsService {
        private final List<UserDetailsService> services;

        public ChainedUserDetailsService(List<UserDetailsService> services) {
            this.services = List.copyOf(services);
        }

        @Override
        public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
            for (UserDetailsService service : services) {
                try {
                    return service.loadUserByUsername(username);
                } catch (UsernameNotFoundException e) {
                    // Continue to next service
                }
            }
            throw new UsernameNotFoundException("User not found in any service: " + username);
        }
    }

    // User validation utilities
    public static class UserValidator {
        private final UserDetailsService userDetailsService;

        public UserValidator(UserDetailsService userDetailsService) {
            this.userDetailsService = userDetailsService;
        }

        public boolean isUsernameTaken(String username) {
            try {
                userDetailsService.loadUserByUsername(username);
                return true;
            } catch (UsernameNotFoundException e) {
                return false;
            }
        }

        public boolean isUserEnabled(String username) {
            try {
                UserDetails user = userDetailsService.loadUserByUsername(username);
                return user.isEnabled();
            } catch (UsernameNotFoundException e) {
                return false;
            }
        }

        public boolean hasRole(String username, String role) {
            try {
                UserDetails user = userDetailsService.loadUserByUsername(username);
                return user.getAuthorities().stream()
                        .anyMatch(auth -> auth.getAuthority().equals(role));
            } catch (UsernameNotFoundException e) {
                return false;
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Spring Security UserDetailsService Examples ===\n");

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        // Demo 1: Custom InMemory UserDetailsService
        System.out.println("--- Demo 1: Custom InMemory UserDetailsService ---");
        var customService = new CustomInMemoryUserDetailsService(passwordEncoder);
        customService.addUser("alice", "alice123", true, "ROLE_USER");
        customService.addUser("bob", "bob123", true, "ROLE_ADMIN", "ROLE_USER");
        customService.addUser("charlie", "charlie123", false, "ROLE_USER");

        UserDetails alice = customService.loadUserByUsername("alice");
        System.out.println("Alice: " + alice.getUsername() + ", enabled=" + alice.isEnabled());
        System.out.println("Authorities: " + alice.getAuthorities());
        System.out.println("All usernames: " + customService.getAllUsernames());

        // Demo 2: User Management
        System.out.println("\n--- Demo 2: User Management ---");
        System.out.println("Alice exists: " + customService.userExists("alice"));
        System.out.println("Dave exists: " + customService.userExists("dave"));

        customService.updateUser("alice", false);
        UserDetails disabledAlice = customService.loadUserByUsername("alice");
        System.out.println("Alice after disable: enabled=" + disabledAlice.isEnabled());

        customService.updateUser("alice", true);
        customService.changePassword("alice", "newpassword123");
        System.out.println("Alice password changed successfully");

        // Demo 3: Simulated JDBC UserDetailsService
        System.out.println("\n--- Demo 3: Simulated JDBC UserDetailsService ---");
        var jdbcService = new SimulatedJdbcUserDetailsService(passwordEncoder);
        UserDetails admin = jdbcService.loadUserByUsername("admin");
        System.out.println("Admin loaded: " + admin.getUsername());
        System.out.println("Admin authorities: " + admin.getAuthorities());
        System.out.println("Total users: " + jdbcService.getUserCount());

        jdbcService.insertUser("newuser", "newpass123", true, "ROLE_USER");
        System.out.println("After insert: " + jdbcService.getUserCount() + " users");

        // Demo 4: Logging UserDetailsService
        System.out.println("\n--- Demo 4: Logging UserDetailsService ---");
        var loggingService = new LoggingUserDetailsService(customService);
        loggingService.loadUserByUsername("alice");
        loggingService.loadUserByUsername("nonexistent");

        System.out.println("Logs:");
        for (String log : loggingService.getLogs()) {
            System.out.println("  " + log);
        }

        // Demo 5: Chained UserDetailsService
        System.out.println("\n--- Demo 5: Chained UserDetailsService ---");
        var emptyService = new CustomInMemoryUserDetailsService(passwordEncoder);
        var chainedService = new ChainedUserDetailsService(List.of(emptyService, jdbcService));

        UserDetails chainedAdmin = chainedService.loadUserByUsername("admin");
        System.out.println("Chained service found: " + chainedAdmin.getUsername());

        try {
            chainedService.loadUserByUsername("nonexistent");
        } catch (UsernameNotFoundException e) {
            System.out.println("Expected error: " + e.getMessage());
        }

        // Demo 6: User Validator
        System.out.println("\n--- Demo 6: User Validator ---");
        var validator = new UserValidator(customService);
        System.out.println("Alice is taken: " + validator.isUsernameTaken("alice"));
        System.out.println("Dave is taken: " + validator.isUsernameTaken("dave"));
        System.out.println("Alice enabled: " + validator.isUserEnabled("alice"));
        System.out.println("Alice has ROLE_USER: " + validator.hasRole("alice", "ROLE_USER"));
        System.out.println("Alice has ROLE_ADMIN: " + validator.hasRole("alice", "ROLE_ADMIN"));

        // Demo 7: UserDetailsService Removal
        System.out.println("\n--- Demo 7: UserDetailsService Removal ---");
        System.out.println("Users before removal: " + customService.getAllUsernames());
        customService.removeUser("charlie");
        System.out.println("Users after removing charlie: " + customService.getAllUsernames());
        System.out.println("Charlie exists: " + customService.userExists("charlie"));

        System.out.println("\n=== All demos completed successfully ===");
    }
}
