// C++ Move Semantics

#include <iostream>
#include <string>
#include <vector>
using namespace std;

class Buffer {
private:
    int* data;
    size_t size;
public:
    Buffer(size_t s) : size(s), data(new int[s]) {
        cout << "allocated " << size << " ints" << endl;
    }
    ~Buffer() { delete[] data; cout << "freed " << size << " ints" << endl; }

    Buffer(Buffer&& other) noexcept : data(other.data), size(other.size) {
        other.data = nullptr;
        other.size = 0;
        cout << "moved buffer" << endl;
    }

    Buffer& operator=(Buffer&& other) noexcept {
        if (this != &other) {
            delete[] data;
            data = other.data;
            size = other.size;
            other.data = nullptr;
            other.size = 0;
        }
        return *this;
    }
};

int main() {
    Buffer a(10);
    Buffer b = std::move(a);

    vector<string> vec;
    string s = "hello";
    vec.push_back(std::move(s));
    cout << "s empty: " << s.empty() << endl;

    return 0;
}
