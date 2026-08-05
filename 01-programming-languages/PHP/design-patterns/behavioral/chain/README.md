# Chain of Responsibility Pattern in PHP

The Chain of Responsibility pattern passes a request along a chain of handlers. In PHP, this is implemented using interfaces and linked handler objects.

## When to Use

- Request processing pipelines
- Middleware stacks (web frameworks)
- Event handling chains
- Logging levels
- Approval workflows

## Implementation

### Interface-Based Chain

```php
interface Handler
{
    public function handle(string $request): ?string;
    public function setNext(Handler $handler): Handler;
}

class AuthHandler implements Handler
{
    private ?Handler $next = null;

    public function handle(string $request): ?string
    {
        if (str_contains($request, 'auth')) {
            return "AuthHandler processed";
        }
        return $this->next?->handle($request);
    }

    public function setNext(Handler $handler): Handler
    {
        $this->next = $handler;
        return $handler;
    }
}

class ValidationHandler implements Handler
{
    private ?Handler $next = null;

    public function handle(string $request): ?string
    {
        if (str_contains($request, 'valid')) {
            return "ValidationHandler processed";
        }
        return $this->next?->handle($request);
    }

    public function setNext(Handler $handler): Handler
    {
        $this->next = $handler;
        return $handler;
    }
}

// Usage
$auth = new AuthHandler();
$validation = new ValidationHandler();
$auth->setNext($validation);

echo $auth->handle('auth valid request');
```

### Middleware Chain

```php
class MiddlewareChain
{
    private array $middlewares = [];

    public function add(callable $middleware): self
    {
        $this->middlewares[] = $middleware;
        return $this;
    }

    public function execute(string $request): ?string
    {
        $result = null;
        foreach ($this->middlewares as $middleware) {
            $result = $middleware($request);
            if ($result !== null) {
                return $result;
            }
        }
        return null;
    }
}

// Usage
$chain = new MiddlewareChain();
$chain->add(fn($r) => str_contains($r, 'auth') ? "Authenticated" : null);
$chain->add(fn($r) => str_contains($r, 'valid') ? "Validated" : null);
echo $chain->execute('auth valid request');
```

### Logger Chain

```php
class LogHandler
{
    private array $handlers = [];

    public function addHandler(string $level, callable $handler): void
    {
        $this->handlers[$level] = $handler;
    }

    public function log(string $level, string $message): bool
    {
        if (isset($this->handlers[$level])) {
            $this->handlers[$level]($message);
            return true;
        }
        return false;
    }
}

// Usage
$logger = new LogHandler();
$logger->addHandler('info', fn($msg) => echo "[INFO] {$msg}\n");
$logger->addHandler('error', fn($msg) => echo "[ERROR] {$msg}\n");
$logger->log('info', 'Application started');
```

### Approval Chain

```php
interface Approver
{
    public function approve(float $amount): ?string;
    public function setNext(Approver $next): void;
}

class Manager implements Approver
{
    private ?Approver $next = null;

    public function approve(float $amount): ?string
    {
        if ($amount <= 1000) {
            return "Manager approved \${$amount}";
        }
        return $this->next?->approve($amount);
    }

    public function setNext(Approver $next): void
    {
        $this->next = $next;
    }
}

class Director implements Approver
{
    private ?Approver $next = null;

    public function approve(float $amount): ?string
    {
        if ($amount <= 10000) {
            return "Director approved \${$amount}";
        }
        return $this->next?->approve($amount);
    }

    public function setNext(Approver $next): void
    {
        $this->next = $next;
    }
}
```

## Best Practices

- Keep handlers independent; avoid coupling between elements
- Use `?string` return type for optional handling
- Document the chain order and handler responsibilities
- Use null coalescing operator for optional chaining
- Consider using traits for common handler behavior

## Interview Questions

1. How does the chain of responsibility differ from the observer pattern?
2. When should you break the chain vs returning null?
3. How do you handle circular chains in PHP?
4. How do you test individual handlers in a chain?
5. What are the performance implications of long chains?

## References

- [PHP Interfaces](https://www.php.net/language.oop5.interfaces)
- [Design Patterns in PHP](https://github.com/domnikl/DesignPatternsPHP)
- [PHP The Right Way](https://phptherightway.com/)
