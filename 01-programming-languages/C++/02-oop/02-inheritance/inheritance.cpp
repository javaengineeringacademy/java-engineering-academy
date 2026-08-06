// C++ Inheritance

#include <iostream>
#include <string>
using namespace std;

class Animal {
public:
    virtual void speak() { cout << "..." << endl; }
    virtual ~Animal() { cout << "Animal destroyed" << endl; }
};

class Dog : public Animal {
public:
    void speak() override { cout << "Woof!" << endl; }
    ~Dog() override { cout << "Dog destroyed" << endl; }
};

class Cat : public Animal {
public:
    void speak() override { cout << "Meow!" << endl; }
    ~Cat() override { cout << "Cat destroyed" << endl; }
};

int main() {
    Dog dog;
    Cat cat;

    Animal* animals[] = {&dog, &cat};
    for (Animal* a : animals) {
        a->speak();
    }

    return 0;
}
