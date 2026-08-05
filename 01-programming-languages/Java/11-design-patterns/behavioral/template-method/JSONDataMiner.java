package academy.javaengineering.patterns.behavioral.templatemethod;

/**
 * Concrete implementation for mining JSON data.
 * Implements the abstract steps specific to JSON processing.
 */
public class JSONDataMiner extends DataMiner {

    @Override
    protected void openFile() {
        System.out.println("Opening JSON file: " + getFileName());
    }

    @Override
    protected void extractData() {
        System.out.println("Extracting data from JSON nodes...");
    }

    @Override
    protected void parseData() {
        System.out.println("Parsing JSON into Java objects...");
    }

    @Override
    protected void analyzeData() {
        System.out.println("Analyzing JSON structure...");
    }

    @Override
    protected void closeFile() {
        System.out.println("Closing JSON file.");
    }

    @Override
    protected String getFileName() {
        return "data.json";
    }
}
