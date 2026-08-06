fn main() {
    // Immutable references (&T)
    let s1 = String::from("hello");
    let len = calculate_length(&s1);
    println!("Length of '{}' is {}", s1, len);

    // Mutable references (&mut T)
    let mut s2 = String::from("hello");
    change(&mut s2);
    println!("Modified: {}", s2);

    // Multiple immutable references
    let r1 = &s2;
    let r2 = &s2;
    println!("r1: {}, r2: {}", r1, r2);

    // Mutable reference (only one at a time)
    let r3 = &mut s2;
    r3.push_str(", world");
    println!("r3: {}", r3);

    // Borrowing in structs
    let mut book = Book {
        title: String::from("Rust Programming"),
        author: String::from("John"),
        pages: 300,
    };

    let title_ref = &book.title;
    println!("Title: {}", title_ref);

    modify_book(&mut book);
    println!("Modified: {:?}", book);

    // Lifetimes in borrowing
    let result;
    let string1 = String::from("long string");
    {
        let string2 = String::from("xyz");
        result = longest(string1.as_str(), string2.as_str());
        println!("Longest: {}", result);
    }

    // Slices as references
    let numbers = vec![1, 2, 3, 4, 5];
    let slice = &numbers[1..4];
    println!("Slice: {:?}", slice);

    // String slices
    let sentence = String::from("hello world");
    let word = first_word(&sentence);
    println!("First word: {}", word);

    // Borrowing rules
    // 1. Any number of immutable references OR
    // 2. Exactly one mutable reference
    // 3. References must always be valid

    // NLL (Non-Lexical Lifetimes)
    let mut data = vec![1, 2, 3];
    let first = &data[0];
    println!("First: {}", first);
    data.push(4);  // OK: first is no longer used

    // Reference patterns
    let opt = Some(String::from("hello"));
    if let Some(ref s) = opt {
        println!("Ref pattern: {}", s);
    }
    println!("Opt still valid: {:?}", opt);
}

fn calculate_length(s: &String) -> usize {
    s.len()
}

fn change(s: &mut String) {
    s.push_str(", world");
}

#[derive(Debug)]
struct Book {
    title: String,
    author: String,
    pages: u32,
}

fn modify_book(book: &mut Book) {
    book.pages += 50;
}

fn longest<'a>(x: &'a str, y: &'a str) -> &'a str {
    if x.len() > y.len() { x } else { y }
}

fn first_word(s: &str) -> &str {
    let bytes = s.as_bytes();
    for (i, &byte) in bytes.iter().enumerate() {
        if byte == b' ' {
            return &s[0..i];
        }
    }
    &s[..]
}
