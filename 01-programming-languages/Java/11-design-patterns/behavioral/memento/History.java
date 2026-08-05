package academy.javaengineering.patterns.behavioral.memento;

import java.util.ArrayList;
import java.util.List;

/**
 * Caretaker class - Manages mementos for the originator.
 * Stores the history of states without modifying them.
 */
public class History {

    private final List<Memento> mementos = new ArrayList<>();
    private int currentIndex = -1;

    public void push(Memento memento) {
        if (currentIndex < mementos.size() - 1) {
            mementos.subList(currentIndex + 1, mementos.size()).clear();
        }
        mementos.add(memento);
        currentIndex++;
    }

    public Memento pop() {
        if (currentIndex >= 0) {
            return mementos.get(currentIndex--);
        }
        return null;
    }

    public Memento peek() {
        if (currentIndex >= 0) {
            return mementos.get(currentIndex);
        }
        return null;
    }

    public boolean canUndo() {
        return currentIndex >= 0;
    }

    public int size() {
        return mementos.size();
    }
}
