package academy.javaengineering.modern.records;

import java.util.List;
import java.util.Objects;

/**
 * Solutions for Records practice exercises.
 */
public class RecordsSolutions {

    // Exercise 1: Temperature Record
    public record Temperature(double value, String unit) {
        public Temperature {
            if (Double.isNaN(value)) throw new IllegalArgumentException("Value cannot be NaN");
            Objects.requireNonNull(unit, "Unit cannot be null");
            if (!List.of("C", "F", "K").contains(unit)) {
                throw new IllegalArgumentException("Invalid unit: " + unit);
            }
        }

        public double toCelsius() {
            return switch (unit) {
                case "C" -> value;
                case "F" -> (value - 32) * 5 / 9;
                case "K" -> value - 273.15;
                default -> throw new IllegalStateException("Unexpected unit: " + unit);
            };
        }

        public double toFahrenheit() {
            double celsius = toCelsius();
            return celsius * 9 / 5 + 32;
        }

        public boolean isFreezing() {
            return toCelsius() < 0;
        }
    }

    // Exercise 2: Rectangle Record
    public record Rectangle(double width, double height) {
        public Rectangle {
            if (width <= 0) throw new IllegalArgumentException("Width must be positive");
            if (height <= 0) throw new IllegalArgumentException("Height must be positive");
        }

        public double area() {
            return width * height;
        }

        public double perimeter() {
            return 2 * (width + height);
        }

        public boolean isSquare() {
            return Double.compare(width, height) == 0;
        }

        public Rectangle scale(double factor) {
            if (factor <= 0) throw new IllegalArgumentException("Factor must be positive");
            return new Rectangle(width * factor, height * factor);
        }
    }

    // Exercise 3: Color Record
    public record Color(int red, int green, int blue) {
        public Color {
            if (red < 0 || red > 255) throw new IllegalArgumentException("Red must be 0-255");
            if (green < 0 || green > 255) throw new IllegalArgumentException("Green must be 0-255");
            if (blue < 0 || blue > 255) throw new IllegalArgumentException("Blue must be 0-255");
        }

        public String toHex() {
            return String.format("#%02X%02X%02X", red, green, blue);
        }

        public double brightness() {
            return 0.299 * red + 0.587 * green + 0.114 * blue;
        }

        public boolean isGrayscale() {
            return red == green && green == blue;
        }
    }

    // Exercise 4: Student Record
    public record Student(String name, List<Integer> grades) {
        public Student {
            Objects.requireNonNull(name, "Name cannot be null");
            Objects.requireNonNull(grades, "Grades cannot be null");
        }

        public double average() {
            return grades.stream()
                .mapToInt(Integer::intValue)
                .average()
                .orElse(0);
        }

        public int highest() {
            return grades.stream()
                .mapToInt(Integer::intValue)
                .max()
                .orElse(0);
        }

        public int lowest() {
            return grades.stream()
                .mapToInt(Integer::intValue)
                .min()
                .orElse(0);
        }

        public boolean isPassing() {
            return average() >= 60;
        }
    }

    public static void main(String[] args) {
        // Test Temperature
        System.out.println("--- Temperature ---");
        var temp = new Temperature(100, "F");
        System.out.println(temp + " = " + temp.toCelsius() + "°C");
        System.out.println("Is freezing: " + temp.isFreezing());

        // Test Rectangle
        System.out.println("\n--- Rectangle ---");
        var rect = new Rectangle(5, 10);
        System.out.println(rect);
        System.out.println("Area: " + rect.area());
        System.out.println("Perimeter: " + rect.perimeter());
        System.out.println("Is square: " + rect.isSquare());
        System.out.println("Scaled: " + rect.scale(2));

        // Test Color
        System.out.println("\n--- Color ---");
        var color = new Color(255, 128, 0);
        System.out.println(color);
        System.out.println("Hex: " + color.toHex());
        System.out.println("Brightness: " + color.brightness());
        System.out.println("Is grayscale: " + color.isGrayscale());

        // Test Student
        System.out.println("\n--- Student ---");
        var student = new Student("Alice", List.of(85, 92, 78, 95));
        System.out.println(student);
        System.out.println("Average: " + student.average());
        System.out.println("Highest: " + student.highest());
        System.out.println("Lowest: " + student.lowest());
        System.out.println("Is passing: " + student.isPassing());
    }
}
