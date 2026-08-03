package academy.javaengineering.patterns.decorator;

/**
 * Demonstrates the Decorator design pattern for dynamic behavior extension.
 *
 * <p>The Decorator pattern allows adding new behaviors to objects dynamically by
 * wrapping them in decorator objects. It provides an alternative to subclassing
 * for extending functionality.</p>
 *
 * <h3>Key Concepts:</h3>
 * <ul>
 *   <li>Component interface for decorated objects</li>
 *   <li>Abstract decorator maintaining component reference</li>
 *   <li>Concrete decorators adding behavior</li>
 * </ul>
 *
 * @author Java Engineering Academy
 * @since 1.0
 */
public class DecoratorExample {

    /**
     * Component interface for coffee objects.
     */
    public interface Coffee {
        /**
         * Gets the cost of the coffee.
         *
         * @return the cost
         */
        double getCost();

        /**
         * Gets the description of the coffee.
         *
         * @return the description
         */
        String getDescription();
    }

    /**
     * Simple coffee implementation (base component).
     */
    public static class SimpleCoffee implements Coffee {
        @Override
        public double getCost() {
            return 5.00;
        }

        @Override
        public String getDescription() {
            return "Simple coffee";
        }
    }

    /**
     * Abstract decorator class for adding behavior to coffee.
     */
    public abstract static class CoffeeDecorator implements Coffee {
        protected final Coffee coffee;

        /**
         * Creates a decorator wrapping the specified coffee.
         *
         * @param coffee the coffee to decorate
         */
        protected CoffeeDecorator(Coffee coffee) {
            this.coffee = coffee;
        }

        @Override
        public double getCost() {
            return coffee.getCost();
        }

        @Override
        public String getDescription() {
            return coffee.getDescription();
        }
    }

    /**
     * Decorator adding milk to coffee.
     */
    public static class MilkDecorator extends CoffeeDecorator {
        public MilkDecorator(Coffee coffee) {
            super(coffee);
        }

        @Override
        public double getCost() {
            return super.getCost() + 1.50;
        }

        @Override
        public String getDescription() {
            return super.getDescription() + ", milk";
        }
    }

    /**
     * Decorator adding sugar to coffee.
     */
    public static class SugarDecorator extends CoffeeDecorator {
        public SugarDecorator(Coffee coffee) {
            super(coffee);
        }

        @Override
        public double getCost() {
            return super.getCost() + 0.75;
        }

        @Override
        public String getDescription() {
            return super.getDescription() + ", sugar";
        }
    }

    /**
     * Demonstrates decorator pattern usage.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        Coffee coffee = new SimpleCoffee();
        coffee = new MilkDecorator(coffee);
        coffee = new SugarDecorator(coffee);

        System.out.println(coffee.getDescription() + " = $" + coffee.getCost());
    }
}
