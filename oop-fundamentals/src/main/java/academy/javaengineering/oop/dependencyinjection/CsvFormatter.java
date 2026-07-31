package academy.javaengineering.oop.dependencyinjection;

/**
 * CsvFormatter - Concrete implementation of Formatter interface.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
public class CsvFormatter implements Formatter {

    @Override
    public String format(String data) {
        return "col1,col2,col3\n" + data;
    }

    @Override
    public String getFileExtension() {
        return ".csv";
    }
}