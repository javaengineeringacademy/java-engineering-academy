package academy.javaengineering.oop.dependencyinjection;

/**
 * FileRepository - Concrete implementation of Repository interface.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
public class FileRepository implements Repository {

    @Override
    public void save(String entity) {
        System.out.println("  [FILE] Saving: " + entity);
    }

    @Override
    public String findById(String id) {
        return "Entity from File: " + id;
    }
}