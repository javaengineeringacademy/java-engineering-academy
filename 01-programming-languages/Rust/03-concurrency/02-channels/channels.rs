// Channels in Rust

use std::sync::mpsc;
use std::thread;
use std::time::Duration;

fn main() {
    // Basic channel
    let (tx, rx) = mpsc::channel();

    thread::spawn(move || {
        let msg = String::from("hello");
        tx.send(msg).unwrap();
    });

    let received = rx.recv().unwrap();
    println!("received: {}", received);

    // Multiple messages
    let (tx, rx) = mpsc::channel();

    thread::spawn(move || {
        let messages = vec!["hello", "world", "from", "rust"];
        for msg in messages {
            tx.send(String::from(msg)).unwrap();
            thread::sleep(Duration::from_millis(100));
        }
    });

    for received in rx {
        println!("got: {}", received);
    }

    // Multiple producers
    let (tx, rx) = mpsc::channel();
    let tx1 = tx.clone();
    let tx2 = tx.clone();

    thread::spawn(move || {
        tx1.send(String::from("from tx1")).unwrap();
    });

    thread::spawn(move || {
        tx2.send(String::from("from tx2")).unwrap();
    });

    thread::spawn(move || {
        tx.send(String::from("from tx")).unwrap();
    });

    for received in rx {
        println!("received: {}", received);
    }
}
