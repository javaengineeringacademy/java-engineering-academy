package academy.javaengineering.oop.dependencyinjection;

/**
 * Repository - Interface for data access strategy.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
public interface Repository {

    void save(String entity);
    String findById(String id);
}