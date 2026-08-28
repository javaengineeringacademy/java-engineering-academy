package academy.javaengineering.modern.switchexpressions;

/**
 * Solutions for Switch Expressions practice exercises.
 */
public class SwitchExpressionsSolutions {

    // Exercise 1: Roman Numeral Converter
    public static String toRoman(int number) {
        if (number < 1 || number > 10) {
            throw new IllegalArgumentException("Number must be between 1 and 10");
        }
        return switch (number) {
            case 1 -> "I";
            case 2 -> "II";
            case 3 -> "III";
            case 4 -> "IV";
            case 5 -> "V";
            case 6 -> "VI";
            case 7 -> "VII";
            case 8 -> "VIII";
            case 9 -> "IX";
            case 10 -> "X";
            default -> throw new IllegalStateException("Unexpected value: " + number);
        };
    }

    // Exercise 2: Temperature Converter
    public static double convertTemperature(double temperature, String from, String to) {
        return switch (from.toUpperCase() + "_" + to.toUpperCase()) {
            case "C_F" -> temperature * 9 / 5 + 32;
            case "F_C" -> (temperature - 32) * 5 / 9;
            case "C_K" -> temperature + 273.15;
            case "K_C" -> temperature - 273.15;
            case "F_K" -> (temperature - 32) * 5 / 9 + 273.15;
            case "K_F" -> (temperature - 273.15) * 9 / 5 + 32;
            default -> throw new IllegalArgumentException("Invalid conversion: " + from + " to " + to);
        };
    }

    // Exercise 3: Day of Week Activities
    public static String getDayActivity(String day) {
        return switch (day.toLowerCase()) {
            case "monday" -> "Start work week";
            case "tuesday", "wednesday", "thursday" -> "Work days";
            case "friday" -> "End of work week";
            case "saturday" -> "Day off";
            case "sunday" -> "Rest day";
            case null -> "No day specified";
            default -> "Invalid day";
        };
    }

    // Exercise 4: Simple Calculator
    public static String calculate(double a, double b, char operator) {
        return switch (operator) {
            case '+' -> "Result: " + (a + b);
            case '-' -> "Result: " + (a - b);
            case '*' -> "Result: " + (a * b);
            case '/' -> {
                if (b == 0) {
                    yield "Error: Division by zero";
                } else {
                    yield "Result: " + (a / b);
                }
            }
            default -> "Error: Invalid operator '" + operator + "'";
        };
    }

    public static void main(String[] args) {
        // Test Exercise 1
        System.out.println("--- Exercise 1: Roman Numeral Converter ---");
        for (int i = 1; i <= 10; i++) {
            System.out.println(i + " -> " + toRoman(i));
        }

        // Test Exercise 2
        System.out.println("\n--- Exercise 2: Temperature Converter ---");
        System.out.println("100 C to F: " + convertTemperature(100, "C", "F") + "°F");
        System.out.println("212 F to C: " + convertTemperature(212, "F", "C") + "°C");
        System.out.println("0 C to K: " + convertTemperature(0, "C", "K") + "K");

        // Test Exercise 3
        System.out.println("\n--- Exercise 3: Day of Week Activities ---");
        String[] days = {"Monday", "Tuesday", "Friday", "Saturday", "Sunday"};
        for (String day : days) {
            System.out.println(day + ": " + getDayActivity(day));
        }

        // Test Exercise 4
        System.out.println("\n--- Exercise 4: Simple Calculator ---");
        System.out.println("10 + 5: " + calculate(10, 5, '+'));
        System.out.println("10 - 5: " + calculate(10, 5, '-'));
        System.out.println("10 * 5: " + calculate(10, 5, '*'));
        System.out.println("10 / 5: " + calculate(10, 5, '/'));
        System.out.println("10 / 0: " + calculate(10, 0, '/'));
        System.out.println("10 % 5: " + calculate(10, 5, '%'));
    }
}
