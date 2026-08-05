package academy.javaengineering.patterns.behavioral.templatemethod;

/**
 * Abstract class implementing the Template Method pattern.
 * Defines the skeleton of the algorithm with abstract steps
 * that subclasses must implement.
 */
public abstract class DataMiner {

    /**
     * Template method defining the algorithm skeleton.
     * Final to prevent subclasses from overriding the algorithm structure.
     */
    public final void mine() {
        openFile();
        extractData();
        parseData();
        analyzeData();
        sendReport();
        closeFile();
    }

    protected abstract void openFile();

    protected abstract void extractData();

    protected abstract void parseData();

    protected abstract void analyzeData();

    protected void sendReport() {
        System.out.println("Sending report...");
    }

    protected abstract void closeFile();

    protected String getFileName() {
        return "default.dat";
    }
}
