package academy.javaengineering.modern.pattern;

/**
 * Practice exercises for Pattern Matching.
 */
public class PatternMatchingPractice {

    /*
     * Exercise 1: Shape Calculator
     * 
     * Create a sealed interface Shape with:
     * - Circle(radius)
     * - Rectangle(width, height)
     * - Triangle(base, height)
     * 
     * Write methods using pattern matching:
     * - area(Shape) - returns area
     * - perimeter(Shape) - returns perimeter
     * - describe(Shape) - returns description string
     */

    /*
     * Exercise 2: Object Classifier
     * 
     * Write a method that classifies objects using pattern matching:
     * - String: "Short" if length < 5, "Medium" if length < 10, "Long" otherwise
     * - Integer: "Negative", "Zero", "Positive"
     * - List: "Empty", "Single", "Multiple"
     * - null: "Null"
     * - Other: "Unknown"
     */

    /*
     * Exercise 3: Expression Evaluator
     * 
     * Create a sealed interface Expression with:
     * - Number(value)
     * - Add(left, right)
     * - Multiply(left, right)
     * - Power(base, exponent)
     * 
     * Write an eval method using pattern matching
     */

    /*
     * Exercise 4: HTTP Status Handler
     * 
     * Create a sealed interface HttpStatus with:
     * - Success(code, body)
     * - Redirect(url)
     * - ClientError(code, message)
     * - ServerError(code, message)
     * 
     * Write a method that returns appropriate response based on status type
     */
}
