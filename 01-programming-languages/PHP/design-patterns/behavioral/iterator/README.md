# Iterator Pattern in PHP

The Iterator pattern provides a way to access elements sequentially. In PHP, this is implemented using the `Iterator` or `IteratorAggregate` interface.

## When to Use

- Traversing collections
- Custom data structure traversal
- Filtering and transforming collections
- Implementing range-based operations
- Database result sets

## Implementation

### IteratorAggregate

```php
class UserCollection implements \IteratorAggregate
{
    private array $users = [];

    public function addUser(string $name, int $age): void
    {
        $this->users[] = ['name' => $name, 'age' => $age];
    }

    public function getIterator(): \Traversable
    {
        return new \ArrayIterator($this->users);
    }
}

// Usage
$users = new UserCollection();
$users->addUser('Alice', 30);
$users->addUser('Bob', 25);

foreach ($users as $user) {
    echo "{$user['name']}: {$user['age']}\n";
}
```

### Custom Iterator

```php
class Counter implements \Iterator
{
    private int $count = 0;
    private int $max;

    public function __construct(int $max)
    {
        $this->max = $max;
    }

    public function current(): int { return $this->count; }
    public function key(): int { return $this->count; }
    public function next(): void { $this->count++; }
    public function rewind(): void { $this->count = 0; }
    public function valid(): bool { return $this->count < $this->max; }
}

// Usage
$counter = new Counter(5);
foreach ($counter as $num) {
    echo $num . "\n";
}
```

### Filtered Iterator

```php
class FilteredIterator implements \Iterator
{
    private int $position = 0;
    private array $filtered = [];

    public function __construct(array $data, callable $filter)
    {
        $this->filtered = array_values(array_filter($data, $filter));
    }

    public function current(): mixed { return $this->filtered[$this->position]; }
    public function key(): int { return $this->position; }
    public function next(): void { $this->position++; }
    public function rewind(): void { $this->position = 0; }
    public function valid(): bool { return isset($this->filtered[$this->position]); }
}
```

### Tree Iterator

```php
class TreeNode
{
    private array $children = [];

    public function __construct(public readonly string $value) {}

    public function add(TreeNode $child): void
    {
        $this->children[] = $child;
    }

    public function getIterator(): \Generator
    {
        yield $this->value;
        foreach ($this->children as $child) {
            yield from $child->getIterator();
        }
    }
}
```

### Infinite Iterator

```php
class FibonacciIterator implements \Iterator
{
    private int $a = 0;
    private int $b = 1;
    private int $position = 0;
    private int $max;

    public function __construct(int $max = PHP_INT_MAX)
    {
        $this->max = $max;
    }

    public function current(): int { return $this->a; }
    public function key(): int { return $this->position; }
    public function next(): void
    {
        $newB = $this->a + $this->b;
        $this->a = $this->b;
        $this->b = $newB;
        $this->position++;
    }
    public function rewind(): void { $this->a = 0; $this->b = 1; $this->position = 0; }
    public function valid(): bool { return $this->position < $this->max; }
}
```

## Best Practices

- Implement `IteratorAggregate` for simpler iteration
- Use `Iterator` for custom iteration logic
- Use generators for lazy evaluation
- Document element order and behavior
- Consider using `Countable` for count support

## Interview Questions

1. What is the difference between `Iterator` and `IteratorAggregate`?
2. How do you implement a bidirectional iterator?
3. How do you handle errors in iterators?
4. What are generators and when should you use them?
5. How do you make an iterator countable?

## References

- [Iterator Interface](https://www.php.net/class.iterator)
- [IteratorAggregate](https://www.php.net/class.iteratoraggregate)
- [Design Patterns in PHP](https://github.com/domnikl/DesignPatternsPHP)
