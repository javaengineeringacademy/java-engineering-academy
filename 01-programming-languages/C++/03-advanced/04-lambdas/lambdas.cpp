// C++ Lambdas

#include <iostream>
#include <vector>
#include <algorithm>
using namespace std;

int main() {
    auto add = [](int a, int b) { return a + b; };
    cout << "add: " << add(2, 3) << endl;

    int x = 10;
    auto f = [x]() { cout << "x: " << x << endl; };
    f();

    vector<int> nums = {3, 1, 4, 1, 5, 9};
    sort(nums.begin(), nums.end(), [](int a, int b) { return a > b; });
    for (int n : nums) cout << n << " ";
    cout << endl;

    return 0;
}
