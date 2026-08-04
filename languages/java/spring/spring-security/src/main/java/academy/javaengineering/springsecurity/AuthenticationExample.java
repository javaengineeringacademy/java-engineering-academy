package academy.javaengineering.springsecurity;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Demonstrates Spring Security authentication concepts including
 * AuthenticationManager, AuthenticationProvider, SecurityContext, and tokens.
 */
public class AuthenticationExample {

    // Simple user record for demonstration
    public record AppUser(String username, String password, List<String> roles) {
        public AppUser {
            Objects.requireNonNull(username, "Username cannot be null");
            Objects.requireNonNull(password, "Password cannot be null");
            Objects.requireNonNull(roles, "Roles cannot be null");
        }
    }

    // Custom UserDetailsService implementation
    public static class CustomUserDetailsService implements UserDetailsService {
        private final List<AppUser> users = new ArrayList<>();

        public CustomUserDetailsService() {
            users.add(new AppUser("admin", "admin123", List.of("ROLE_ADMIN", "ROLE_USER")));
            users.add(new AppUser("user", "user123", List.of("ROLE_USER")));
            users.add(new AppUser("manager", "manager123", List.of("ROLE_MANAGER", "ROLE_USER")));
        }

        @Override
        public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
            return users.stream()
                    .filter(u -> u.username().equals(username))
                    .map(u -> User.builder()
                            .username(u.username())
                            .password(u.password())
                            .authorities(u.roles().stream()
                                    .map(SimpleGrantedAuthority::new)
                                    .toList())
                            .build())
                    .findFirst()
                    .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        }

        public void addUser(AppUser user) {
            users.add(user);
        }
    }

    // Custom AuthenticationProvider
    public static class CustomAuthenticationProvider implements AuthenticationProvider {
        private final UserDetailsService userDetailsService;

        public CustomAuthenticationProvider(UserDetailsService userDetailsService) {
            this.userDetailsService = userDetailsService;
        }

        @Override
        public Authentication authenticate(Authentication authentication) throws AuthenticationException {
            String username = authentication.getName();
            String password = authentication.getCredentials().toString();

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (userDetails.getPassword().equals(password)) {
                return new UsernamePasswordAuthenticationToken(
                        userDetails, password, userDetails.getAuthorities());
            }
            throw new BadCredentialsException("Invalid credentials for user: " + username);
        }

        @Override
        public boolean supports(Class<?> authentication) {
            return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
        }
    }

    // Custom AuthenticationManager
    public static class CustomAuthenticationManager implements AuthenticationManager {
        private final List<AuthenticationProvider> providers = new ArrayList<>();

        public void addProvider(AuthenticationProvider provider) {
            providers.add(provider);
        }

        @Override
        public Authentication authenticate(Authentication authentication) throws AuthenticationException {
            for (AuthenticationProvider provider : providers) {
                if (provider.supports(authentication.getClass())) {
                    return provider.authenticate(authentication);
                }
            }
            throw new BadCredentialsException("No authentication provider found");
        }
    }

    // Security context holder demonstration
    public static class SecurityContextDemo {
        public static void setAuthentication(Authentication authentication) {
            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authentication);
            SecurityContextHolder.setContext(context);
        }

        public static Authentication getAuthentication() {
            SecurityContext context = SecurityContextHolder.getContext();
            return context != null ? context.getAuthentication() : null;
        }

        public static void clearContext() {
            SecurityContextHolder.clearContext();
        }

        public static boolean isAuthenticated() {
            Authentication auth = getAuthentication();
            return auth != null && auth.isAuthenticated();
        }
    }

    // Authentication token creation utilities
    public static class TokenFactory {
        public static UsernamePasswordAuthenticationToken createUnauthenticated(
                String username, String password) {
            return new UsernamePasswordAuthenticationToken(username, password);
        }

        public static UsernamePasswordAuthenticationToken createAuthenticated(
                String username, String password, Collection<? extends GrantedAuthority> authorities) {
            return new UsernamePasswordAuthenticationToken(username, password, authorities);
        }

        public static UsernamePasswordAuthenticationToken createWithPrincipal(
                Object principal, Object credentials,
                Collection<? extends GrantedAuthority> authorities) {
            return new UsernamePasswordAuthenticationToken(principal, credentials, authorities);
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Spring Security Authentication Examples ===\n");

        // Demo 1: UserDetailsService
        System.out.println("--- Demo 1: UserDetailsService ---");
        var userDetailsService = new CustomUserDetailsService();
        UserDetails admin = userDetailsService.loadUserByUsername("admin");
        System.out.println("Loaded user: " + admin.getUsername());
        System.out.println("Authorities: " + admin.getAuthorities());
        System.out.println("Account non-locked: " + admin.isAccountNonLocked());

        // Demo 2: AuthenticationProvider
        System.out.println("\n--- Demo 2: AuthenticationProvider ---");
        var provider = new CustomAuthenticationProvider(userDetailsService);
        var authRequest = UsernamePasswordAuthenticationToken.unauthenticated("admin", "admin123");
        System.out.println("Supports token type: " + provider.supports(authRequest.getClass()));

        Authentication authResult = provider.authenticate(authRequest);
        System.out.println("Authenticated: " + authResult.isAuthenticated());
        System.out.println("Principal: " + authResult.getPrincipal());
        System.out.println("Authorities: " + authResult.getAuthorities());

        // Demo 3: AuthenticationManager
        System.out.println("\n--- Demo 3: AuthenticationManager ---");
        var manager = new CustomAuthenticationManager();
        manager.addProvider(provider);
        var userToken = UsernamePasswordAuthenticationToken.unauthenticated("user", "user123");
        Authentication userAuth = manager.authenticate(userToken);
        System.out.println("User authenticated: " + userAuth.isAuthenticated());
        System.out.println("User authorities: " + userAuth.getAuthorities());

        // Demo 4: SecurityContext
        System.out.println("\n--- Demo 4: SecurityContext ---");
        SecurityContextDemo.setAuthentication(userAuth);
        System.out.println("Context authenticated: " + SecurityContextDemo.isAuthenticated());
        Authentication currentAuth = SecurityContextDemo.getAuthentication();
        System.out.println("Current principal: " + currentAuth.getName());
        System.out.println("Current authorities: " + currentAuth.getAuthorities());
        SecurityContextDemo.clearContext();
        System.out.println("After clear - authenticated: " + SecurityContextDemo.isAuthenticated());

        // Demo 5: Token Factory
        System.out.println("\n--- Demo 5: Token Factory ---");
        var unauthToken = TokenFactory.createUnauthenticated("manager", "pass");
        System.out.println("Unauthenticated token: " + unauthToken.getName());
        System.out.println("Token authenticated: " + unauthToken.isAuthenticated());

        var authorities = List.of(new SimpleGrantedAuthority("ROLE_MANAGER"));
        var authedToken = TokenFactory.createAuthenticated("manager", "pass", authorities);
        System.out.println("Authenticated token: " + authedToken.getName());
        System.out.println("Token authorities: " + authedToken.getAuthorities());

        // Demo 6: Invalid credentials
        System.out.println("\n--- Demo 6: Invalid Credentials ---");
        try {
            var badToken = UsernamePasswordAuthenticationToken.unauthenticated("admin", "wrongpassword");
            provider.authenticate(badToken);
        } catch (BadCredentialsException e) {
            System.out.println("Expected error: " + e.getMessage());
        }

        System.out.println("\n=== All demos completed successfully ===");
    }
}
