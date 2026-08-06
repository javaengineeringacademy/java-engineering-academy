fn main() {
    // Basic enum
    let direction = Direction::North;
    println!("Direction: {:?}", direction);

    // Enum with data
    let msg = Message::Echo(String::from("Hello"));
    process_message(msg);

    // Option enum
    let some_val: Option<i32> = Some(42);
    let none_val: Option<i32> = None;

    println!("Some: {:?}", some_val);
    println!("None: {:?}", none_val);

    // Option methods
    let value = some_val.unwrap_or(0);
    println!("Unwrapped: {}", value);

    let mapped = some_val.map(|x| x * 2);
    println!("Mapped: {:?}", mapped);

    let and_then = some_val.and_then(|x| if x > 0 { Some(x) } else { None });
    println!("And then: {:?}", and_then);

    // Result enum
    let success: Result<i32, String> = Ok(42);
    let error: Result<i32, String> = Err(String::from("Error"));

    match success {
        Ok(val) => println!("Success: {}", val),
        Err(e) => println!("Error: {}", e),
    }

    // Result methods
    let value = success.unwrap_or(0);
    println!("Value: {}", value);

    let mapped = success.map(|x| x * 2);
    println!("Mapped: {:?}", mapped);

    // Custom enum with methods
    let shape = Shape::Circle(5.0);
    println!("Area: {}", shape.area());
    println!("Perimeter: {}", shape.perimeter());

    // Enum for state machine
    let mut state = State::Idle;
    println!("State: {:?}", state);

    state = state.transition(Event::Start);
    println!("After Start: {:?}", state);

    state = state.transition(Event::Process);
    println!("After Process: {:?}", state);

    state = state.transition(Event::Complete);
    println!("After Complete: {:?}", state);

    // C-like enum
    let color = Color::Red;
    println!("Color value: {}", color.value());

    // Enum with From/Into
    let num: i32 = 5;
    let even_odd = EvenOdd::from(num);
    println!("{} is {:?}", num, even_odd);

    // Pattern matching with enums
    let values = vec![
        Some(1),
        None,
        Some(2),
        None,
        Some(3),
    ];

    for val in &values {
        match val {
            Some(n) if *n > 1 => println!("Large: {}", n),
            Some(n) => println!("Small: {}", n),
            None => println!("None"),
        }
    }
}

#[derive(Debug)]
enum Direction {
    North,
    South,
    East,
    West,
}

#[derive(Debug)]
enum Message {
    Echo(String),
    Move { x: i32, y: i32 },
    Color(u8, u8, u8),
}

fn process_message(msg: Message) {
    match msg {
        Message::Echo(s) => println!("Echo: {}", s),
        Message::Move { x, y } => println!("Move to ({}, {})", x, y),
        Message::Color(r, g, b) => println!("Color: ({}, {}, {})", r, g, b),
    }
}

#[derive(Debug)]
enum Shape {
    Circle(f64),
    Rectangle(f64, f64),
    Triangle(f64, f64, f64),
}

impl Shape {
    fn area(&self) -> f64 {
        match self {
            Shape::Circle(r) => std::f64::consts::PI * r * r,
            Shape::Rectangle(w, h) => w * h,
            Shape::Triangle(a, b, c) => {
                let s = (a + b + c) / 2.0;
                (s * (s - a) * (s - b) * (s - c)).sqrt()
            }
        }
    }

    fn perimeter(&self) -> f64 {
        match self {
            Shape::Circle(r) => 2.0 * std::f64::consts::PI * r,
            Shape::Rectangle(w, h) => 2.0 * (w + h),
            Shape::Triangle(a, b, c) => a + b + c,
        }
    }
}

#[derive(Debug)]
enum State {
    Idle,
    Running,
    Completed,
    Error(String),
}

#[derive(Debug)]
enum Event {
    Start,
    Process,
    Complete,
    Fail(String),
}

impl State {
    fn transition(self, event: Event) -> State {
        match (self, event) {
            (State::Idle, Event::Start) => State::Running,
            (State::Running, Event::Process) => State::Running,
            (State::Running, Event::Complete) => State::Completed,
            (State::Running, Event::Fail(e)) => State::Error(e),
            (State::Completed, Event::Start) => State::Running,
            (State::Error(_), Event::Start) => State::Running,
            (state, _) => state,
        }
    }
}

#[derive(Debug)]
enum Color {
    Red,
    Green,
    Blue,
    Custom(u8, u8, u8),
}

impl Color {
    fn value(&self) -> u32 {
        match self {
            Color::Red => 0xFF0000,
            Color::Green => 0x00FF00,
            Color::Blue => 0x0000FF,
            Color::Custom(r, g, b) => (*r as u32) << 16 | (*g as u32) << 8 | *b as u32,
        }
    }
}

#[derive(Debug)]
enum EvenOdd {
    Even,
    Odd,
}

impl From<i32> for EvenOdd {
    fn from(n: i32) -> Self {
        if n % 2 == 0 { EvenOdd::Even } else { EvenOdd::Odd }
    }
}
