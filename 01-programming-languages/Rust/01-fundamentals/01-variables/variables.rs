// Variables in Rust

fn main() {
    // Immutable variables (default)
    let x = 5;
    println!("x = {}", x);

    // Mutable variables
    let mut y = 10;
    println!("y = {}", y);
    y = 20;
    println!("y = {}", y);

    // Type inference
    let a = 42; // i32
    let b = 3.14; // f64
    let c = true; // bool
    let d = 'A'; // char
    println!("a: {}, b: {}, c: {}, d: {}", a, b, c, d);

    // Explicit types
    let explicit_i64: i64 = 100000;
    let explicit_f32: f32 = 2.718;
    println!("i64: {}, f32: {}", explicit_i64, explicit_f32);

    // Shadowing
    let x = x + 1;
    println!("x after shadowing: {}", x);
    let x = x * 2;
    println!("x after second shadowing: {}", x);

    // Shadowing with type change
    let spaces = "   ";
    let spaces = spaces.len();
    println!("spaces: {}", spaces);

    // Constants
    const MAX_POINTS: u32 = 100_000;
    println!("MAX_POINTS: {}", MAX_POINTS);

    // Underscores for readability
    let million = 1_000_000;
    println!("million: {}", million);

    // Type ascription
    let guess: u32 = "42".parse().expect("Not a number!");
    println!("guess: {}", guess);

    // Tuple destructuring
    let tup: (i32, f64, u8) = (500, 6.4, 1);
    let (x, y, z) = tup;
    println!("tuple: x={}, y={}, z={}", x, y, z);

    // Array
    let arr = [1, 2, 3, 4, 5];
    let [a, b, c, d, e] = arr;
    println!("array: a={}, b={}, c={}, d={}, e={}", a, b, c, d, e);

    // Never type and unit type
    let unit: () = ();
    println!("unit: {:?}", unit);
}
