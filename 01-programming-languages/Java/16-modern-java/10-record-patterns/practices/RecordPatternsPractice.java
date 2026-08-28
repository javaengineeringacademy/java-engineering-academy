package academy.javaengineering.modern.recordpatterns;

/**
 * Practice exercises for Record Patterns.
 */
public class RecordPatternsPractice {

    /*
     * Exercise 1: Create a Geometry Calculator
     * 
     * Create records for:
     * - Point(x, y)
     * - Circle(center: Point, radius: double)
     * - Rectangle(topLeft: Point, bottomRight: Point)
     * - Triangle(a: Point, b: Point, c: Point)
     * 
     * Write methods using record patterns to:
     * - Calculate area
     * - Calculate perimeter
     * - Find centroid
     */

    /*
     * Exercise 2: Create a JSON Parser
     * 
     * Create records for:
     * - JsonString(value: String)
     * - JsonNumber(value: double)
     * - JsonArray(elements: List<Json>)
     * - JsonObject(entries: Map<String, Json>)
     * 
     * Write a method using record patterns to:
     * - Pretty print JSON
     * - Calculate JSON depth
     * - Extract values by path
     */

    /*
     * Exercise 3: Create a Expression Evaluator
     * 
     * Create records for:
     * - Number(value: double)
     * - Add(left: Expr, right: Expr)
     * - Multiply(left: Expr, right: Expr)
     * - Power(base: Expr, exponent: Expr)
     * 
     * Write a method using record patterns to:
     * - Evaluate expression
     * - Convert to string
     * - Simplify expression
     */

    /*
     * Exercise 4: Create a AST Visitor
     * 
     * Create a sealed interface for a simple AST:
     * - Literal(value: int)
     * - Variable(name: String)
     * - BinaryOp(op: String, left: Expr, right: Expr)
     * 
     * Write methods using record patterns to:
     * - Evaluate expression
     * - Convert to string
     * - Count nodes
     */
}
