// Enums in Rust

#[derive(Debug)]
enum IpAddr {
    V4(u8, u8, u8, u8),
    V6(String),
}

#[derive(Debug)]
enum Message {
    Quit,
    Move { x: i32, y: i32 },
    Write(String),
    Color(u8, u8, u8),
}

impl Message {
    fn call(&self) {
        match self {
            Message::Quit => println!("Quit message"),
            Message::Move { x, y } => println!("Move to ({}, {})", x, y),
            Message::Write(msg) => println!("Write: {}", msg),
            Message::Color(r, g, b) => println!("Color: {}, {}, {}", r, g, b),
        }
    }
}

#[derive(Debug)]
enum Coin {
    Penny,
    Nickel,
    Dime,
    Quarter,
}

impl Coin {
    fn value_in_cents(&self) -> u32 {
        match self {
            Coin::Penny => 1,
            Coin::Nickel => 5,
            Coin::Dime => 10,
            Coin::Quarter => 25,
        }
    }
}

fn main() {
    // Basic enum
    let home = IpAddr::V4(127, 0, 0, 1);
    let loopback = IpAddr::V6(String::from("::1"));
    println!("Home: {:?}", home);
    println!("Loopback: {:?}", loopback);

    // Enum with methods
    let msg = Message::Write(String::from("hello"));
    msg.call();

    // Pattern matching with if let
    let some_value: Option<i32> = Some(42);
    if let Some(v) = some_value {
        println!("Value: {}", v);
    }

    // Coin example
    let coin = Coin::Quarter;
    println!("Quarter value: {} cents", coin.value_in_cents());

    // Option<T>
    let x: i32 = 5;
    let y: Option<i32> = Some(10);
    let sum = x + y.unwrap_or(0);
    println!("Sum: {}", sum);

    // Result<T, E>
    let result: Result<i32, String> = Ok(42);
    match result {
        Ok(v) => println!("Success: {}", v),
        Err(e) => println!("Error: {}", e),
    }

    // Exhaustive matching
    let numbers = vec![Some(1), None, Some(3)];
    for num in &numbers {
        match num {
            Some(v) => println!("Got: {}", v),
            None => println!("Nothing"),
        }
    }
}
