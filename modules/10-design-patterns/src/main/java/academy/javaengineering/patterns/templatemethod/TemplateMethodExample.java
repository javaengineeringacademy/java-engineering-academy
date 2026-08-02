package academy.javaengineering.patterns.templatemethod;

/**
 * Demonstrates the Template Method design pattern for algorithm structure.
 *
 * <p>The Template Method pattern defines the skeleton of an algorithm in a base
 * class, letting subclasses override specific steps without changing the algorithm's
 * structure.</p>
 *
 * <h3>Key Concepts:</h3>
 * <ul>
 *   <li>Template method defines algorithm skeleton</li>
 *   <li>Abstract methods for subclass implementation</li>
 *   <li>Hook methods with default implementations</li>
 * </ul>
 *
 * @author Java Engineering Academy
 * @since 1.0
 */
public class TemplateMethodExample {

    /**
     * Abstract base class defining the data mining algorithm template.
     */
    public abstract static class DataMiner {
        /**
         * Template method defining the mining algorithm steps.
         */
        public final void mine() {
            openFile();
            extractData();
            parseData();
            analyzeData();
            closeFile();
        }

        private void openFile() {
            System.out.println("Opening file");
        }

        /**
         * Subclasses implement data extraction logic.
         */
        protected abstract void extractData();

        /**
         * Subclasses implement data parsing logic.
         */
        protected abstract void parseData();

        /**
         * Hook method for data analysis (can be overridden).
         */
        protected void analyzeData() {
            System.out.println("Analyzing data");
        }

        private void closeFile() {
            System.out.println("Closing file");
        }
    }

    /**
     * CSV data miner implementation.
     */
    public static class CsvMiner extends DataMiner {
        @Override
        protected void extractData() {
            System.out.println("Extracting CSV data");
        }

        @Override
        protected void parseData() {
            System.out.println("Parsing CSV data");
        }
    }

    /**
     * JSON data miner implementation.
     */
    public static class JsonMiner extends DataMiner {
        @Override
        protected void extractData() {
            System.out.println("Extracting JSON data");
        }

        @Override
        protected void parseData() {
            System.out.println("Parsing JSON data");
        }
    }

    /**
     * Demonstrates template method pattern usage.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        DataMiner miner = new CsvMiner();
        miner.mine();

        System.out.println("---");

        miner = new JsonMiner();
        miner.mine();
    }
}
