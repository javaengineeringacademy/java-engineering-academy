package academy.javaengineering.oop.solid;

/**
 * UserService2 - Single Responsibility: Only handles user operations.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
public class UserService2 {

    public void createUser(String name) {
        System.out.println("  [USER SERVICE] Creating user: " + name);
    }

    public String getUser(String id) {
        return "User-" + id;
    }

    public void deleteUser(String id) {
        System.out.println("  [USER SERVICE] Deleting user: " + id);
    }
}