package academy.javaengineering.exercises.solutions;

/**
 * Solutions: Variables, Types, and Type Casting
 */
public class VariableSolutions {

    public Object[] primitiveTypeValues() {
        return new Object[]{
            (byte) 42,
            (short) 1000,
            100000,
            999999999L,
            3.14f,
            2.718281828,
            'A',
            true
        };
    }

    public Object[] typeCasting() {
        int intVal = 100;
        long longVal = 1000L;
        double doubleVal = 9.99;
        int intVal2 = 200;

        long widened = intVal;       // int -> long (widening)
        int narrowed1 = (int) longVal;  // long -> int (narrowing)
        int narrowed2 = (int) doubleVal;  // double -> int (narrowing)
        double widened2 = intVal2;   // int -> double (widening)

        return new Object[]{widened, narrowed1, narrowed2, widened2};
    }

    public double compoundInterest(double principal, double annualRate, int years, int compoundingsPerYear) {
        double rate = annualRate / 100.0;
        double amount = principal * Math.pow(1 + rate / compoundingsPerYear, compoundingsPerYear * years);
        return Math.round(amount * 100.0) / 100.0;
    }

    public double[] temperatureConversion(double celsius, double fahrenheit) {
        double convertedFahrenheit = (celsius * 9.0 / 5.0) + 32;
        double convertedCelsius = (fahrenheit - 32) * 5.0 / 9.0;
        return new double[]{convertedCelsius, convertedFahrenheit};
    }

    public boolean isValidVariableName(String name) {
        if (name == null || name.isEmpty()) return false;
        if (!Character.isLetter(name.charAt(0)) && name.charAt(0) != '_') return false;
        for (int i = 1; i < name.length(); i++) {
            char c = name.charAt(i);
            if (!Character.isLetterOrDigit(c) && c != '_') return false;
        }
        return true;
    }

    public static void main(String[] args) {
        VariableSolutions solutions = new VariableSolutions();
        System.out.println("=== Variable Solutions ===\n");

        System.out.println("1. Primitive Values:");
        Object[] values = solutions.primitiveTypeValues();
        for (Object v : values) System.out.println("  " + v.getClass().getSimpleName() + ": " + v);

        System.out.println("\n2. Type Casting:");
        Object[] casts = solutions.typeCasting();
        for (Object c : casts) System.out.println("  " + c);

        System.out.println("\n3. Compound Interest: " + solutions.compoundInterest(1000, 5, 10, 12));

        System.out.println("\n4. Temperature Conversion:");
        double[] temps = solutions.temperatureConversion(100, 32);
        System.out.println("  100C -> " + temps[1] + "F");
        System.out.println("  32F -> " + temps[0] + "C");

        System.out.println("\n5. Variable Name Validation:");
        System.out.println("  myVariable: " + solutions.isValidVariableName("myVariable"));
        System.out.println("  _invalid: " + solutions.isValidVariableName("_invalid"));
        System.out.println("  123bad: " + solutions.isValidVariableName("123bad"));
    }
}
