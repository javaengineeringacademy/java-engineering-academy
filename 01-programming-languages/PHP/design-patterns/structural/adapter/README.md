# Adapter Pattern in PHP

The Adapter pattern converts the interface of a class into another interface clients expect. In PHP, this is implemented using classes that implement the target interface and wrap the adaptee.

## When to Use

- Integrating third-party libraries
- Making existing types work with new code
- Unifying multiple interfaces
- Legacy system integration

## Implementation

### Class Adapter

```php
interface MediaPlayer
{
    public function play(string $file): string;
}

class VlcPlayer
{
    public function playVlc(string $path): string
    {
        return "Playing VLC: {$path}";
    }
}

class VlcAdapter implements MediaPlayer
{
    private VlcPlayer $player;

    public function __construct(VlcPlayer $player)
    {
        $this->player = $player;
    }

    public function play(string $file): string
    {
        return $this->player->playVlc($file);
    }
}
```

### Object Adapter

```php
interface Logger
{
    public function log(string $message): void;
}

class LegacyLogger
{
    public function writeLog(string $msg): void
    {
        echo "LEGACY: {$msg}\n";
    }
}

class LegacyLoggerAdapter implements Logger
{
    private LegacyLogger $logger;

    public function __construct(LegacyLogger $logger)
    {
        $this->logger = $logger;
    }

    public function log(string $message): void
    {
        $this->logger->writeLog($message);
    }
}
```

### Multiple Adapter

```php
interface Renderer
{
    public function renderCircle(float $x, float $y, float $radius): string;
    public function renderRectangle(float $x, float $y, float $w, float $h): string;
}

class SVGRenderer
{
    public function drawCircle(float $cx, float $cy, float $r): string
    {
        return "<circle cx='{$cx}' cy='{$cy}' r='{$r}'/>";
    }
}

class SVGAdapter implements Renderer
{
    private SVGRenderer $renderer;

    public function __construct(SVGRenderer $renderer)
    {
        $this->renderer = $renderer;
    }

    public function renderCircle(float $x, float $y, float $radius): string
    {
        return $this->renderer->drawCircle($x, $y, $radius);
    }

    public function renderRectangle(float $x, float $y, float $w, float $h): string
    {
        return "<rect x='{$x}' y='{$y}' width='{$w}' height='{$h}'/>";
    }
}
```

### Generic Adapter

```php
interface Formatter
{
    public function format(mixed $data): string;
}

class JsonFormatter
{
    public function toJson(array $data): string
    {
        return json_encode($data);
    }
}

class XmlFormatter
{
    public function toXml(array $data): string
    {
        $xml = new \SimpleXMLElement('<root/>');
        foreach ($data as $key => $value) {
            $xml->addChild($key, $value);
        }
        return $xml->asXML();
    }
}

class JsonAdapter implements Formatter
{
    public function __construct(private JsonFormatter $formatter) {}

    public function format(mixed $data): string
    {
        return $this->formatter->toJson((array) $data);
    }
}
```

## Best Practices

- Keep adapters lightweight; prefer composition
- Implement the target interface on the adapter
- Document the mapping between old and new interfaces
- Use type hints for adapter parameters
- Test adapters with both source and target contracts

## Interview Questions

1. What is the difference between an adapter and a facade?
2. When would you use a class adapter vs an object adapter?
3. How do you handle adapters that need to maintain state?
4. Can you combine the adapter pattern with the decorator pattern?
5. How do you test adapters in PHP?

## References

- [PHP Interfaces](https://www.php.net/language.oop5.interfaces)
- [Design Patterns in PHP](https://github.com/domnikl/DesignPatternsPHP)
- [PHP The Right Way](https://phptherightway.com/)
