package academy.javaengineering.springsecurity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("SecurityConfig Tests")
class SecurityConfigTest {

    private PasswordEncoder passwordEncoder;
    private SecurityConfig.SecurityConfiguration config;
    private SecurityConfig.HttpSecurityConfigurator configurator;
    private SecurityConfig.UrlPatternMatcher patternMatcher;

    @BeforeEach
    void setUp() {
        passwordEncoder = new BCryptPasswordEncoder();
        config = new SecurityConfig.SecurityConfiguration();
        configurator = new SecurityConfig.HttpSecurityConfigurator(config);
        patternMatcher = new SecurityConfig.UrlPatternMatcher();
    }

    @Test
    @DisplayName("Should configure CSRF settings")
    void testCsrfConfiguration() {
        configurator.configureCsrf(true);
        assertTrue(config.isCsrfEnabled());

        configurator.configureCsrf(false);
        assertFalse(config.isCsrfEnabled());
    }

    @Test
    @DisplayName("Should configure form login")
    void testFormLoginConfiguration() {
        configurator.configureFormLogin("/custom-login", "/dashboard");

        assertTrue(config.isFormLoginEnabled());
        assertEquals("/custom-login", config.getLoginPage());
        assertEquals("/dashboard", config.getDefaultSuccessUrl());
    }

    @Test
    @DisplayName("Should match URL patterns correctly")
    void testUrlPatternMatching() {
        assertTrue(patternMatcher.matches("/public/**", "/public/page"));
        assertTrue(patternMatcher.matches("/public/**", "/public/nested/page"));
        assertFalse(patternMatcher.matches("/public/**", "/private/page"));

        assertFalse(patternMatcher.matches("/*", "/anything"));
        assertFalse(patternMatcher.matches("/specific", "/other"));
    }

    @Test
    @DisplayName("Should identify public URLs")
    void testPublicUrlIdentification() {
        List<String> publicPatterns = List.of("/public/**", "/resources/**", "/api/health");

        assertTrue(patternMatcher.isPublicUrl("/public/page", publicPatterns));
        assertTrue(patternMatcher.isPublicUrl("/resources/css/style.css", publicPatterns));
        assertTrue(patternMatcher.isPublicUrl("/api/health", publicPatterns));
        assertFalse(patternMatcher.isPublicUrl("/admin/dashboard", publicPatterns));
    }

    @Test
    @DisplayName("Should create in-memory user details service")
    void testInMemoryUserDetailsService() {
        var userDetailsServiceConfig = new SecurityConfig.UserDetailsServiceConfig(passwordEncoder);
        var userDetailsService = userDetailsServiceConfig.createInMemoryUserDetailsService();

        assertNotNull(userDetailsService);

        UserDetails user = userDetailsService.loadUserByUsername("user");
        assertNotNull(user);
        assertEquals("user", user.getUsername());
        assertTrue(user.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_USER")));

        UserDetails admin = userDetailsService.loadUserByUsername("admin");
        assertNotNull(admin);
        assertTrue(admin.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));
    }

    @Test
    @DisplayName("Should configure CORS settings")
    void testCorsConfiguration() {
        var corsConfig = new SecurityConfig.CorsConfiguration();
        corsConfig.setAllowedOrigins(List.of("https://example.com"));
        corsConfig.setAllowedMethods(List.of("GET", "POST"));
        corsConfig.setAllowCredentials(false);
        corsConfig.setMaxAge(7200);

        assertEquals(1, corsConfig.getAllowedOrigins().size());
        assertEquals(2, corsConfig.getAllowedMethods().size());
        assertFalse(corsConfig.isAllowCredentials());
        assertEquals(7200, corsConfig.getMaxAge());
    }
}
