package academy.javaengineering.oop.practices;

/**
 * Practice: Enums in Java
 * Complete the TODO items below. Run main() to verify your solutions.
 *
 * Topics tested:
 * - Defining enums with fields, constructors, and methods
 * - Using enum values in switch statements
 * - Iterating over all enum values
 * - Understanding ordinal() vs name()
 * - Implementing behavior in enum constants
 */
public class Practices {
    public static void main(String[] args) {
        System.out.println("=== Practice: 28-enums ===\n");

        // Test Exercise 1: Day enum with isWeekend()
        Day monday = Day.MONDAY;
        Day saturday = Day.SATURDAY;
        System.out.println("Exercise 1 - Day.isWeekend(): "
            + (!monday.isWeekend() && saturday.isWeekend() ? "PASS" : "FAIL"));

        // Test Exercise 2: getAbbreviation()
        System.out.println("Exercise 2 - getAbbreviation(): "
            + ("Mon".equals(monday.getAbbreviation()) && "Sat".equals(saturday.getAbbreviation()) ? "PASS" : "FAIL"));

        // Test Exercise 3: nextDay() wraps around
        Day sunday = Day.SUNDAY;
        Day nextDay = sunday.nextDay();
        System.out.println("Exercise 3 - nextDay(): "
            + (nextDay == Day.MONDAY ? "PASS" : "FAIL"));

        // Test Exercise 4: fromString() static method
        Day parsed = Day.fromString("WEDNESDAY");
        Day invalid = Day.fromString("INVALID");
        System.out.println("Exercise 4 - fromString(): "
            + (parsed == Day.WEDNESDAY && invalid == null ? "PASS" : "FAIL"));

        // Test Exercise 5: values() iteration
        Day[] allDays = Day.values();
        System.out.println("Exercise 5 - values(): "
            + (allDays.length == 7 ? "PASS" : "FAIL"));
    }
}

/**
 * TODO 1: Complete the Day enum with:
 * - Fields: abbreviation (String)
 * - Constructor that takes the abbreviation
 * - isWeekend() method returning true for SATURDAY and SUNDAY
 * - getAbbreviation() returning the abbreviation
 * - nextDay() returning the next day (SUNDAY wraps to MONDAY)
 * - static fromString(String name) returning the Day or null if invalid
 */
enum Day {
    // YOUR CODE HERE: define constants with abbreviations
    // MONDAY("Mon"), TUESDAY("Tue"), ...

    // YOUR CODE HERE: fields, constructor, methods
}
