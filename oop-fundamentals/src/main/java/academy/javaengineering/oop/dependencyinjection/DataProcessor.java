package academy.javaengineering.oop.dependencyinjection;

/**
 * DataProcessor - Demonstrates method injection.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
public class DataProcessor {

    public void processData(Transformer transformer, String data) {
        System.out.println("  Original: " + data);
        String transformed = transformer.transform(data);
        System.out.println("  Transformed: " + transformed);
    }
}