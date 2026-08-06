// Structs in Rust

struct User {
    username: String,
    email: String,
    active: bool,
    sign_in_count: u64,
}

struct Color(i32, i32, i32);
struct Point(i32, i32, i32);

struct Rectangle {
    width: u32,
    height: u32,
}

impl Rectangle {
    // Associated function (constructor)
    fn new(width: u32, height: u32) -> Self {
        Self { width, height }
    }

    // Method
    fn area(&self) -> u32 {
        self.width * self.height
    }

    // Method with mutable self
    fn scale(&mut self, factor: u32) {
        self.width *= factor;
        self.height *= factor;
    }

    // Method that consumes self
    fn into_square(self) -> Rectangle {
        let side = self.width.max(self.height);
        Rectangle::new(side, side)
    }
}

impl Rectangle {
    fn can_hold(&self, other: &Rectangle) -> bool {
        self.width > other.width && self.height > other.height
    }
}

fn main() {
    // Basic struct
    let user1 = User {
        username: String::from("alice"),
        email: String::from("alice@example.com"),
        active: true,
        sign_in_count: 1,
    };
    println!("User: {} ({})", user1.username, user1.email);

    // Struct update syntax
    let user2 = User {
        email: String::from("bob@example.com"),
        ..user1 // user1.username is moved here
    };
    println!("User2: {} ({})", user2.username, user2.email);

    // Tuple structs
    let black = Color(0, 0, 0);
    let origin = Point(0, 0, 0);
    println!("Black: ({}, {}, {})", black.0, black.1, black.2);

    // Struct with impl
    let mut rect = Rectangle::new(10, 5);
    println!("Area: {}", rect.area());

    rect.scale(2);
    println!("Scaled area: {}", rect.area());

    // Method comparison
    let rect1 = Rectangle::new(10, 10);
    let rect2 = Rectangle::new(5, 5);
    println!("Can hold: {}", rect1.can_hold(&rect2));

    // Debug trait
    println!("Rectangle: {:?}", rect1);
}
