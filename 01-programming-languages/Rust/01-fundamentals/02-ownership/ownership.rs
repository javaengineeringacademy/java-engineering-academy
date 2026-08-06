// Ownership in Rust

fn main() {
    // Ownership basics
    let s1 = String::from("hello");
    let s2 = s1; // s1 is moved to s2
    // println!("{}", s1); // error: value used after move
    println!("s2 = {}", s2);

    // Clone
    let s3 = String::from("world");
    let s4 = s3.clone(); // deep copy
    println!("s3 = {}, s4 = {}", s3, s4);

    // Ownership with functions
    let s5 = String::from("hello");
    takes_ownership(s5); // s5 is moved into the function
    // println!("{}", s5); // error: value used after move

    let x = 5;
    makes_copy(x); // i32 is copied, not moved
    println!("x = {}", x); // works fine

    // Return ownership
    let s6 = gives_ownership();
    println!("s6 = {}", s6);

    let s7 = String::from("hello");
    let s8 = takes_and_gives_back(s7);
    println!("s8 = {}", s8);

    // Tuple returns for multiple values
    let s9 = String::from("hello");
    let (s10, len) = calculate_length(s9);
    println!("'{}' has length {}", s10, len);

    // Stack vs Heap
    // Stack: i32, f64, bool, char, fixed arrays
    // Heap: String, Vec<T>, Box<T>

    // Copy trait
    // Types that implement Copy are copied, not moved
    let a = 1; // Copy
    let b = a; // Copy
    println!("a = {}, b = {}", a, b);
}

fn takes_ownership(s: String) {
    println!("took ownership of: {}", s);
} // s is dropped here

fn makes_copy(x: i32) {
    println!("copied: {}", x);
} // x is dropped here

fn gives_ownership() -> String {
    String::from("hello")
}

fn takes_and_gives_back(s: String) -> String {
    s // return ownership to caller
}

fn calculate_length(s: String) -> (String, usize) {
    let length = s.len();
    (s, length)
}
