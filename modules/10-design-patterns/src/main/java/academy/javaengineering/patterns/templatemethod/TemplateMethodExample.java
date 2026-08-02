package academy.javaengineering.patterns.templatemethod;

public class TemplateMethodExample {

    public abstract static class DataMiner {
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

        protected abstract void extractData();

        protected abstract void parseData();

        protected void analyzeData() {
            System.out.println("Analyzing data");
        }

        private void closeFile() {
            System.out.println("Closing file");
        }
    }

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

    public static void main(String[] args) {
        DataMiner miner = new CsvMiner();
        miner.mine();

        System.out.println("---");

        miner = new JsonMiner();
        miner.mine();
    }
}
