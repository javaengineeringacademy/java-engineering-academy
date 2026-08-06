// C++ Strings

#include <iostream>
#include <string>
#include <string_view>
using namespace std;

int main() {
    // Basic string
    string s = "hello";
    s += " world";
    cout << "string: " << s << endl;
    cout << "length: " << s.length() << endl;

    // Substring
    string sub = s.substr(0, 5);
    cout << "substr: " << sub << endl;

    // Find
    size_t pos = s.find("world");
    if (pos != string::npos) {
        cout << "found at: " << pos << endl;
    }

    // String view (C++17)
    string_view sv = "hello view";
    cout << "string_view: " << sv << endl;
    cout << "sv length: " << sv.length() << endl;

    // Iteration
    for (char c : s) {
        cout << c << " ";
    }
    cout << endl;

    // Concatenation
    string a = "hello";
    string b = "world";
    string c = a + " " + b;
    cout << "concat: " << c << endl;

    return 0;
}
