fn main() {
    // Basic trait
    let circle = Circle { radius: 5.0 };
    let rectangle = Rectangle { width: 10.0, height: 5.0 };

    println!("Circle area: {}", circle.area());
    println!("Rectangle area: {}", rectangle.area());

    // Trait as parameter
    print_area(&circle);
    print_area(&rectangle);

    // Default implementation
    let dog = Dog { name: String::from("Rex") };
    let cat = Cat { name: String::from("Whiskers") };

    dog.speak();
    cat.speak();

    // Trait with generic
    let vec1 = vec![1, 2, 3];
    let vec2 = vec![4, 5, 6];
    println!("Sum: {}", vec1.sum());

    // Trait bounds
    print_largest(&vec![1, 2, 3, 4, 5]);
    print_largest(&vec!['a', 'b', 'c', 'd']);

    // Multiple trait bounds
    fn compare_and_print<T: std::fmt::Display + PartialOrd>(a: T, b: T) {
        if a > b {
            println!("{} > {}", a, b);
        } else {
            println!("{} <= {}", a, b);
        }
    }

    compare_and_print(5, 3);
    compare_and_print("hello", "world");

    // Where clause
    fn process<T>(item: T) -> String
    where
        T: std::fmt::Display + Clone,
    {
        format!("Processed: {}", item)
    }

    println!("{}", process(42));

    // Trait object
    let shapes: Vec<Box<dyn Shape>> = vec![
        Box::new(Circle { radius: 5.0 }),
        Box::new(Rectangle { width: 10.0, height: 5.0 }),
    ];

    for shape in &shapes {
        println!("Area: {}", shape.area());
    }

    // Associated types
    let point = Point { x: 3.0, y: 4.0 };
    println!("Distance: {}", point.distance_from_origin());

    // Blanket implementation
    println!("Sum of 1..=5: {}", (1..=5).sum::<i32>());

    // Derive trait
    #[derive(Debug, Clone, PartialEq)]
    struct Person {
        name: String,
        age: u32,
    }

    let p1 = Person { name: String::from("Alice"), age: 30 };
    let p2 = p1.clone();
    println!("Equal: {}", p1 == p2);
    println!("Debug: {:?}", p1);
}

// Basic trait
trait Shape {
    fn area(&self) -> f64;
    fn perimeter(&self) -> f64;

    fn description(&self) -> String {
        format!("Shape with area {}", self.area())
    }
}

struct Circle { radius: f64 }
struct Rectangle { width: f64, height: f64 }

impl Shape for Circle {
    fn area(&self) -> f64 {
        std::f64::consts::PI * self.radius * self.radius
    }

    fn perimeter(&self) -> f64 {
        2.0 * std::f64::consts::PI * self.radius
    }
}

impl Shape for Rectangle {
    fn area(&self) -> f64 {
        self.width * self.height
    }

    fn perimeter(&self) -> f64 {
        2.0 * (self.width + self.height)
    }
}

fn print_area(shape: &impl Shape) {
    println!("Area: {}", shape.area());
}

// Default implementation
trait Animal {
    fn speak(&self);
    fn name(&self) -> &str;

    fn introduce(&self) {
        println!("I am {} and I say:", self.name());
        self.speak();
    }
}

struct Dog { name: String }
struct Cat { name: String }

impl Animal for Dog {
    fn speak(&self) { println!("Woof!"); }
    fn name(&self) -> &str { &self.name }
}

impl Animal for Cat {
    fn speak(&self) { println!("Meow!"); }
    fn name(&self) -> &str { &self.name }
}

// Generic trait
trait Summable {
    fn sum(&self) -> i32;
}

impl Summable for Vec<i32> {
    fn sum(&self) -> i32 {
        self.iter().sum()
    }
}

// Trait bounds
fn print_largest<T: PartialOrd>(list: &[T]) {
    let mut largest = &list[0];
    for item in &list[1..] {
        if item > largest {
            largest = item;
        }
    }
    println!("Largest: {:?}", largest);
}

// Associated types
trait Distance {
    type Output;
    fn distance_from_origin(&self) -> Self::Output;
}

struct Point { x: f64, y: f64 }

impl Distance for Point {
    type Output = f64;

    fn distance_from_origin(&self) -> f64 {
        (self.x.powi(2) + self.y.powi(2)).sqrt()
    }
}
