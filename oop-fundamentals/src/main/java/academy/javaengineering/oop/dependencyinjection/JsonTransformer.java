package academy.javaengineering.oop.dependencyinjection;

/**
 * JsonTransformer - Concrete implementation of Transformer interface.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
public class JsonTransformer implements Transformer {

    @Override
    public String transform(String data) {
        return "{\"data\": \"" + data + "\"}";
    }
}