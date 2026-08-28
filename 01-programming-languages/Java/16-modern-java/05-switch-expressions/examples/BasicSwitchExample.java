package academy.javaengineering.modern.switchexpressions;

/**
 * Basic switch expression examples.
 */
public class BasicSwitchExample {

    enum Day {
        MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
    }

    public static void main(String[] args) {
        // Basic arrow syntax
        String day = "MON";
        String result = switch (day) {
            case "MON" -> "Monday";
            case "TUE" -> "Tuesday";
            case "WED" -> "Wednesday";
            case "THU" -> "Thursday";
            case "FRI" -> "Friday";
            case "SAT", "SUN" -> "Weekend";
            default -> throw new IllegalArgumentException("Invalid day: " + day);
        };
        System.out.println("Arrow syntax: " + result);

        // Multiple values
        int number = 3;
        String category = switch (number) {
            case 1, 2, 3 -> "Low";
            case 4, 5, 6 -> "Medium";
            case 7, 8, 9 -> "High";
            default -> "Invalid";
        };
        System.out.println("Multiple values: " + category);

        // Enum switch
        Day today = Day.WEDNESDAY;
        String dayName = switch (today) {
            case MONDAY -> "Monday";
            case TUESDAY -> "Tuesday";
            case WEDNESDAY -> "Wednesday";
            case THURSDAY -> "Thursday";
            case FRIDAY -> "Friday";
            case SATURDAY, SUNDAY -> "Weekend";
        };
        System.out.println("Enum switch: " + dayName);

        // Colon syntax with yield
        String input = "hello";
        String processed = switch (input) {
            case "hello":
                yield "Hello!";
            case "bye":
                yield "Goodbye!";
            default:
                yield "Unknown: " + input;
        };
        System.out.println("Colon syntax: " + processed);

        // Switch with null
        String nullInput = null;
        String nullResult = switch (nullInput) {
            case "hello" -> "Hello";
            case null -> "Null received";
            default -> "Other";
        };
        System.out.println("Null handling: " + nullResult);

        // Complex switch with yield
        Object obj = "Hello, World!";
        String description = switch (obj) {
            case Integer i -> "Integer: " + i;
            case String s && s.length() > 10 -> "Long string";
            case String s -> "String: " + s;
            case null -> "Null";
            default -> "Other";
        };
        System.out.println("Pattern switch: " + description);
    }
}
