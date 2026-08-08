// Module 02: Object-Oriented Programming — Solutions
// These are the complete solutions to the exercises.
// Study these after attempting the exercises yourself.

#include <iostream>
#include <string>
#include <vector>
#include <memory>
#include <cmath>
#include <cassert>
#include <sstream>

// ============================================================================
// Exercise 1 Solution: BankAccount with Rule of Three
// Key lesson: When a class manages raw resources (new/delete), you MUST
// implement the destructor, copy constructor, and copy assignment operator.
// ============================================================================

class BankAccount {
private:
    std::string owner_;
    double balance_;
    std::string* transactions_;  // Dynamic array of transaction descriptions
    size_t transaction_count_;
    size_t transaction_capacity_;

    // Helper: resize the transaction log
    void resize() {
        transaction_capacity_ *= 2;
        std::string* new_log = new std::string[transaction_capacity_];
        for (size_t i = 0; i < transaction_count_; ++i) {
            new_log[i] = transactions_[i];
        }
        delete[] transactions_;
        transactions_ = new_log;
    }

    // Helper: record a transaction
    void recordTransaction(const std::string& desc) {
        if (transaction_count_ >= transaction_capacity_) {
            resize();
        }
        transactions_[transaction_count_++] = desc;
    }

public:
    BankAccount(const std::string& owner, double initial_balance)
        : owner_(owner), balance_(initial_balance),
          transactions_(new std::string[4]),
          transaction_count_(0), transaction_capacity_(4) {}

    // DESTRUCTOR: Free dynamically allocated memory
    ~BankAccount() {
        delete[] transactions_;
    }

    // COPY CONSTRUCTOR: Deep copy — each object owns its own data
    BankAccount(const BankAccount& other)
        : owner_(other.owner_), balance_(other.balance_),
          transactions_(new std::string[other.transaction_capacity_]),
          transaction_count_(other.transaction_count_),
          transaction_capacity_(other.transaction_capacity_) {
        for (size_t i = 0; i < transaction_count_; ++i) {
            transactions_[i] = other.transactions_[i];
        }
    }

    // COPY ASSIGNMENT: Handle self-assignment, free old, deep copy
    BankAccount& operator=(const BankAccount& other) {
        if (this != &other) {
            // Free old data
            delete[] transactions_;

            // Copy new data
            owner_ = other.owner_;
            balance_ = other.balance_;
            transaction_capacity_ = other.transaction_capacity_;
            transaction_count_ = other.transaction_count_;
            transactions_ = new std::string[transaction_capacity_];
            for (size_t i = 0; i < transaction_count_; ++i) {
                transactions_[i] = other.transactions_[i];
            }
        }
        return *this;
    }

    void deposit(double amount) {
        if (amount > 0) {
            balance_ += amount;
            std::ostringstream oss;
            oss << "Deposit: +$" << amount << " (Balance: $" << balance_ << ")";
            recordTransaction(oss.str());
        }
    }

    void withdraw(double amount) {
        if (amount > 0 && amount <= balance_) {
            balance_ -= amount;
            std::ostringstream oss;
            oss << "Withdrawal: -$" << amount << " (Balance: $" << balance_ << ")";
            recordTransaction(oss.str());
        }
    }

    double getBalance() const { return balance_; }
    const std::string& getOwner() const { return owner_; }

    void printTransactions() const {
        std::cout << "Transactions for " << owner_ << ":\n";
        for (size_t i = 0; i < transaction_count_; ++i) {
            std::cout << "  " << (i + 1) << ". " << transactions_[i] << "\n";
        }
    }
};

void exercise1() {
    std::cout << "\n=== Exercise 1: Rule of Three ===\n";

    BankAccount acc("Alice", 1000.0);
    acc.deposit(500.0);
    acc.withdraw(200.0);
    acc.printTransactions();

    // Copy constructor test
    BankAccount acc2 = acc;
    std::cout << "\nCopied account:\n";
    acc2.printTransactions();

    // Modification should not affect original
    acc2.deposit(10000.0);
    std::cout << "\nOriginal after modifying copy:\n";
    acc.printTransactions();  // Should NOT show the $10,000 deposit
    std::cout << "Original balance: $" << acc.getBalance() << "\n";
    std::cout << "Copy balance: $" << acc2.getBalance() << "\n";
}

// ============================================================================
// Exercise 2 Solution: Shape Hierarchy
// Key lesson: Pure virtual functions make a class abstract — you cannot
// instantiate it. Each derived class MUST implement all pure virtuals.
// ============================================================================

class Shape2 {
public:
    virtual ~Shape2() = default;
    virtual double area() const = 0;
    virtual void describe() const = 0;
    virtual std::unique_ptr<Shape2> clone() const = 0;
};

class Circle2 : public Shape2 {
    double radius_;
public:
    explicit Circle2(double radius) : radius_(radius) {}

    double area() const override {
        return M_PI * radius_ * radius_;
    }

    void describe() const override {
        std::cout << "Circle with radius " << radius_;
    }

    std::unique_ptr<Shape2> clone() const override {
        return std::make_unique<Circle2>(*this);
    }
};

class Rectangle2 : public Shape2 {
    double width_, height_;
public:
    Rectangle2(double w, double h) : width_(w), height_(h) {}

    double area() const override {
        return width_ * height_;
    }

    void describe() const override {
        std::cout << "Rectangle " << width_ << "x" << height_;
    }

    std::unique_ptr<Shape2> clone() const override {
        return std::make_unique<Rectangle2>(*this);
    }
};

class Triangle2 : public Shape2 {
    double base_, height_;
public:
    Triangle2(double base, double height) : base_(base), height_(height) {}

    double area() const override {
        return 0.5 * base_ * height_;
    }

    void describe() const override {
        std::cout << "Triangle base " << base_ << " height " << height_;
    }

    std::unique_ptr<Shape2> clone() const override {
        return std::make_unique<Triangle2>(*this);
    }
};

void exercise2() {
    std::cout << "\n=== Exercise 2: Shape Hierarchy ===\n";

    Circle2 c(5.0);
    Rectangle2 r(3.0, 4.0);
    Triangle2 t(6.0, 2.0);

    // Polymorphic usage — same interface, different behavior
    std::vector<std::unique_ptr<Shape2>> shapes;
    shapes.push_back(c.clone());
    shapes.push_back(r.clone());
    shapes.push_back(t.clone());

    double total = 0;
    for (const auto& s : shapes) {
        s->describe();
        std::cout << " — Area: " << s->area() << "\n";
        total += s->area();
    }
    std::cout << "Total area: " << total << "\n";
}

// ============================================================================
// Exercise 3 Solution: Composition — Car and Engine
// Key lesson: Composition gives you explicit control over lifetime and
// ownership. The Car OWNS its Engine — when Car is destroyed, Engine is too.
// ============================================================================

class Transmission2 {
    int current_gear_;
    int max_gears_;
public:
    explicit Transmission2(int max_gears = 6)
        : current_gear_(0), max_gears_(max_gears) {}

    void shift_up() {
        if (current_gear_ < max_gears_) {
            ++current_gear_;
            std::cout << "  Shifted up to gear " << current_gear_ << "\n";
        } else {
            std::cout << "  Already at max gear " << max_gears_ << "\n";
        }
    }

    void shift_down() {
        if (current_gear_ > 0) {
            --current_gear_;
            if (current_gear_ == 0)
                std::cout << "  Shifted to neutral\n";
            else
                std::cout << "  Shifted down to gear " << current_gear_ << "\n";
        } else {
            std::cout << "  Already in neutral\n";
        }
    }

    int get_gear() const { return current_gear_; }
};

class Car2 {
    std::string model_;
    Engine engine_;
    Transmission2 transmission_;
    bool moving_;
public:
    Car2(const std::string& model, int hp, int gears = 6)
        : model_(model), engine_(hp), transmission_(gears), moving_(false) {}

    void start() {
        std::cout << model_ << ": ";
        engine_.start();
    }

    void stop() {
        std::cout << model_ << ": ";
        if (moving_) {
            moving_ = false;
            std::cout << "Car stopped\n";
        }
        transmission_.shift_down();  // Return to neutral
        engine_.stop();
    }

    void accelerate() {
        if (engine_.isRunning()) {
            std::cout << model_ << ": ";
            transmission_.shift_up();
            moving_ = true;
        } else {
            std::cout << model_ << ": Engine not running!\n";
        }
    }

    void brake() {
        if (moving_) {
            std::cout << model_ << ": ";
            transmission_.shift_down();
            if (transmission_.get_gear() == 0) {
                moving_ = false;
                std::cout << "  Car stopped\n";
            }
        }
    }

    void status() const {
        std::cout << model_
                  << " | Engine: " << (engine_.isRunning() ? "ON" : "OFF")
                  << " (" << engine_.getHorsepower() << " HP)"
                  << " | Gear: " << transmission_.get_gear()
                  << " | Moving: " << (moving_ ? "YES" : "NO") << "\n";
    }
};

void exercise3() {
    std::cout << "\n=== Exercise 3: Composition ===\n";

    Car2 car("Tesla Model 3", 283);
    car.status();
    car.start();
    car.accelerate();
    car.accelerate();
    car.accelerate();
    car.status();
    car.brake();
    car.brake();
    car.brake();
    car.status();
    car.stop();
    car.status();
}

// ============================================================================
// Exercise 4 Solution: Encapsulation — Temperature
// Key lesson: Encapsulation protects invariants. By making celsius_ private
// and controlling access through methods, we ensure temperature is always
// valid (>= -273.15).
// ============================================================================

class Temperature2 {
    static constexpr double ABSOLUTE_ZERO = -273.15;

    double celsius_;

    bool isValid(double c) const {
        return c >= ABSOLUTE_ZERO;
    }

public:
    explicit Temperature2(double c) : celsius_(0.0) {
        if (!isValid(c)) {
            throw std::invalid_argument(
                "Temperature below absolute zero: " + std::to_string(c));
        }
        celsius_ = c;
    }

    double getCelsius() const { return celsius_; }

    double getFahrenheit() const {
        return celsius_ * 9.0 / 5.0 + 32.0;
    }

    void setCelsius(double c) {
        if (!isValid(c)) {
            throw std::invalid_argument(
                "Temperature below absolute zero: " + std::to_string(c));
        }
        celsius_ = c;
    }

    void setFahrenheit(double f) {
        double c = (f - 32.0) * 5.0 / 9.0;
        setCelsius(c);  // Reuse validation
    }

    std::string toString() const {
        std::ostringstream oss;
        oss << celsius_ << "°C (" << getFahrenheit() << "°F)";
        return oss.str();
    }
};

void exercise4() {
    std::cout << "\n=== Exercise 4: Encapsulation ===\n";

    Temperature2 t(100.0);
    std::cout << "Boiling point: " << t.toString() << "\n";

    t.setFahrenheit(32.0);
    std::cout << "Freezing point: " << t.toString() << "\n";

    // Uncomment to test validation:
    // Temperature2 invalid(-300.0);  // Throws exception
    // t.setCelsius(-500.0);          // Throws exception
}

// ============================================================================
// Exercise 5 Solution: Polymorphic Container
// Key lesson: A vector of unique_ptr<Base> lets you store ANY derived type
// and call virtual functions polymorphically — the right version runs.
// ============================================================================

void exercise5() {
    std::cout << "\n=== Exercise 5: Polymorphic Container ===\n";

    std::vector<std::unique_ptr<Shape2>> shapes;

    shapes.push_back(std::make_unique<Circle2>(5.0));
    shapes.push_back(std::make_unique<Rectangle2>(3.0, 4.0));
    shapes.push_back(std::make_unique<Triangle2>(6.0, 2.0));

    double total_area = 0;
    for (const auto& shape : shapes) {
        shape->describe();
        double a = shape->area();
        std::cout << " — Area: " << a << "\n";
        total_area += a;
    }
    std::cout << "Total area: " << total_area << "\n";

    // Clone one shape
    auto cloned = shapes[0]->clone();
    std::cout << "\nCloned: ";
    cloned->describe();
    std::cout << "\n";

    shapes.push_back(std::move(cloned));
    std::cout << "Total shapes after clone: " << shapes.size() << "\n";
}

// ============================================================================
// Main
// ============================================================================

int main() {
    std::cout << "=== Module 02: OOP Solutions ===\n";

    exercise1();
    exercise2();
    exercise3();
    exercise4();
    exercise5();

    std::cout << "\nAll exercises completed!\n";
    return 0;
}
