package academy.javaengineering.patterns.behavioral.state;

/**
 * State interface for defining state-specific behavior.
 * Each concrete state implements operations specific to that state.
 */
public interface State {

    /**
     * Handle the next action in the state machine.
     *
     * @param context the context object
     */
    void handle(Order context);

    /**
     * Get the name of the state.
     *
     * @return the state name
     */
    String getStateName();
}
