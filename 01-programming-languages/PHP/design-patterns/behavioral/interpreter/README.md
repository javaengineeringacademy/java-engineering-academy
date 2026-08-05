# Interpreter Pattern in PHP

The Interpreter pattern defines a grammar for a language and provides an interpreter for it. In PHP, this is implemented using interfaces for AST nodes and method dispatch for evaluation.

## When to Use

- Simple language parsing
- Expression evaluation
- Configuration file parsing
- Query languages
- DSL design

## Implementation

### Expression Interpreter

```php
interface Expression
{
    public function evaluate(): float;
    public function __toString(): string;
}

class Number implements Expression
{
    public function __construct(private float $value) {}

    public function evaluate(): float { return $this->value; }
    public function __toString(): string { return (string) $this->value; }
}

class Add implements Expression
{
    public function __construct(
        private Expression $left,
        private Expression $right
    ) {}

    public function evaluate(): float
    {
        return $this->left->evaluate() + $this->right->evaluate();
    }

    public function __toString(): string
    {
        return "({$this->left} + {$this->right})";
    }
}

class Multiply implements Expression
{
    public function __construct(
        private Expression $left,
        private Expression $right
    ) {}

    public function evaluate(): float
    {
        return $this->left->evaluate() * $this->right->evaluate();
    }

    public function __toString(): string
    {
        return "({$this->left} * {$this->right})";
    }
}

// Usage
$expr = new Add(new Number(5), new Multiply(new Number(3), new Number(2)));
echo "{$expr} = " . $expr->evaluate();
```

### Rule Interpreter

```php
interface Rule
{
    public function evaluate(Context $context): bool;
}

class Context
{
    public function __construct(
        public readonly float $temperature,
        public readonly float $humidity,
        public readonly float $windSpeed
    ) {}
}

class AndRule implements Rule
{
    public function __construct(
        private Rule $left,
        private Rule $right
    ) {}

    public function evaluate(Context $context): bool
    {
        return $this->left->evaluate($context) && $this->right->evaluate($context);
    }
}

class TemperatureRule implements Rule
{
    public function __construct(
        private float $min,
        private float $max
    ) {}

    public function evaluate(Context $context): bool
    {
        return $context->temperature >= $this->min && $context->temperature <= $this->max;
    }
}
```

### Simple Calculator

```php
function parseAndEvaluate(string $input): float
{
    $tokens = explode(' ', $input);
    $stack = [];

    foreach ($tokens as $token) {
        switch ($token) {
            case '+':
                $b = array_pop($stack);
                $a = array_pop($stack);
                $stack[] = $a + $b;
                break;
            case '-':
                $b = array_pop($stack);
                $a = array_pop($stack);
                $stack[] = $a - $b;
                break;
            case '*':
                $b = array_pop($stack);
                $a = array_pop($stack);
                $stack[] = $a * $b;
                break;
            default:
                $stack[] = (float) $token;
        }
    }

    return $stack[0];
}

echo parseAndEvaluate("3 4 + 2 *"); // 14
```

### DSL Interpreter

```php
interface Query
{
    public function toSql(): string;
}

class Select implements Query
{
    public function __construct(
        private array $fields,
        private string $table
    ) {}

    public function toSql(): string
    {
        return "SELECT " . implode(', ', $this->fields) . " FROM {$this->table}";
    }
}

class Where implements Query
{
    public function __construct(
        private Query $query,
        private string $condition
    ) {}

    public function toSql(): string
    {
        return $this->query->toSql() . " WHERE {$this->condition}";
    }
}

// Usage
$query = new Where(new Select(['name', 'age'], 'users'), 'age > 25');
echo $query->toSql();
```

## Best Practices

- Use interfaces for AST node types
- Implement `__toString` for debugging
- Document the grammar syntax and supported operations
- Consider using parser combinator libraries for complex grammars
- Add error handling for malformed expressions

## Interview Questions

1. When should you use the interpreter pattern vs a parser library?
2. How do you handle operator precedence?
3. How do you implement error recovery in interpreters?
4. What are the limitations of the interpreter pattern?
5. How do you optimize interpreter performance?

## References

- [PHP Interfaces](https://www.php.net/language.oop5.interfaces)
- [Design Patterns in PHP](https://github.com/domnikl/DesignPatternsPHP)
- [PHP The Right Way](https://phptherightway.com/)
