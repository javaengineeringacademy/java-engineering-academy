package com.javaacademy.sprint1.controlflow;

/**
 * SwitchStatement - Demonstrates switch statement and switch expressions (Java 12+).
 * 
 * <p><b>Switch Statement (Traditional):</b>
 * <ul>
 *   <li>Fall-through behavior (break required)</li>
 *   <li>Works with: byte, short, char, int, String, enum</li>
 *   <li>Case values must be compile-time constants</li>
 * </ul>
 * 
 * <p><b>Switch Expression (Java 12+, Standard in Java 14):</b>
 * <ul>
 *   <li>Returns a value (expression, not statement)</li>
 *   <li>Arrow syntax ({@code ->}): no fall-through, no break needed</li>
 *   <li>Yield keyword for block bodies</li>
 *   <li>Exhaustiveness checked by compiler</li>
 * </ul>
 * 
 * <p><b>Real-world analogy:</b> Like a multi-way intersection with signs - 
 * each road (case) leads to a destination. Switch expression = GPS that gives you the route.
 * 
 * <p><b>Best Practice:</b> Prefer switch expressions for value returns; 
 * use enum for type-safe switching.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 * @since 1.0
 */
public final class SwitchStatement {

    private SwitchStatement() {
        throw new UnsupportedOperationException("Utility class");
    }

    enum Day { MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY }
    enum Status { PENDING, APPROVED, REJECTED, CANCELLED }

    public static void main(String[] args) {
        int dayNumber = 3;
        String dayName = "Wednesday";
        Day dayEnum = Day.WEDNESDAY;
        Status status = Status.APPROVED;

        System.out.println("=== Switch Statement (Traditional) ===\n");

        // Traditional switch with fall-through
        System.out.println("--- Traditional Switch (int) ---");
        switch (dayNumber) {
            case 1:
                System.out.println("Monday");
                break;
            case 2:
                System.out.println("Tuesday");
                break;
            case 3:
                System.out.println("Wednesday");
                break;
            case 4:
                System.out.println("Thursday");
                break;
            case 5:
                System.out.println("Friday");
                break;
            case 6:
            case 7: // Multiple cases, same outcome
                System.out.println("Weekend");
                break;
            default:
                System.out.println("Invalid day");
        }

        // Switch with String
        System.out.println("\n--- Switch with String ---");
        switch (dayName) {
            case "Monday":
            case "Tuesday":
            case "Wednesday":
            case "Thursday":
            case "Friday":
                System.out.println("Weekday");
                break;
            case "Saturday":
            case "Sunday":
                System.out.println("Weekend");
                break;
            default:
                System.out.println("Unknown day");
        }

        System.out.println("\n=== Switch Expression (Java 12+) ===\n");

        // Switch expression with arrow syntax (no fall-through)
        System.out.println("--- Switch Expression (Arrow) ---");
        String dayType = switch (dayNumber) {
            case 1, 2, 3, 4, 5 -> "Weekday";
            case 6, 7 -> "Weekend";
            default -> "Invalid";
        };
        System.out.println("Day " + dayNumber + " is a " + dayType);

        // Switch expression with yield (block body)
        System.out.println("\n--- Switch Expression (yield) ---");
        String description = switch (dayEnum) {
            case MONDAY -> "Start of work week";
            case FRIDAY -> "End of work week";
            case SATURDAY, SUNDAY -> {
                String msg = "Weekend!";
                yield msg; // yield returns value from block
            }
            default -> "Midweek day";
        };
        System.out.println(dayEnum + ": " + description);

        // Switch expression with enum (exhaustive - no default needed if all cases covered)
        System.out.println("\n--- Exhaustive Enum Switch ---");
        String action = switch (status) {
            case PENDING -> "Waiting for review";
            case APPROVED -> "Proceed with execution";
            case REJECTED -> "Notify applicant";
            case CANCELLED -> "Archive request";
            // No default needed - compiler verifies all enum values covered
        };
        System.out.println("Action: " + action);

        // Pattern Matching for switch (Java 21+ preview)
        System.out.println("\n--- Pattern Matching Switch (Java 21) ---");
        Object obj = "Hello";
        String result = switch (obj) {
            case Integer i -> "Integer: " + i;
            case String s -> "String length: " + s.length();
            case Double d -> "Double: " + d;
            case null -> "Null value";
            default -> "Unknown type: " + obj.getClass().getSimpleName();
        };
        System.out.println(result);

        // Guarded patterns (when clause)
        Object num = 42;
        String classification = switch (num) {
            case Integer i when i > 0 -> "Positive integer: " + i;
            case Integer i when i < 0 -> "Negative integer: " + i;
            case Integer i -> "Zero";
            default -> "Not an integer";
        };
        System.out.println(classification);

        // Expected output shows all switch variants
    }
}