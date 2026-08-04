package academy.javaengineering.springsecurity;

import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;
import org.springframework.security.web.header.writers.XXssProtectionHeaderWriter;

import java.util.List;

/**
 * Demonstrates Spring Security configuration including SecurityFilterChain,
 * HTTP security, CORS, CSRF, session management, and method security.
 */
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    // Security configuration holder for demonstration
    public static class SecurityConfiguration {
        private boolean csrfEnabled = true;
        private boolean formLoginEnabled = true;
        private boolean httpBasicEnabled = false;
        private String loginPage = "/login";
        private String defaultSuccessUrl = "/";
        private List<String> permittedUrls = List.of("/public/**", "/resources/**");
        private String sessionCreationPolicy = "IF_REQUIRED";
        private boolean rememberMeEnabled = false;

        public boolean isCsrfEnabled() {
            return csrfEnabled;
        }

        public void setCsrfEnabled(boolean csrfEnabled) {
            this.csrfEnabled = csrfEnabled;
        }

        public boolean isFormLoginEnabled() {
            return formLoginEnabled;
        }

        public void setFormLoginEnabled(boolean formLoginEnabled) {
            this.formLoginEnabled = formLoginEnabled;
        }

        public boolean isHttpBasicEnabled() {
            return httpBasicEnabled;
        }

        public void setHttpBasicEnabled(boolean httpBasicEnabled) {
            this.httpBasicEnabled = httpBasicEnabled;
        }

        public String getLoginPage() {
            return loginPage;
        }

        public void setLoginPage(String loginPage) {
            this.loginPage = loginPage;
        }

        public String getDefaultSuccessUrl() {
            return defaultSuccessUrl;
        }

        public void setDefaultSuccessUrl(String defaultSuccessUrl) {
            this.defaultSuccessUrl = defaultSuccessUrl;
        }

        public List<String> getPermittedUrls() {
            return permittedUrls;
        }

        public void setPermittedUrls(List<String> permittedUrls) {
            this.permittedUrls = permittedUrls;
        }

        public String getSessionCreationPolicy() {
            return sessionCreationPolicy;
        }

        public void setSessionCreationPolicy(String sessionCreationPolicy) {
            this.sessionCreationPolicy = sessionCreationPolicy;
        }

        public boolean isRememberMeEnabled() {
            return rememberMeEnabled;
        }

        public void setRememberMeEnabled(boolean rememberMeEnabled) {
            this.rememberMeEnabled = rememberMeEnabled;
        }
    }

    // HTTP security configurator
    public static class HttpSecurityConfigurator {
        private final SecurityConfiguration config;

        public HttpSecurityConfigurator(SecurityConfiguration config) {
            this.config = config;
        }

        public void configureCsrf(boolean enabled) {
            config.setCsrfEnabled(enabled);
            System.out.println("CSRF protection: " + (enabled ? "enabled" : "disabled"));
        }

        public void configureFormLogin(String loginPage, String successUrl) {
            config.setLoginPage(loginPage);
            config.setDefaultSuccessUrl(successUrl);
            config.setFormLoginEnabled(true);
            System.out.println("Form login configured: " + loginPage + " -> " + successUrl);
        }

        public void configureHttpBasic(boolean enabled) {
            config.setHttpBasicEnabled(enabled);
            System.out.println("HTTP Basic: " + (enabled ? "enabled" : "disabled"));
        }

        public void configureSessionManagement(String policy) {
            config.setSessionCreationPolicy(policy);
            System.out.println("Session policy: " + policy);
        }

        public void configureRememberMe(boolean enabled) {
            config.setRememberMeEnabled(enabled);
            System.out.println("Remember me: " + (enabled ? "enabled" : "disabled"));
        }

        public void permitPublicUrls(String... urls) {
            config.setPermittedUrls(List.of(urls));
            System.out.println("Permitted URLs: " + List.of(urls));
        }
    }

    // CORS configuration holder
    public static class CorsConfiguration {
        private List<String> allowedOrigins = List.of("http://localhost:3000");
        private List<String> allowedMethods = List.of("GET", "POST", "PUT", "DELETE");
        private List<String> allowedHeaders = List.of("*");
        private boolean allowCredentials = true;
        private long maxAge = 3600;

        public List<String> getAllowedOrigins() {
            return allowedOrigins;
        }

        public void setAllowedOrigins(List<String> allowedOrigins) {
            this.allowedOrigins = allowedOrigins;
        }

        public List<String> getAllowedMethods() {
            return allowedMethods;
        }

        public void setAllowedMethods(List<String> allowedMethods) {
            this.allowedMethods = allowedMethods;
        }

        public List<String> getAllowedHeaders() {
            return allowedHeaders;
        }

        public void setAllowedHeaders(List<String> allowedHeaders) {
            this.allowedHeaders = allowedHeaders;
        }

        public boolean isAllowCredentials() {
            return allowCredentials;
        }

        public void setAllowCredentials(boolean allowCredentials) {
            this.allowCredentials = allowCredentials;
        }

        public long getMaxAge() {
            return maxAge;
        }

        public void setMaxAge(long maxAge) {
            this.maxAge = maxAge;
        }
    }

    // Security headers configuration
    public static class SecurityHeaders {
        private boolean xssProtection = true;
        private boolean contentTypeOptions = true;
        private boolean frameOptions = true;
        private String frameOptionsValue = "DENY";
        private boolean httpStrictTransportSecurity = true;
        private String referrerPolicy = "strict-origin-when-cross-origin";
        private boolean contentSecurityPolicy = false;
        private String contentSecurityPolicyValue = "default-src 'self'";

        public boolean isXssProtection() {
            return xssProtection;
        }

        public void setXssProtection(boolean xssProtection) {
            this.xssProtection = xssProtection;
        }

        public boolean isContentTypeOptions() {
            return contentTypeOptions;
        }

        public void setContentTypeOptions(boolean contentTypeOptions) {
            this.contentTypeOptions = contentTypeOptions;
        }

        public boolean isFrameOptions() {
            return frameOptions;
        }

        public void setFrameOptions(boolean frameOptions) {
            this.frameOptions = frameOptions;
        }

        public String getFrameOptionsValue() {
            return frameOptionsValue;
        }

        public void setFrameOptionsValue(String frameOptionsValue) {
            this.frameOptionsValue = frameOptionsValue;
        }

        public boolean isHttpStrictTransportSecurity() {
            return httpStrictTransportSecurity;
        }

        public void setHttpStrictTransportSecurity(boolean httpStrictTransportSecurity) {
            this.httpStrictTransportSecurity = httpStrictTransportSecurity;
        }

        public String getReferrerPolicy() {
            return referrerPolicy;
        }

        public void setReferrerPolicy(String referrerPolicy) {
            this.referrerPolicy = referrerPolicy;
        }

        public boolean isContentSecurityPolicy() {
            return contentSecurityPolicy;
        }

        public void setContentSecurityPolicy(boolean contentSecurityPolicy) {
            this.contentSecurityPolicy = contentSecurityPolicy;
        }

        public String getContentSecurityPolicyValue() {
            return contentSecurityPolicyValue;
        }

        public void setContentSecurityPolicyValue(String contentSecurityPolicyValue) {
            this.contentSecurityPolicyValue = contentSecurityPolicyValue;
        }
    }

    // URL pattern matcher for authorization rules
    public static class UrlPatternMatcher {
        public boolean matches(String pattern, String url) {
            if (pattern.endsWith("/**")) {
                String prefix = pattern.substring(0, pattern.length() - 3);
                return url.startsWith(prefix);
            }
            if (pattern.endsWith("/*")) {
                String prefix = pattern.substring(0, pattern.length() - 2);
                int lastSlash = url.lastIndexOf('/');
                return url.substring(0, lastSlash + 1).equals(prefix);
            }
            return pattern.equals(url);
        }

        public boolean isPublicUrl(String url, List<String> publicPatterns) {
            return publicPatterns.stream().anyMatch(pattern -> matches(pattern, url));
        }
    }

    // UserDetailsService configuration helper
    public static class UserDetailsServiceConfig {
        private final PasswordEncoder passwordEncoder;

        public UserDetailsServiceConfig(PasswordEncoder passwordEncoder) {
            this.passwordEncoder = passwordEncoder;
        }

        public UserDetailsService createInMemoryUserDetailsService() {
            var user1 = User.builder()
                    .username("user")
                    .password(passwordEncoder.encode("password"))
                    .roles("USER")
                    .build();

            var user2 = User.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("admin"))
                    .roles("USER", "ADMIN")
                    .build();

            var user3 = User.builder()
                    .username("manager")
                    .password(passwordEncoder.encode("manager"))
                    .roles("USER", "MANAGER")
                    .build();

            return new InMemoryUserDetailsManager(user1, user2, user3);
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Spring Security Configuration Examples ===\n");

        // Demo 1: Security Configuration
        System.out.println("--- Demo 1: Security Configuration ---");
        var config = new SecurityConfiguration();
        var configurator = new HttpSecurityConfigurator(config);

        configurator.configureCsrf(true);
        configurator.configureFormLogin("/login", "/dashboard");
        configurator.configureSessionManagement("IF_REQUIRED");
        configurator.permitPublicUrls("/public/**", "/api/health");

        System.out.println("CSRF enabled: " + config.isCsrfEnabled());
        System.out.println("Login page: " + config.getLoginPage());
        System.out.println("Session policy: " + config.getSessionCreationPolicy());

        // Demo 2: CORS Configuration
        System.out.println("\n--- Demo 2: CORS Configuration ---");
        var corsConfig = new CorsConfiguration();
        corsConfig.setAllowedOrigins(List.of("http://localhost:3000", "https://example.com"));
        corsConfig.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        corsConfig.setAllowCredentials(true);
        corsConfig.setMaxAge(7200);

        System.out.println("Allowed origins: " + corsConfig.getAllowedOrigins());
        System.out.println("Allowed methods: " + corsConfig.getAllowedMethods());
        System.out.println("Credentials allowed: " + corsConfig.isAllowCredentials());
        System.out.println("Max age: " + corsConfig.getMaxAge() + " seconds");

        // Demo 3: Security Headers
        System.out.println("\n--- Demo 3: Security Headers ---");
        var headers = new SecurityHeaders();
        headers.setXssProtection(true);
        headers.setContentTypeOptions(true);
        headers.setFrameOptionsValue("DENY");
        headers.setHttpStrictTransportSecurity(true);
        headers.setReferrerPolicy("strict-origin-when-cross-origin");

        System.out.println("XSS Protection: " + headers.isXssProtection());
        System.out.println("Content Type Options: " + headers.isContentTypeOptions());
        System.out.println("Frame Options: " + headers.getFrameOptionsValue());
        System.out.println("HSTS: " + headers.isHttpStrictTransportSecurity());
        System.out.println("Referrer Policy: " + headers.getReferrerPolicy());

        // Demo 4: URL Pattern Matching
        System.out.println("\n--- Demo 4: URL Pattern Matching ---");
        var patternMatcher = new UrlPatternMatcher();
        var publicPatterns = List.of("/public/**", "/resources/**", "/api/health");

        String[] testUrls = {"/public/page", "/resources/css/style.css",
                "/api/health", "/admin/dashboard", "/private/settings"};
        for (String url : testUrls) {
            boolean isPublic = patternMatcher.isPublicUrl(url, publicPatterns);
            System.out.println(url + " is public: " + isPublic);
        }

        // Demo 5: UserDetailsService Configuration
        System.out.println("\n--- Demo 5: UserDetailsService ---");
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        var userDetailsServiceConfig = new UserDetailsServiceConfig(encoder);
        var userDetailsService = userDetailsServiceConfig.createInMemoryUserDetailsService();

        var user1 = userDetailsService.loadUserByUsername("user");
        var admin = userDetailsService.loadUserByUsername("admin");
        System.out.println("User roles: " + user1.getAuthorities());
        System.out.println("Admin roles: " + admin.getAuthorities());

        // Demo 6: Session Management
        System.out.println("\n--- Demo 6: Session Management ---");
        String[] policies = {"IF_REQUIRED", "STATELESS", "ALWAYS", "NEVER"};
        for (String policy : policies) {
            config.setSessionCreationPolicy(policy);
            System.out.println("Session policy set to: " + config.getSessionCreationPolicy());
        }

        // Demo 7: Security Filter Chain Summary
        System.out.println("\n--- Demo 7: Security Filter Chain Summary ---");
        System.out.println("Configuration Summary:");
        System.out.println("  CSRF: " + (config.isCsrfEnabled() ? "enabled" : "disabled"));
        System.out.println("  Form Login: " + config.getLoginPage());
        System.out.println("  HTTP Basic: " + (config.isHttpBasicEnabled() ? "enabled" : "disabled"));
        System.out.println("  Session: " + config.getSessionCreationPolicy());
        System.out.println("  Remember Me: " + (config.isRememberMeEnabled() ? "enabled" : "disabled"));
        System.out.println("  Permitted URLs: " + config.getPermittedUrls());

        System.out.println("\n=== All demos completed successfully ===");
    }
}
