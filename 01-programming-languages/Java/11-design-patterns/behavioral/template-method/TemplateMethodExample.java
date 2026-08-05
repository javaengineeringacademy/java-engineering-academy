package academy.javaengineering.patterns.behavioral.templatemethod;

/**
 * Real-world example demonstrating the Template Method pattern.
 * Shows different data miners with shared algorithm structure.
 */
public class TemplateMethodExample {

    public static void main(String[] args) {
        System.out.println("=== CSV Data Mining ===");
        DataMiner csvMiner = new CSVDataMiner();
        csvMiner.mine();

        System.out.println("\n=== JSON Data Mining ===");
        DataMiner jsonMiner = new JSONDataMiner();
        jsonMiner.mine();
    }
}
