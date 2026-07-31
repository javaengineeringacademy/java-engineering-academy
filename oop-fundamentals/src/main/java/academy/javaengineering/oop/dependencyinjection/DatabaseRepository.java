package academy.javaengineering.oop.dependencyinjection;

/**
 * DatabaseRepository - Concrete implementation of Repository interface.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
public class DatabaseRepository implements Repository {

    @Override
    public void save(String entity) {
        System.out.println("  [DATABASE] Saving: " + entity);
    }

    @Override
    public String findById(String id) {
        return "Entity from DB: " + id;
    }
}