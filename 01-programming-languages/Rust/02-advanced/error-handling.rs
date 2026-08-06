use std::error::Error;
use std::fmt;

fn main() -> Result<(), Box<dyn Error>> {
    // Option enum
    let some_number: Option<i32> = Some(42);
    let no_number: Option<i32> = None;

    println!("Some: {:?}", some_number);
    println!("None: {:?}", no_number);

    // Option methods
    let value = some_number.unwrap_or(0);
    println!("Unwrapped: {}", value);

    let doubled = some_number.map(|x| x * 2);
    println!("Doubled: {:?}", doubled);

    let filtered = some_number.filter(|&x| x > 50);
    println!("Filtered: {:?}", filtered);

    let and_then = some_number.and_then(|x| if x > 0 { Some(x) } else { None });
    println!("And then: {:?}", and_then);

    let or_else = no_number.or_else(|| Some(100));
    println!("Or else: {:?}", or_else);

    // Result enum
    let success: Result<i32, String> = Ok(42);
    let error: Result<i32, String> = Err(String::from("Something went wrong"));

    println!("Success: {:?}", success);
    println!("Error: {:?}", error);

    // Result methods
    let value = success.unwrap_or(0);
    println!("Value: {}", value);

    let mapped = success.map(|x| x * 2);
    println!("Mapped: {:?}", mapped);

    let mapped_err = error.map_err(|e| format!("Error: {}", e));
    println!("Mapped error: {:?}", mapped_err);

    let and_then = success.and_then(|x| if x > 0 { Ok(x) } else { Err("Negative") });
    println!("And then: {:?}", and_then);

    // Custom error type
    let result = divide(10.0, 2.0);
    match result {
        Ok(value) => println!("Result: {}", value),
        Err(e) => println!("Error: {}", e),
    }

    let result = divide(10.0, 0.0);
    match result {
        Ok(value) => println!("Result: {}", value),
        Err(e) => println!("Error: {}", e),
    }

    // Error chaining
    let result = process_data("42");
    match result {
        Ok(value) => println!("Processed: {}", value),
        Err(e) => println!("Error: {}", e),
    }

    // ? operator
    let result = read_and_parse("42")?;
    println!("Parsed: {}", result);

    // Custom error with context
    let result = validate_age(25);
    println!("Age 25: {:?}", result);

    let result = validate_age(-5);
    println!("Age -5: {:?}", result);

    // Conversion between error types
    let option: Option<i32> = Some(42);
    let result: Result<i32, &str> = option.ok_or("Value missing");
    println!("Option to Result: {:?}", result);

    let result: Option<i32> = Ok(42).ok();
    println!("Result to Option: {:?}", result);

    Ok(())
}

// Option and Result basics
fn find_user(id: u32) -> Option<String> {
    match id {
        1 => Some(String::from("Alice")),
        2 => Some(String::from("Bob")),
        _ => None,
    }
}

// Custom error type
#[derive(Debug)]
enum MathError {
    DivisionByZero,
    NegativeSquareRoot,
    Overflow,
}

impl fmt::Display for MathError {
    fn fmt(&self, f: &mut fmt::Formatter<'_>) -> fmt::Result {
        match self {
            MathError::DivisionByZero => write!(f, "Division by zero"),
            MathError::NegativeSquareRoot => write!(f, "Negative square root"),
            MathError::Overflow => write!(f, "Overflow"),
        }
    }
}

impl Error for MathError {}

fn divide(a: f64, b: f64) -> Result<f64, MathError> {
    if b == 0.0 {
        Err(MathError::DivisionByZero)
    } else {
        Ok(a / b)
    }
}

// Error chaining
fn parse_number(s: &str) -> Result<i32, Box<dyn Error>> {
    let n: i32 = s.parse()?;
    Ok(n)
}

fn process_data(s: &str) -> Result<i32, Box<dyn Error>> {
    let n = parse_number(s)?;
    if n < 0 {
        Err("Negative number".into())
    } else {
        Ok(n * 2)
    }
}

// ? operator
fn read_and_parse(s: &str) -> Result<i32, Box<dyn Error>> {
    let n: i32 = s.parse()?;
    Ok(n)
}

// Custom error with context
#[derive(Debug)]
struct ValidationError {
    field: String,
    message: String,
}

impl fmt::Display for ValidationError {
    fn fmt(&self, f: &mut fmt::Formatter<'_>) -> fmt::Result {
        write!(f, "Validation error on {}: {}", self.field, self.message)
    }
}

impl Error for ValidationError {}

fn validate_age(age: i32) -> Result<i32, ValidationError> {
    if age < 0 {
        Err(ValidationError {
            field: "age".to_string(),
            message: "Age cannot be negative".to_string(),
        })
    } else if age > 150 {
        Err(ValidationError {
            field: "age".to_string(),
            message: "Age seems unrealistic".to_string(),
        })
    } else {
        Ok(age)
    }
}
