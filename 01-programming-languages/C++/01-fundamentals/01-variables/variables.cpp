// C++ Variables

#include <iostream>
#include <string>
using namespace std;

int main() {
    // Basic types
    int x = 42;
    double pi = 3.14;
    bool flag = true;
    char c = 'A';
    string name = "C++";

    cout << "int: " << x << endl;
    cout << "double: " << pi << endl;
    cout << "bool: " << flag << endl;
    cout << "char: " << c << endl;
    cout << "string: " << name << endl;

    // Type inference
    auto a = 42;
    auto b = 3.14;
    auto d = "hello";
    cout << "auto int: " << a << endl;
    cout << "auto double: " << b << endl;

    // References
    int ref = x;
    ref = 100;
    cout << "ref: " << ref << ", x: " << x << endl;

    // Constants
    const int MAX = 100;
    constexpr int SIZE = 50;
    cout << "MAX: " << MAX << ", SIZE: " << SIZE << endl;

    return 0;
}
