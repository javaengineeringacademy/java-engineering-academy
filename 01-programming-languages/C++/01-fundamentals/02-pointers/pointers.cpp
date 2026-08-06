// C++ Pointers

#include <iostream>
#include <memory>
using namespace std;

int main() {
    // Basic pointer
    int x = 42;
    int* ptr = &x;
    cout << "value: " << *ptr << endl;
    *ptr = 100;
    cout << "new value: " << x << endl;

    // References
    int ref = x;
    ref = 200;
    cout << "ref: " << ref << ", x: " << x << endl;

    // Smart pointers
    auto unique = make_unique<int>(42);
    cout << "unique: " << *unique << endl;

    auto shared = make_shared<int>(100);
    cout << "shared: " << *shared << endl;

    auto weak = weak_ptr<int>(shared);
    cout << "weak: " << *weak.lock() << endl;

    // Array pointer
    int arr[] = {1, 2, 3, 4, 5};
    int* arrPtr = arr;
    for (int i = 0; i < 5; i++) {
        cout << "arr[" << i << "]: " << *(arrPtr + i) << endl;
    }

    // Null pointer
    int* nullPtr = nullptr;
    if (nullPtr == nullptr) {
        cout << "null pointer" << endl;
    }

    return 0;
}
