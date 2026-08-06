package academy.javaengineering.exercises;

import java.util.*;
import java.util.function.*;

/**
 * Exercises: Behavioral Design Patterns (Observer, Strategy, Command)
 *
 * Complete the TODO sections below.
 */
public class BehavioralExercises {

    // TODO 1: Implement Observer pattern for a stock price monitor
    public interface StockObserver {
        void onPriceUpdate(String symbol, double price);
    }

    public static class StockMarket {
        private final Map<String, List<StockObserver>> observers = new HashMap<>();
        private final Map<String, Double> prices = new HashMap<>();

        public void subscribe(String symbol, StockObserver observer) {
            // TODO: implement
        }

        public void unsubscribe(String symbol, StockObserver observer) {
            // TODO: implement
        }

        public void updatePrice(String symbol, double price) {
            // TODO: implement - notify all observers
        }

        public double getPrice(String symbol) {
            return prices.getOrDefault(symbol, 0.0);
        }
    }

    // TODO 2: Implement Strategy pattern for sorting algorithms
    @FunctionalInterface
    public interface SortStrategy<T> {
        List<T> sort(List<T> list, Comparator<T> comparator);
    }

    public static class BubbleSort<T> implements SortStrategy<T> {
        @Override
        public List<T> sort(List<T> list, Comparator<T> comparator) {
            // TODO: implement bubble sort
            return new ArrayList<>(list);
        }
    }

    public static class InsertionSort<T> implements SortStrategy<T> {
        @Override
        public List<T> sort(List<T> list, Comparator<T> comparator) {
            // TODO: implement insertion sort
            return new ArrayList<>(list);
        }
    }

    public static class Sorter<T> {
        private SortStrategy<T> strategy;

        public Sorter(SortStrategy<T> strategy) {
            this.strategy = strategy;
        }

        public void setStrategy(SortStrategy<T> strategy) {
            this.strategy = strategy;
        }

        public List<T> sort(List<T> list, Comparator<T> comparator) {
            // TODO: delegate to strategy
            return new ArrayList<>(list);
        }
    }

    // TODO 3: Implement Command pattern for a text editor
    public interface Command {
        void execute();
        void undo();
    }

    public static class TextEditor {
        private StringBuilder text = new StringBuilder();
        private Deque<Command> history = new ArrayDeque<>();

        public void executeCommand(Command cmd) {
            // TODO: execute and push to history
        }

        public void undo() {
            // TODO: pop from history and undo
        }

        public String getText() {
            return text.toString();
        }

        public StringBuilder getBuffer() {
            return text;
        }
    }

    public static class InsertCommand implements Command {
        private final TextEditor editor;
        private final String text;
        private final int position;

        public InsertCommand(TextEditor editor, String text, int position) {
            this.editor = editor;
            this.text = text;
            this.position = position;
        }

        @Override
        public void execute() {
            // TODO: insert text at position
        }

        @Override
        public void undo() {
            // TODO: remove inserted text
        }
    }

    public static class DeleteCommand implements Command {
        private final TextEditor editor;
        private final int start;
        private final int length;
        private String deletedText = "";

        public DeleteCommand(TextEditor editor, int start, int length) {
            this.editor = editor;
            this.start = start;
            this.length = length;
        }

        @Override
        public void execute() {
            // TODO: delete and save deleted text
        }

        @Override
        public void undo() {
            // TODO: re-insert deleted text
        }
    }

    // ==================== TEST METHODS ====================

    public static void main(String[] args) {
        BehavioralExercises exercises = new BehavioralExercises();
        int passed = 0;
        int total = 0;

        System.out.println("=== BehavioralExercises Tests ===\n");

        // Test 1 - Observer
        total++;
        StockMarket market = new StockMarket();
        List<String> updates = new ArrayList<>();
        StockObserver observer = (symbol, price) -> updates.add(symbol + ":" + price);
        market.subscribe("AAPL", observer);
        market.updatePrice("AAPL", 150.0);
        market.updatePrice("AAPL", 155.0);
        if (updates.size() == 2 && "AAPL:150.0".equals(updates.get(0)) && "AAPL:155.0".equals(updates.get(1))) {
            System.out.println("Test 1a PASSED: Observer subscribe/update");
            passed++;
        } else {
            System.out.println("Test 1a FAILED: Observer - " + updates);
        }

        total++;
        market.unsubscribe("AAPL", observer);
        market.updatePrice("AAPL", 160.0);
        if (updates.size() == 2) {
            System.out.println("Test 1b PASSED: Observer unsubscribe");
            passed++;
        } else {
            System.out.println("Test 1b FAILED: Observer unsubscribe - " + updates.size());
        }

        // Test 2 - Strategy
        total++;
        Sorter<Integer> sorter = new Sorter<>(new BubbleSort<>());
        List<Integer> sorted = sorter.sort(List.of(5, 3, 1, 4, 2), Integer::compareTo);
        if (sorted.equals(List.of(1, 2, 3, 4, 5))) {
            System.out.println("Test 2a PASSED: BubbleSort");
            passed++;
        } else {
            System.out.println("Test 2a FAILED: BubbleSort - " + sorted);
        }

        total++;
        sorter.setStrategy(new InsertionSort<>());
        sorted = sorter.sort(List.of(5, 3, 1, 4, 2), Integer::compareTo);
        if (sorted.equals(List.of(1, 2, 3, 4, 5))) {
            System.out.println("Test 2b PASSED: InsertionSort");
            passed++;
        } else {
            System.out.println("Test 2b FAILED: InsertionSort - " + sorted);
        }

        // Test 3 - Command
        total++;
        TextEditor editor = new TextEditor();
        editor.executeCommand(new InsertCommand(editor, "Hello", 0));
        if ("Hello".equals(editor.getText())) {
            System.out.println("Test 3a PASSED: InsertCommand");
            passed++;
        } else {
            System.out.println("Test 3a FAILED: InsertCommand - '" + editor.getText() + "'");
        }

        total++;
        editor.executeCommand(new InsertCommand(editor, " World", 5));
        editor.undo();
        if ("Hello".equals(editor.getText())) {
            System.out.println("Test 3b PASSED: Undo insert");
            passed++;
        } else {
            System.out.println("Test 3b FAILED: Undo insert - '" + editor.getText() + "'");
        }

        total++;
        editor.executeCommand(new InsertCommand(editor, " World", 5));
        editor.executeCommand(new DeleteCommand(editor, 0, 5));
        if (" World".equals(editor.getText())) {
            editor.undo();
            if ("Hello World".equals(editor.getText())) {
                System.out.println("Test 3c PASSED: DeleteCommand and undo");
                passed++;
            } else {
                System.out.println("Test 3c FAILED: Undo delete - '" + editor.getText() + "'");
            }
        } else {
            System.out.println("Test 3c FAILED: DeleteCommand - '" + editor.getText() + "'");
        }

        System.out.println("\nResults: " + passed + "/" + total + " tests passed");
    }
}
