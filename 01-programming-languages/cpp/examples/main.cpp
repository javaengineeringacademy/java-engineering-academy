#include <iostream>
#include <vector>
#include <algorithm>
#include <memory>

class Person {
private:
    std::string name;
    int age;

public:
    Person(std::string n, int a) : name(n), age(a) {}
    
    void greet() const {
        std::cout << "Hello, I'm " << name << "!" << std::endl;
    }
    
    virtual ~Person() = default;
};

int main() {
    // Variables
    std::string name = "C++";
    int version = 23;
    std::cout << "Language: " << name << ", Version: " << version << std::endl;

    // Vectors
    std::vector<int> numbers = {1, 2, 3, 4, 5};
    std::vector<int> doubled;
    std::transform(numbers.begin(), numbers.end(), std::back_inserter(doubled),
        [](int x) { return x * 2; });
    
    std::cout << "Doubled: ";
    for (int n : doubled) std::cout << n << " ";
    std::cout << std::endl;

    // Smart Pointers
    auto person = std::make_unique<Person>("Alice", 30);
    person->greet();

    // Lambda
    auto add = [](int a, int b) { return a + b; };
    std::cout << "Add: " << add(5, 3) << std::endl;

    return 0;
}
