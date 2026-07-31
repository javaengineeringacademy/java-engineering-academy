package academy.javaengineering.oop.dependencyinjection;

/**
 * Transformer - Interface for data transformation strategy.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
public interface Transformer {

    String transform(String data);
}