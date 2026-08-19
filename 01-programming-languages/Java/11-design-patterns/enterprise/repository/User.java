package academy.javaengineering.patterns.enterprise.repository;

/**
 * User domain entity representing a registered user in the system.
 */
public class User extends Entity {

    private String email;
    private String name;

    public User() {}

    public User(String email, String name) {
        this.email = email;
        this.name = name;
    }

    public User(Long id, String email, String name) {
        super(id);
        this.email = email;
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "User{id=" + id + ", email='" + email + "', name='" + name + "'}";
    }
}
