package academy.javaengineering.testing.examples;

import java.util.*;

/**
 * Mutation Testing Demo - PIT (PITest)
 */
class MutationTestingDemo {

    // ============================================
    // Mutation Testing Concept
    // ============================================

    /*
     * Mutation testing evaluates the quality of tests by introducing
     * small changes (mutations) to the source code and checking if
     * the tests catch the changes.
     * 
     * Key concepts:
     * - Mutant: A modified version of the source code
     * - Killed: Mutant detected by tests (good!)
     * - Survived: Mutant NOT detected by tests (bad - weak tests)
     * - Mutation Score: Killed mutants / Total mutants
     * 
     * PIT (PITest) is the most popular mutation testing tool for Java.
     */

    // ============================================
    // Production Code with Potential Mutations
    // ============================================

    static class GradeCalculator {
        // MUTATIONS:
        // - Condition boundary: > to >=, < to <=
        // - Return value: return true/false
        // - Negate condition: !condition

        static String calculateGrade(int score) {
            if (score < 0 || score > 100) {
                throw new IllegalArgumentException("Score must be 0-100");
            }
            if (score >= 90) return "A";
            if (score >= 80) return "B";
            if (score >= 70) return "C";
            if (score >= 60) return "D";
            return "F";
        }

        // MUTATIONS:
        // - Arithmetic: + to -, * to /, etc.
        // - Condition boundary
        // - Increment/Decrement
        static double calculateAverage(List<Integer> scores) {
            if (scores == null || scores.isEmpty()) {
                throw new IllegalArgumentException("Scores list cannot be empty");
            }
            int sum = 0;
            for (int score : scores) {
                sum += score;
            }
            return (double) sum / scores.size();
        }

        // MUTATIONS:
        // - Boolean return: return true to return false
        // - Negate condition
        // - Remove method call
        static boolean isPassing(int score) {
            return score >= 50;
        }
    }

    // ============================================
    // Tests That Would Catch Mutations
    // ============================================

    static class GradeCalculatorTest {
        // GOOD TESTS - These would kill most mutants

        static void testGradeA() {
            assert "A".equals(GradeCalculator.calculateGrade(95));
        }

        static void testGradeB() {
            assert "B".equals(GradeCalculator.calculateGrade(85));
        }

        static void testGradeC() {
            assert "C".equals(GradeCalculator.calculateGrade(75));
        }

        static void testGradeD() {
            assert "D".equals(GradeCalculator.calculateGrade(65));
        }

        static void testGradeF() {
            assert "F".equals(GradeCalculator.calculateGrade(45));
        }

        static void testBoundary90() {
            assert "A".equals(GradeCalculator.calculateGrade(90));
        }

        static void testBoundary80() {
            assert "B".equals(GradeCalculator.calculateGrade(80));
        }

        static void testInvalidScore() {
            try {
                GradeCalculator.calculateGrade(-1);
                assert false : "Should throw";
            } catch (IllegalArgumentException e) {
                assert true;
            }
        }

        static void testAverage() {
            List<Integer> scores = Arrays.asList(80, 90, 70, 85);
            double avg = GradeCalculator.calculateAverage(scores);
            assert avg == 81.25 : "Average should be 81.25 but was " + avg;
        }

        static void testIsPassing() {
            assert GradeCalculator.isPassing(50);
            assert !GradeCalculator.isPassing(49);
        }
    }

    // ============================================
    // WEAK TESTS - These would let mutants survive
    // ============================================

    static class WeakGradeCalculatorTest {
        // WEAK TESTS - Missing edge cases, boundary conditions

        static void testGradeReturnsString() {
            // This test is too weak - doesn't verify specific grades
            String result = GradeCalculator.calculateGrade(85);
            assert result != null : "Should return non-null";
        }

        static void testAveragePositive() {
            // Too weak - doesn't verify exact value
            List<Integer> scores = Arrays.asList(80, 90, 70);
            double avg = GradeCalculator.calculateAverage(scores);
            assert avg > 0 : "Average should be positive";
        }
    }

    // ============================================
    // PITest Configuration
    // ============================================

    /*
     * Maven Configuration:
     * 
     * <plugin>
     *     <groupId>org.pitest</groupId>
     *     <artifactId>pitest-maven</artifactId>
     *     <version>1.15.0</version>
     *     <configuration>
     *         <targetClasses>com.example.*</targetClasses>
     *         <targetTests>com.example.*Test</targetTests>
     *         <mutationThreshold>85</mutationThreshold>
     *         <timestampedReports>true</timestampedReports>
     *     </configuration>
     *     <executions>
     *         <execution>
     *             <id>pitest</id>
     *             <phase>verify</phase>
     *             <goals>
     *                 <goal>mutationCoverage</goal>
     *             </goals>
     *         </execution>
     *     </executions>
     * </plugin>
     * 
     * Run: mvn org.pitest:pitest-maven:mutationCoverage
     */

    // ============================================
    // Mutation Types
    // ============================================

    /*
     * Common Mutation Operators:
     * 
     * 1. Condition Boundary:
     *    - score > 50  -->  score >= 50
     *    - score < 100  -->  score <= 100
     * 
     * 2. Negate Condition:
     *    - if (score >= 50)  -->  if (!(score >= 50))
     * 
     * 3. Return Values:
     *    - return true  -->  return false
     *    - return "A"  -->  return "B"
     * 
     * 4. Math Operations:
     *    - a + b  -->  a - b
     *    - a * b  -->  a / b
     * 
     * 5. Increment/Decrement:
     *    - i++  -->  i--
     *    - count++  -->  count--
     * 
     * 6. Void Method Calls:
     *    - Remove method call
     * 
     * 7. Null Returns:
     *    - return value  -->  return null
     */

    public static void main(String[] args) {
        System.out.println("=== Mutation Testing Demo ===\n");

        System.out.println("--- Running Good Tests ---");
        GradeCalculatorTest.testGradeA();
        GradeCalculatorTest.testGradeB();
        GradeCalculatorTest.testGradeC();
        GradeCalculatorTest.testGradeD();
        GradeCalculatorTest.testGradeF();
        GradeCalculatorTest.testBoundary90();
        GradeCalculatorTest.testBoundary80();
        GradeCalculatorTest.testInvalidScore();
        GradeCalculatorTest.testAverage();
        GradeCalculatorTest.testIsPassing();
        System.out.println("All good tests PASSED\n");

        System.out.println("--- Running Weak Tests ---");
        WeakGradeCalculatorTest.testGradeReturnsString();
        WeakGradeCalculatorTest.testAveragePositive();
        System.out.println("Weak tests PASSED (but mutants would survive)\n");

        System.out.println("--- Mutation Score Comparison ---");
        System.out.println("Good tests: High mutation score (~95%+)");
        System.out.println("Weak tests: Low mutation score (~30%)");
        System.out.println("\nMutation testing reveals test quality gaps!");

        System.out.println("\n--- How to Improve Mutation Score ---");
        System.out.println("1. Add boundary value tests");
        System.out.println("2. Test all branches (if/else)");
        System.out.println("3. Verify exact return values");
        System.out.println("4. Test edge cases (empty, null, extreme values)");
        System.out.println("5. Check that mutations change behavior");

        System.out.println("\n=== Mutation Testing Demo Complete ===");
    }
}
