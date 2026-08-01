# Serialization - Medium Exercises

## Exercise 1: Custom Serialization with writeObject/readObject

### Problem Statement
Implement custom serialization logic using `writeObject()` and `readObject()` methods.

### Requirements
1. Create a `BankAccount` class with fields: `accountNumber` (String), `balance` (double), `transactionHistory` (List<String>)
2. Implement custom serialization to:
   - Encrypt sensitive data (account number) during serialization
   - Decrypt during deserialization
   - Validate data integrity
3. Use `ObjectOutputStream.PutField` and `ObjectInputStream.GetField` for custom field handling

### Starter Code
```java
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class BankAccount implements Serializable {
    private static final long serialVersionUID = 1L;

    private String accountNumber;
    private double balance;
    private List<String> transactionHistory;
    private transient long lastAccessTime;

    public BankAccount(String accountNumber, double balance) {
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.transactionHistory = new ArrayList<>();
        this.lastAccessTime = System.currentTimeMillis();
    }

    // TODO: Implement private writeObject(ObjectOutputStream oos)

    // TODO: Implement private readObject(ObjectInputStream ois)

    // Helper methods for encryption/decryption
    private String encrypt(String data) {
        // Simple XOR encryption for demo
        // TODO: Implement encryption logic
    }

    private String decrypt(String data) {
        // TODO: Implement decryption logic
    }

    // Getters

    public void addTransaction(String transaction) {
        transactionHistory.add(transaction);
    }

    @Override
    public String toString() {
        return "BankAccount{" +
                "accountNumber='" + accountNumber + '\'' +
                ", balance=" + balance +
                ", transactions=" + transactionHistory.size() +
                '}';
    }
}
```

### Expected Behavior
```java
BankAccount original = new BankAccount("1234-5678-9012-3456", 10000.00);
original.addTransaction("Deposit: $5000");
original.addTransaction("Withdrawal: $2000");

System.out.println("Original: " + original);

// Serialize
ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("account.dat"));
oos.writeObject(original);
oos.close();

// Deserialize
ObjectInputStream ois = new ObjectInputStream(new FileInputStream("account.dat"));
BankAccount loaded = (BankAccount) ois.readObject();
ois.close();

System.out.println("Loaded: " + loaded);

// Verify custom serialization
System.out.println("Account number preserved: " + original.getAccountNumber().equals(loaded.getAccountNumber()));
System.out.println("Balance preserved: " + (original.getBalance() == loaded.getBalance()));
System.out.println("Transactions preserved: " + original.getTransactionHistory().equals(loaded.getTransactionHistory()));
```

### Hints
- `writeObject()` and `readObject()` must be `private` methods
- Call `defaultWriteObject()` or `defaultReadObject()` for default behavior
- Use `oos.defaultWriteObject()` to serialize non-transient fields
- Use `ois.defaultReadObject()` to deserialize non-transient fields

### Evaluation Criteria
- [ ] `writeObject()` and `readObject()` are implemented as private methods
- [ ] Custom serialization logic is applied
- [ ] Sensitive data is encrypted/decrypted
- [ ] Data integrity is maintained
- [ ] `transient` fields are handled appropriately

---

## Exercise 2: Serialization Proxy Pattern

### Problem Statement
Implement the serialization proxy pattern to protect object invariants during deserialization.

### Requirements
1. Create a `Money` class with fields: `amount` (double), `currency` (String)
2. Create a `MoneySerializationProxy` inner class
3. The proxy should validate data during deserialization
4. Protect against malicious deserialization attacks

### Starter Code
```java
import java.io.Serializable;
import java.util.Currency;
import java.util.Locale;

public final class Money implements Serializable {
    private static final long serialVersionUID = 1L;

    private final double amount;
    private final Currency currency;

    public Money(double amount, Currency currency) {
        if (amount < 0) {
            throw new IllegalArgumentException("Amount cannot be negative");
        }
        this.amount = amount;
        this.currency = currency;
    }

    // TODO: Implement readReplace() method

    // Getters

    @Override
    public String toString() {
        return Currency.getInstance("USD").getSymbol() + " " + amount;
    }

    // TODO: Implement MoneySerializationProxy inner class
    private static class MoneySerializationProxy implements Serializable {
        private static final long serialVersionUID = 1L;

        // TODO: Implement proxy fields and methods

        // TODO: Implement readResolve() method
    }
}
```

### Expected Behavior
```java
Money original = new Money(100.50, Currency.getInstance("USD"));
System.out.println("Original: " + original);

// Serialize
ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("money.dat"));
oos.writeObject(original);
oos.close();

// Deserialize
ObjectInputStream ois = new ObjectInputStream(new FileInputStream("money.dat"));
Money loaded = (Money) ois.readObject();
ois.close();

System.out.println("Loaded: " + loaded);

// Verify
System.out.println("Amount preserved: " + (original.getAmount() == loaded.getAmount()));
System.out.println("Currency preserved: " + original.getCurrency().equals(loaded.getCurrency()));

// Test validation
try {
    // This should fail during deserialization
    // Create a file with invalid data manually
} catch (Exception e) {
    System.out.println("Validation caught invalid data: " + e.getMessage());
}
```

### Hints
- `readReplace()` method returns the proxy object instead of the original
- The proxy's `readResolve()` method creates and returns the actual `Money` object
- This pattern protects against deserialization attacks and invariant violations
- The proxy class should be `private static`

### Evaluation Criteria
- [ ] `readReplace()` method is implemented
- [ ] `MoneySerializationProxy` class is created
- [ ] Proxy validates data during deserialization
- [ ] Invariants are protected
- [ ] Pattern is implemented correctly with proper access modifiers

---

## Exercise 3: Singleton Serialization with readResolve

### Problem Statement
Maintain singleton pattern during serialization using the `readResolve()` method.

### Requirements
1. Create a `DatabaseConnection` singleton class
2. Ensure only one instance exists even after deserialization
3. Implement `readResolve()` to return the existing instance
4. Test that deserialization doesn't create a new instance

### Starter Code
```java
import java.io.Serializable;

public class DatabaseConnection implements Serializable {
    private static final long serialVersionUID = 1L;

    private static DatabaseConnection instance;

    private String connectionUrl;
    private int maxConnections;
    private boolean isConnected;

    private DatabaseConnection() {
        this.connectionUrl = "jdbc:mysql://localhost:3306/mydb";
        this.maxConnections = 10;
        this.isConnected = false;
    }

    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    // TODO: Implement readResolve() method

    // Getters

    public void connect() {
        if (!isConnected) {
            System.out.println("Connecting to: " + connectionUrl);
            isConnected = true;
        }
    }

    public void disconnect() {
        if (isConnected) {
            System.out.println("Disconnected from database");
            isConnected = false;
        }
    }

    @Override
    public String toString() {
        return "DatabaseConnection{" +
                "url='" + connectionUrl + '\'' +
                ", connected=" + isConnected +
                ", hashCode=" + System.identityHashCode(this) +
                '}';
    }
}
```

### Expected Behavior
```java
// Get original instance
DatabaseConnection original = DatabaseConnection.getInstance();
original.connect();
System.out.println("Original: " + original);

// Serialize
ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("db.dat"));
oos.writeObject(original);
oos.close();

// Deserialize
ObjectInputStream ois = new ObjectInputStream(new FileInputStream("db.dat"));
DatabaseConnection loaded = (DatabaseConnection) ois.readObject();
ois.close();

System.out.println("Loaded: " + loaded);

// Verify singleton
System.out.println("Same instance: " + (original == loaded));
System.out.println("Same hashCode: " + (original.hashCode() == loaded.hashCode()));
```

### Hints
- `readResolve()` should return `getInstance()` to maintain singleton
- The method must be `private` and return `Object`
- Without `readResolve()`, deserialization creates a new instance
- Test by comparing hash codes or using `==` operator

### Evaluation Criteria
- [ ] Singleton pattern is implemented correctly
- [ ] `readResolve()` method is implemented
- [ ] Deserialization returns the same instance
- [ ] `==` comparison returns `true` for original and loaded
- [ ] Singleton behavior is maintained throughout lifecycle
