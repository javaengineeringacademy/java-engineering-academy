package academy.javaengineering.patterns.enterprise.dto;

/**
 * Domain entity representing a user with all internal fields,
 * including sensitive data that should not be exposed via APIs.
 */
public class User {

    private Long id;
    private String username;
    private String password;
    private String email;
    private String name;
    private boolean active;

    public User() {}

    public User(Long id, String username, String password, String email, String name, boolean active) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.name = name;
        this.active = active;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    @Override
    public String toString() {
        return "User{id=" + id + ", username='" + username + "', name='" + name + "'}";
    }
}
