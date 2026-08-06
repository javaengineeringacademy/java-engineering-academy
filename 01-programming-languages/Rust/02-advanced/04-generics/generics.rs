// Generics in Rust

fn largest<T: PartialOrd>(list: &[T]) -> &T {
    let mut largest = &list[0];
    for item in &list[1..] {
        if item > largest {
            largest = item;
        }
    }
    largest
}

struct Point<T> {
    x: T,
    y: T,
}

impl<T> Point<T> {
    fn x(&self) -> &T {
        &self.x
    }
}

struct Point2<T, U> {
    x: T,
    y: U,
}

impl<T, U> Point2<T, U> {
    fn mixup(self, other: Point2<T, U>) -> Point2<T, U> {
        Point2 {
            x: self.x,
            y: other.y,
        }
    }
}

fn main() {
    let numbers = vec![34, 50, 25, 100, 65];
    println!("Largest number: {}", largest(&numbers));

    let chars = vec!['y', 'm', 'a', 'q'];
    println!("Largest char: {}", largest(&chars));

    let integer_point = Point { x: 5, y: 10 };
    let float_point = Point { x: 1.0, y: 4.5 };
    println!("Integer point x: {}", integer_point.x());
    println!("Float point x: {}", float_point.x());

    let p1 = Point2 { x: 5, y: 10.4 };
    let p2 = Point2 { x: "Hello", y: 'c' };
    let p3 = p1.mixup(p2);
    println!("p3: x = {}, y = {}", p3.x, p3.y);
}
