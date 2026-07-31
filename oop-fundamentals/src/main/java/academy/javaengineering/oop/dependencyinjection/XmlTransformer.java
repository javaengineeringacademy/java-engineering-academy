package academy.javaengineering.oop.dependencyinjection;

/**
 * XmlTransformer - Concrete implementation of Transformer interface.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
public class XmlTransformer implements Transformer {

    @Override
    public String transform(String data) {
        return "<data>" + data + "</data>";
    }
}