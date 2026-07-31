package academy.javaengineering.oop.dependencyinjection;

/**
 * UserService - Interface for user operations.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
public interface UserService {

    void createUser(String name);
    String getUser(String id);
}