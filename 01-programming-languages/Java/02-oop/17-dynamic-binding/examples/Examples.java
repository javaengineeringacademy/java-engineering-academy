package academy.javaengineering.oop.examples;

public class Examples {
    public static void main(String[] args) {
        System.out.println("=== Dynamic Binding ===\n");

        // WHY: Dynamic binding enables runtime polymorphism - JVM finds correct method at execution
        // INTERNAL: invokevirtual/invokeinterface instruction → vtable lookup → execute
        // ENGINEERING: Critical for frameworks, dependency injection, plugin architectures

        Processor[] processors = {
            new DataProcessor("CSV"),
            new DataProcessor("JSON"),
            new NetworkProcessor("HTTP"),
            new DatabaseProcessor("PostgreSQL")
        };

        for (Processor p : processors) {
            // Which process() runs? Determined at runtime by actual object type
            p.process();
            System.out.println("  Type: " + p.getType());
            System.out.println();
        }

        // TRADE-OFF: Dynamic binding adds slight overhead vs static dispatch
        // But enables huge flexibility - most modern frameworks depend on it
    }
}

class Processor {
    protected String type;

    Processor(String type) { this.type = type; }

    public void process() {
        System.out.println("Processing with " + type);
    }

    public String getType() { return type; }
}

class DataProcessor extends Processor {
    DataProcessor(String type) { super(type); }

    @Override
    public void process() {
        System.out.println("Processing data: " + type + " format");
    }
}

class NetworkProcessor extends Processor {
    NetworkProcessor(String type) { super(type); }

    @Override
    public void process() {
        System.out.println("Processing network: " + type + " protocol");
    }
}

class DatabaseProcessor extends Processor {
    DatabaseProcessor(String type) { super(type); }

    @Override
    public void process() {
        System.out.println("Processing database: " + type + " queries");
    }
}
