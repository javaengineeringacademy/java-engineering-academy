package academy.javaengineering.patterns.behavioral.templatemethod;

/**
 * Concrete implementation for mining CSV data.
 * Implements the abstract steps specific to CSV processing.
 */
public class CSVDataMiner extends DataMiner {

    @Override
    protected void openFile() {
        System.out.println("Opening CSV file: " + getFileName());
    }

    @Override
    protected void extractData() {
        System.out.println("Extracting data from CSV columns...");
    }

    @Override
    protected void parseData() {
        System.out.println("Parsing CSV rows into objects...");
    }

    @Override
    protected void analyzeData() {
        System.out.println("Analyzing CSV data patterns...");
    }

    @Override
    protected void closeFile() {
        System.out.println("Closing CSV file.");
    }

    @Override
    protected String getFileName() {
        return "data.csv";
    }
}
