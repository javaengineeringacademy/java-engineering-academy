// Async Programming in Rust

use tokio::time::{sleep, Duration};

async fn fetch_data(id: u32) -> String {
    sleep(Duration::from_millis(100)).await;
    format!("data_{}", id)
}

async fn process_all() {
    let mut handles = vec![];

    for i in 1..=5 {
        handles.push(tokio::spawn(async move {
            let data = fetch_data(i).await;
            println!("processed: {}", data);
        }));
    }

    for handle in handles {
        handle.await.unwrap();
    }
}

#[tokio::main]
async fn main() {
    // Basic async
    let data = fetch_data(1).await;
    println!("got: {}", data);

    // Multiple tasks
    process_all().await;

    println!("all done");
}
