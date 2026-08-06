// Traits in Rust

trait Summary {
    fn summarize(&self) -> String {
        String::from("(Read more...)")
    }
}

struct NewsArticle {
    title: String,
    author: String,
    content: String,
}

impl Summary for NewsArticle {
    fn summarize(&self) -> String {
        format!("{}, by {}", self.title, self.author)
    }
}

struct Tweet {
    username: String,
    content: String,
    reply: bool,
    retweet: bool,
}

impl Summary for Tweet {
    fn summarize(&self) -> String {
        format!("{}: {}", self.username, self.content)
    }
}

fn notify(item: &impl Summary) {
    println!("Breaking news! {}", item.summarize());
}

fn notify_multi<T: Summary + std::fmt::Display>(item: &T) {
    println!("Display: {}, Summary: {}", item, item.summarize());
}

impl std::fmt::Display for Tweet {
    fn fmt(&self, f: &mut std::fmt::Formatter) -> std::fmt::Result {
        write!(f, "@{}: {}", self.username, self.content)
    }
}

impl std::fmt::Display for NewsArticle {
    fn fmt(&self, f: &mut std::fmt::Formatter) -> std::fmt::Result {
        write!(f, "{}", self.title)
    }
}

fn main() {
    let tweet = Tweet {
        username: String::from("rustlang"),
        content: String::from("Rust 1.0 released!"),
        reply: false,
        retweet: false,
    };

    let article = NewsArticle {
        title: String::from("Rust Programming"),
        author: String::from("Team Rust"),
        content: String::from("Rust is a systems language..."),
    };

    println!("Tweet: {}", tweet.summarize());
    println!("Article: {}", article.summarize());
    notify(&tweet);
    notify(&article);

    // Default trait implementation
    println!("Default: {}", tweet.summarize());
}
