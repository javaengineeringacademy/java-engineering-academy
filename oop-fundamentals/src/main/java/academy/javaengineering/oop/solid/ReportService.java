package academy.javaengineering.oop.solid;

/**
 * ReportService - Single Responsibility: Only handles report generation.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
public class ReportService {

    public void generateSalesReport() {
        System.out.println("  [REPORT] Generating sales report...");
        System.out.println("  [REPORT] Report generated successfully!");
    }

    public void generateInventoryReport() {
        System.out.println("  [REPORT] Generating inventory report...");
    }
}