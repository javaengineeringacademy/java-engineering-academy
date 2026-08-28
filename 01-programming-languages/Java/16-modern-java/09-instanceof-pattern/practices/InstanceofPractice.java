package academy.javaengineering.modern.instanceofpattern;

/**
 * Practice exercises for instanceof Pattern Matching.
 */
public class InstanceofPractice {

    /*
     * Exercise 1: Create a Type Converter
     * 
     * Create a method that:
     * - Takes an Object
     * - Uses instanceof pattern matching to convert to appropriate type
     * - Returns the converted value as a String
     * - Handle: Integer, Double, String, Boolean, List
     */

    /*
     * Exercise 2: Create a Shape Analyzer
     * 
     * Create a sealed interface Shape with Circle, Rectangle, Triangle
     * - Use instanceof pattern matching to:
     *   - Calculate area
     *   - Determine if shape is regular
     *   - Classify shape size (small, medium, large)
     */

    /*
     * Exercise 3: Create a Data Validator
     * 
     * Create a method that:
     * - Takes an Object
     * - Uses instanceof pattern matching to validate:
     *   - String: non-null, length > 0, no special characters
     *   - Integer: positive, within range
     *   - List: non-null, contains specific types
     * - Returns validation result
     */

    /*
     * Exercise 4: Create a Response Handler
     * 
     * Create a method that:
     * - Takes an Object (API response)
     * - Uses instanceof pattern matching to:
     *   - Handle success responses
     *   - Handle error responses
     *   - Handle redirect responses
     *   - Handle unknown responses
     */
}
