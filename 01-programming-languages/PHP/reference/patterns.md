# PHP Patterns

## Repository Pattern
```php
interface UserRepository {
    public function find(int $id): ?User;
    public function save(User $user): void;
}
```

## Factory Pattern
```php
class UserFactory {
    public static function create(array $data): User {
        return new User($data['name'], $data['email']);
    }
}
```

## Strategy Pattern (via Interface)
```php
interface SortStrategy {
    public function sort(array &$data): void;
}
```

## Observer Pattern (via SplObserver)
```php
class EventManager implements \SplObserver {
    public function update(\SplSubject $subject): void {
        // handle event
    }
}
```

## Decorator Pattern (via Traits)
```php
trait Cacheable {
    public function get(string $key): mixed {
        return $this->cache->get($key) ?? $this->fetch($key);
    }
}
```
