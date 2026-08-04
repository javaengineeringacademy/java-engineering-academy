package academy.javaengineering.springsecurity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Authentication Tests")
class AuthenticationTest {

    private AuthenticationExample.CustomUserDetailsService userDetailsService;
    private AuthenticationExample.CustomAuthenticationProvider authProvider;
    private AuthenticationExample.CustomAuthenticationManager authManager;

    @BeforeEach
    void setUp() {
        userDetailsService = new AuthenticationExample.CustomUserDetailsService();
        authProvider = new AuthenticationExample.CustomAuthenticationProvider(userDetailsService);
        authManager = new AuthenticationExample.CustomAuthenticationManager();
        authManager.addProvider(authProvider);
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("Should load user by username from UserDetailsService")
    void testLoadUserByUsername() {
        UserDetails admin = userDetailsService.loadUserByUsername("admin");

        assertNotNull(admin);
        assertEquals("admin", admin.getUsername());
        assertNotNull(admin.getPassword());
        assertFalse(admin.getAuthorities().isEmpty());
    }

    @Test
    @DisplayName("Should throw UsernameNotFoundException for unknown user")
    void testLoadUnknownUser() {
        assertThrows(UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername("unknown"));
    }

    @Test
    @DisplayName("Should authenticate valid credentials")
    void testValidAuthentication() {
        var token = UsernamePasswordAuthenticationToken.unauthenticated("admin", "admin123");
        Authentication result = authProvider.authenticate(token);

        assertNotNull(result);
        assertTrue(result.isAuthenticated());
        assertEquals("admin", result.getName());
        assertFalse(result.getAuthorities().isEmpty());
    }

    @Test
    @DisplayName("Should reject invalid credentials")
    void testInvalidAuthentication() {
        var token = UsernamePasswordAuthenticationToken.unauthenticated("admin", "wrongpassword");
        assertThrows(org.springframework.security.authentication.BadCredentialsException.class,
                () -> authProvider.authenticate(token));
    }

    @Test
    @DisplayName("Should manage SecurityContext correctly")
    void testSecurityContext() {
        AuthenticationExample.SecurityContextDemo.clearContext();
        assertNull(AuthenticationExample.SecurityContextDemo.getAuthentication());
        assertFalse(AuthenticationExample.SecurityContextDemo.isAuthenticated());

        var token = UsernamePasswordAuthenticationToken.unauthenticated("user", "user123");
        Authentication auth = authProvider.authenticate(token);
        AuthenticationExample.SecurityContextDemo.setAuthentication(auth);

        assertTrue(AuthenticationExample.SecurityContextDemo.isAuthenticated());
        assertNotNull(AuthenticationExample.SecurityContextDemo.getAuthentication());
        assertEquals("user", AuthenticationExample.SecurityContextDemo.getAuthentication().getName());
    }

    @Test
    @DisplayName("Should authenticate via AuthenticationManager")
    void testAuthenticationManager() {
        var token = UsernamePasswordAuthenticationToken.unauthenticated("manager", "manager123");
        Authentication result = authManager.authenticate(token);

        assertNotNull(result);
        assertTrue(result.isAuthenticated());
        assertEquals("manager", result.getName());
    }

    @Test
    @DisplayName("Should create tokens via TokenFactory")
    void testTokenFactory() {
        var unauthToken = AuthenticationExample.TokenFactory.createUnauthenticated("test", "pass");
        assertNotNull(unauthToken);
        assertEquals("test", unauthToken.getName());
        assertFalse(unauthToken.isAuthenticated());

        var authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
        var authedToken = AuthenticationExample.TokenFactory.createAuthenticated(
                "test", "pass", authorities);
        assertNotNull(authedToken);
        assertTrue(authedToken.isAuthenticated());
        assertEquals(1, authedToken.getAuthorities().size());
    }
}
