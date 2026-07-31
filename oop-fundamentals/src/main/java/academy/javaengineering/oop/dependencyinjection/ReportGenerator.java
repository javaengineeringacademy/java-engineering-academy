package academy.javaengineering.oop.dependencyinjection;

/**
 * ReportGenerator - Demonstrates setter injection.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
public class ReportGenerator {

    private Formatter formatter;

    // Default constructor - no injection required
    public ReportGenerator() {
    }

    // Setter injection - can change dependency at runtime
    public void setFormatter(Formatter formatter) {
        this.formatter = formatter;
    }

    public void generate(String data) {
        if (formatter == null) {
            System.out.println("  No formatter set!");
            return;
        }
        String formatted = formatter.format(data);
        System.out.println("  Report generated (" + formatter.getFileExtension() + "):");
        System.out.println("  " + formatted);
    }
}