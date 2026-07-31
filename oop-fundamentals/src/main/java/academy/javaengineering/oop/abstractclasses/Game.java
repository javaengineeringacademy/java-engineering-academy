package academy.javaengineering.oop.abstractclasses;

/**
 * Game - Abstract class demonstrating Template Method pattern.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
public abstract class Game {

    // Template method - final to prevent overriding
    public final void play() {
        System.out.println("Starting " + getGameName() + "...");
        initialize();
        playGame();
        if (isWinner()) {
            announceWinner();
        }
        cleanup();
    }

    // Abstract methods - subclasses must implement
    protected abstract String getGameName();
    protected abstract void initialize();
    protected abstract void playGame();
    protected abstract boolean isWinner();

    // Hook methods - optional to override
    protected void announceWinner() {
        System.out.println("We have a winner!");
    }

    protected void cleanup() {
        System.out.println("Cleaning up " + getGameName() + "...");
    }
}