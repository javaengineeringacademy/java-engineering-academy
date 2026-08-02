package academy.javaengineering.patterns.decorator;

public class DecoratorExample {

    public interface Coffee {
        double getCost();
        String getDescription();
    }

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

    public abstract static class CoffeeDecorator implements Coffee {
        protected final Coffee coffee;

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

    public static void main(String[] args) {
        Coffee coffee = new SimpleCoffee();
        coffee = new MilkDecorator(coffee);
        coffee = new SugarDecorator(coffee);

        System.out.println(coffee.getDescription() + " = $" + coffee.getCost());
    }
}
