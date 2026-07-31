package academy.javaengineering.oop.dependencyinjection;

/**
 * UserServiceImpl - Concrete implementation of UserService.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
public class UserServiceImpl implements UserService {

    @Override
    public void createUser(String name) {
        System.out.println("  [SERVICE] Creating user: " + name);
    }

    @Override
    public String getUser(String id) {
        return "User-" + id;
    }
}