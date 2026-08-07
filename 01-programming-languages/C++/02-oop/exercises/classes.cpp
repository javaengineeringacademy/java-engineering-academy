/*
 * Exercise: Classes in C++
 * Difficulty: ★★★☆☆ (3/5)
 * Learning Objectives:
 *   - Define and use classes
 *   - Understand access specifiers (public, private, protected)
 *   - Implement constructors and destructors
 *   - Practice encapsulation and data hiding
 */

#include <iostream>
#include <string>
using namespace std;

/*
 * TODO 1: Create a Rectangle class with:
 * - Private members: width, height (double)
 * - Public constructor(s)
 * - Public methods: area(), perimeter(), get_width(), get_height()
 * - Method to set dimensions with validation
 */

/*
 * TODO 2: Create a BankAccount class with:
 * - Private members: owner_name, balance (double), account_id (int)
 * - Public constructor(s)
 * - Methods: deposit(), withdraw(), get_balance(), display()
 * - Validation: cannot withdraw more than balance
 */

/*
 * TODO 3: Create a Student class with:
 * - Private members: name, grades (array or vector), gpa
 * - Public constructor(s)
 * - Methods: add_grade(), calculate_gpa(), display_info()
 * - Use encapsulation to protect grade data
 */

/*
 * TODO 4: Implement the Rule of Three for a class that manages dynamic memory
 * - Destructor
 * - Copy constructor
 * - Copy assignment operator
 */

/*
 * TODO 5: Create a static member variable and static method
 * - Counter class that tracks number of instances
 */

/*
 * TODO 6: Implement a friend function for a class
 * - Allow a function to access private members
 */

/*
 * TODO 7: Create an operator overloading for your class
 * - Overload + operator for adding two objects
 * - Overload << for printing
 */

int main() {
    /* Test cases */
    /*
    cout << "=== Class Test Cases ===" << endl;

    // Test Rectangle
    Rectangle rect(5.0, 3.0);
    cout << "Rectangle area: " << rect.area() << " (expected: 15)" << endl;
    cout << "Rectangle perimeter: " << rect.perimeter() << " (expected: 16)" << endl;

    // Test BankAccount
    BankAccount account("John Doe", 1000.0);
    account.deposit(500.0);
    cout << "Balance after deposit: " << account.get_balance() << " (expected: 1500)" << endl;
    account.withdraw(200.0);
    cout << "Balance after withdrawal: " << account.get_balance() << " (expected: 1300)" << endl;

    // Test Student
    Student student("Alice");
    student.add_grade(95.0);
    student.add_grade(87.5);
    student.add_grade(92.0);
    cout << "Student GPA: " << student.calculate_gpa() << endl;

    // Test static counter
    cout << "Number of students: " << Student::get_count() << endl;
    */

    return 0;
}
