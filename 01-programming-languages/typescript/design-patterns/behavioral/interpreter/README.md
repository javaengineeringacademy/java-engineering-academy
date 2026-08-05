# Interpreter Pattern (TypeScript)

## Overview

The Interpreter pattern defines a grammatical representation for a language and provides
an interpreter to work with this grammar. TypeScript's type system enables type-safe
interpreter implementations.

## When to Use

- Simple grammar representation needed
- Efficiency is not critical concern
- Grammar is simple and simple interpreter suffices
- Similar patterns of expressions occur frequently

## TypeScript Implementation

### Typed Interpreter

```typescript
interface Expression {
  interpret(): number;
}

class NumberExpression implements Expression {
  constructor(private number: number) {}

  interpret(): number {
    return this.number;
  }
}

class AddExpression implements Expression {
  constructor(
    private left: Expression,
    private right: Expression
  ) {}

  interpret(): number {
    return this.left.interpret() + this.right.interpret();
  }
}
```

### Functional Interpreter

```typescript
type InterpreterFn = () => number;

function number(value: number): InterpreterFn {
  return () => value;
}

function add(left: InterpreterFn, right: InterpreterFn): InterpreterFn {
  return () => left() + right();
}

function multiply(left: InterpreterFn, right: InterpreterFn): InterpreterFn {
  return () => left() * right();
}

const expr = add(number(5), multiply(number(3), number(2)));
```

### Variable Interpreter

```typescript
type VariableMap = { [key: string]: number };

function createInterpreter(variables: VariableMap) {
  return {
    number: (value: number) => () => value,
    variable: (name: string) => () => variables[name],
    add: (left: () => number, right: () => number) => () => left() + right(),
    subtract: (left: () => number, right: () => number) => () => left() - right()
  };
}

const interp = createInterpreter({ x: 10, y: 5 });
const expr = interp.add(interp.variable('x'), interp.variable('y'));
```

### AST Interpreter

```typescript
type AST =
  | { type: 'number'; value: number }
  | { type: 'add'; left: AST; right: AST }
  | { type: 'multiply'; left: AST; right: AST };

function evaluate(ast: AST): number {
  switch (ast.type) {
    case 'number':
      return ast.value;
    case 'add':
      return evaluate(ast.left) + evaluate(ast.right);
    case 'multiply':
      return evaluate(ast.left) * evaluate(ast.right);
  }
}
```

## Best Practices

- Use interfaces for type safety
- Keep grammar simple
- Document grammar rules clearly
- Consider caching for repeated interpretations
- Use closures for lazy evaluation

## Interview Questions

1. When should you use Interpreter pattern?
2. How do you implement expression trees?
3. Can Interpreter be used for SQL parsing?
4. What are alternatives to Interpreter for complex grammars?
5. How do you handle operator precedence?

## References

- TypeScript Handbook: Types
- "TypeScript Design Patterns" by Vaskaran Sarcar
- "Compilers: Principles, Techniques, and Tools"
