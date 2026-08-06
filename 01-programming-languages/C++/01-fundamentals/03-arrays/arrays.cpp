// C++ Arrays

#include <iostream>
#include <array>
#include <vector>
using namespace std;

int main() {
    // C-style array
    int arr[5] = {1, 2, 3, 4, 5};
    for (int i = 0; i < 5; i++) {
        cout << "arr[" << i << "]: " << arr[i] << endl;
    }

    // std::array
    array<int, 5> stdArr = {10, 20, 30, 40, 50};
    for (const auto& val : stdArr) {
        cout << "stdArr: " << val << endl;
    }

    // std::vector
    vector<int> vec = {1, 2, 3};
    vec.push_back(4);
    for (const auto& val : vec) {
        cout << "vec: " << val << endl;
    }

    // 2D array
    int matrix[3][3] = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
    for (int i = 0; i < 3; i++) {
        for (int j = 0; j < 3; j++) {
            cout << matrix[i][j] << " ";
        }
        cout << endl;
    }

    return 0;
}
