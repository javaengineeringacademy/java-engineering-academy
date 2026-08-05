package academy.javaengineering.patterns.behavioral.strategy;

/**
 * Strategy interface for defining a family of algorithms.
 * Each concrete strategy implements a specific algorithm.
 */
public interface Strategy {

    /**
     * Execute the strategy algorithm.
     *
     * @param data the input data
     * @return the result of the algorithm execution
     */
    String execute(String data);
}
