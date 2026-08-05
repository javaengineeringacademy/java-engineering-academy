# Flyweight Pattern in Rust

The Flyweight pattern minimizes memory usage by sharing data across similar objects. In Rust, this is implemented using `Rc`, `Arc`, or interned strings with `String` deduplication.

## When to Use

- Large numbers of similar objects
- Memory-constrained environments
- Text editors with character objects
- Game objects with shared textures or models
- String deduplication

## Implementation

### String Interning

```rust
use std::collections::HashMap;

struct StringInterner {
    strings: Vec<String>,
    map: HashMap<String, usize>,
}

impl StringInterner {
    fn new() -> Self {
        StringInterner {
            strings: Vec::new(),
            map: HashMap::new(),
        }
    }

    fn intern(&mut self, s: &str) -> usize {
        if let Some(&id) = self.map.get(s) {
            return id;
        }
        let id = self.strings.len();
        self.strings.push(s.to_string());
        self.map.insert(s.to_string(), id);
        id
    }

    fn get(&self, id: usize) -> &str {
        &self.strings[id]
    }
}
```

### Shared State Flyweight

```rust
use std::rc::Rc;

struct FlyweightData {
    shared_state: String,
}

struct Flyweight {
    unique_state: (f64, f64),
    data: Rc<FlyweightData>,
}

struct FlyweightFactory {
    pool: Vec<Rc<FlyweightData>>,
}

impl FlyweightFactory {
    fn new() -> Self {
        FlyweightFactory { pool: Vec::new() }
    }

    fn get_data(&mut self, state: &str) -> Rc<FlyweightData> {
        if let Some(existing) = self.pool.iter().find(|d| d.shared_state == state) {
            return Rc::clone(existing);
        }
        let data = Rc::new(FlyweightData {
            shared_state: state.to_string(),
        });
        self.pool.push(Rc::clone(&data));
        data
    }
}
```

### Character Flyweight

```rust
use std::collections::HashMap;

struct CharacterGlyph {
    font: String,
    size: u32,
}

struct CharacterFlyweight {
    glyph: Rc<CharacterGlyph>,
    position: (f64, f64),
}

struct TextEditor {
    characters: Vec<CharacterFlyweight>,
    glyph_cache: HashMap<String, Rc<CharacterGlyph>>,
}

impl TextEditor {
    fn new() -> Self {
        TextEditor {
            characters: Vec::new(),
            glyph_cache: HashMap::new(),
        }
    }

    fn insert_char(&mut self, ch: char, font: &str, size: u32, pos: (f64, f64)) {
        let key = format!("{}:{}", font, size);
        let glyph = self.glyph_cache.entry(key)
            .or_insert_with(|| {
                Rc::new(CharacterGlyph {
                    font: font.to_string(),
                    size,
                })
            })
            .clone();

        self.characters.push(CharacterFlyweight {
            glyph,
            position: pos,
        });
    }
}
```

## Best Practices

- Use `Rc<T>` for single-threaded shared ownership
- Use `Arc<T>` for multi-threaded shared ownership
- Cache frequently accessed flyweights for performance
- Consider using string interning for repeated string values
- Document the shared state boundaries clearly

## Interview Questions

1. What is the difference between flyweight and prototype patterns?
2. When does the flyweight pattern become counterproductive?
3. How does `Rc<T>` enable flyweight sharing in Rust?
4. How do you handle mutable shared state in flyweights?
5. What are the thread-safety implications of using `Rc<T>`?

## References

- [Rust Design Patterns - Flyweight](https://rust-unofficial.github.io/patterns/)
- [Reference Counting](https://doc.rust-lang.org/book/ch15-04-refcell.html)
- [Rust by Example - Rc](https://doc.rust-lang.org/rust-by-example/std/rc.html)
