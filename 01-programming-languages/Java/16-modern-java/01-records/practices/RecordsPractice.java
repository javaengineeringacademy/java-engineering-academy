package academy.javaengineering.modern.records;

/**
 * Practice exercises for Records.
 */
public class RecordsPractice {

    /*
     * Exercise 1: Create a Temperature Record
     * 
     * Create a record called Temperature with:
     * - value (double)
     * - unit (String) - "C", "F", or "K"
     * 
     * Add validation to ensure:
     * - value is not NaN
     * - unit is one of "C", "F", or "K"
     * 
     * Add methods:
     * - toCelsius() - converts to Celsius
     * - toFahrenheit() - converts to Fahrenheit
     * - isFreezing() - returns true if below 0°C
     */

    /*
     * Exercise 2: Create a Rectangle Record
     * 
     * Create a record called Rectangle with:
     * - width (double)
     * - height (double)
     * 
     * Add validation:
     * - width and height must be positive
     * 
     * Add methods:
     * - area() - returns the area
     * - perimeter() - returns the perimeter
     * - isSquare() - returns true if width equals height
     * - scale(double factor) - returns a new scaled rectangle
     */

    /*
     * Exercise 3: Create a Color Record
     * 
     * Create a record called Color with:
     * - red (int) - 0-255
     * - green (int) - 0-255
     * - blue (int) - 0-255
     * 
     * Add validation:
     * - Each component must be between 0 and 255
     * 
     * Add methods:
     * - toHex() - returns hex string like "#FF0000"
     * - brightness() - returns perceived brightness (0.299*R + 0.587*G + 0.114*B)
     * - isGrayscale() - returns true if R == G == B
     */

    /*
     * Exercise 4: Record with Collections
     * 
     * Create a record called Student with:
     * - name (String)
     * - grades (List<Integer>)
     * 
     * Add methods:
     * - average() - returns average grade
     * - highest() - returns highest grade
     * - lowest() - returns lowest grade
     * - isPassing() - returns true if average >= 60
     */
}
