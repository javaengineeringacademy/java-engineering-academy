// Error Handling in Rust

use std::fs;
use std::io;
use std::num::ParseIntError;

#[derive(Debug)]
enum AppError {
    IoError(io::Error),
    ParseError(ParseIntError),
    Custom(String),
}

impl std::fmt::Display for AppError {
    fn fmt(&self, f: &mut std::fmt::Formatter) -> std::fmt::Result {
        match self {
            AppError::IoError(e) => write!(f, "IO error: {}", e),
            AppError::ParseError(e) => write!(f, "Parse error: {}", e),
            AppError::Custom(msg) => write!(f, "Error: {}", msg),
        }
    }
}

impl From<io::Error> for AppError {
    fn from(error: io::Error) -> Self {
        AppError::IoError(error)
    }
}

impl From<ParseIntError> for AppError {
    fn from(error: ParseIntError) -> Self {
        AppError::ParseError(error)
    }
}

fn read_number(path: &str) -> Result<i32, AppError> {
    let content = fs::read_to_string(path)?;
    let number: i32 = content.trim().parse()?;
    Ok(number)
}

fn main() {
    // Basic Result usage
    let result: Result<i32, String> = Ok(42);
    match result {
        Ok(v) => println!("Success: {}", v),
        Err(e) => println!("Error: {}", e),
    }

    // ? operator example
    match read_number("number.txt") {
        Ok(n) => println!("Number: {}", n),
        Err(e) => println!("Failed: {}", e),
    }

    // unwrap (panics on error)
    // let value = read_number("number.txt").unwrap();

    // expect with message
    // let value = read_number("number.txt").expect("Failed to read number");

    // Custom error propagation
    let custom_err = AppError::Custom(String::from("something went wrong"));
    println!("Custom error: {}", custom_err);

    // Handling multiple error types
    fn process_data(input: &str) -> Result<i32, AppError> {
        let num: i32 = input.parse()?;
        if num < 0 {
            return Err(AppError::Custom(String::from("negative number")));
        }
        Ok(num * 2)
    }

    println!("Result: {:?}", process_data("21"));
    println!("Result: {:?}", process_data("-5"));
    println!("Result: {:?}", process_data("abc"));
}
