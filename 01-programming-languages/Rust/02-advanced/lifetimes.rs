fn main() {
    // Basic lifetime
    let string1 = String::from("long string");
    let result;
    {
        let string2 = String::from("xyz");
        result = longest(string1.as_str(), string2.as_str());
        println!("Longest: {}", result);
    }

    // Lifetime in structs
    let novel = Novel {
        title: String::from("Great Novel"),
        first_sentence: "Once upon a time...",
    };
    println!("Title: {}", novel.title());
    println!("Excerpt: {}", novel.excerpt());

    // Lifetime with methods
    let excerpt = Excerpt::new("This is an excerpt from a longer text.");
    println!("First word: {}", excerpt.first_word());
    println!("Full: {}", excerpt.full());

    // Multiple lifetimes
    let string_a = String::from("hello");
    let result;
    {
        let string_b = String::from("world");
        result = longest_with_announcement(string_a.as_str(), string_b.as_str(), "Comparing...");
    }
    println!("Result: {}", result);

    // Lifetime elision rules
    let word = first_word("hello world");
    println!("First word: {}", word);

    // Static lifetime
    let s: &'static str = "I live forever";
    println!("Static: {}", s);

    // Generic with lifetimes
    let result = longest_generic("hello", "world");
    println!("Longest generic: {}", result);

    // Lifetime in closures
    let result = with_lifetime("hello", |s| s.len());
    println!("Length: {}", result);

    // Avoiding lifetime issues with owned types
    let result = longest_owned(String::from("hello"), String::from("world"));
    println!("Owned longest: {}", result);
}

// Basic lifetime annotation
fn longest<'a>(x: &'a str, y: &'a str) -> &'a str {
    if x.len() > y.len() { x } else { y }
}

// Lifetime in struct
struct Novel<'a> {
    title: String,
    first_sentence: &'a str,
}

impl<'a> Novel<'a> {
    fn title(&self) -> &str {
        &self.title
    }

    fn excerpt(&self) -> &str {
        self.first_sentence
    }
}

// Lifetime with methods
struct Excerpt<'a> {
    text: &'a str,
}

impl<'a> Excerpt<'a> {
    fn new(text: &'a str) -> Self {
        Excerpt { text }
    }

    fn first_word(&self) -> &str {
        self.text.split_whitespace().next().unwrap_or("")
    }

    fn full(&self) -> &str {
        self.text
    }
}

// Multiple lifetimes
fn longest_with_announcement<'a>(x: &'a str, y: &'a str, ann: &str) -> &'a str {
    println!("Announcement: {}", ann);
    if x.len() > y.len() { x } else { y }
}

// Lifetime elision
fn first_word(s: &str) -> &str {
    let bytes = s.as_bytes();
    for (i, &byte) in bytes.iter().enumerate() {
        if byte == b' ' {
            return &s[0..i];
        }
    }
    &s[..]
}

// Generic with lifetimes
fn longest_generic<'a, T: AsRef<str>>(x: T, y: T) -> String {
    let x_str = x.as_ref();
    let y_str = y.as_ref();
    if x_str.len() > y_str.len() {
        x_str.to_string()
    } else {
        y_str.to_string()
    }
}

// Lifetime with closure
fn with_lifetime<'a, F>(s: &'a str, f: F) -> usize
where
    F: Fn(&str) -> usize,
{
    f(s)
}

// Owned types to avoid lifetimes
fn longest_owned(x: String, y: String) -> String {
    if x.len() > y.len() { x } else { y }
}
