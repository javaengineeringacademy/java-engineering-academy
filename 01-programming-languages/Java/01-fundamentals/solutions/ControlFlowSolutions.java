package academy.javaengineering.exercises.solutions;

/**
 * Solutions: Control Flow (if-else, switch, loops)
 */
public class ControlFlowSolutions {

    public String[] fizzBuzz(int n) {
        String[] result = new String[n];
        for (int i = 0; i < n; i++) {
            int num = i + 1;
            if (num % 15 == 0) result[i] = "FizzBuzz";
            else if (num % 3 == 0) result[i] = "Fizz";
            else if (num % 5 == 0) result[i] = "Buzz";
            else result[i] = String.valueOf(num);
        }
        return result;
    }

    public int secondLargest(int[] numbers) {
        if (numbers == null || numbers.length < 2) return Integer.MIN_VALUE;
        int largest = Integer.MIN_VALUE;
        int second = Integer.MIN_VALUE;
        for (int num : numbers) {
            if (num > largest) {
                second = largest;
                largest = num;
            } else if (num > second && num != largest) {
                second = num;
            }
        }
        return second == Integer.MIN_VALUE ? Integer.MIN_VALUE : second;
    }

    public boolean isValidPassword(String password) {
        if (password == null || password.length() < 8) return false;
        boolean hasUpper = false, hasLower = false, hasDigit = false, hasSpecial = false;
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) hasUpper = true;
            else if (Character.isLowerCase(c)) hasLower = true;
            else if (Character.isDigit(c)) hasDigit = true;
            else if ("!@#$%^&*".indexOf(c) >= 0) hasSpecial = true;
        }
        return hasUpper && hasLower && hasDigit && hasSpecial;
    }

    public int[] countVowelsConsonants(String text) {
        int vowels = 0, consonants = 0;
        for (char c : text.toLowerCase().toCharArray()) {
            if (Character.isLetter(c)) {
                if ("aeiou".indexOf(c) >= 0) vowels++;
                else consonants++;
            }
        }
        return new int[]{vowels, consonants};
    }

    public double calculate(double a, double b, char operator) {
        return switch (operator) {
            case '+' -> a + b;
            case '-' -> a - b;
            case '*' -> a * b;
            case '/' -> {
                if (b == 0) throw new IllegalArgumentException("Division by zero");
                yield a / b;
            }
            case '%' -> a % b;
            default -> throw new IllegalArgumentException("Invalid operator: " + operator);
        };
    }

    public int[][] pascalsTriangle(int n) {
        int[][] triangle = new int[n][];
        for (int i = 0; i < n; i++) {
            triangle[i] = new int[i + 1];
            triangle[i][0] = 1;
            triangle[i][i] = 1;
            for (int j = 1; j < i; j++) {
                triangle[i][j] = triangle[i - 1][j - 1] + triangle[i - 1][j];
            }
        }
        return triangle;
    }

    public static void main(String[] args) {
        ControlFlowSolutions solutions = new ControlFlowSolutions();
        System.out.println("=== Control Flow Solutions ===\n");

        System.out.println("1. FizzBuzz(15):");
        String[] fb = solutions.fizzBuzz(15);
        for (int i = 0; i < Math.min(15, fb.length); i++) {
            System.out.print(fb[i] + " ");
        }
        System.out.println();

        System.out.println("\n2. Second Largest [1,2,3,4,5]: " + solutions.secondLargest(new int[]{1, 2, 3, 4, 5}));
        System.out.println("3. Valid Password 'MyP@ss123': " + solutions.isValidPassword("MyP@ss123"));
        System.out.println("4. Vowels/Consonants 'Hello World': ");
        int[] vc = solutions.countVowelsConsonants("Hello World");
        System.out.println("   Vowels: " + vc[0] + ", Consonants: " + vc[1]);
        System.out.println("5. 10 + 5 = " + solutions.calculate(10, 5, '+'));
        System.out.println("6. Pascal's Triangle (5 rows):");
        int[][] tri = solutions.pascalsTriangle(5);
        for (int[] row : tri) {
            for (int val : row) System.out.print(val + " ");
            System.out.println();
        }
    }
}
