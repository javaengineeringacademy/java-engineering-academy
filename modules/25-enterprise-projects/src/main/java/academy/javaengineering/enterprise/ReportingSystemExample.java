package academy.javaengineering.enterprise;

import java.util.Map;

public class ReportingSystemExample {
    public static void main(String[] args) {
        System.out.println("=== Reporting System Examples ===\n");
        demonstrateETL();
        demonstrateComponents();
    }

    public static void demonstrateETL() {
        System.out.println("--- ETL Pipeline ---");
        Map<String, String> etl = Map.of(
            "Extract", "Pull data from sources",
            "Transform", "Clean and aggregate",
            "Load", "Store in target system"
        );
        etl.forEach((k, v) -> System.out.printf("  %-12s - %s%n", k, v));
        System.out.println();
    }

    public static void demonstrateComponents() {
        System.out.println("--- Reporting Components ---");
        Map<String, String> components = Map.of(
            "Data Warehouse", "Central analytics repository",
            "ETL Pipeline", "Data processing",
            "OLAP Cube", "Multi-dimensional analysis",
            "BI Tools", "Visualization and dashboards"
        );
        components.forEach((k, v) -> System.out.printf("  %-18s - %s%n", k, v));
        System.out.println();
    }
}
