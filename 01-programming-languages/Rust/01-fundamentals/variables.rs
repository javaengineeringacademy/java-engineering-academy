fn main() {
    // Immutable variables (let)
    let name = "Rust";
    let version = 1.75;
    let is_awesome = true;
    println!("Language: {}, Version: {}, Awesome: {}", name, version, is_awesome);

    // Mutable variables (mut)
    let mut counter = 0;
    counter = 1;
    counter += 10;
    println!("Counter: {}", counter);

    // Type inference
    let inferred_int = 42;
    let inferred_float = 3.14;
    let inferred_bool = false;
    let inferred_char = 'A';
    println!("{}, {}, {}, {}", inferred_int, inferred_float, inferred_bool, inferred_char);

    // Explicit types
    let explicit_int: i32 = 100;
    let explicit_long: i64 = 1_000_000;
    let explicit_float: f32 = 2.5;
    let explicit_double: f64 = 2.5;
    let explicit_byte: u8 = 255;
    let explicit_short: u16 = 32767;
    println!("{}, {}, {}, {}, {}, {}", explicit_int, explicit_long, explicit_float, explicit_double, explicit_byte, explicit_short);

    // String formatting
    let language = "Rust";
    let year = 2015;
    println!("{} was released in {}", language, year);
    println!("Length of language: {}", language.len());
    println!("Result: {}", if year > 2010 { "Modern" } else { "Legacy" });

    // Multi-line strings
    let multi_line = "
        This is a multi-line string.
        It can span multiple lines.
    ";
    println!("{}", multi_line);

    // Constants
    const MAX_SIZE: u32 = 100;
    const APP_NAME: &str = "MyApp";
    println!("Max size: {}, App: {}", MAX_SIZE, APP_NAME);

    // Arrays
    let array = [1, 2, 3, 4, 5];
    let mutable_array = [1, 2, 3];
    println!("Array: {:?}, Mutable: {:?}", array, mutable_array);

    // Vectors
    let vector = vec![1, 2, 3, 4, 5];
    let mut mutable_vector = vec![1, 2, 3];
    mutable_vector.push(4);
    println!("Vector: {:?}, Mutable: {:?}", vector, mutable_vector);

    // Tuples
    let tuple = (1, 2.0, "hello", true);
    let (a, b, c, d) = tuple;
    println!("Tuple: {}, {}, {}, {}", a, b, c, d);

    // Destructuring
    let (x, y, z) = (10, 20, 30);
    println!("Destructured: {}, {}, {}", x, y, z);

    // Shadowing
    let shadow = 5;
    let shadow = shadow + 1;
    let shadow = shadow * 2;
    println!("Shadow: {}", shadow);

    // Type shadowing
    let spaces = "   ";
    let spaces = spaces.len();
    println!("Spaces: {}", spaces);
}
