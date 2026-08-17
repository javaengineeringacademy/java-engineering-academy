package academy.javaengineering.exercises;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Exercises: Function Composition
 *
 * Complete the TODO sections below.
 */
public class CompositionExercises {

    // TODO 1: Create a Pipeline class that chains functions
    // The Pipeline should:
    // - Start with an initial function
    // - Allow adding functions via add() method
    // - Execute all functions in sequence via execute()
    public static class Pipeline<I, O> {
        private final List<Function<?, ?>> functions = new ArrayList<>();

        public Pipeline(Function<I, O> initial) {
            // TODO: store the initial function
        }

        public <NO> Pipeline<I, NO> add(Function<O, NO> next) {
            // TODO: add the next function and return new Pipeline
            return null;
        }

        public O execute(I input) {
            // TODO: apply all functions in sequence
            return null;
        }
    }

    // TODO 2: Create a compose function that chains two functions
    // compose(f, g) should return a function that applies g then f
    // (Mathematical composition: (f ∘ g)(x) = f(g(x)))
    public static <A, B, C> Function<A, C> compose(Function<B, C> f, Function<A, B> g) {
        // TODO: implement
        return null;
    }

    // TODO 3: Create a pipeline of string transformations
    // Trim -> lowercase -> remove special chars -> replace spaces with hyphens
    public Function<String, String> createSlugPipeline() {
        // TODO: implement using Function.andThen() composition
        return null;
    }

    // TODO 4: Create a validator pipeline
    // Each validator takes a string and returns true if valid
    // Chain multiple validators and return true only if ALL pass
    public static class ValidatorPipeline {
        private final List<Function<String, Boolean>> validators = new ArrayList<>();

        public ValidatorPipeline addValidator(Function<String, Boolean> validator) {
            // TODO: add validator and return this
            return null;
        }

        public boolean validate(String input) {
            // TODO: return true only if all validators pass
            return false;
        }
    }

    // TODO 5: Implement function memoization
    // Create a memoize wrapper that caches results of a function
    public static <T, R> Function<T, R> memoize(Function<T, R> fn) {
        // TODO: implement caching using a Map
        return null;
    }

    // ==================== TEST METHODS ====================

    public static void main(String[] args) {
        int passed = 0;
        int total = 0;

        System.out.println("=== CompositionExercises Tests ===\n");

        // Test 1: Pipeline
        total++;
        try {
            Pipeline<String, String> pipeline = new Pipeline<>(String::trim);
            pipeline.add(s -> s.toLowerCase());
            pipeline.add(s -> s.replaceAll("[^a-z ]", ""));
            pipeline.add(s -> s.replaceAll("\\s+", "-"));

            String result = pipeline.execute("  Hello, World!  ");
            if ("hello-world".equals(result)) {
                System.out.println("Test 1 PASSED: Pipeline");
                passed++;
            } else {
                System.out.println("Test 1 FAILED: Pipeline - got '" + result + "'");
            }
        } catch (Exception e) {
            System.out.println("Test 1 FAILED: Pipeline - " + e.getMessage());
        }

        // Test 2: compose
        total++;
        try {
            Function<Integer, Integer> doubleIt = x -> x * 2;
            Function<Integer, Integer> addTen = x -> x + 10;
            Function<Integer, Integer> composed = compose(doubleIt, addTen);
            // (doubleIt ∘ addTen)(5) = doubleIt(addTen(5)) = doubleIt(15) = 30
            if (composed != null && composed.apply(5) == 30) {
                System.out.println("Test 2 PASSED: compose");
                passed++;
            } else {
                System.out.println("Test 2 FAILED: compose - got " + (composed != null ? composed.apply(5) : "null"));
            }
        } catch (Exception e) {
            System.out.println("Test 2 FAILED: compose - " + e.getMessage());
        }

        // Test 3: Slug pipeline
        total++;
        try {
            Function<String, String> slugPipeline = createSlugPipeline();
            String slug = slugPipeline.apply("  Hello, World!  ");
            if ("hello-world".equals(slug)) {
                System.out.println("Test 3 PASSED: slug pipeline");
                passed++;
            } else {
                System.out.println("Test 3 FAILED: slug pipeline - got '" + slug + "'");
            }
        } catch (Exception e) {
            System.out.println("Test 3 FAILED: slug pipeline - " + e.getMessage());
        }

        // Test 4: Validator pipeline
        total++;
        try {
            ValidatorPipeline validator = new ValidatorPipeline();
            validator.addValidator(s -> s != null && !s.isEmpty());
            validator.addValidator(s -> s.length() >= 3);
            validator.addValidator(s -> s.matches("[a-zA-Z]+"));

            if (validator.validate("hello") && !validator.validate("") && !validator.validate("ab")) {
                System.out.println("Test 4 PASSED: ValidatorPipeline");
                passed++;
            } else {
                System.out.println("Test 4 FAILED: ValidatorPipeline");
            }
        } catch (Exception e) {
            System.out.println("Test 4 FAILED: ValidatorPipeline - " + e.getMessage());
        }

        // Test 5: Memoize
        total++;
        try {
            int[] callCount = {0};
            Function<Integer, Integer> expensiveFn = x -> {
                callCount[0]++;
                return x * x;
            };
            Function<Integer, Integer> memoized = memoize(expensiveFn);

            int r1 = memoized.apply(5);
            int r2 = memoized.apply(5);
            int r3 = memoized.apply(3);

            if (r1 == 25 && r2 == 25 && r3 == 9 && callCount[0] == 2) {
                System.out.println("Test 5 PASSED: memoize");
                passed++;
            } else {
                System.out.println("Test 5 FAILED: memoize - calls=" + callCount[0] + ", r1=" + r1 + ", r3=" + r3);
            }
        } catch (Exception e) {
            System.out.println("Test 5 FAILED: memoize - " + e.getMessage());
        }

        System.out.println("\nResults: " + passed + "/" + total + " tests passed");
    }
}
