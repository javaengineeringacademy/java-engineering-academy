// C++ Templates

#include <iostream>
#include <vector>
using namespace std;

template <typename T>
T max(T a, T b) {
    return (a > b) ? a : b;
}

template <typename T>
class Stack {
private:
    vector<T> data;
public:
    void push(T value) { data.push_back(value); }
    T pop() {
        T val = data.back();
        data.pop_back();
        return val;
    }
    bool empty() const { return data.empty(); }
    size_t size() const { return data.size(); }
};

int main() {
    cout << max(3, 5) << endl;
    cout << max(3.14, 2.71) << endl;

    Stack<int> s;
    s.push(1);
    s.push(2);
    s.push(3);
    cout << "size: " << s.size() << endl;
    cout << "pop: " << s.pop() << endl;

    return 0;
}
