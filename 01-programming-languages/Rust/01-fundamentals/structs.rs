fn main() {
    // Basic struct
    let user = User {
        name: String::from("Alice"),
        email: String::from("alice@example.com"),
        age: 30,
        active: true,
    };
    println!("User: {:?}", user);

    // Struct with methods
    let rect = Rectangle::new(10.0, 5.0);
    println!("Area: {}", rect.area());
    println!("Perimeter: {}", rect.perimeter());
    println!("Is square: {}", rect.is_square());

    // Tuple struct
    let point = Point(3.0, 4.0);
    println!("Point: ({}, {})", point.0, point.1);
    println!("Distance from origin: {}", point.distance_from_origin());

    // Unit struct
    let marker = Marker;
    println!("Marker created");

    // Struct update syntax
    let user2 = User {
        name: String::from("Bob"),
        ..user
    };
    println!("User2: {:?}", user2);

    // Struct with lifetime
    let novel = Novel {
        title: String::from("Great Novel"),
        excerpt: "Once upon a time...",
    };
    println!("Excerpt: {}", novel.excerpt());

    // Enum basics
    let direction = Direction::North;
    match direction {
        Direction::North => println!("Going North"),
        Direction::South => println!("Going South"),
        Direction::East => println!("Going East"),
        Direction::West => println!("Going West"),
    }

    // Enum with data
    let message = Message::Quit;
    let message2 = Message::Echo(String::from("Hello"));
    let message3 = Message::Move { x: 10, y: 20 };
    let message4 = Message::Color(255, 128, 0);

    process_message(message);
    process_message(message2);
    process_message(message3);
    process_message(message4);

    // Option enum
    let some_number: Option<i32> = Some(42);
    let no_number: Option<i32> = None;

    match some_number {
        Some(n) => println!("Number: {}", n),
        None => println!("No number"),
    }

    // Option methods
    let result = some_number.unwrap_or(0);
    println!("Result: {}", result);

    // Result enum
    let success: Result<i32, String> = Ok(42);
    let error: Result<i32, String> = Err(String::from("Something went wrong"));

    match success {
        Ok(n) => println!("Success: {}", n),
        Err(e) => println!("Error: {}", e),
    }

    // Result methods
    let value = success.unwrap_or(0);
    println!("Value: {}", value);
}

#[derive(Debug)]
struct User {
    name: String,
    email: String,
    age: u32,
    active: bool,
}

struct Rectangle {
    width: f64,
    height: f64,
}

impl Rectangle {
    fn new(width: f64, height: f64) -> Self {
        Rectangle { width, height }
    }

    fn area(&self) -> f64 {
        self.width * self.height
    }

    fn perimeter(&self) -> f64 {
        2.0 * (self.width + self.height)
    }

    fn is_square(&self) -> bool {
        self.width == self.height
    }
}

struct Point(f64, f64);

impl Point {
    fn distance_from_origin(&self) -> f64 {
        (self.0.powi(2) + self.1.powi(2)).sqrt()
    }
}

struct Marker;

struct Novel<'a> {
    title: String,
    excerpt: &'a str,
}

impl<'a> Novel<'a> {
    fn excerpt(&self) -> &str {
        self.excerpt
    }
}

#[derive(Debug)]
enum Direction {
    North,
    South,
    East,
    West,
}

enum Message {
    Quit,
    Echo(String),
    Move { x: i32, y: i32 },
    Color(u8, u8, u8),
}

fn process_message(msg: Message) {
    match msg {
        Message::Quit => println!("Quit"),
        Message::Echo(s) => println!("Echo: {}", s),
        Message::Move { x, y } => println!("Move to ({}, {})", x, y),
        Message::Color(r, g, b) => println!("Color: ({}, {}, {})", r, g, b),
    }
}
