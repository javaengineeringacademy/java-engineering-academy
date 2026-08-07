/*
 * Exercise: Smart Pointers in C++
 * Difficulty: ★★★★☆ (4/5)
 * Learning Objectives:
 *   - Master unique_ptr usage
 *   - Understand shared_ptr and reference counting
 *   - Learn about weak_ptr and circular references
 *   - Practice using make_unique and make_shared
 */

#include <iostream>
#include <memory>
#include <string>
#include <vector>
using namespace std;

/*
 * TODO 1: Demonstrate unique_ptr basics
 * - Create, access, transfer ownership
 * - Show that copying is not allowed
 */

/*
 * TODO 2: Create a class hierarchy for smart pointer demonstration
 * - Base class: Resource
 * - Derived classes: Database, File, Network
 */
class Resource {
public:
    Resource(const string &n) : name(n) {
        cout << "Resource " << name << " created" << endl;
    }
    virtual ~Resource() {
        cout << "Resource " << name << " destroyed" << endl;
    }
    virtual void use() {
        cout << "Using resource: " << name << endl;
    }
    string name;
};

class Database : public Resource {
public:
    Database() : Resource("Database") {}
    void use() override {
        cout << "Connecting to database..." << endl;
    }
};

class File : public Resource {
public:
    File() : Resource("File") {}
    void use() override {
        cout << "Reading file..." << endl;
    }
};

/*
 * TODO 3: Demonstrate shared_ptr
 * - Create multiple shared_ptrs to same object
 * - Show reference counting
 */

/*
 * TODO 4: Demonstrate weak_ptr
 * - Create a circular reference
 * - Show how weak_ptr breaks the cycle
 */
class Node {
public:
    string name;
    shared_ptr<Node> next;
    weak_ptr<Node> prev;  // Use weak_ptr to avoid circular reference
    Node(const string &n) : name(n) {
        cout << "Node " << name << " created" << endl;
    }
    ~Node() {
        cout << "Node " << name << " destroyed" << endl;
    }
};

/*
 * TODO 5: Implement a factory function that returns unique_ptr
 */
unique_ptr<Resource> create_resource(const string &type) {
    /* Your code here */
    return nullptr;
}

/*
 * TODO 6: Implement a class that uses smart pointers internally
 * - Use unique_ptr for exclusive ownership
 * - Use shared_ptr for shared ownership
 */
class ResourceManager {
private:
    vector<unique_ptr<Resource>> exclusive_resources;
    vector<shared_ptr<Resource>> shared_resources;

public:
    void add_exclusive(unique_ptr<Resource> res);
    void add_shared(shared_ptr<Resource> res);
    void use_all();
    int get_exclusive_count() const;
    int get_shared_count() const;
};

/*
 * TODO 7: Demonstrate custom deleters
 * - Create a unique_ptr with a custom deleter
 * - Show how to use it with C-style resources
 */

/*
 * TODO 8: Implement a smart pointer-based linked list
 * Use unique_ptr for next pointers
 */

/*
 * TODO 9: Show common pitfalls and best practices
 * - Avoid creating circular references
 * - Use make_unique/make_shared when possible
 * - Prefer unique_ptr over shared_ptr when possible
 */

int main() {
    /* Test cases */
    /*
    cout << "=== Smart Pointer Test Cases ===" << endl;

    // Test unique_ptr
    cout << "\n--- unique_ptr ---" << endl;
    unique_ptr<int> uptr = make_unique<int>(42);
    cout << "Value: " << *uptr << " (expected: 42)" << endl;

    // Transfer ownership
    unique_ptr<int> uptr2 = move(uptr);
    cout << "After move, uptr2: " << *uptr2 << endl;

    // Test factory function
    unique_ptr<Resource> res = create_resource("database");
    if (res) res->use();

    // Test shared_ptr
    cout << "\n--- shared_ptr ---" << endl;
    shared_ptr<Database> sp1 = make_shared<Database>();
    cout << "Reference count: " << sp1.use_count() << " (expected: 1)" << endl;
    {
        shared_ptr<Database> sp2 = sp1;
        cout << "Reference count: " << sp1.use_count() << " (expected: 2)" << endl;
    }
    cout << "Reference count: " << sp1.use_count() << " (expected: 1)" << endl;

    // Test weak_ptr
    cout << "\n--- weak_ptr ---" << endl;
    shared_ptr<Node> node1 = make_shared<Node>("A");
    shared_ptr<Node> node2 = make_shared<Node>("B");
    node1->next = node2;
    node2->prev = node1;  // weak_ptr, no circular reference

    cout << "Node1 ref count: " << node1.use_count() << " (expected: 1)" << endl;
    cout << "Node2 ref count: " << node2.use_count() << " (expected: 2)" << endl;

    // Test ResourceManager
    cout << "\n--- ResourceManager ---" << endl;
    ResourceManager manager;
    manager.add_exclusive(make_unique<Database>());
    manager.add_shared(make_shared<File>());
    manager.add_shared(make_shared<File>());
    cout << "Exclusive: " << manager.get_exclusive_count() << " (expected: 1)" << endl;
    cout << "Shared: " << manager.get_shared_count() << " (expected: 2)" << endl;

    // Test custom deleter
    cout << "\n--- Custom Deleter ---" << endl;
    auto deleter = [](int *p) {
        cout << "Custom delete: " << *p << endl;
        delete p;
    };
    unique_ptr<int, decltype(deleter)> custom_ptr(new int(99), deleter);
    cout << "Custom ptr: " << *custom_ptr << endl;
    */

    return 0;
}
