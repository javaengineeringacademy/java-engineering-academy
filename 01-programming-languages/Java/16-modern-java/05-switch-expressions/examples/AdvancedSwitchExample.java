package academy.javaengineering.modern.switchexpressions;

/**
 * Advanced switch expression usage.
 */
public class AdvancedSwitchExample {

    enum Season {
        SPRING, SUMMER, FALL, WINTER
    }

    enum TrafficLight {
        RED, YELLOW, GREEN
    }

    public static void main(String[] args) {
        // Season-based pricing
        System.out.println("=== Season Pricing ===");
        Season season = Season.SUMMER;
        double price = switch (season) {
            case SPRING -> 100.00;
            case SUMMER -> 150.00;
            case FALL -> 120.00;
            case WINTER -> 80.00;
        };
        System.out.println("Season: " + season + ", Price: $" + price);

        // Traffic light
        System.out.println("\n=== Traffic Light ===");
        TrafficLight light = TrafficLight.GREEN;
        String action = switch (light) {
            case RED -> "Stop";
            case YELLOW -> "Prepare to stop";
            case GREEN -> "Go";
        };
        System.out.println("Light: " + light + ", Action: " + action);

        // Complex calculation
        System.out.println("\n=== Grade Calculator ===");
        int score = 85;
        String grade = switch (score / 10) {
            case 10, 9 -> "A";
            case 8 -> "B";
            case 7 -> "C";
            case 6 -> "D";
            default -> "F";
        };
        System.out.println("Score: " + score + ", Grade: " + grade);

        // String processing
        System.out.println("\n=== String Processor ===");
        String input = "  Hello, World!  ";
        String processed = switch (input.trim()) {
            case String s && s.isEmpty() -> "Empty string";
            case String s && s.length() < 5 -> "Short: " + s;
            case String s && s.length() < 10 -> "Medium: " + s;
            case String s -> "Long: " + s.length() + " chars";
            case null -> "Null input";
        };
        System.out.println("Input: '" + input + "'");
        System.out.println("Processed: " + processed);

        // Nested switch
        System.out.println("\n=== Nested Switch ===");
        String type = "string";
        Object value = "Hello";
        String result = switch (type) {
            case "integer" -> switch ((Integer) value) {
                case int i && i > 0 -> "Positive integer: " + i;
                case int i && i < 0 -> "Negative integer: " + i;
                default -> "Zero";
            };
            case "string" -> switch ((String) value) {
                case String s && s.isEmpty() -> "Empty string";
                case String s -> "String: " + s;
                default -> "Invalid string";
            };
            default -> "Unknown type";
        };
        System.out.println("Result: " + result);
    }
}
