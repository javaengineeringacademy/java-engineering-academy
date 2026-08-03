use std::collections::HashMap;

fn main() {
    // Variables
    let name: &str = "Rust";
    let version: f64 = 1.75;
    println!("Language: {}, Version: {:.2}", name, version);

    // Ownership
    let s1 = String::from("hello");
    let s2 = s1.clone(); // Deep copy
    println!("s1: {}, s2: {}", s1, s2);

    // Structs
    struct Person {
        name: String,
        age: u32,
    }
    let p = Person {
        name: String::from("Alice"),
        age: 30,
    };
    println!("Person: {} is {} years old", p.name, p.age);

    // Enums
    enum Direction {
        North,
        South,
        East,
        West,
    }
    let dir = Direction::North;

    // Pattern matching
    match dir {
        Direction::North => println!("Going North"),
        Direction::South => println!("Going South"),
        Direction::East => println!("Going East"),
        Direction::West => println!("Going West"),
    }

    // Vectors
    let mut numbers = vec![1, 2, 3, 4, 5];
    numbers.push(6);
    println!("Numbers: {:?}", numbers);

    // HashMap
    let mut languages = HashMap::new();
    languages.insert("rust", "Rust");
    languages.insert("go", "Golang");
    println!("Languages: {:?}", languages);
}
