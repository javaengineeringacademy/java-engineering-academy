// C++ Smart Pointers

#include <iostream>
#include <memory>
using namespace std;

int main() {
    // unique_ptr
    auto unique = make_unique<int>(42);
    cout << "unique: " << *unique << endl;

    // shared_ptr
    auto shared1 = make_shared<int>(100);
    auto shared2 = shared1;
    cout << "shared: " << *shared1 << ", count: " << shared1.use_count() << endl;

    // weak_ptr
    weak_ptr<int> weak = shared1;
    if (auto locked = weak.lock()) {
        cout << "weak: " << *locked << endl;
    }

    return 0;
}
