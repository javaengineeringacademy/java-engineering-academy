# Spring Security

## Comprehensive Guide to Spring Security

Spring Security is a powerful and highly customizable authentication and access-control framework for Spring applications. This guide covers authentication, authorization, OAuth2, JWT, and method-level security.

---

## Table of Contents

1. [Authentication](#authentication)
2. [Authorization](#authorization)
3. [OAuth2](#oauth2)
4. [JWT](#jwt)
5. [Method Security](#method-security)
6. [Password Encoding](#password-encoding)
7. [Best Practices](#best-practices)

---

## Authentication

### Basic Authentication Setup

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
```

### Form-Based Authentication

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/public/**").permitAll()
                .requestMatchers("/login").permitAll()
                .anyRequest().authenticated())
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/dashboard")
                .failureUrl("/login?error=true")
                .permitAll())
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout=true")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll());
        
        return http.build();
    }
}
```

### Custom UserDetailsService

```java
@Service
public class CustomUserDetailsService implements UserDetailsService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException(
                "User not found: " + username));
        
        return User.builder()
            .username(user.getUsername())
            .password(user.getPassword())
            .authorities(user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList()))
            .accountExpired(false)
            .accountLocked(false)
            .credentialsExpired(false)
            .disabled(false)
            .build();
    }
}
```

### Custom Authentication Provider

```java
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
    
    @Autowired
    private UserDetailsService userDetailsService;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Override
    public Authentication authenticate(Authentication authentication) 
            throws AuthenticationException {
        
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();
        
        UserDetails user = userDetailsService.loadUserByUsername(username);
        
        if (passwordEncoder.matches(password, user.getPassword())) {
            return new UsernamePasswordAuthenticationToken(
                user, password, user.getAuthorities());
        } else {
            throw new BadCredentialsException("Invalid credentials");
        }
    }
    
    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
```

### Remember Me Authentication

```java
http
    .rememberMe(remember -> remember
        .key("uniqueAndSecret")
        .tokenValiditySeconds(86400) // 1 day
        .userDetailsService(userDetailsService));
```

---

## Authorization

### URL-Based Authorization

```java
http
    .authorizeHttpRequests(auth -> auth
        .requestMatchers(HttpMethod.GET, "/api/users").hasRole("USER")
        .requestMatchers(HttpMethod.POST, "/api/users").hasRole("ADMIN")
        .requestMatchers("/api/admin/**").hasRole("ADMIN")
        .requestMatchers("/api/public/**").permitAll()
        .requestMatchers("/actuator/**").hasAnyRole("ADMIN", "ACTUATOR")
        .anyRequest().authenticated());
```

### Custom Authorization Manager

```java
@Component
public class CustomAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {
    
    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication,
                                       RequestAuthorizationContext context) {
        
        HttpServletRequest request = context.getRequest();
        String resourceId = request.getHeader("X-Resource-Id");
        
        Authentication auth = authentication.get();
        
        if (auth == null || !auth.isAuthenticated()) {
            return new AuthorizationDecision(false);
        }
        
        // Custom logic
        boolean authorized = hasAccessToResource(auth, resourceId);
        
        return new AuthorizationDecision(authorized);
    }
    
    private boolean hasAccessToResource(Authentication auth, String resourceId) {
        // Implement custom authorization logic
        return true;
    }
}

// Usage
http
    .authorizeHttpRequests(auth -> auth
        .requestMatchers("/api/resources/**")
            .access(new CustomAuthorizationManager())
        .anyRequest().authenticated());
```

### Role Hierarchy

```java
@Bean
public RoleHierarchy roleHierarchy() {
    return RoleHierarchyImpl.withDefaultRolePrefix()
        .role("ADMIN").implies("USER")
        .role("USER").implies("GUEST")
        .build();
}
```

### Permission-Based Authorization

```java
@Component
public class CustomPermissionEvaluator implements PermissionEvaluator {
    
    @Override
    public boolean hasPermission(Authentication auth, Object targetDomainObject, 
                                 Object permission) {
        if (auth == null || !(targetDomainObject instanceof String)) {
            return false;
        }
        
        String targetType = targetDomainObject.toString().toUpperCase();
        String perm = permission.toString().toUpperCase();
        
        return auth.getAuthorities().stream()
            .anyMatch(g -> g.getAuthority().equals(targetType + "_" + perm));
    }
    
    @Override
    public boolean hasPermission(Authentication auth, Serializable targetId, 
                                 String targetType, Object permission) {
        // Check permission for specific target
        return hasPermission(auth, targetType, permission);
    }
}

// Configuration
http
    .authorizeHttpRequests(auth -> auth
        .anyRequest().authenticated())
    .exceptionHandling(ex -> ex
        .accessDeniedHandler(new AccessDeniedHandlerImpl()));
```

---

## OAuth2

### OAuth2 Client Configuration

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-oauth2-client</artifactId>
</dependency>
```

```yaml
spring:
  security:
    oauth2:
      client:
        registration:
          github:
            client-id: your-client-id
            client-secret: your-client-secret
            scope:
              - read:user
              - user:email
          
          google:
            client-id: your-client-id
            client-secret: your-client-secret
            scope:
              - openid
              - profile
              - email
          
          custom:
            client-id: your-client-id
            client-secret: your-client-secret
            client-authentication-method: client_secret_basic
            authorization-grant-type: authorization_code
            redirect-uri: "{baseUrl}/login/oauth2/code/{registrationId}"
            scope:
              - read
              - write
            client-name: Custom Provider
        
        provider:
          custom:
            authorization-uri: https://auth.example.com/authorize
            token-uri: https://auth.example.com/token
            user-info-uri: https://api.example.com/userinfo
            jwk-set-uri: https://auth.example.com/.well-known/jwks.json
            user-name-attribute: sub
```

### OAuth2 Client Usage

```java
@RestController
public class OAuth2Controller {
    
    @GetMapping("/user")
    public Map<String, Object> user(@AuthenticationPrincipal OAuth2User principal) {
        return Map.of(
            "name", principal.getAttribute("name"),
            "email", principal.getAttribute("email"),
            "authorities", principal.getAuthorities()
        );
    }
    
    @GetMapping("/oauth2/callback")
    public String callback(@RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient client) {
        // Access tokens
        String accessToken = client.getAccessToken().getTokenValue();
        String refreshToken = client.getRefreshToken().getTokenValue();
        
        return "Access Token: " + accessToken;
    }
}
```

### OAuth2 Resource Server

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-oauth2-resource-server</artifactId>
</dependency>
```

```yaml
spring:
  security:
    oauth2:
      resourceserver:
        jwt:
          issuer-uri: https://auth.example.com/
          jwk-set-uri: https://auth.example.com/.well-known/jwks.json
          audiences: https://api.example.com
```

```java
@Configuration
@EnableWebSecurity
public class ResourceServerConfig {
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/public/**").permitAll()
                .requestMatchers("/api/**").authenticated()
                .anyRequest().authenticated())
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt
                    .jwtAuthenticationConverter(jwtAuthenticationConverter())));
        
        return http.build();
    }
    
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = 
            new JwtGrantedAuthoritiesConverter();
        grantedAuthoritiesConverter.setAuthoritiesClaimName("roles");
        grantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");
        
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
        return converter;
    }
}
```

---

## JWT

### JWT Token Service

```java
@Service
public class JwtTokenService {
    
    @Value("${jwt.secret}")
    private String secret;
    
    @Value("${jwt.expiration}")
    private long expiration;
    
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", userDetails.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toList()));
        
        return Jwts.builder()
            .setClaims(claims)
            .setSubject(userDetails.getUsername())
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + expiration))
            .signWith(getSigningKey(), SignatureAlgorithm.HS512)
            .compact();
    }
    
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }
    
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
    
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(getSigningKey())
            .build()
            .parseClaimsJws(token)
            .getBody();
    }
    
    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }
    
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
}
```

### JWT Authentication Filter

```java
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    
    @Autowired
    private JwtTokenService jwtTokenService;
    
    @Autowired
    private UserDetailsService userDetailsService;
    
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;
        
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        
        jwt = authHeader.substring(7);
        userEmail = jwtTokenService.extractUsername(jwt);
        
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
            
            if (jwtTokenService.isTokenValid(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = 
                    new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities());
                
                authToken.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request));
                
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        
        filterChain.doFilter(request, response);
    }
}
```

### JWT Configuration

```java
@Configuration
public class JwtConfig {
    
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
```

---

## Method Security

### Enable Method Security

```java
@Configuration
@EnableMethodSecurity
public class MethodSecurityConfig {
    // Method security is enabled
}
```

### Pre/Post Authorization

```java
@Service
public class UserService {
    
    @PreAuthorize("hasRole('USER')")
    public UserDTO getUser(Long id) {
        // Only users with USER role can access
        return userRepository.findById(id).orElseThrow();
    }
    
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    public UserDTO getUserById(Long id) {
        // Admin or owner can access
        return userRepository.findById(id).orElseThrow();
    }
    
    @PostAuthorize("returnObject.username == authentication.name")
    public UserDTO getUserAfter(Long id) {
        // Check return value
        return userRepository.findById(id).orElseThrow();
    }
    
    @PreFilter("filterObject.owner == authentication.name")
    public void processItems(List<Item> items) {
        // Filter input items
    }
    
    @PostFilter("filterObject.owner == authentication.name")
    public List<Item> getAllItems() {
        // Filter output items
        return itemRepository.findAll();
    }
}
```

### Custom Security Expressions

```java
@Component("securityExpressionRoot")
public class CustomSecurityExpressionRoot {
    
    public boolean hasIpAddress(String ipAddress) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) return false;
        
        // Custom logic
        return true;
    }
    
    public boolean isOwner(Long resourceId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) return false;
        
        // Check ownership
        return true;
    }
}

// Usage
@PreAuthorize("@securityExpressionRoot.isOwner(#id)")
public Resource getResource(Long id) {
    // ...
}
```

### Secure Methods with Annotations

```java
// Using @Secured (simpler)
@Secured("ROLE_ADMIN")
public void adminOnlyMethod() {
    // ...
}

// Using @RolesAllowed (JSR-250)
@RolesAllowed({"ROLE_USER", "ROLE_ADMIN"})
public void userOrAdminMethod() {
    // ...
}
```

---

## Password Encoding

### Password Encoder Options

```java
@Configuration
public class PasswordConfig {
    
    @Bean
    public PasswordEncoder bcryptPasswordEncoder() {
        return new BCryptPasswordEncoder(12); // Strength 12
    }
    
    @Bean
    public PasswordEncoder argon2PasswordEncoder() {
        return Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8();
    }
    
    @Bean
    public PasswordEncoder pbkdf2PasswordEncoder() {
        return Pbkdf2PasswordEncoder.defaultsForSpringSecurity_v5_8();
    }
    
    @Bean
    public DelegatingPasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}

// Usage
@Service
public class UserService {
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    public void createUser(CreateUserRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
    }
    
    public boolean verifyPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
```

---

## Best Practices

### Security Configuration

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // Disable CSRF for REST APIs
            .csrf(csrf -> csrf.disable())
            
            // Enable CORS
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            
            // Session management
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            
            // Exception handling
            .exceptionHandling(ex -> ex
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                .accessDeniedHandler(new CustomAccessDeniedHandler()));
        
        return http.build();
    }
    
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("https://example.com"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
```

### Custom Error Handling

```java
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        
        ErrorResponse error = new ErrorResponse(
            HttpStatus.UNAUTHORIZED.value(),
            "Unauthorized",
            authException.getMessage());
        
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        
        new ObjectMapper().writeValue(response.getOutputStream(), error);
    }
}

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    
    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {
        
        ErrorResponse error = new ErrorResponse(
            HttpStatus.FORBIDDEN.value(),
            "Forbidden",
            accessDeniedException.getMessage());
        
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        
        new ObjectMapper().writeValue(response.getOutputStream(), error);
    }
}
```

### Security Headers

```java
http
    .headers(headers -> headers
        .contentSecurityPolicy(csp -> csp
            .policyDirectives("default-src 'self'"))
        .httpStrictTransportSecurity(hsts -> hsts
            .includeSubDomains(true)
            .maxAgeInSeconds(31536000))
        .contentTypeOptions(Customizer.withDefaults())
        .frameOptions(frame -> frame
            .deny()));
```

---

## Common Pitfalls

### 1. Storing Passwords in Plain Text

```java
// Bad
user.setPassword(request.getPassword());

// Good
user.setPassword(passwordEncoder.encode(request.getPassword()));
```

### 2. Weak Password Validation

```java
// Bad - No validation
public void createUser(String password) {
    user.setPassword(passwordEncoder.encode(password));
}

// Good - Strong validation
public void createUser(String password) {
    if (password.length() < 8) {
        throw new ValidationException("Password must be at least 8 characters");
    }
    if (!password.matches(".*[A-Z].*")) {
        throw new ValidationException("Password must contain uppercase letter");
    }
    user.setPassword(passwordEncoder.encode(password));
}
```

### 3. Exposing Sensitive Data

```java
// Bad
@RestController
public class UserController {
    @GetMapping("/user/{id}")
    public User getUser(@PathVariable Long id) {
        return userRepository.findById(id).orElseThrow(); // Exposes password hash
    }
}

// Good
@RestController
public class UserController {
    @GetMapping("/user/{id}")
    public UserDTO getUser(@PathVariable Long id) {
        User user = userRepository.findById(id).orElseThrow();
        return UserDTO.from(user); // Excludes sensitive fields
    }
}
```

---

## Further Reading

- [Spring Security Official Documentation](https://spring.io/projects/spring-security)
- [Spring Security Reference](https://docs.spring.io/spring-security/reference/)
- [Baeldung Spring Security](https://www.baeldung.com/spring-security)
- [OAuth 2.0 Specification](https://oauth.net/2/)
