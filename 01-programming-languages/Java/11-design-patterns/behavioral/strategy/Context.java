package academy.javaengineering.patterns.behavioral.strategy;

/**
 * Context class that maintains a reference to a Strategy object.
 * Delegates the algorithm execution to the current strategy.
 */
public class Context {

    private Strategy strategy;
    private PaymentStrategy paymentStrategy;

    public Context(Strategy strategy) {
        this.strategy = strategy;
    }

    public Context(PaymentStrategy paymentStrategy) {
        this.paymentStrategy = paymentStrategy;
    }

    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }

    public void setPaymentStrategy(PaymentStrategy paymentStrategy) {
        this.paymentStrategy = paymentStrategy;
    }

    public String executeStrategy(String data) {
        return strategy.execute(data);
    }

    public boolean executePayment(double amount) {
        return paymentStrategy.pay(amount);
    }
}
