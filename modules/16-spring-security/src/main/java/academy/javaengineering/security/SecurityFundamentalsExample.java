package academy.javaengineering.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collection;

public class SecurityFundamentalsExample {

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final InMemoryUserDetailsManager userManager;

    public SecurityFundamentalsExample() {
        this.userManager = new InMemoryUserDetailsManager();
        initializeUsers();
    }

    private void initializeUsers() {
        UserDetails admin = User.builder()
                .username("admin")
                .password(passwordEncoder.encode("admin123"))
                .roles("ADMIN", "USER")
                .build();

        UserDetails user = User.builder()
                .username("user")
                .password(passwordEncoder.encode("user123"))
                .roles("USER")
                .build();

        UserDetails viewer = User.builder()
                .username("viewer")
                .password(passwordEncoder.encode("viewer123"))
                .roles("VIEWER")
                .build();

        userManager.createUser(admin);
        userManager.createUser(user);
        userManager.createUser(viewer);
    }

    public boolean authenticate(String username, String password) {
        if (!userManager.userExists(username)) {
            System.out.println("User not found: " + username);
            return false;
        }

        UserDetails userDetails = userManager.loadUserByUsername(username);
        boolean matches = passwordEncoder.matches(password, userDetails.getPassword());

        if (matches) {
            Authentication authentication = new org.springframework.security.authentication
                    .UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            System.out.println("Authentication successful for: " + username);
        } else {
            System.out.println("Authentication failed for: " + username);
        }

        return matches;
    }

    public boolean hasRole(String role) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return false;
        }

        return authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_" + role));
    }

    public Collection<? extends GrantedAuthority> getCurrentUserAuthorities() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return java.util.Collections.emptyList();
        }
        return authentication.getAuthorities();
    }

    public String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return null;
        }
        return authentication.getName();
    }

    public void demonstrateAuthorization() {
        System.out.println("\n=== Authorization Demo ===");
        System.out.println("Current user: " + getCurrentUsername());
        System.out.println("Authorities: " + getCurrentUserAuthorities());
        System.out.println("Has ADMIN role: " + hasRole("ADMIN"));
        System.out.println("Has USER role: " + hasRole("USER"));
        System.out.println("Has VIEWER role: " + hasRole("VIEWER"));
    }

    public void demonstratePasswordEncoding() {
        System.out.println("\n=== Password Encoding Demo ===");
        String rawPassword = "mypassword";
        String encodedPassword = passwordEncoder.encode(rawPassword);
        System.out.println("Raw password: " + rawPassword);
        System.out.println("Encoded password: " + encodedPassword);
        System.out.println("Matches: " + passwordEncoder.matches(rawPassword, encodedPassword));
    }

    public static void main(String[] args) {
        SecurityFundamentalsExample example = new SecurityFundamentalsExample();

        System.out.println("=== Security Fundamentals Demo ===\n");

        example.demonstratePasswordEncoding();

        System.out.println("\n--- Authenticating admin ---");
        example.authenticate("admin", "admin123");
        example.demonstrateAuthorization();

        System.out.println("\n--- Authenticating user ---");
        example.authenticate("user", "user123");
        example.demonstrateAuthorization();

        System.out.println("\n--- Authenticating with wrong password ---");
        example.authenticate("admin", "wrongpassword");

        SecurityContextHolder.clearContext();
        System.out.println("\n--- After clearing context ---");
        System.out.println("Current user: " + example.getCurrentUsername());
    }
}
