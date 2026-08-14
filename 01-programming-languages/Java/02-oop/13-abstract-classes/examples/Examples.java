package academy.javaengineering.oop.examples;

public class Examples {
    public static void main(String[] args) {
        System.out.println("=== Abstract Class Patterns ===\n");

        // WHY: Abstract classes provide partial implementation + enforce contract
        // INTERNAL: Cannot be instantiated, provides concrete methods + abstract declarations
        // ENGINEERING: Use when subclasses share significant implementation

        Logger console = new ConsoleLogger();
        Logger file = new FileLogger("/tmp/app.log");

        console.log("Application started");
        file.log("User logged in");
        console.log("Error occurred");
        file.log("Connection timeout");

        // TRADE-OFF: Template Method pattern (abstract class) vs Strategy (interface)
        // Template Method: fixed algorithm skeleton, override steps
        // Strategy: swap entire algorithm at runtime
    }
}

abstract class Logger {
    protected String prefix;

    protected Logger(String prefix) {
        this.prefix = prefix;
    }

    // Template method - defines algorithm skeleton
    public final void log(String message) {
        String formatted = format(message);
        write(formatted);
    }

    // Abstract - subclasses must implement
    protected abstract void write(String message);

    // Concrete - shared implementation
    protected String format(String message) {
        return prefix + " [" + java.time.LocalTime.now() + "] " + message;
    }
}

class ConsoleLogger extends Logger {
    ConsoleLogger() { super("CONSOLE"); }

    @Override
    protected void write(String message) {
        System.out.println(message);
    }
}

class FileLogger extends Logger {
    private final String filePath;

    FileLogger(String filePath) {
        super("FILE");
        this.filePath = filePath;
    }

    @Override
    protected void write(String message) {
        System.out.println("Writing to " + filePath + ": " + message);
    }
}
