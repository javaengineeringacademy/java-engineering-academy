// Threads in Rust

use std::thread;
use std::time::Duration;

fn main() {
    // Basic thread spawning
    let handle = thread::spawn(|| {
        for i in 1..=5 {
            println!("spawned thread: {}", i);
            thread::sleep(Duration::from_millis(100));
        }
    });

    for i in 1..=3 {
        println!("main thread: {}", i);
        thread::sleep(Duration::from_millis(150));
    }

    handle.join().unwrap();

    // move closure
    let v = vec![1, 2, 3];
    let handle = thread::spawn(move || {
        println!("vector: {:?}", v);
    });
    handle.join().unwrap();

    // Multiple threads
    let mut handles = vec![];
    for i in 0..5 {
        let handle = thread::spawn(move || {
            println!("thread {} spawned", i);
        });
        handles.push(handle);
    }

    for handle in handles {
        handle.join().unwrap();
    }

    println!("all threads completed");
}
