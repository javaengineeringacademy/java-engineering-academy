package academy.javaengineering.patterns.behavioral.strategy;

/**
 * Concrete Strategy implementation - Credit Card payment.
 * Processes payments using credit card details.
 */
public class CreditCardPayment implements PaymentStrategy {

    private final String cardNumber;
    private final String holderName;

    public CreditCardPayment(String cardNumber, String holderName) {
        this.cardNumber = cardNumber;
        this.holderName = holderName;
    }

    @Override
    public boolean pay(double amount) {
        System.out.printf("Credit Card Payment: $%.2f using card %s%n",
                amount, maskCardNumber(cardNumber));
        return true;
    }

    private String maskCardNumber(String card) {
        return "****-****-****-" + card.substring(card.length() - 4);
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getHolderName() {
        return holderName;
    }
}
