# Interpreter Pattern

## Overview
The Interpreter pattern defines a grammatical representation for a language and provides an interpreter to deal with this grammar. It represents grammar rules as classes and uses a composite structure to build abstract syntax trees (AST) that can be evaluated.

## When to Use
- Need to evaluate sentences in a simple language (SQL, math expressions, regex)
- Grammar is simple and efficiency is not a critical concern
- Building compilers, expression parsers, or rule engines
- DSLs (Domain-Specific Languages) for configuration or business rules

## Code Structure
```
Expression (interface)              Client
    |                               |
Number (Terminal)           builds AST nodes
Add, Subtract, Multiply (Non-Terminal)
    |
evaluate() → double
```

## Key Benefits
- Grammar is represented as classes — easy to modify and extend
- Each grammar rule becomes a class, following the Open/Closed Principle
- Composite structure naturally represents nested expressions
- Easy to add new expressions without changing existing classes

## Common Mistakes
- Using Interpreter for complex grammars — use a parser generator instead
- Not handling errors gracefully (division by zero, invalid tokens)
- Creating excessive object overhead for simple evaluations
- Ignoring precedence — multiplication must bind tighter than addition

## Interview Questions
1. What is the difference between Interpreter and Composite patterns?
2. How does the Interpreter pattern handle operator precedence?
3. What are the limitations of the Interpreter pattern?
4. How would you extend the pattern to support variables?

## Performance

Interpreter creates an object per grammar element. For expression "3+4*5", the AST has 5 nodes (3, 4, 5, *, +). Each node consumes ~16-32 bytes on the heap. Evaluation traverses the tree recursively — O(n) where n is node count. For deep trees, consider tail-call optimization or iterative evaluation. For high-throughput scenarios, compile the AST to bytecode or use a visitor for direct evaluation.

## Examples

```java
// Boolean expression interpreter
interface BooleanExpression {
    boolean interpret(Map<String, Boolean> context);
}

class VariableExpression implements BooleanExpression {
    private final String name;
    VariableExpression(String name) { this.name = name; }
    
    @Override
    public boolean interpret(Map<String, Boolean> context) {
        return context.getOrDefault(name, false);
    }
}

class AndExpression implements BooleanExpression {
    private final BooleanExpression left, right;
    AndExpression(BooleanExpression left, BooleanExpression right) {
        this.left = left;
        this.right = right;
    }
    
    @Override
    public boolean interpret(Map<String, Boolean> context) {
        return left.interpret(context) && right.interpret(context);
    }
}

class NotExpression implements BooleanExpression {
    private final BooleanExpression operand;
    NotExpression(BooleanExpression operand) { this.operand = operand; }
    
    @Override
    public boolean interpret(Map<String, Boolean> context) {
        return !operand.interpret(context);
    }
}

// Usage: (x AND y) AND NOT z
BooleanExpression expr = new AndExpression(
    new AndExpression(new VariableExpression("x"), new VariableExpression("y")),
    new NotExpression(new VariableExpression("z"))
);
Map<String, Boolean> ctx = Map.of("x", true, "y", true, "z", false);
System.out.println(expr.interpret(ctx)); // true
```

## Internal Working

The interpreter builds a composite tree of Expression nodes. Terminal nodes (Number) return raw values. Non-terminal nodes (Add, Subtract) delegate to child expressions and combine results. Evaluation is a recursive tree walk: Add.evaluate() calls left.evaluate() + right.evaluate(). The tree structure naturally handles precedence if built correctly — the parser must construct deeper trees for higher-precedence operators. This is a direct mapping of grammar rules to object hierarchy.

## Why This Concept Exists

Directly parsing and evaluating string expressions requires complex state management and operator precedence handling. Interpreter externalizes grammar rules into classes, making each rule independently testable and extensible. The composite pattern gives us nested evaluation for free. This approach trades performance (object allocation) for clarity and maintainability — ideal for small, stable grammars.

## Pitfalls

1. **Complex grammar support**: Interpreter works best for simple grammars — complex ones need parser generators (ANTLR, JavaCC)
2. **Class explosion**: Each grammar rule becomes a class — a full expression language may need 30+ classes
3. **No optimization**: The interpreter evaluates naively — no constant folding, common subexpression elimination, or caching
4. **Memory overhead**: Each node is a separate object — for millions of evaluations, this adds GC pressure
5. **Precedence bugs**: Incorrect tree construction leads to wrong evaluation order — must be validated carefully

## References

- [Refactoring.Guru - Interpreter Pattern](https://refactoring.guru/design-patterns/interpreter)
- [Head First Design Patterns - Interpreter Pattern](https://www.oreilly.com/library/view/head-first-design/0596007124/)
- [Java Expression Evaluator (JEP)](http://www.cin.ufpe.br/~jhosen/Downloads/jep-2.3.1-tutorial/src/org/nfunk/jep/package-summary.html)
