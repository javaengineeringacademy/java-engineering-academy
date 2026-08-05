# Interpreter Pattern (JavaScript)

## Overview

The Interpreter pattern defines a grammatical representation for a language and provides
an interpreter to work with this grammar. JavaScript's dynamic typing and functional
style make interpreters particularly elegant.

## When to Use

- Simple grammar representation needed
- Efficiency is not critical concern
- Grammar is simple and simple interpreter suffices
- Similar patterns of expressions occur frequently

## JavaScript Implementation

### Basic Interpreter

```javascript
class Expression {
  interpret() {}
}

class NumberExpression extends Expression {
  constructor(number) {
    super();
    this.number = number;
  }

  interpret() {
    return this.number;
  }
}

class AddExpression extends Expression {
  constructor(left, right) {
    super();
    this.left = left;
    this.right = right;
  }

  interpret() {
    return this.left.interpret() + this.right.interpret();
  }
}
```

### Functional Interpreter

```javascript
const interpreter = {
  number: (value) => () => value,
  add: (left, right) => () => left() + right(),
  multiply: (left, right) => () => left() * right()
};

const expr = interpreter.add(
  interpreter.number(5),
  interpreter.multiply(
    interpreter.number(3),
    interpreter.number(2)
  )
);

console.log(expr());
```

### Variable Interpreter

```javascript
function createInterpreter(variables) {
  return {
    number: (value) => () => value,
    variable: (name) => () => variables[name],
    add: (left, right) => () => left() + right(),
    subtract: (left, right) => () => left() - right()
  };
}

const interp = createInterpreter({ x: 10, y: 5 });
const expr = interp.add(interp.variable('x'), interp.variable('y'));
```

### Parser

```javascript
function parseExpression(tokens) {
  let position = 0;

  function parse() {
    const token = tokens[position++];
    if (token.type === 'number') {
      return { type: 'number', value: token.value };
    } else if (token.type === 'operator') {
      const left = parse();
      const right = parse();
      return { type: token.value, left, right };
    }
  }

  return parse();
}
```

## Best Practices

- Keep grammar simple
- Consider using parser generators for complex grammars
- Use closures for lazy evaluation
- Document grammar rules clearly
- Consider caching for repeated interpretations

## Interview Questions

1. When should you use Interpreter pattern?
2. How do you implement expression trees?
3. Can Interpreter be used for SQL parsing?
4. What are alternatives to Interpreter for complex grammars?
5. How do you handle operator precedence?

## References

- MDN: Interpreter Pattern
- "Learning JavaScript Design Patterns" by Addy Osmani
- "Compilers: Principles, Techniques, and Tools"
