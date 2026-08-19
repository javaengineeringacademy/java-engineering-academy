package academy.javaengineering.patterns.enterprise.dto;

/**
 * Data Transfer Object for User — contains only fields safe for
 * external consumption. Hides sensitive fields like password.
 */
public class UserDTO {

    private Long id;
    private String username;
    private String email;
    private String name;
    private boolean active;

    public UserDTO() {}

    public UserDTO(Long id, String username, String email, String name, boolean active) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.name = name;
        this.active = active;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    @Override
    public String toString() {
        return "UserDTO{id=" + id + ", username='" + username + "', email='" + email
                + "', name='" + name + "', active=" + active + "}";
    }
}
