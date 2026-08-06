// Borrowing in Rust

fn main() {
    // Immutable references
    let s1 = String::from("hello");
    let len = calculate_length(&s1); // borrow s1
    println!("'{}' has length {}", s1, len);

    // Multiple immutable references
    let r1 = &s1;
    let r2 = &s1;
    println!("r1 = {}, r2 = {}", r1, r2);

    // Mutable references
    let mut s2 = String::from("hello");
    change(&mut s2);
    println!("s2 = {}", s2);

    // Rules: cannot mix immutable and mutable
    let mut s3 = String::from("hello");
    let r1 = &s3;
    let r2 = &s3;
    println!("r1 = {}, r2 = {}", r1, r2);
    // Cannot borrow as mutable here because immutable borrows are still used

    // NLL (Non-Lexical Lifetimes)
    let mut s4 = String::from("hello");
    let r1 = &s4;
    let r2 = &s4;
    println!("{} {}", r1, r2);
    // r1 and r2 are no longer used after this point
    let r3 = &mut s4; // mutable borrow is fine now
    r3.push_str(" world");
    println!("s4 = {}", s4);

    // Dangling references are prevented
    // let reference = dangle(); // would cause compile error

    // Lifetimes in functions
    let string1 = String::from("long string is long");
    let string2 = String::from("xyz");
    let result = longest(string1.as_str(), string2.as_str());
    println!("longest = {}", result);

    // Str slices
    let s = String::from("hello world");
    let word = first_word(&s);
    println!("first word: {}", word);
}

fn calculate_length(s: &String) -> usize {
    s.len()
}

fn change(s: &mut String) {
    s.push_str(", world");
}

// This function would cause a dangling reference
// fn dangle() -> &String {
//     let s = String::from("hello");
//     &s // s is dropped when function returns
// }

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
