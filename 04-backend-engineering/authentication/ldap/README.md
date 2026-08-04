# LDAP Authentication

## Comprehensive Guide to LDAP Integration

LDAP (Lightweight Directory Access Protocol) is used for centralized authentication and directory services.

---

## Table of Contents

1. [LDAP Overview](#ldap-overview)
2. [LDAP Operations](#ldap-operations)
3. [Spring Security LDAP](#spring-security-ldap)
4. [Best Practices](#best-practices)

---

## LDAP Overview

### LDAP Structure

```
dc=example,dc=com (Base DN)
+-- ou=users
|   +-- uid=john.doe
|   |   +-- cn=John Doe
|   |   +-- mail=john@example.com
|   |   +-- userPassword={SSHA}...
|   +-- uid=jane.smith
|       +-- cn=Jane Smith
|       +-- mail=jane@example.com
+-- ou=groups
    +-- cn=developers
    |   +-- member=uid=john.doe,ou=users,dc=example,dc=com
    +-- cn=admins
        +-- member=uid=jane.smith,ou=users,dc=example,dc=com
```

### Common Object Classes

```
person              - Basic person object
inetOrgPerson       - Internet person (extends person)
organizationalUnit  - OU (organizational unit)
groupOfNames        - Group with member attribute
posixAccount        - POSIX account
```

---

## LDAP Operations

### Bind (Authentication)

```java
DirContext ctx = ldapTemplate.getContextSource().getReadWriteContext();
ctx.addToEnvironment(Context.SECURITY_PRINCIPAL,
    "uid=john.doe,ou=users,dc=example,dc=com");
ctx.addToEnvironment(Context.SECURITY_CREDENTIALS, "password123");
```

### Search

```java
LdapQuery query = LdapQueryBuilder.query()
    .base("ou=users")
    .where("uid").is("john.doe");

User user = ldapTemplate.searchForObject(query, userMapper);
```

### Modify

```java
Attributes attrs = new BasicAttributes();
attrs.put("mail", "newemail@example.com");

DirContextOperations context = ldapTemplate.searchForObject(
    query, (ctx, i) -> ctx);
ldapTemplate.modifyAttributes(context, attrs);
```

---

## Spring Security LDAP

### Configuration

```java
@Configuration
@EnableWebSecurity
public class LdapSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorize -> authorize
                .anyRequest().authenticated())
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/dashboard")
                .permitAll())
            .ldapAuthentication(ldap -> ldap
                .userDnPatterns("uid={0},ou=users")
                .groupSearchBase("ou=groups")
                .contextSource()
                    .url("ldap://localhost:389/dc=example,dc=com")
                    .and()
                .passwordCompare()
                    .passwordEncoder(new BCryptPasswordEncoder())
                    .passwordAttribute("userPassword"));

        return http.build();
    }

    @Bean
    public EmbeddedLdapServerFactoryBean embeddedLdapServerFactoryBean() {
        EmbeddedLdapServerFactoryBean factory =
            new EmbeddedLdapServerFactoryBean();
        factory.setBaseDn("dc=example,dc=com");
        factory.setPort(10389);
        return factory;
    }
}
```

### Custom User Context Mapper

```java
@Component
public class CustomUserContextMapper implements UserDetailsService {

    private final LdapTemplate ldapTemplate;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        LdapQuery query = LdapQueryBuilder.query()
            .base("ou=users")
            .where("uid").is(username);

        return ldapTemplate.searchForObject(query, (attrs, ctx) -> {
            String userDn = (String) ctx.getObjectBinding();

            return User.builder()
                .username(username)
                .password((String) attrs.get("userPassword").get())
                .authorities(getAuthorities(userDn))
                .disabled(!isActive(attrs))
                .build();
        });
    }

    private Collection<? extends GrantedAuthority> getAuthorities(String userDn) {
        LdapQuery groupQuery = LdapQueryBuilder.query()
            .base("ou=groups")
            .where("member").is(userDn);

        List<String> groups = ldapTemplate.search(groupQuery,
            (attrs, ctx) -> (String) attrs.get("cn").get());

        return groups.stream()
            .map(g -> new SimpleGrantedAuthority("ROLE_" + g.toUpperCase()))
            .collect(Collectors.toList());
    }
}
```

### User Search Filter

```java
@Bean
public LdapUserSearch userSearch() {
    return new FilterBasedLdapUserSearch(
        "ou=users",
        "(uid={0})",
        ldapContextSource());
}
```

### Group Search

```java
@Bean
public LdapAuthoritiesPopulator authoritiesPopulator() {
    DefaultLdapAuthoritiesPopulator populator =
        new DefaultLdapAuthoritiesPopulator(ldapContextSource, "ou=groups");
    populator.setGroupRoleAttribute("cn");
    populator.setRolePrefix("ROLE_");
    populator.setSearchSubtree(true);
    return populator;
}
```

---

## Best Practices

### 1. Use Connection Pooling

```yaml
spring:
  ldap:
    embedded:
      base-dn: dc=example,dc=com
      port: 10389
    urls: ldap://localhost:389
    base: dc=example,dc=com
    username: cn=admin
    password: admin
```

### 2. Enable SSL/TLS

```yaml
spring:
  ldap:
    urls: ldaps://ldap.example.com:636
    base: dc=example,dc=com
```

### 3. Handle Failures

```java
@Component
public class LdapAuthenticationProvider implements AuthenticationProvider {

    @Override
    public Authentication authenticate(Authentication authentication)
            throws AuthenticationException {
        try {
            // LDAP authentication logic
            return authenticateUser(authentication);
        } catch (AuthenticationException e) {
            throw e;
        } catch (Exception e) {
            throw new BadCredentialsException(
                "LDAP authentication failed", e);
        }
    }
}
```

### 4. Cache User Data

```java
@Cacheable("ldap-users")
public UserDetails loadUserByUsername(String username) {
    return ldapTemplate.searchForObject(query, userMapper);
}
```

### 5. Use Read-Only Replicas

```java
@Bean
public ContextSource readWriteContextSource() {
    LdapContextSource source = new LdapContextSource();
    source.setUrls("ldap://master.example.com:389");
    return source;
}

@Bean
public ContextSource readOnlyContextSource() {
    LdapContextSource source = new LdapContextSource();
    source.setUrls("ldap://replica.example.com:389");
    return source;
}
```

---

## Further Reading

- [Spring Security LDAP](https://docs.spring.io/spring-security/reference/servlet/authentication/passwords/ldap.html)
- [LDAP Documentation](https://ldap.com/)
- [Apache Directory](https://directory.apache.org/)
- [OpenLDAP](https://www.openldap.org/)
