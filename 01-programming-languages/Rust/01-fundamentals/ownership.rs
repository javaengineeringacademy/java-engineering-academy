fn main() {
    // Ownership rules
    // 1. Each value has an owner
    // 2. Only one owner at a time
    // 3. When owner goes out of scope, value is dropped

    // Move semantics
    let s1 = String::from("hello");
    let s2 = s1;  // s1 is moved to s2
    // println!("{}", s1);  // Error: value used after move
    println!("{}", s2);

    // Clone
    let s3 = String::from("world");
    let s4 = s3.clone();
    println!("s3: {}, s4: {}", s3, s4);

    // Copy trait (stack-only types)
    let x = 5;
    let y = x;  // x is copied, not moved
    println!("x: {}, y: {}", x, y);

    // String ownership
    let name = String::from("Alice");
    let greeting = format!("Hello, {}!", name);
    println!("{}", greeting);
    println!("Name still valid: {}", name);

    // Vector ownership
    let v1 = vec![1, 2, 3];
    let v2 = v1.clone();
    println!("v1: {:?}, v2: {:?}", v1, v2);

    // Function ownership
    let s = String::from("hello");
    take_ownership(s);
    // println!("{}", s);  // Error: value moved

    let x = 5;
    make_copy(x);
    println!("x still valid: {}", x);

    // Return values
    let s1 = gives_ownership();
    let s2 = String::from("hello");
    let s3 = takes_and_gives_back(s2);
    println!("s1: {}, s3: {}", s1, s3);

    // Tuple ownership
    let t1 = (String::from("hello"), 42);
    let t2 = t1.clone();
    println!("t1: {:?}, t2: {:?}", t1, t2);

    // Ownership with structs
    let person = Person {
        name: String::from("Bob"),
        age: 30,
    };
    let person2 = person;
    println!("Person2: {:?}", person2);

    // Drop trait
    let resource = Resource { id: 1 };
    println!("Created resource {}", resource.id);
    // resource is dropped here
}

fn take_ownership(s: String) {
    println!("Taking ownership: {}", s);
}

fn make_copy(x: i32) {
    println!("Making copy: {}", x);
}

fn gives_ownership() -> String {
    String::from("hello")
}

fn takes_and_gives_back(s: String) -> String {
    s
}

#[derive(Debug)]
struct Person {
    name: String,
    age: u32,
}

struct Resource {
    id: u32,
}

impl Drop for Resource {
    fn drop(&mut self) {
        println!("Dropping resource {}", self.id);
    }
}
