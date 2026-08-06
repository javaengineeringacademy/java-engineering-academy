// Lifetimes in Rust

// Basic lifetime annotation
fn longest<'a>(x: &'a str, y: &'a str) -> &'a str {
    if x.len() > y.len() { x } else { y }
}

// Struct with lifetime
struct ImportantExcerpt<'a> {
    part: &'a str,
}

impl<'a> ImportantExcerpt<'a> {
    fn announce_and_return(&self, announcement: &str) -> &str {
        println!("Attention: {}", announcement);
        self.part
    }
}

// Lifetime elision examples
fn first_word(s: &str) -> &str {
    let bytes = s.as_bytes();
    for (i, &byte) in bytes.iter().enumerate() {
        if byte == b' ' {
            return &s[0..i];
        }
    }
    s
}

fn main() {
    // Basic lifetime usage
    let string1 = String::from("long string is long");
    let result;
    {
        let string2 = String::from("xyz");
        result = longest(string1.as_str(), string2.as_str());
        println!("Longest: {}", result);
    }

    // Struct with lifetime
    let novel = String::from("Call me Ishmael. Some years ago...");
    let first_sentence;
    {
        let i = novel.find('.').unwrap_or(novel.len());
        first_sentence = ImportantExcerpt { part: &novel[0..i] };
    }
    println!("Excerpt: {}", first_sentence.part);

    // Elision rules
    let s = String::from("hello world");
    let word = first_word(&s);
    println!("First word: {}", word);

    // Static lifetime
    let s: &'static str = "I live forever";
    println!("Static: {}", s);
}
