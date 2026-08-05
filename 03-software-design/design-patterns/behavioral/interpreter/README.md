# Interpreter Pattern

## Overview
The Interpreter pattern provides a way to evaluate language grammar or expression in a given language. Given a language, define a representation for its grammar along with an interpreter that uses the representation to interpret sentences in the language.

## Intent
Define a grammatical representation for a language and provide an interpreter with an interface to interpret sentences. Map each grammar rule to a class, making the grammar easy to change and extend.

## When to Use
- Database query languages (SQL, OQL)
- Configuration languages (CSS selectors, XPath)
- Arithmetic expressions evaluation
- Regular expression parsing
- Simple grammars where efficiency is not a concern

## UML Diagram Description
- **AbstractExpression**: Declares an `interpret()` method common to all nodes
- **TerminalExpression**: Implements interpret for terminal symbols (leaf nodes)
- **NonterminalExpression**: Implements interpret for nonterminal symbols (composite nodes)
- **Context**: Contains information global to the interpreter
- **Client**: Builds the abstract syntax tree and invokes interpret

## Java Implementation
```java
public interface Expression {
    boolean interpret(String context);
}

public class TerminalExpression implements Expression {
    private String data;
    public TerminalExpression(String data) { this.data = data; }
    @Override
    public boolean interpret(String context) {
        return context.contains(data);
    }
}

public class OrExpression implements Expression {
    private Expression expr1, expr2;
    public OrExpression(Expression e1, Expression e2) { expr1 = e1; expr2 = e2; }
    @Override
    public boolean interpret(String context) {
        return expr1.interpret(context) || expr2.interpret(context);
    }
}

public class AndExpression implements Expression {
    private Expression expr1, expr2;
    public AndExpression(Expression e1, Expression e2) { expr1 = e1; expr2 = e2; }
    @Override
    public boolean interpret(String context) {
        return expr1.interpret(context) && expr2.interpret(context);
    }
}
```

## Real-World Examples
- SQL Parser: Interprets SQL statements for database queries
- Regular Expressions: Pattern matching using interpreted expressions
- Mathematical Expression Evaluator: Evaluates arithmetic expressions
- Rule Engines: Business rule processing systems

## Advantages and Disadvantages
**Advantages:**
- Easy to implement simple grammars
- Adding new interpretation ways is straightforward
- Each grammar rule is represented as a class
- Grammar changes are localized to specific expression classes

**Disadvantages:**
- Complex grammars become difficult to maintain
- Performance can be slow for complex expressions
- Changes to grammar require modifying all related expression classes
- Not suitable for complex grammars with many rules

## Related Patterns
- **Composite**: Interpreter uses Composite to represent grammar as a tree
- **Visitor**: Can perform operations on expression trees
- **Flyweight**: Shares terminal expressions when multiple occurrences exist

## Best Practices
- Keep grammar simple and focused on a specific domain
- Use Composite pattern to build the expression tree
- Consider performance implications for complex expressions
- Document the grammar clearly before implementing

## Interview Questions
1. What is the Interpreter pattern and when would you use it?
2. How does the Interpreter pattern differ from the Composite pattern?
3. Describe a real-world scenario where the Interpreter pattern is applied.
4. What are the limitations of using Interpreter for complex grammars?
5. How would you optimize an Interpreter implementation for performance?

## References
- Design Patterns: Elements of Reusable Object-Oriented Software (Gang of Four)
- Head First Design Patterns
- Pattern-Oriented Software Architecture
