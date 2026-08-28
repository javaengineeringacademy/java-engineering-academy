package academy.javaengineering.modern.sealed;

/**
 * Practice exercises for Sealed Classes.
 */
public class SealedClassesPractice {

    /*
     * Exercise 1: Create a Sealed Payment Hierarchy
     * 
     * Create a sealed interface Payment with permits for:
     * - CreditCard (cardNumber, expiry)
     * - BankTransfer (accountNumber, routingNumber)
     * - PayPal (email)
     * - Cryptocurrency (walletAddress, currency)
     * 
     * Write a method that processes each payment type differently
     */

    /*
     * Exercise 2: Create a Sealed Result Type
     * 
     * Create a sealed interface Result<T> with:
     * - Success<T>(T value)
     * - Failure<T>(Exception error)
     * 
     * Add methods:
     * - map() - transform success value
     * - flatMap() - chain operations
     * - orElse() - provide default value
     */

    /*
     * Exercise 3: Create a Sealed AST
     * 
     * Create a sealed interface Expression for a simple calculator:
     * - Number(double value)
     * - Add(Expression left, Expression right)
     * - Multiply(Expression left, Expression right)
     * - Negate(Expression operand)
     * 
     * Write an eval() method that computes the result
     */

    /*
     * Exercise 4: Create a Sealed Permission System
     * 
     * Create a sealed interface Permission with:
     * - Read(resource)
     * - Write(resource)
     * - Execute(resource)
     * - Admin(resource)
     * 
     * Write a method that checks if a user has a specific permission
     */
}
