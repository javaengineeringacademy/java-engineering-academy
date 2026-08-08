// Module 09: Design Patterns — Solutions
// Study these after attempting the exercises yourself.

#include <iostream>
#include <memory>
#include <vector>
#include <string>
#include <functional>
#include <cassert>
#include <algorithm>
#include <map>
#include <mutex>
#include <optional>

// ============================================================================
// Exercise 1 Solution: Strategy Pattern
// ============================================================================

class TextFormatter {
public:
    virtual ~TextFormatter() = default;
    virtual std::string format(const std::string& text) const = 0;
};

class UppercaseFormatter : public TextFormatter {
public:
    std::string format(const std::string& text) const override {
        std::string result = text;
        std::transform(result.begin(), result.end(), result.begin(),
                       [](unsigned char c) { return std::toupper(c); });
        return result;
    }
};

class TruncateFormatter : public TextFormatter {
    size_t max_len_;
public:
    explicit TruncateFormatter(size_t max_len) : max_len_(max_len) {}

    std::string format(const std::string& text) const override {
        if (text.size() <= max_len_) return text;
        return text.substr(0, max_len_) + "...";
    }
};

class MarkdownBoldFormatter : public TextFormatter {
public:
    std::string format(const std::string& text) const override {
        return "**" + text + "**";
    }
};

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
// Exercise 2 Solution: Observer Pattern
// ============================================================================

using ObserverCallback = std::function<void(const std::string&, int)>;

class EventBus {
    struct Observer {
        size_t id;
        ObserverCallback callback;
    };
    std::vector<Observer> observers_;
    size_t next_id_ = 0;
    std::mutex mutex_;

public:
    size_t subscribe(ObserverCallback callback) {
        std::lock_guard<std::mutex> lock(mutex_);
        size_t id = next_id_++;
        observers_.push_back({id, std::move(callback)});
        return id;
    }

    void unsubscribe(size_t id) {
        std::lock_guard<std::mutex> lock(mutex_);
        observers_.erase(
            std::remove_if(observers_.begin(), observers_.end(),
                           [id](const Observer& o) { return o.id == id; }),
            observers_.end());
    }

    void notify(const std::string& event, int value) {
        std::lock_guard<std::mutex> lock(mutex_);
        for (const auto& observer : observers_) {
            observer.callback(event, value);
        }
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
    assert(log1.size() == 1);
    assert(log2.size() == 2 && log2[1] == "price:200");
    std::cout << "Unsubscribe works: OK\n";
}

// ============================================================================
// Exercise 3 Solution: Factory Pattern
// ============================================================================

class Notification {
public:
    virtual ~Notification() = default;
    virtual std::string send() const = 0;
    virtual std::string type() const = 0;
};

class EmailNotification : public Notification {
    std::string to_;
    std::string message_;
public:
    EmailNotification(const std::string& to, const std::string& msg)
        : to_(to), message_(msg) {}

    std::string send() const override {
        return "Email to " + to_ + ": " + message_;
    }
    std::string type() const override { return "email"; }
};

class SMSNotification : public Notification {
    std::string phone_;
    std::string message_;
public:
    SMSNotification(const std::string& phone, const std::string& msg)
        : phone_(phone), message_(msg) {}

    std::string send() const override {
        return "SMS to " + phone_ + ": " + message_;
    }
    std::string type() const override { return "sms"; }
};

class PushNotification : public Notification {
    std::string device_;
    std::string message_;
public:
    PushNotification(const std::string& device, const std::string& msg)
        : device_(device), message_(msg) {}

    std::string send() const override {
        return "Push to " + device_ + ": " + message_;
    }
    std::string type() const override { return "push"; }
};

class NotificationFactory {
public:
    static std::unique_ptr<Notification> create(
        const std::string& type,
        const std::string& target,
        const std::string& message) {
        if (type == "email") {
            return std::make_unique<EmailNotification>(target, message);
        } else if (type == "sms") {
            return std::make_unique<SMSNotification>(target, message);
        } else if (type == "push") {
            return std::make_unique<PushNotification>(target, message);
        }
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
// Exercise 4 Solution: Decorator Pattern
// ============================================================================

class Logger {
public:
    virtual ~Logger() = default;
    virtual std::string log(const std::string& message) const = 0;
};

class ConsoleLogger : public Logger {
public:
    std::string log(const std::string& message) const override {
        return message;
    }
};

class TimestampDecorator : public Logger {
    std::unique_ptr<Logger> logger_;
public:
    explicit TimestampDecorator(std::unique_ptr<Logger> l) : logger_(std::move(l)) {}

    std::string log(const std::string& message) const override {
        return "[2026-08-08 12:00:00] " + logger_->log(message);
    }
};

class LevelDecorator : public Logger {
    std::unique_ptr<Logger> logger_;
    std::string level_;
public:
    LevelDecorator(std::unique_ptr<Logger> l, const std::string& level)
        : logger_(std::move(l)), level_(level) {}

    std::string log(const std::string& message) const override {
        return "[" + level_ + "] " + logger_->log(message);
    }
};

class PrefixDecorator : public Logger {
    std::unique_ptr<Logger> logger_;
    std::string prefix_;
public:
    PrefixDecorator(std::unique_ptr<Logger> l, const std::string& prefix)
        : logger_(std::move(l)), prefix_(prefix) {}

    std::string log(const std::string& message) const override {
        return "[" + prefix_ + "] " + logger_->log(message);
    }
};

void exercise4() {
    std::cout << "\n=== Exercise 4: Decorator Pattern ===\n";

    std::unique_ptr<Logger> logger = std::make_unique<ConsoleLogger>();
    logger = std::make_unique<TimestampDecorator>(std::move(logger));
    logger = std::make_unique<LevelDecorator>(std::move(logger), "INFO");
    logger = std::make_unique<PrefixDecorator>(std::move(logger), "APP");

    std::string result = logger->log("Server started");
    std::cout << "Decorated log: " << result << "\n";

    assert(result.find("APP") != std::string::npos);
    assert(result.find("INFO") != std::string::npos);
    assert(result.find("Server started") != std::string::npos);
    std::cout << "Decorator pattern: OK\n";
}

// ============================================================================
// Exercise 5 Solution: Singleton
// ============================================================================

class Config {
    std::map<std::string, std::string> settings_;
    mutable std::mutex mutex_;

    Config() = default;

public:
    Config(const Config&) = delete;
    Config& operator=(const Config&) = delete;

    static Config& getInstance() {
        static Config instance;
        return instance;
    }

    std::optional<std::string> get(const std::string& key) const {
        std::lock_guard<std::mutex> lock(mutex_);
        auto it = settings_.find(key);
        if (it != settings_.end()) return it->second;
        return std::nullopt;
    }

    void set(const std::string& key, const std::string& value) {
        std::lock_guard<std::mutex> lock(mutex_);
        settings_[key] = value;
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

    auto& config2 = Config::getInstance();
    config2.set("app.version", "1.0");
    assert(config.get("app.version") == "1.0");

    std::cout << "Singleton config: OK\n";
}

// ============================================================================
// Main
// ============================================================================

int main() {
    std::cout << "=== Module 09: Design Patterns Solutions ===\n";

    exercise1();
    exercise2();
    exercise3();
    exercise4();
    exercise5();

    std::cout << "\nAll exercises completed!\n";
    return 0;
}
