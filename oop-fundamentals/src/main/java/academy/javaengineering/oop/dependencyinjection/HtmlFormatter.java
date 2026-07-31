package academy.javaengineering.oop.dependencyinjection;

/**
 * HtmlFormatter - Concrete implementation of Formatter interface.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
public class HtmlFormatter implements Formatter {

    @Override
    public String format(String data) {
        return "<html><body>" + data + "</body></html>";
    }

    @Override
    public String getFileExtension() {
        return ".html";
    }
}