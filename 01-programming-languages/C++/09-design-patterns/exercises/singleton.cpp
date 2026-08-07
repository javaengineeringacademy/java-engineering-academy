/*
 * Exercise: Design Patterns in C++
 * Difficulty: ★★★★★ (5/5)
 * Learning Objectives:
 *   - Implement Singleton pattern
 *   - Understand Factory pattern
 *   - Practice Observer pattern
 *   - Learn about Strategy pattern
 */

#include <iostream>
#include <string>
#include <vector>
#include <map>
#include <memory>
#include <functional>
using namespace std;

/*
 * TODO 1: Implement Singleton pattern
 * - Ensure only one instance exists
 * - Provide global access point
 * - Use thread-safe implementation
 */
class Singleton {
private:
    static Singleton* instance;
    int data;

    Singleton() : data(0) {}
    ~Singleton() = default;
    Singleton(const Singleton&) = delete;
    Singleton& operator=(const Singleton&) = delete;

public:
    static Singleton* get_instance();
    int get_data() const;
    void set_data(int value);
};

Singleton* Singleton::instance = nullptr;

Singleton* Singleton::get_instance() {
    /* Your code here - implement thread-safe singleton */
    return nullptr;
}

/*
 * TODO 2: Implement Factory pattern
 * - Create objects without specifying exact class
 * - Use virtual functions for polymorphism
 */
class Product {
public:
    virtual ~Product() = default;
    virtual string get_name() const = 0;
    virtual void use() = 0;
};

class ConcreteProductA : public Product {
public:
    string get_name() const override { return "Product A"; }
    void use() override { cout << "Using Product A" << endl; }
};

class ConcreteProductB : public Product {
public:
    string get_name() const override { return "Product B"; }
    void use() override { cout << "Using Product B" << endl; }
};

class Factory {
public:
    static unique_ptr<Product> create_product(const string &type);
};

unique_ptr<Product> Factory::create_product(const string &type) {
    /* Your code here */
    return nullptr;
}

/*
 * TODO 3: Implement Observer pattern
 * - Subject maintains list of observers
 * - Observers get notified of changes
 */
class Observer {
public:
    virtual ~Observer() = default;
    virtual void update(const string &message) = 0;
};

class Subject {
private:
    vector<Observer*> observers;
    string state;

public:
    void attach(Observer *observer);
    void detach(Observer *observer);
    void notify();
    void set_state(const string &new_state);
    string get_state() const;
};

class ConcreteObserver : public Observer {
private:
    string name;
    string last_message;

public:
    ConcreteObserver(const string &n);
    void update(const string &message) override;
    string get_last_message() const;
};

/*
 * TODO 4: Implement Strategy pattern
 * - Define a family of algorithms
 * - Encapsulate each algorithm
 */
class SortStrategy {
public:
    virtual ~SortStrategy() = default;
    virtual void sort(vector<int> &data) = 0;
    virtual string get_name() const = 0;
};

class BubbleSortStrategy : public SortStrategy {
public:
    void sort(vector<int> &data) override;
    string get_name() const override { return "Bubble Sort"; }
};

class QuickSortStrategy : public SortStrategy {
public:
    void sort(vector<int> &data) override;
    string get_name() const override { return "Quick Sort"; }
};

class Sorter {
private:
    unique_ptr<SortStrategy> strategy;

public:
    void set_strategy(unique_ptr<SortStrategy> s);
    void perform_sort(vector<int> &data);
};

/*
 * TODO 5: Implement Builder pattern
 * - Construct complex objects step by step
 */
class Meal {
public:
    string main_dish;
    string side_dish;
    string drink;
    string dessert;

    void display() const {
        cout << "Meal: " << main_dish << ", " << side_dish
             << ", " << drink << ", " << dessert << endl;
    }
};

class MealBuilder {
private:
    Meal meal;

public:
    MealBuilder& set_main(const string &main);
    MealBuilder& set_side(const string &side);
    MealBuilder& set_drink(const string &drink);
    MealBuilder& set_dessert(const string &dessert);
    Meal build();
};

/*
 * TODO 6: Implement Decorator pattern
 * - Add responsibilities to objects dynamically
 */
class Coffee {
public:
    virtual ~Coffee() = default;
    virtual double get_cost() const = 0;
    virtual string get_description() const = 0;
};

class SimpleCoffee : public Coffee {
public:
    double get_cost() const override { return 5.0; }
    string get_description() const override { return "Simple coffee"; }
};

class CoffeeDecorator : public Coffee {
protected:
    unique_ptr<Coffee> coffee;

public:
    CoffeeDecorator(unique_ptr<Coffee> c) : coffee(move(c)) {}
};

class MilkDecorator : public CoffeeDecorator {
public:
    using CoffeeDecorator::CoffeeDecorator;
    double get_cost() const override;
    string get_description() const override;
};

int main() {
    /* Test cases */
    /*
    cout << "=== Design Patterns Test Cases ===" << endl;

    // Test Singleton
    cout << "\n--- Singleton ---" << endl;
    Singleton *s1 = Singleton::get_instance();
    Singleton *s2 = Singleton::get_instance();
    s1->set_data(42);
    cout << "Same instance: " << (s1 == s2) << " (expected: 1)" << endl;
    cout << "Data: " << s2->get_data() << " (expected: 42)" << endl;

    // Test Factory
    cout << "\n--- Factory ---" << endl;
    unique_ptr<Product> p1 = Factory::create_product("A");
    unique_ptr<Product> p2 = Factory::create_product("B");
    if (p1) cout << "Created: " << p1->get_name() << endl;
    if (p2) cout << "Created: " << p2->get_name() << endl;

    // Test Observer
    cout << "\n--- Observer ---" << endl;
    Subject subject;
    ConcreteObserver obs1("Observer1");
    ConcreteObserver obs2("Observer2");
    subject.attach(&obs1);
    subject.attach(&obs2);
    subject.set_state("State 1");
    cout << "obs1 last message: " << obs1.get_last_message() << endl;
    cout << "obs2 last message: " << obs2.get_last_message() << endl;

    // Test Strategy
    cout << "\n--- Strategy ---" << endl;
    Sorter sorter;
    vector<int> data = {5, 2, 8, 1, 9};

    sorter.set_strategy(make_unique<BubbleSortStrategy>());
    sorter.perform_sort(data);
    cout << "After Bubble Sort: ";
    for (int n : data) cout << n << " ";
    cout << endl;

    data = {5, 2, 8, 1, 9};
    sorter.set_strategy(make_unique<QuickSortStrategy>());
    sorter.perform_sort(data);
    cout << "After Quick Sort: ";
    for (int n : data) cout << n << " ";
    cout << endl;

    // Test Builder
    cout << "\n--- Builder ---" << endl;
    Meal meal = MealBuilder()
        .set_main("Steak")
        .set_side("Salad")
        .set_drink("Wine")
        .set_dessert("Cake")
        .build();
    meal.display();

    // Test Decorator
    cout << "\n--- Decorator ---" << endl;
    unique_ptr<Coffee> coffee = make_unique<SimpleCoffee>();
    coffee = make_unique<MilkDecorator>(move(coffee));
    cout << coffee->get_description() << ": $" << coffee->get_cost() << endl;
    */

    return 0;
}
