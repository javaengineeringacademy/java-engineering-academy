// Module 09: Design Patterns — Exercises
// Complete each exercise by filling in the TODO sections.
// Compile: g++ -std=c++17 -Wall -Wextra -o exercises exercises.cpp

#include <iostream>
#include <memory>
#include <vector>
#include <string>
#include <functional>
#include <cassert>
#include <algorithm>
#include <map>

// ============================================================================
// Exercise 1: Strategy Pattern
// Implement a text formatting system that can apply different formatting strategies.
// ============================================================================

// TODO: Define an interface for text formatting strategies
// It should have a method: std::string format(const std::string& text) const
class TextFormatter {
public:
    virtual ~TextFormatter() = default;
    virtual std::string format(const std::string& text) const = 0;
};

// TODO: Implement an UppercaseFormatter that converts text to uppercase
class UppercaseFormatter : public TextFormatter {
public:
    std::string format(const std::string& text) const override {
        // Your code here
        return "";
    }
};

// TODO: Implement a TruncateFormatter that truncates text to max_len characters
// and appends "..." if truncated
class TruncateFormatter : public TextFormatter {
    size_t max_len_;
public:
    explicit TruncateFormatter(size_t max_len) : max_len_(max_len) {}

    std::string format(const std::string& text) const override {
        // Your code here
        return "";
    }
};

// TODO: Implement a MarkdownBoldFormatter that wraps text in ** **
class MarkdownBoldFormatter : public TextFormatter {
public:
    std::string format(const std::string& text) const override {
        // Your code here
        return "";
    }
};

// Context that uses a formatter
class TextProcessor {
    std::unique_ptr<TextFormatter> formatter_;
public:
    void setFormatter(std::unique_ptr<TextFormatter> f) {
        formatter_ = std::move(f);
    }

    std::string process(const std::string& text) const {
        if (!formatter_) return text;
        return formatter_->format(text);
    }
};

void exercise1() {
    std::cout << "\n=== Exercise 1: Strategy Pattern ===\n";

    TextProcessor processor;

    processor.setFormatter(std::make_unique<UppercaseFormatter>());
    assert(processor.process("hello") == "HELLO");
    std::cout << "Uppercase: " << processor.process("hello") << "\n";

    processor.setFormatter(std::make_unique<TruncateFormatter>(5));
    assert(processor.process("hello world") == "hello...");
    std::cout << "Truncate: " << processor.process("hello world") << "\n";

    processor.setFormatter(std::make_unique<MarkdownBoldFormatter>());
    assert(processor.process("important") == "**important**");
    std::cout << "Bold: " << processor.process("important") << "\n";

    std::cout << "Strategy pattern: OK\n";
}

// ============================================================================
// Exercise 2: Observer Pattern
// Implement a simple event system where subscribers get notified of events.
// ============================================================================

// TODO: Implement a Subject class that manages observers
// Observers are callbacks: std::function<void(const std::string& event, int value)>
using ObserverCallback = std::function<void(const std::string&, int)>;

class EventBus {
    // Store observer callbacks
    // Your code here
public:
    // TODO: Register an observer. Returns an ID (size_t) for later unregistration.
    size_t subscribe(ObserverCallback callback) {
        // Your code here
        return 0;
    }

    // TODO: Unregister an observer by ID
    void unsubscribe(size_t id) {
        // Your code here
    }

    // TODO: Notify all observers
    void notify(const std::string& event, int value) {
        // Your code here
    }
};

void exercise2() {
    std::cout << "\n=== Exercise 2: Observer Pattern ===\n";

    EventBus bus;
    std::vector<std::string> log1, log2;

    auto id1 = bus.subscribe([&log1](const std::string& e, int v) {
        log1.push_back(e + ":" + std::to_string(v));
    });
    auto id2 = bus.subscribe([&log2](const std::string& e, int v) {
        log2.push_back(e + ":" + std::to_string(v));
    });

    bus.notify("price", 100);
    assert(log1.size() == 1 && log1[0] == "price:100");
    assert(log2.size() == 1 && log2[0] == "price:100");
    std::cout << "Both observers notified: OK\n";

    bus.unsubscribe(id1);
    bus.notify("price", 200);
    assert(log1.size() == 1);  // No new notification
    assert(log2.size() == 2 && log2[1] == "price:200");
    std::cout << "Unsubscribe works: OK\n";
}

// ============================================================================
// Exercise 3: Factory Pattern
// Create a notification system with different notification types.
// ============================================================================

// TODO: Define a Notification interface
class Notification {
public:
    virtual ~Notification() = default;
    virtual std::string send() const = 0;
    virtual std::string type() const = 0;
};

// TODO: Implement EmailNotification
class EmailNotification : public Notification {
    std::string to_;
    std::string message_;
public:
    EmailNotification(const std::string& to, const std::string& msg)
        : to_(to), message_(msg) {}

    std::string send() const override {
        // Your code here — return something like "Email to alice: Hello!"
        return "";
    }
    std::string type() const override { return "email"; }
};

// TODO: Implement SMSNotification
class SMSNotification : public Notification {
    std::string phone_;
    std::string message_;
public:
    SMSNotification(const std::string& phone, const std::string& msg)
        : phone_(phone), message_(msg) {}

    std::string send() const override {
        // Your code here — return something like "SMS to +1234: Hello!"
        return "";
    }
    std::string type() const override { return "sms"; }
};

// TODO: Implement PushNotification
class PushNotification : public Notification {
    std::string device_;
    std::string message_;
public:
    PushNotification(const std::string& device, const std::string& msg)
        : device_(device), message_(msg) {}

    std::string send() const override {
        // Your code here — return something like "Push to iPhone: Hello!"
        return "";
    }
    std::string type() const override { return "push"; }
};

// TODO: Implement NotificationFactory with a create method
// The factory should accept a type string ("email", "sms", "push")
// and the relevant parameters, returning a unique_ptr<Notification>
class NotificationFactory {
public:
    static std::unique_ptr<Notification> create(
        const std::string& type,
        const std::string& target,
        const std::string& message) {
        // Your code here
        return nullptr;
    }
};

void exercise3() {
    std::cout << "\n=== Exercise 3: Factory Pattern ===\n";

    auto email = NotificationFactory::create("email", "alice@example.com", "Hello!");
    assert(email != nullptr);
    assert(email->send() == "Email to alice@example.com: Hello!");
    assert(email->type() == "email");
    std::cout << "Email: " << email->send() << "\n";

    auto sms = NotificationFactory::create("sms", "+1234567890", "Urgent!");
    assert(sms != nullptr);
    assert(sms->send() == "SMS to +1234567890: Urgent!");
    std::cout << "SMS: " << sms->send() << "\n";

    auto push = NotificationFactory::create("push", "iPhone", "New message");
    assert(push != nullptr);
    assert(push->send() == "Push to iPhone: New message");
    std::cout << "Push: " << push->send() << "\n";

    auto invalid = NotificationFactory::create("fax", "123", "test");
    assert(invalid == nullptr);
    std::cout << "Invalid type returns nullptr: OK\n";
}

// ============================================================================
// Exercise 4: Decorator Pattern
// Build a logging system with stackable decorators.
// ============================================================================

// TODO: Define a Logger interface
class Logger {
public:
    virtual ~Logger() = default;
    virtual std::string log(const std::string& message) const = 0;
};

// TODO: Implement ConsoleLogger — returns message as-is
class ConsoleLogger : public Logger {
public:
    std::string log(const std::string& message) const override {
        return message;
    }
};

// TODO: Implement TimestampDecorator — prepends "[TIMESTAMP] "
class TimestampDecorator : public Logger {
    std::unique_ptr<Logger> logger_;
public:
    explicit TimestampDecorator(std::unique_ptr<Logger> l) : logger_(std::move(l)) {}

    std::string log(const std::string& message) const override {
        // Your code here — call logger_->log() and prepend timestamp
        return "";
    }
};

// TODO: Implement LevelDecorator — prepends "[LEVEL] " where level is set in constructor
class LevelDecorator : public Logger {
    std::unique_ptr<Logger> logger_;
    std::string level_;
public:
    LevelDecorator(std::unique_ptr<Logger> l, const std::string& level)
        : logger_(std::move(l)), level_(level) {}

    std::string log(const std::string& message) const override {
        // Your code here
        return "";
    }
};

// TODO: Implement PrefixDecorator — prepends a custom prefix
class PrefixDecorator : public Logger {
    std::unique_ptr<Logger> logger_;
    std::string prefix_;
public:
    PrefixDecorator(std::unique_ptr<Logger> l, const std::string& prefix)
        : logger_(std::move(l)), prefix_(prefix) {}

    std::string log(const std::string& message) const override {
        // Your code here
        return "";
    }
};

void exercise4() {
    std::cout << "\n=== Exercise 4: Decorator Pattern ===\n";

    // Stack decorators: Console -> Timestamp -> Level -> Prefix
    std::unique_ptr<Logger> logger = std::make_unique<ConsoleLogger>();
    logger = std::make_unique<TimestampDecorator>(std::move(logger));
    logger = std::make_unique<LevelDecorator>(std::move(logger), "INFO");
    logger = std::make_unique<PrefixDecorator>(std::move(logger), "APP");

    std::string result = logger->log("Server started");
    std::cout << "Decorated log: " << result << "\n";

    // Verify structure
    assert(result.find("APP") != std::string::npos);
    assert(result.find("INFO") != std::string::npos);
    assert(result.find("Server started") != std::string::npos);
    std::cout << "Decorator pattern: OK\n";
}

// ============================================================================
// Exercise 5: Singleton — Thread-Safe Configuration
// Implement a thread-safe singleton for application configuration.
// ============================================================================

// TODO: Implement a Config singleton that:
// - Has a static getInstance() method
// - Stores key-value pairs (string -> string)
// - Has get(key) that returns std::optional<std::string>
// - Has set(key, value) method
// - Is non-copyable, non-movable
class Config {
    std::map<std::string, std::string> settings_;
    // Your code here — mutex if needed

    Config() = default;

public:
    // TODO: Delete copy/move operations
    Config(const Config&) = delete;
    Config& operator=(const Config&) = delete;

    // TODO: Implement getInstance
    static Config& getInstance() {
        // Your code here
        static Config instance;
        return instance;
    }

    // TODO: Get a value, return std::nullopt if not found
    std::optional<std::string> get(const std::string& key) const {
        // Your code here
        return std::nullopt;
    }

    // TODO: Set a key-value pair
    void set(const std::string& key, const std::string& value) {
        // Your code here
    }
};

void exercise5() {
    std::cout << "\n=== Exercise 5: Singleton ===\n";

    auto& config = Config::getInstance();

    config.set("database.host", "localhost");
    config.set("database.port", "5432");
    config.set("app.name", "MyApp");

    assert(config.get("database.host") == "localhost");
    assert(config.get("database.port") == "5432");
    assert(config.get("app.name") == "MyApp");
    assert(!config.get("nonexistent").has_value());

    // Verify same instance
    auto& config2 = Config::getInstance();
    config2.set("app.version", "1.0");
    assert(config.get("app.version") == "1.0");

    std::cout << "Singleton config: OK\n";
}

// ============================================================================
// Main
// ============================================================================

int main() {
    std::cout << "=== Module 09: Design Patterns Exercises ===\n";

    exercise1();
    exercise2();
    exercise3();
    exercise4();
    exercise5();

    std::cout << "\nAll exercises completed!\n";
    return 0;
}
