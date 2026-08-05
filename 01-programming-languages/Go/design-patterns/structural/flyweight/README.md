# Flyweight Pattern

## Overview

Flyweight uses sharing to support large numbers of fine-grained objects efficiently. Go uses maps to cache common state.

## When to Use

- Large numbers of similar objects
- Memory optimization critical
- Objects share common immutable state

## Go Implementation

```go
type TreeType struct {
    Name, Color, Texture string
}

type TreeFactory struct {
    types map[string]*TreeType
}

func (f *TreeFactory) Get(name, color, texture string) *TreeType {
    key := name + color + texture
    if t, ok := f.types[key]; ok { return t }
    f.types[key] = &TreeType{Name: name, Color: color, Texture: texture}
    return f.types[key]
}
```

## Go-Idiomatic Alternative

```go
type Factory struct{ cache sync.Map }

func (f *Factory) Get(key string, creator func() *TreeType) *TreeType {
    if v, ok := f.cache.Load(key); ok { return v.(*TreeType) }
    t := creator()
    actual, _ := f.cache.LoadOrStore(key, t)
    return actual.(*TreeType)
}
```

## Real-World Example

```go
type CharacterFactory struct{ fonts map[string]*FontType }

func (f *CharacterFactory) GetFont(name string) *FontType {
    if font, ok := f.fonts[name]; ok { return font }
    font := loadFont(name)
    f.fonts[name] = font
    return font
}
```

## Best Practices

- Separate intrinsic (shared) from extrinsic (unique) state
- Use `sync.Map` for concurrent factories
- Consider object pooling as simpler alternative

## Interview Questions

1. What is the difference between Flyweight and Object Pool?
2. How do you handle concurrent access to the factory?
3. When does Flyweight become an anti-pattern?
4. How do you determine intrinsic vs extrinsic state?
5. Can Flyweight work with value types?

## References

- "Design Patterns" - GoF Chapter 4
- Go Blog: "Go sync package"
