package academy.javaengineering.oop.abstractclasses;

/**
 * Chess - Concrete implementation of abstract Game class.
 * 
 * @author Java Engineering Academy
 * @version 1.0
 */
public class Chess extends Game {

    @Override
    protected String getGameName() {
        return "Chess";
    }

    @Override
    protected void initialize() {
        System.out.println("  Setting up 32 pieces on 64 squares");
    }

    @Override
    protected void playGame() {
        System.out.println("  Players take turns moving pieces");
        System.out.println("  Strategic thinking required!");
    }

    @Override
    protected boolean isWinner() {
        return true; // Simulated
    }

    @Override
    protected void announceWinner() {
        System.out.println("  Checkmate! White wins!");
    }
}