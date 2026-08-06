// C++ Classes

#include <iostream>
#include <string>
using namespace std;

class Person {
private:
    string name;
    int age;

public:
    Person(string n, int a) : name(n), age(a) {}
    ~Person() { cout << "destroyed: " << name << endl; }

    string getName() const { return name; }
    int getAge() const { return age; }

    void greet() const {
        cout << "Hello, " << name << endl;
    }
};

class Resource {
public:
    Resource() { cout << "Resource created" << endl; }
    ~Resource() { cout << "Resource destroyed" << endl; }
};

int main() {
    Person p("Alice", 30);
    p.greet();
    cout << "age: " << p.getAge() << endl;

    {
        Resource r;
    }

    return 0;
}
