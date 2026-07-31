package academy.javaengineering.oop.dependencyinjection;

/**
 * Formatter - Interface for report formatting strategy.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
public interface Formatter {

    String format(String data);
    String getFileExtension();
}