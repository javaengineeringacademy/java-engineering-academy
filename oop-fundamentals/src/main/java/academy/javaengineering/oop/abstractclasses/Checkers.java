package academy.javaengineering.oop.abstractclasses;

/**
 * Checkers - Concrete implementation of abstract Game class.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
public class Checkers extends Game {

    @Override
    protected String getGameName() {
        return "Checkers";
    }

    @Override
    protected void initialize() {
        System.out.println("  Placing 24 pieces on 64 squares");
    }

    @Override
    protected void playGame() {
        System.out.println("  Players jump over opponent's pieces");
        System.out.println("  Simple rules, deep strategy!");
    }

    @Override
    protected boolean isWinner() {
        return true; // Simulated
    }

    @Override
    protected void announceWinner() {
        System.out.println("  All opponent's pieces captured! You win!");
    }
}