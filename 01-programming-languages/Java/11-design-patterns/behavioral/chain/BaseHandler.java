package academy.javaengineering.patterns.behavioral.chain;

/**
 * Base handler implementation providing common chain functionality.
 * Handles chaining logic and optional processing.
 */
public abstract class BaseHandler implements Handler {

    private Handler next;

    @Override
    public Handler setNext(Handler next) {
        this.next = next;
        return next;
    }

    @Override
    public boolean handle(String request) {
        if (process(request)) {
            return true;
        }
        if (next != null) {
            return next.handle(request);
        }
        return false;
    }

    protected abstract boolean process(String request);

    protected Handler getNext() {
        return next;
    }
}
